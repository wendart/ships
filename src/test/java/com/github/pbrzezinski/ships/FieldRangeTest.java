package com.github.pbrzezinski.ships;

import org.junit.jupiter.api.Test;

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

	@Test
	void shouldProperlyCreateOneFieldWideHorizontalFieldRange() {
		FieldRange range = new FieldRange("A1:C1");

		assertThat(range.getLowerBoundary()).isEqualTo(new Field("A1"));
		assertThat(range.getUpperBoundary()).isEqualTo(new Field("C1"));
	}

	@Test
	void shouldProperlyCreateOneFieldWideVerticalFieldRange() {
		FieldRange range = new FieldRange("A1:A3");

		assertThat(range.getLowerBoundary()).isEqualTo(new Field("A1"));
		assertThat(range.getUpperBoundary()).isEqualTo(new Field("A3"));
	}

	@Test
	void shouldProperlyCreateFieldRange() {
		FieldRange range = new FieldRange("A1:H8");

		assertThat(range.getLowerBoundary()).isEqualTo(new Field("A1"));
		assertThat(range.getUpperBoundary()).isEqualTo(new Field("H8"));
	}

	@Test
	void shouldProperlyCreateOneFieldFieldRange() {
		FieldRange range = new FieldRange("H8:H8");

		assertThat(range.getLowerBoundary()).isEqualTo(new Field("H8"));
		assertThat(range.getUpperBoundary()).isEqualTo(new Field("H8"));
	}

	@Test
	void shouldThrowExtensionIllegalFieldFormat() {
		assertThrows(
				IllegalArgumentException.class,
				()-> new FieldRange("A1:")
		);
	}

	@Test
	void shouldProperlyCreateInvertedFieldRange() {
		FieldRange range = new FieldRange("H8:A1");

		assertThat(range.getLowerBoundary()).isEqualTo(new Field("A1"));
		assertThat(range.getUpperBoundary()).isEqualTo(new Field("H8"));
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

	@Test
	void shouldProperlyCreateFieldRangeFromLowerCase() {
		FieldRange range = new FieldRange("c3:i7");

		assertThat(range.getLowerBoundary()).isEqualTo(new Field("C3"));
		assertThat(range.getUpperBoundary()).isEqualTo(new Field("I7"));
	}
}

