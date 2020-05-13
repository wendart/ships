package com.github.pbrzezinski.ships;

public class ShotResult {

	public enum ShotMark {
		HIT, MISS, SINK
	}

	private ShotMark hitMark;
	private Ship ship;

	public ShotResult(ShotMark hitMark, Ship ship) {
		this.hitMark = hitMark;
		this.ship = ship;
	}

	public ShotMark getHitMark() {
		return hitMark;
	}

	public Ship getShip() {
		return ship;
	}
}
