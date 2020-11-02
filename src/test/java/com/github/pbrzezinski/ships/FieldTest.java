package com.github.pbrzezinski.ships;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FieldTest {

	@ParameterizedTest
	@CsvSource(value = {"A1,0,0", "d5,3,4", " d5 ,3,4",})
	void shouldProperlyCreateField(String testedValue, int expectedXCoordinate, int expectedYCoordinate) {
		Field field = new Field(testedValue);

		assertThat(field.getXCoordinate()).isEqualTo(expectedXCoordinate);
		assertThat(field.getYCoordinate()).isEqualTo(expectedYCoordinate);
	}

	@ParameterizedTest
	@ValueSource(strings = {"P1", "A11", "U21", "6", "D"})
	void shouldThrowExceptionWhenLetterOutOfRange(String testedValue) {
		assertThrows(
				IllegalArgumentException.class,
				() -> new Field(testedValue)
		);
	}

	@Test
	void shouldThrowExceptionWhenXOutOfRange() {
		assertThrows(
				IllegalArgumentException.class,
				() -> new Field(12,6)
		);
	}

	@Test
	void shouldThrowExceptionWhenYOutOfRange() {
		assertThrows(
				IllegalArgumentException.class,
				() -> new Field(3,-3)
		);
	}

	@Test
	void shouldProperlyCreateFieldFromCoordinates() {
		Field field = new Field(3,4);

		assertThat(field.getXCoordinate()).isEqualTo(3);
		assertThat(field.getYCoordinate()).isEqualTo(4);
	}

	@Test
	void shouldProperlyReturnFieldAsString() {
		Field field = new Field("d5");

		assertThat(field.getAsString()).isEqualTo("D5");
	}

	@Test
	void shouldProperlyCompareFieldsInTheSameRow() {
		Field field = new Field("d5");
		Field otherField = new Field("f5");

		assertThat(field).isLessThan(otherField);
	}

	@Test
	void shouldProperlyCompareFieldsInTheSameColumn() {
		Field field = new Field("B1");
		Field otherField = new Field("B7");

		assertThat(field).isLessThan(otherField);
	}

	@Test
	void shouldProperlyCompareSameFields() {
		Field field = new Field("H7");
		Field otherField = new Field("H7");

		assertThat(field).isEqualByComparingTo(otherField);
	}

	@Test
	void shouldProperlyCompareFields() {
		Field field = new Field("J1");
		Field otherField = new Field("A2");

		assertThat(field).isLessThan(otherField);
	}
}