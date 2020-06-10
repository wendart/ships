package com.github.pbrzezinski.ships;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pbrzezinski.ships.state.GameState;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Engine {// TODO WYDZIELIC KLASY

	public final static int BOARD_SIZE = 10;
	public final static List<ShipSpec> SHIP_SPEC = Arrays.asList(
			//new com.github.pbrzezinski.ships.ShipSpec(1,4)
			//new com.github.pbrzezinski.ships.ShipSpec(2,3),
			new com.github.pbrzezinski.ships.ShipSpec(3,2),
			new ShipSpec(4, 1)
	);

	public static final Path GAME_STATE_PATH = Paths.get("GameState.json");

	private final Console console = new Console();
	private final GameInterface gameInterface = new GameInterface(console);
	private Player passivePlayer;
	private Player activePlayer;
	private boolean saved = false;


	public void start() {
		boolean playAgain = false;
		do {
			setupGame(playAgain);
			playGame();
			if (saved) {
				console.writeMessage("Thanks for playing, your game is saved for next time");
			} else {
				congratulateWinner();
				playAgain = askIfPlayAgain();
			}
		} while (playAgain);

	}

	private void setupGame(boolean playAgain) {
		Optional<GameState> gameState = loadGameStateFromFile();
		if (gameState.isPresent() && askIfLoadGame()) {
			loadGame(gameState.get());
			return;
		}

		deleteGameStateIfExists();

		if (!playAgain) {
			activePlayer = new Player(console.askForName(), gameInterface);
			passivePlayer = new Player(console.askForName(), gameInterface);
		}

		preparePlayerBoard(activePlayer);
		preparePlayerBoard(passivePlayer);
	}

	private void playGame() {
		while (isGameInProgress()) {
			playOneRound();
		}
	}

	private void playOneRound() {
		activePlayer.showBoards();
		PlayerDecision decision = activePlayer.makeDecisionShootOrSave();
		if (decision.isSaveGame()) {
			saveGame();
			return;
		}

		Field shotPlacementField = decision.toField();
		ShotResult shotResult = passivePlayer.checkShot(shotPlacementField);
		if (shotResult.getHitMark() == ShotResult.ShotMark.SINK) {
			passivePlayer.shipDeletion(shotResult.getShip());
			console.writeMessage("SINK");
		}

		activePlayer.markShot(shotResult, shotPlacementField);
		if (shotResult.getHitMark() == ShotResult.ShotMark.MISS) {
			console.writeMessage("MISS");
			console.getChar();
			console.writeEnters();
			swapPlayers();
		} else if (shotResult.getHitMark() == ShotResult.ShotMark.HIT) {
			console.writeMessage("HIT");
		}
	}

	private void swapPlayers() {
		Player swap = activePlayer;
		activePlayer = passivePlayer;
		passivePlayer = swap;
	}

	private void congratulateWinner() {
		if (!activePlayer.isAlive()) {
			console.writeMessage("The winner is " + passivePlayer.getName() + "\n CONGRATULATIONS \n\n");
		} else {
			console.writeMessage("The winner is " + activePlayer.getName() + "\n CONGRATULATIONS \n\n");
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

	private boolean isGameInProgress() {
		return activePlayer.isAlive() && passivePlayer.isAlive() && !saved;
	}


	private void saveGame() {

		GameState gameState = new GameState(
				activePlayer.save(true),
				passivePlayer.save(false)
		);

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			byte[] bytes = objectMapper.writeValueAsBytes(gameState);
			Files.write(GAME_STATE_PATH, bytes);
			saved = true;
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

	private void preparePlayerBoard(Player player) {
		player.prepareBoard();
		console.getChar();
		console.writeEnters();
	}

	private void deleteGameStateIfExists() {
		try {
			Files.deleteIfExists(GAME_STATE_PATH);
		} catch (IOException e) {
		}
	}

	private void loadGame(GameState state) {
		Player player1 = new Player(state.getPlayer1(), gameInterface);
		Player player2 = new Player(state.getPlayer2(), gameInterface);

		if (state.getPlayer1().isActive()) {
			activePlayer = player1;
			passivePlayer = player2;
		} else {
			activePlayer = player2;
			passivePlayer = player1;
		}
	}


	private Optional<GameState> loadGameStateFromFile() {
		try {
			byte[] bytes = Files.readAllBytes(GAME_STATE_PATH);
			ObjectMapper objectMapper = new ObjectMapper();
			return Optional.ofNullable(objectMapper.readValue(bytes, GameState.class));
		} catch (IOException e) {
			System.err.println("No previous game state");
		}

		return Optional.empty();
	}

}

