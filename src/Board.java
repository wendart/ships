public class Board {

	public State markShot(Field shotPlacement) {
		if (isFieldEmpty(shotPlacement)) {
			//setField(shotPlacement, State.MISS);
			return State.MISS;
		} else {
			//setField(shotPlacement, State.HIT);
			return State.HIT;
		}
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

		for (int i = 0; i < board.length; i++) {
			sb.append(String.format("%2s", i + 1)).append(" ");
			for (int j = 0; j < board.length; j++) {
				if (board[j][i] == State.EMPTY) {
					sb.append(". ");
				} else if (board[j][i] == State.MISS) {
					sb.append("O ");
				} else {
					sb.append("X ");
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
