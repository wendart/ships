package com.github.pbrzezinski.ships;

import com.github.pbrzezinski.ships.board.Board;
import com.github.pbrzezinski.ships.console.ConsoleSpec;

import java.util.Scanner;

public class Console {

	private Scanner scan = new Scanner(System.in);

	public String askForName() {
		writeMessage("Place your name: ");
		return scan.nextLine();
	}

	public FieldRange askForShipPlacement(int shipSize, String name) {

		return ConsoleSpec.<FieldRange>askFor(name + ", please place " + shipSize + " mast ship: ")
				.map(placement -> {
					if (shipSize == 1) {
						return new FieldRange(new Field(placement));
					} else {
						return new FieldRange(placement);
					}
				})
				.execute();

//		while (true) {
//			System.out.print(name + ", please place " + shipSize + " mast ship: ");
//			String placement = scan.nextLine();
//			try {
//				if (shipSize == 1) {
//					return new FieldRange(new Field(placement));
//				} else {
//					return new FieldRange(placement);
//				}
//			} catch (Exception ex) {
//				System.out.println(ex.getMessage());
//			}
//		}
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

	public String askForShot(String name) {
		System.out.print("Where do you want to shoot " + name + "? ");
		String string = scan.nextLine();
		string = string.toUpperCase();
		string = string.trim();
		return string;
	}

	public String getInput(String msg) {
		System.out.print(msg);
		String string = scan.nextLine();
		string = string.toUpperCase();
		string = string.trim();
		return string;
	}

	public void getChar() {
		scan.nextLine();
	}

	public void writeEnters() {
		for (int i = 0; i < 37; i++) {
			System.out.println();
		}
	}

	public String getNewLine() {
		return scan.nextLine();
	}
}
