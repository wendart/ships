package com.github.pbrzezinski.ships;

import com.github.pbrzezinski.ships.board.Board;
import com.github.pbrzezinski.ships.state.PlayerState;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Player {

	private String name;
	private Board ownBoard = new Board();
	private Board enemyBoard = new Board();
	private List<Ship> ships = new ArrayList<>();
	private GameInterface gameInterface;

	public static Player createPlayer(String name, GameInterface gameInterface){
		return new Player(name, gameInterface, new Board(), new Board());
	}

	Player(String name, GameInterface gameInterface, Board enemyBoard, Board ownBoard) {
		this.name = name;
		this.gameInterface = gameInterface;
		this.ownBoard = ownBoard;
		this.enemyBoard = enemyBoard;
	}

	public static Player createPlayerFromGameState(PlayerState state, GameInterface gameInterface){
		return new Player(state,gameInterface);
	}

	private Player(PlayerState playerState, GameInterface gameInterface) {
		this.ships = playerState.getShips().stream()
				.map(range -> new FieldRange(range))
				.map(Ship::new)// poprzednia notacja range -> new Ship(range)
				.collect(toList());

		this.name = playerState.getName();
		this.ownBoard = new Board(playerState.getBoard());
		this.enemyBoard = new Board(playerState.getRadar());
		this.gameInterface = gameInterface;
	}


	public String getName() {
		return name;
	}

	public void prepareBoard() {
		this.ships = gameInterface.prepareShips(name, ownBoard);
		this.ships.forEach(ship -> ownBoard.placeShip(ship));
	}

	public boolean isAlive() {
		return !ships.isEmpty();
	}

	public void showBoards() {
		gameInterface.printBoards(ownBoard, enemyBoard);
	}

	public PlayerDecision makeDecisionShootOrSave() {
		return gameInterface.makeDecisionShootOrSave(name, enemyBoard);
	}

	public ShotResult takeShot(Field shotPlacement) {
		if (ownBoard.markShot(shotPlacement) == Board.State.MISS) {
			return ShotResult.miss();
		}

		ShotResult shotResult = ships.stream()
				.filter(ship -> ship.getPlacement().getRangeFields().contains(shotPlacement))
				.filter(ship1 -> isSink(ship1))
				.findFirst()
				.map(ship2 -> ShotResult.sink(ship2))
				.orElseGet(() -> ShotResult.hit());

		if(shotResult.getHitMark() == ShotResult.ShotMark.SINK){
			shipDeletion(shotResult.getShip());
		}

		return shotResult;
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
