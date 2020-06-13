package com.github.pbrzezinski.ships;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FieldRangeTest {
	@Test
	void shouldProperlyCreateRangedFieldsList() {
		FieldRange fieldRange = new FieldRange("A1:A3");

		assertThat(fieldRange.getRangeFields())       //extracting(field -> field.getAsString()) pozwala wyjąć coś z kalsy (inne parametry w contains)
				.hasSize(3)
				.containsExactly(new Field("A1"), new Field("A2"), new Field("A3")); //tylko kiedy mam equals
	}
}