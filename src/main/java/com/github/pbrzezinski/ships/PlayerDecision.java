package com.github.pbrzezinski.ships;

public class PlayerDecision {
	private String decision;

	public PlayerDecision(String decision) {
		this.decision = decision;
	}

	public boolean isSaveGame() {
		return decision.equals("SAVE");
	}

	public Field toField() {
		return  new Field(decision);
	}
}
