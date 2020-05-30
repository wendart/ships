package com.github.pbrzezinski.ships;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pbrzezinski.ships.state.GameState;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Engine {

	public final static int BOARD_SIZE = 10;
	public final static List<ShipSpec> SHIP_SPEC = Arrays.asList(
			//new com.github.pbrzezinski.ships.ShipSpec(1,4)
			//new com.github.pbrzezinski.ships.ShipSpec(2,3),
			//new com.github.pbrzezinski.ships.ShipSpec(3,2),
			new ShipSpec(4, 1)
	);

	private final Console console = new Console();
	private Player player1;
	private Player player2;
	private Player passivePlayer;
	private Player activePlayer;
	private boolean saved = false;


	public void start() {
		boolean playAgain = false;
		do {
			setupGame(playAgain);
			playGame();
			if (saved) {
				console.writeMessage("Thanks for playing your game is saved for next time");
			} else {
				congratulateWinner();
				playAgain = askIfPlayAgain();
			}
		} while (playAgain);

	}

	private void congratulateWinner() {
		if (!player1.isAlive()) {
			console.writeMessage("The winner is " + player2.getName() + "\n CONGRATULATIONS \n\n");
		} else {
			console.writeMessage("The winner is " + player1.getName() + "\n CONGRATULATIONS \n\n");
		}
	}

	private boolean askIfPlayAgain() {

		console.writeMessage("Do you want to play again? [YES] [NO]");
		String playAgain = "";
		do {
			playAgain = console.getNewLine();
			playAgain = playAgain.trim();
			playAgain = playAgain.toUpperCase();
		} while (!playAgain.equals("YES") && !playAgain.equals("NO"));

		return playAgain.equals("YES");
	}

	private void playGame() {
		while (isGameInProgress()) {
			activePlayer.showBoards();
			String shotPlacement = activePlayer.shoot();
			if (!wantSaveGame(shotPlacement)) {
				Field shotPlacementField = new Field(shotPlacement);
				ShotResult shotResult = passivePlayer.checkShot(shotPlacementField);
				if (shotResult.getHitMark() == ShotResult.ShotMark.SINK) {
					passivePlayer.shipDeletion(shotResult.getShip());
				}
				activePlayer.markShot(shotResult, shotPlacementField);

				if (shotResult.getHitMark() == ShotResult.ShotMark.MISS) {
					console.writeMessage("MISS");
					console.getChar();
					Player swap = activePlayer;
					activePlayer = passivePlayer;
					passivePlayer = swap;
					console.writeEnters();
				} else if (shotResult.getHitMark() == ShotResult.ShotMark.HIT) {
					console.writeMessage("HIT");
				} else {
					console.writeMessage("SINK");
				}
			} else {
				saveGame(activePlayer);
				saved = true;
			}

		}


	}

	private boolean isGameInProgress() {
		return player1.isAlive() && player2.isAlive() && !saved;
	}

	private boolean wantSaveGame(String decision) {
		return decision.equals("SAVE");
	}


	private void saveGame(Player activePlayer) {
		GameState gameState = new GameState(
				player1.save(player1 == activePlayer),
				player2.save(player2 == activePlayer)
		);

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			byte[] bytes = objectMapper.writeValueAsBytes(gameState);
			Files.write(
					Paths.get("GameState.json"),
					bytes
			);
		} catch (IOException e) {
			System.err.println("Critical save error");
		}

	}

	private boolean askIfLoadGame() {
		console.writeMessage("Do you want to load previous game? ");
		String decision = console.getNewLine();
		decision = decision.toUpperCase();
		return decision.equals("YES");
	}

	private void setupGame(boolean playAgain) {


		GameState gameState = loadGame();
		if (gameState != null && askIfLoadGame()) {


			player1 = new Player(gameState.getPlayer1(), console);
			player2 = new Player(gameState.getPlayer2(), console);

			if (gameState.getPlayer1().isActive()) {
				activePlayer = player1;
				passivePlayer = player2;
			} else {
				activePlayer = player2;
				passivePlayer = player1;
			}
		} else {
			if(gameState != null) {
				try {
					Files.delete(Paths.get("GameState.json"));
				} catch (IOException e) {
					System.err.println("Nothing to delete");;
				}
			}

			if (!playAgain) {
				player1 = new Player(console.askForName(), console);
				player2 = new Player(console.askForName(), console);
			}

			player1.prepareBoard();
			console.getChar();
			console.writeEnters();
			player2.prepareBoard();
			console.getChar();
			console.writeEnters();
			activePlayer = player1;
			passivePlayer = player2;
		}


	}

	private GameState loadGame() {

		try {
			byte[] bytes = Files.readAllBytes(Paths.get("GameState.json"));

			//GameState load = new GameState();

			ObjectMapper objectMapper = new ObjectMapper();

			return objectMapper.readValue(bytes, GameState.class);

		} catch (IOException e) {
			System.err.println("No previous game state");
		}

		return null;

	}

}

