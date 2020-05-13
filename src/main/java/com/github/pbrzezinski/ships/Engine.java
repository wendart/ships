package com.github.pbrzezinski.ships;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pbrzezinski.ships.state.GameState;

import java.io.IOException;
import java.nio.file.Files;
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


	public void start() {
		boolean playAgain = false;
		do {
			setupGame(playAgain);
			playGame();
			congratulateWinner();
			playAgain = askIfPlayAgain();
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
		String playAgain = new String("");
		do {
			playAgain = console.getNewLine();
			playAgain = playAgain.trim();
			playAgain = playAgain.toUpperCase();
		} while (!playAgain.equals("YES") && !playAgain.equals("NO"));

		return playAgain.equals("YES");
	}

	private void playGame() {

		Player activePlayer = player1;
		Player passivePlayer = player2;

		while (player1.isAlive() && player2.isAlive()) {//TODO is game in progress
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
			}

		}


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


	private void setupGame(boolean playAgain) {
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
	}

}

