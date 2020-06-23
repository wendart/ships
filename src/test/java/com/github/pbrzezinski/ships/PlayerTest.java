package com.github.pbrzezinski.ships;

import com.github.pbrzezinski.ships.board.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sun.net.www.content.text.plain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

class PlayerTest {


	@Mock
	private GameInterface gameInterface;

	private Player player;
	private Board ownBoard;
	private Board enemyBoard;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
		ownBoard = new Board();
		enemyBoard = new Board();
		player = new Player("TestGuy", gameInterface, enemyBoard, ownBoard);
	}

	@Test
	void shouldProperlyPrepareBoard() {
		List<Ship> ships = getShipList();

		when(gameInterface.prepareShips(eq(player.getName()), any()))
				.thenReturn(ships);

		player.prepareBoard();

		assertThat(ownBoard.getFieldState(new Field("A1"))).isEqualTo(Board.State.MAST);
		assertThat(ownBoard.getFieldState(new Field("A2"))).isEqualTo(Board.State.MAST);
		assertThat(ownBoard.getFieldState(new Field("A3"))).isEqualTo(Board.State.MAST);
		assertThat(ownBoard.getFieldState(new Field("A4"))).isEqualTo(Board.State.MAST);
		assertThat(ownBoard.getFieldState(new Field("I10"))).isEqualTo(Board.State.MAST);
		assertThat(ownBoard.getFieldState(new Field("J10"))).isEqualTo(Board.State.MAST);
	}

	@Test
	void shouldNotAliveAfterAllShipsSunk() {
		List<Ship> ships = getShipList();

		when(gameInterface.prepareShips(eq(player.getName()), any()))
				.thenReturn(ships);

		player.prepareBoard();

		player.takeShot(new Field("A1"));
		player.takeShot(new Field("A2"));
		player.takeShot(new Field("A3"));
		player.takeShot(new Field("A4"));
		player.takeShot(new Field("I10"));
		player.takeShot(new Field("J10"));

		assertThat(player.isAlive()).isFalse();
	}

	@Test
	void shouldProperlyMarkHitShot() {
		List<Ship> ships = getShipList();

		when(gameInterface.prepareShips(eq(player.getName()), any()))
				.thenReturn(ships);

		player.prepareBoard();

		player.markShot(ShotResult.hit(), new Field("A1"));
		player.markShot(ShotResult.hit(), new Field("I10"));
		player.markShot(ShotResult.sink(ships.get(1)), new Field("J10"));
		player.markShot(ShotResult.miss(), new Field("B5"));

		assertThat(enemyBoard.getFieldState(new Field("A1"))).isEqualTo(Board.State.HIT);
		assertThat(enemyBoard.getFieldState(new Field("I10"))).isEqualTo(Board.State.HIT);
		assertThat(enemyBoard.getFieldState(new Field("J10"))).isEqualTo(Board.State.HIT);
		assertThat(enemyBoard.getFieldState(new Field("H9"))).isEqualTo(Board.State.MISS);
		assertThat(enemyBoard.getFieldState(new Field("I9"))).isEqualTo(Board.State.MISS);
		assertThat(enemyBoard.getFieldState(new Field("J9"))).isEqualTo(Board.State.MISS);
		assertThat(enemyBoard.getFieldState(new Field("H10"))).isEqualTo(Board.State.MISS);
		assertThat(enemyBoard.getFieldState(new Field("B5"))).isEqualTo(Board.State.MISS);

	}

	private List<Ship> getShipList() {
		return new ArrayList<>(asList(
				new Ship(new FieldRange("A1:A4")),
//				new Ship(new FieldRange("c1:c3")),
//				new Ship(new FieldRange("e5:e6")),
//				new Ship(new FieldRange("G7:G7")),
//				new Ship(new FieldRange("A10:D10")),
//				new Ship(new FieldRange("A7:C7")),
				new Ship(new FieldRange("I10:J10"))
		));
	}
}