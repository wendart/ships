package com.github.pbrzezinski.ships;

import com.github.pbrzezinski.ships.board.Board;
import com.github.pbrzezinski.ships.console.ConsoleSpec;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//1. Walidacja
//2. Wypisanie błędnych wiadomości
//3. Iteracja po czymś
//4. Iteracja do skutku
//5.

public class GameInterface {

	private Console console;

	public GameInterface(Console console) {
		this.console = console;
	}

	public List<Ship> prepareShips(String playerName, Board playerBoard) {
		List<Ship> ships = new ArrayList<>();
		Engine.SHIP_SPEC.forEach(shipSpec -> {
			for (int i = 0; i < shipSpec.getShipCount(); i++) {
				Ship ship = placeShip(shipSpec, ships, playerName);
				ships.add(ship);
				playerBoard.placeShip(ship);
				console.printBoard(playerBoard);
			}
		});

		return ships;
	}

	public PlayerDecision makeDecisionShootOrSave(String name, Board enemyBoard) {
		return ConsoleSpec.<PlayerDecision>askFor("Where do you want to shoot " + name + "? ")
				.map(PlayerDecision::new)
				.validate(pd -> pd.isSaveGame() || enemyBoard.isFieldEmpty(pd.toField()))
				.execute();
	}

//	public PlayerDecision makeDecisionShootOrSave(String playerName, Board enemyBoard) {
//		while (true) {
//			PlayerDecision decision = new PlayerDecision(console.askForShot(playerName));
//			if (decision.isSaveGame()) {
//				return decision;
//			} else if (enemyBoard.isFieldEmpty(decision.toField())) {
//				return decision;
//			} else {
//				console.writeMessage("You've already shot here ;) ");
//			}
//		}
//
//	}


//	private Ship placeShip(ShipSpec shipSpec, List<Ship> currentShips, String playerName) {
//
//	}

	private Ship placeShip(ShipSpec shipSpec, List<Ship> currentShips, String playerName) {
		while (true) {
			try {
				FieldRange shipPlacement = console.askForShipPlacement(shipSpec.getMastCount(), playerName);
				Ship ship = new Ship(shipPlacement);

				Optional<String> errorMsg = validateShip(ship, shipSpec, currentShips);
				if (errorMsg.isPresent()) {
					console.writeMessage(errorMsg.get());
				} else {
					return ship;
				}
			} catch (Exception ex) {
				console.writeMessage(ex.getMessage());
			}
		}
	}

	private Optional<String> validateShip(Ship ship, ShipSpec shipSpec, List<Ship> currentShips) {
		if (ship.getSize() != shipSpec.getMastCount()) {
			return Optional.of("Bad ship size");
		}

		if (isCollisionWithOther(ship, currentShips)) {
			return Optional.of("This ship is in collision with other one on your board");
		}

		return Optional.empty();
	}

	private boolean isCollisionWithOther(Ship otherShip, List<Ship> currentShips) {
		return currentShips.stream()
				.anyMatch(ship -> ship.isCollision(otherShip));
	}


	public void printBoards(Board ownBoard, Board enemyBoard) {
		console.printBoards(ownBoard, enemyBoard);
	}
}




