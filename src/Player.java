import java.util.ArrayList;
import java.util.List;

public class Player {

	private String name;
	private Console console;
	private Board ownBoard = new Board();
	private Board enemyBoard = new Board();
	private List<Ship> ships = new ArrayList<>();
	private boolean alive = true;

	public Player(String name, Console console) {
		this.name = name;
		this.console = console;
	}

	public String getName() {
		return name;
	}

	public void prepareBoard() {

		for (ShipSpec shipSpec : Engine.SHIP_SPEC) {
			for (int i = 0; i < shipSpec.getShipCount(); i++) {
				placeShip(shipSpec);
				console.printBoard(ownBoard);
			}
		}
	}

	public boolean isAlive() {
		if (ships.isEmpty()) {
			alive = false;
		} else {
			alive = true;
		}
		return alive;
	}

	private void placeShip(ShipSpec shipSpec) {

		boolean done = false;

		while (!done) {
			try {
				FieldRange shipPlacement = console.askForShipPlacement(shipSpec.getMastCount(), getName());
				Ship ship = new Ship(shipPlacement);

				if (ship.getSize() != shipSpec.getMastCount()) {
					console.writeMessage("Bad ship size");
				} else if (!isCollisionWithOther(ship)) {
					ships.add(ship);
					ownBoard.setFieldRangeState(shipPlacement, Board.State.MAST);
					done = true;
				} else {
					console.writeMessage("This ship is in collision with other one on your board");
				}

			} catch (Exception ex) {
				console.writeMessage(ex.getMessage());
			}
		}

	}

	private boolean isCollisionWithOther(Ship otherShip) {
		for (Ship ship : ships) {
			if (ship.isCollision(otherShip)) {
				return true;
			}
		}
		return false;
	}

	public void showBoards() {
		console.printBoards(ownBoard, enemyBoard);
	}

	public Field shoot() {
		while (true) {
			Field shotPlacement = console.askForShot(name);
			if (enemyBoard.isFieldEmpty(shotPlacement)) {
				return shotPlacement;
			}
			console.writeMessage("You've already shot here ;) ");
		}
	}

	public ShotResult checkShot(Field shotPlacement) {
		if (ownBoard.markShot(shotPlacement) == Board.State.MISS) {
			return new ShotResult(ShotResult.ShotMark.MISS, null);
		}

		for (Ship ship : ships) {
			if (ship.getPlacement().getRangeFields().contains(shotPlacement)) {
				if (isSink(ship)) {
					return new ShotResult(ShotResult.ShotMark.SINK, ship);
				}
			}
		}
		return new ShotResult(ShotResult.ShotMark.HIT, null);
	}

	public void shipDeletion(Ship ship) {
		ships.remove(ship);
	}

	private boolean isSink(Ship ship) {
		for (Field rangeField : ship.getPlacement().getRangeFields()) {
			if (ownBoard.isFieldMast(rangeField)) {
				return false;
			}
		}
		return true;
	}

	public void markShot(ShotResult shotResult, Field shotPlacement) {
		if (shotResult.getHitMark() == ShotResult.ShotMark.HIT) {
			enemyBoard.setField(shotPlacement, Board.State.HIT);
		} else if (shotResult.getHitMark() == ShotResult.ShotMark.SINK) {

			List<Field> shipPlacement = shotResult.getShip().getPlacement().getRangeFields();
			List<Field> missFields = shotResult.getShip().getPlacement().extend().getRangeFields();
			missFields.removeAll(shipPlacement);

			for (Field field : missFields) {
				enemyBoard.setField(field, Board.State.MISS);
			}

			enemyBoard.setField(shotPlacement, Board.State.HIT);
		} else {
			enemyBoard.setField(shotPlacement, Board.State.MISS);
		}
	}
}
