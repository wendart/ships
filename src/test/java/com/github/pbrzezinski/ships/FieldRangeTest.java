package com.github.pbrzezinski.ships;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FieldRangeTest {
	@Test
	void shouldProperlyCreateRangedFieldsList() {
		FieldRange fieldRange = new FieldRange("A1:A3");

		assertThat(fieldRange.getRangeFields())       //extracting(field -> field.getAsString()) pozwala wyjąć coś z kalsy (inne parametry w contains)
				.hasSize(3)
				.containsExactly(new Field("A1"), new Field("A2"), new Field("A3")); //tylko kiedy mam equals
	}

	@ParameterizedTest
	@CsvSource(value = {"A1:C1,A1,C1", "A1:A3,A1,A3", "A1:H8,A1,H8","H8:H8,H8,H8", "H8:A1,A1,H8", "B2:D4,B2,D4", "A1:C2,A1,C2","c3:i7,C3,I7"})
	void shouldProperlyCreateFieldRange(String testedValue, String expectedLowerBoundary, String expectedUpperBoundary) {
		FieldRange range = new FieldRange(testedValue);

		assertThat(range.getLowerBoundary()).isEqualTo(new Field(expectedLowerBoundary));
		assertThat(range.getUpperBoundary()).isEqualTo(new Field(expectedUpperBoundary));
	}

	@ParameterizedTest(name = "Should throw exception for field format: {0}")
	@ValueSource(strings = {"A1:", "g", "X:33", "V34:F45" , ":A4", "  ", "", "\n"})
	void shouldThrowExceptionIllegalFieldFormat(String testedValue) {
		assertThrows(
				IllegalArgumentException.class,
				()-> new FieldRange(testedValue)
		);
	}

	@Test
	void shouldThrowNullptrExceptionWhenUsedFieldConstructor() {
		assertThrows(
				NullPointerException.class,
				()-> new FieldRange((Field) null)
		);
	}

	@Test
	void shouldThrowNullptrExceptionWhenUsedStringConstructor() {
		assertThrows(
				NullPointerException.class,
				()-> new FieldRange((String) null)
		);
	}

	@Test
	void shouldProperlyCreateNewExtendedFieldRange() {
		FieldRange range = new FieldRange("B2:D4");
		range = range.extend();

		assertThat(range.getLowerBoundary()).isEqualTo(new Field("A1"));
		assertThat(range.getUpperBoundary()).isEqualTo(new Field("E5"));
	}

	@Test
	void shouldProperlyCreateNewExtendedFieldRangeAtCorner() {
		FieldRange range = new FieldRange("A1:C2");
		range = range.extend();

		assertThat(range.getLowerBoundary()).isEqualTo(new Field("A1"));
		assertThat(range.getUpperBoundary()).isEqualTo(new Field("D3"));
	}

	@Test
	void shouldProperlyRecogniseOneFiledFieldRange() {
		FieldRange range = new FieldRange("C7:C7");

		assertThat(range.isOneFieldWide()).isEqualTo(true);
	}

	@Test
	void shouldProperlyReturnFieldRangeAsString() {
		FieldRange range = new FieldRange("D1:d3");

		assertThat(range.getAsString()).isEqualTo("D1:D3");
	}
}

