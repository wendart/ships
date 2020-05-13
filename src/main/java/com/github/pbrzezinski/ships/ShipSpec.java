package com.github.pbrzezinski.ships;

public class ShipSpec {
	private int mastCount;
	private int shipCount;

	public ShipSpec(int mastCount, int shipCount) {
		this.mastCount = mastCount;
		this.shipCount = shipCount;
	}

	public int getMastCount() {
		return mastCount;
	}

	public int getShipCount() {
		return shipCount;
	}
}
