package com.github.pbrzezinski.ships;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FieldTest {

	@Test
	void shouldProperlyCreateField() {
		Field field = new Field("A1");

		assertThat(field.getXCoordinate()).isEqualTo(0);
		assertThat(field.getYCoordinate()).isEqualTo(0);
	}

	@Test
	void shouldThrowExceptionWhenLetterOutOfRange() {
		assertThrows(
				IllegalArgumentException.class,
				() -> new Field("P1")
		);
	}

	@Test
	void shouldThrowExceptionWhenNumberOutOfRange() {
		assertThrows(
				IllegalArgumentException.class,
				() -> new Field("A11")
		);
	}

	@Test
	void shouldThrowExceptionWhenNumberAndLetterOutOfRange() {
		assertThrows(
				IllegalArgumentException.class,
				() -> new Field("U21")
		);
	}

	@Test
	void shouldThrowExceptionWhenNoLetterIsPresent() {
		assertThrows(
				IllegalArgumentException.class,
				() -> new Field("6")
		);
	}

	@Test
	void shouldThrowExceptionWhenNoNumberIsPresent() {
		assertThrows(
				IllegalArgumentException.class,
				() -> new Field("D")
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
	void shouldProperlyCreateFieldFromLowerCase() {
		Field field = new Field("d5");

		assertThat(field.getXCoordinate()).isEqualTo(3);
		assertThat(field.getYCoordinate()).isEqualTo(4);
	}

	@Test
	void shouldProperlyCreateFieldFromCoordinates() {
		Field field = new Field(3,4);

		assertThat(field.getXCoordinate()).isEqualTo(3);
		assertThat(field.getYCoordinate()).isEqualTo(4);
	}

	@Test
	void shouldProperlyCreateFieldWhenWhiteSpace() {
		Field field = new Field(" d5 ");

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