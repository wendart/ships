package com.github.pbrzezinski.ships;

public class ShotResult {

	public enum ShotMark {
		HIT, MISS, SINK
	}

	private ShotMark hitMark;
	private Ship ship;

	private ShotResult(ShotMark hitMark, Ship ship) {
		this.hitMark = hitMark;
		this.ship = ship;
	}

	public static ShotResult miss() {
		return new ShotResult(ShotMark.MISS, null);
	}

	public static ShotResult sink(Ship ship) {
		return new ShotResult(ShotMark.SINK, ship);
	}

	public static ShotResult hit() {
		return new ShotResult(ShotMark.HIT, null);
	}

	public ShotMark getHitMark() {
		return hitMark;
	}

	public Ship getShip() {
		return ship;
	}
}
