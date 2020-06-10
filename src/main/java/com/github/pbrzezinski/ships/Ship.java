package com.github.pbrzezinski.ships;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.ArrayList;
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
		return otherShip.placement.extend().getRangeFields().stream()
				.anyMatch(o -> placement.getRangeFields().contains(o));
	}

	public int getSize() {
		return placement.getRangeFields().size();
	}

	public FieldRange getPlacement() {
		return placement;
	}
}
