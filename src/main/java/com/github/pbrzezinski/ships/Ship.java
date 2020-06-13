package com.github.pbrzezinski.ships;

public class Ship {

	private FieldRange placement;


	public Ship(FieldRange shipPlacement) {

		placement = shipPlacement;
		if (!placement.isOneFieldWide()) {
			throw new IllegalArgumentException("Ship must be one field wide");
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
