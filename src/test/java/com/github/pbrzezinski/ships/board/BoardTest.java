package com.github.pbrzezinski.ships.board;

import com.github.pbrzezinski.ships.Field;
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
	void shouldProperlySetField(Board.State expectedValue, String filed, int XCoordinate, int YCoordinate){
		Board board = new Board();
		board.setField(new Field(filed),expectedValue);
		assertThat(board.getBoard()[XCoordinate][YCoordinate]).isEqualTo(expectedValue);
	}

}