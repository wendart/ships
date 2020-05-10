import java.util.Arrays;
import java.util.List;

public class Engine {

	public final static int BOARD_SIZE = 10;
	public final static List<ShipSpec> SHIP_SPEC = Arrays.asList(
			//new ShipSpec(1,4)
			//new ShipSpec(2,3),
			//new ShipSpec(3,2),
			new ShipSpec(4, 1)
	);

	private final Console console = new Console();
	private Player player1;
	private Player player2;


	public void start() {
		boolean playAgain = false;
		do {
			setupGame();
			playGame();
			playAgain = askIfPlayAgain();
		} while (playAgain);

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

		while (player1.isAlive() && player2.isAlive()) {
			activePlayer.showBoards();
			Field shotPlacement = activePlayer.shoot();
			ShotResult shotResult = passivePlayer.checkShot(shotPlacement);
			if (shotResult.getHitMark() == ShotResult.ShotMark.SINK) {
				passivePlayer.shipDeletion(shotResult.getShip());
			}
			activePlayer.markShot(shotResult, shotPlacement);

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
		}


	}


	private void setupGame() {
		player1 = new Player(console.askForName(), console);
		player2 = new Player(console.askForName(), console);

		player1.prepareBoard();
		console.getChar();
		console.writeEnters();
		player2.prepareBoard();
		console.getChar();
		console.writeEnters();
	}

}

