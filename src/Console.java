import java.util.Scanner;

public class Console {

	private Scanner scan = new Scanner(System.in);

	public String askForName() {
		writeMessage("Place your name: ");
		return scan.nextLine();
	}

	public FieldRange askForShipPlacement(int shipSize) {
		while (true) {
			System.out.print("Place " + shipSize + " mast ship: ");
			String placement = scan.nextLine();
			try {
				if (shipSize == 1) {
					return new FieldRange(new Field(placement));
				} else {
					return new FieldRange(placement);
				}
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
			}
		}
	}

	public void writeMessage(String message) {
		System.out.println(message);
	}

	public void printBoard(Board board) {
		writeMessage(board.toString());
	}

	public void printBoards(Board ownBoard, Board enemyBoard) {
		String[] ownBoardLines = ownBoard.toString().split("\n");
		String[] enemyBoardLines = enemyBoard.toString().split("\n");

		System.out.printf("Board%28sRadar\n", "");
		for (int i = 0; i < ownBoardLines.length; i++) {
			System.out.printf(
					"%23s%10s%23s\n",
					ownBoardLines[i],
					"",
					enemyBoardLines[i]
			);
		}
	}

	public Field askForShot(String name) {
		while (true) {
			System.out.print("Where do you want to shoot " + name + "? ");
			try {
				return new Field(scan.nextLine());
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
			}
		}
	}
}
