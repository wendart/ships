package com.github.pbrzezinski.ships;

import com.github.pbrzezinski.ships.state.PlayerState;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Player {//TODO REFACTOR głębszy

	private String name;
	private Console console;
	private Board ownBoard = new Board();
	private Board enemyBoard = new Board();
	private List<Ship> ships = new ArrayList<>();

	public Player(String name, Console console) {
		this.name = name;
		this.console = console;
	}

	public Player(PlayerState playerState, Console console) {
		this.ships = playerState.getShips().stream()
				.map(FieldRange::new)
				.map(Ship::new)// poprzednia notacja range -> new Ship(range)
				.collect(toList());

		this.name = playerState.getName();
		this.ownBoard = new Board(playerState.getBoard());
		this.enemyBoard = new Board(playerState.getRadar());
		this.console = console;
	}


	public String getName() {
		return name;
	}

	public void prepareBoard() {
		Engine.SHIP_SPEC.forEach(shipSpec -> {
			for (int i = 0; i < shipSpec.getShipCount(); i++) {
				placeShip(shipSpec);
				console.printBoard(ownBoard);
			}
		});
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
		return ships.stream()
				.anyMatch(ship -> ship.isCollision(otherShip));
	}

	public void showBoards() {
		console.printBoards(ownBoard, enemyBoard);
	}

	public PlayerDecision makeDecisionShootOrSave() {
		while (true) {
			String shotPlacement = console.askForShot(name);
			if (!shotPlacement.equals("SAVE")) {
				if (enemyBoard.isFieldEmpty(new Field(shotPlacement))) {
					return new PlayerDecision(shotPlacement);
				}
				console.writeMessage("You've already shot here ;) ");
			} else {
				return new PlayerDecision(shotPlacement);
			}

		}
	}

	public ShotResult checkShot(Field shotPlacement) {
		if (ownBoard.markShot(shotPlacement) == Board.State.MISS) {
			return ShotResult.miss();
		}

		return ships.stream()
				.filter(ship -> ship.getPlacement().getRangeFields().contains(shotPlacement))
				.filter(this::isSink)
				.findFirst()
				.map(ShotResult::sink)
				.orElseGet(ShotResult::hit);
	}

	public void shipDeletion(Ship ship) {
		ships.remove(ship);
	}

	private boolean isSink(Ship ship) {
		return ship.getPlacement().getRangeFields().stream()
				.noneMatch(ownBoard::isFieldMast);
	}

	public void markShot(ShotResult shotResult, Field shotPlacement) {
		if (shotResult.getHitMark() == ShotResult.ShotMark.HIT) {
			enemyBoard.setField(shotPlacement, Board.State.HIT);
		} else if (shotResult.getHitMark() == ShotResult.ShotMark.MISS) {
			enemyBoard.setField(shotPlacement, Board.State.MISS);
		} else {
			List<Field> shipPlacement = shotResult.getShip().getPlacement().getRangeFields();
			List<Field> missFields = shotResult.getShip().getPlacement().extend().getRangeFields();
			missFields.removeAll(shipPlacement);

			missFields.forEach(field -> enemyBoard.setField(field, Board.State.MISS));
			enemyBoard.setField(shotPlacement, Board.State.HIT);
		}
	}


	public PlayerState save(Boolean active) {
		List<String> stringShips = ships.stream()
				.map(ship -> ship.getPlacement().getAsString())
				.collect(toList());

		return new PlayerState(
				name,
				ownBoard.save(),
				enemyBoard.save(),
				stringShips,
				active
		);
	}
}
