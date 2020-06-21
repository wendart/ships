package com.github.pbrzezinski.ships.board;

import com.github.pbrzezinski.ships.Field;
import com.github.pbrzezinski.ships.FieldRange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.beans.beancontext.BeanContextChild;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

	@Test
	void shouldProperlyCreateBoard(){
		Board board = new Board();

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				assertThat(board.getBoard()[i][j]).isEqualTo(Board.State.EMPTY);
			}
		}
	}

	@ParameterizedTest
	@CsvSource(value = {"MISS,A1,0,0", "HIT,D4,3,3", "MAST,H7,7,6", "EMPTY,C2,2,1"})
	void shouldProperlySetFieldState(Board.State expectedValue, String field, int XCoordinate, int YCoordinate){
		Board board = new Board();
		board.setField(new Field(field),expectedValue);
		assertThat(board.getBoard()[XCoordinate][YCoordinate]).isEqualTo(expectedValue);
	}

	@ParameterizedTest
	@CsvSource(value = {"MISS,A1:A2,0,0,0,1", "HIT,D4:D5,3,3,3,4", "MAST,H7:H8,7,6,7,7", "EMPTY,C2:C3,2,1,2,2"})
	void shouldProperlySetFieldRangeState(Board.State expectedValue, String fieldRange, int XCoordinate1, int YCoordinate1, int XCoordinate2, int YCoordinate2){
		Board board = new Board();
		FieldRange range = new FieldRange(fieldRange);
		board.setFieldRangeState(range,expectedValue);

		assertThat(board.getBoard()[XCoordinate1][YCoordinate1]).isEqualTo(expectedValue);
		assertThat(board.getBoard()[XCoordinate2][YCoordinate2]).isEqualTo(expectedValue);
	}

	@Test
	void shouldProperlyRecognizeEmptyField(){
		Board board = new Board();
		assertThat(board.isFieldEmpty(new Field("A2"))).isEqualTo(true);
	}

	@Test
	void shouldProperlyRecognizeMastField(){
		Board board = new Board();
		Field field = new Field("A1");
		board.setField(field, Board.State.MAST);
		assertThat(board.isFieldEmpty(field)).isEqualTo(false);
	}

	@Test
	void shouldProperlyMarkHitShot(){
		Board board = new Board();
		Field field = new Field("B8");
		board.setField(field, Board.State.HIT);
		assertThat(board.markShot(field)).isEqualTo(Board.State.HIT);
	}

	@Test
	void shouldProperlyMarkMissedShot(){
		Board board = new Board();
		assertThat(board.markShot(new Field("A5"))).isEqualTo(Board.State.MISS);
	}

}