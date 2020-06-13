package com.github.pbrzezinski.ships;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.List;

public class Ship {//TODO EW STREAM

	private FieldRange placement;


	public Ship(FieldRange shipPlacement) {

		placement = shipPlacement;
		if (!placement.isOneFieldWide()) {
			throw new IllegalArgumentException("com.github.pbrzezinski.ships.Ship must be one field wide");
		}
	}

	public boolean isCollision(Ship otherShip) {
		List<Field> placementFields = placement.getRangeFields();
		FieldRange extend = otherShip.placement.extend();
		for (Field field : extend.getRangeFields()) {
			if (placementFields.contains(field)) {
				return true;
			}
		}
		return false;
	}

	public int getSize() {
		return placement.getRangeFields().size();
	}

	public FieldRange getPlacement() {
		return placement;
	}
}
