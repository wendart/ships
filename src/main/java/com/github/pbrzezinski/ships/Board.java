package com.github.pbrzezinski.ships;

import java.util.ArrayList;
import java.util.List;

public class Board {

	public State markShot(Field shotPlacement) {
		if (isFieldEmpty(shotPlacement)) {
			setField(shotPlacement, State.MISS);
			return State.MISS;
		} else {
			setField(shotPlacement, State.HIT);
			return State.HIT;
		}
	}

	public List<State> save() {
		List<State> state = new ArrayList<>();
		for (int y = 0; y < board.length; y++) {
			for (int x = 0; x < board.length; x++) {
				state.add(board[x][y]);
			}
		}

		return state;
	}

	public void placeShip(Ship ship) {
		setFieldRangeState(ship.getPlacement(), State.MAST);
	}

	public enum State {
		MISS, HIT, MAST, EMPTY
	}

	private State[][] board = new State[10][10];

	public Board() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				board[i][j] = State.EMPTY;
			}
		}
	}

	public Board(List<Board.State> places) {
		int n = 0;

		for (int y = 0; y < board.length; y++) {
			for (int x = 0; x < board.length; x++) {
				board[x][y] = places.get(x + n);
			}
			n = n + 10;
		}
	}

	public void setField(Field field, State result) {
		board[field.getXCoordinate()][field.getYCoordinate()] = result;
	}

	public void setFieldRangeState(FieldRange fieldRange, State result) {

		for (Field field : fieldRange.getRangeFields()) {
			board[field.getXCoordinate()][field.getYCoordinate()] = result;
		}
	}

	public State getFieldState(Field field) {
		return board[field.getXCoordinate()][field.getYCoordinate()];
	}

	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append("   A B C D E F G H I J \n");

		for (int y = 0; y < board.length; y++) {
			sb.append(String.format("%2s", y + 1)).append(" ");
			for (int x = 0; x < board.length; x++) {
				if (board[x][y] == State.EMPTY) {
					sb.append("  ");
				} else if (board[x][y] == State.MISS) {
					sb.append(GameChars.MISS + " ");
				} else if (board[x][y] == State.HIT) {
					sb.append(GameChars.SHIP_HIT + " ");
				} else {
					sb.append(GameChars.SHIP_MAST + " ");
				}
			}
			sb.append("\n");
		}

		return sb.toString();
	}

	public boolean isFieldEmpty(Field field) {
		return getFieldState(field) == State.EMPTY;
	}

	public boolean isFieldMast(Field field) {
		return getFieldState(field) == State.MAST;
	}
}
