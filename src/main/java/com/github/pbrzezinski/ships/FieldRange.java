package com.github.pbrzezinski.ships;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FieldRange {// TODO SPR Z FIELDEM

	private Field lowerBoundary;
	private Field upperBoundary;

	public FieldRange(String range) {

		Objects.requireNonNull(range, "Range can not be null");
		range = range.trim();
		String[] rangeParts = range.split(":");
		validateFieldRangeFormat(range, rangeParts);

		Field field1 = new Field(rangeParts[0]);
		Field field2 = new Field(rangeParts[1]);

		if (field1.compareTo(field2) < 0) {
			lowerBoundary = field1;
			upperBoundary = field2;
		} else {
			lowerBoundary = field2;
			upperBoundary = field1;
		}
	}

	private void validateFieldRangeFormat(String range, String[] rangeParts) {
		if (rangeParts.length != 2) {
			throw new IllegalArgumentException("Illegal range format: " + range);
		}
	}

	public FieldRange(Field field) {
		this.lowerBoundary = field;
		this.upperBoundary = field;
	}

	private FieldRange(Field lowerBoundary, Field upperBoundary) {
		this.lowerBoundary = lowerBoundary;
		this.upperBoundary = upperBoundary;
	}

	public Field getLowerBoundary() {
		return lowerBoundary;
	}

	public Field getUpperBoundary() {
		return upperBoundary;
	}

	public List<Field> getRangeFields() {

		List<Field> list = new ArrayList<>();
		for (int y = lowerBoundary.getYCoordinate(); y <= upperBoundary.getYCoordinate(); y++) {
			for (int x = lowerBoundary.getXCoordinate(); x <= upperBoundary.getXCoordinate(); x++) {
				list.add(new Field(x, y));
			}
		}
		return list;
	}

	public FieldRange extend() {
		return new FieldRange(
				new Field(Math.max(lowerBoundary.getXCoordinate() - 1, 0), Math.max(lowerBoundary.getYCoordinate() - 1, 0)),
				new Field(Math.min(upperBoundary.getXCoordinate() + 1, Engine.BOARD_SIZE - 1), Math.min(upperBoundary.getYCoordinate() + 1, Engine.BOARD_SIZE - 1))
		);
	}

	public boolean isOneFieldWide() {
		return lowerBoundary.getXCoordinate() == upperBoundary.getXCoordinate() || lowerBoundary.getYCoordinate() == upperBoundary.getYCoordinate();
	}

	public String getAsString() {
		return lowerBoundary.getAsString() + ":" + upperBoundary.getAsString();
	}

	public String getAsString() {
		return lowerBoundary.getAsString()
				+ ":" + upperBoundary.getAsString();
	}
}
