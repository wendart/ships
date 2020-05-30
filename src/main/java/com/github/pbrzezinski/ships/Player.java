package com.github.pbrzezinski.ships;

import com.github.pbrzezinski.ships.state.PlayerState;

import java.util.ArrayList;
import java.util.List;

public class Player {

	private String name;
	private Console console;
	private Board ownBoard = new Board();
	private Board enemyBoard = new Board();
	private List<Ship> ships = new ArrayList<>();

	public Player(String name, Console console) {
		this.name = name;
		this.console = console;
	}

	public Player(PlayerState playerState, Console console){

		List<Ship> ships = new ArrayList<>();
		for (String ship : playerState.getShips()) {
			ships.add(new Ship(new FieldRange(ship)));
		}

		this.name = playerState.getName();
		this.ownBoard = new Board(playerState.getBoard());
		this.enemyBoard = new Board(playerState.getRadar());
		this.ships = ships;
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
		return !ships.isEmpty();
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

	public String shoot() {
		while (true) {
			String shotPlacement = console.askForShot(name);
			if(!shotPlacement.equals("SAVE")) {
				if (enemyBoard.isFieldEmpty(new Field(shotPlacement))) {
					return shotPlacement;
				}
				console.writeMessage("You've already shot here ;) ");
			} else {
				return shotPlacement;
			}

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


	public PlayerState save(Boolean active){

		List<String> stringShips = new ArrayList<>();
		for (Ship ship : ships) {
			stringShips.add(ship.getPlacement().getAsString());
		}

		return new PlayerState(
				name,
				ownBoard.save(),
				enemyBoard.save(),
				stringShips,
				active
		);
	}
}
