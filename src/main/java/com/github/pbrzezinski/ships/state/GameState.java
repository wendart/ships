package com.github.pbrzezinski.ships.state;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GameState {
	private PlayerState player1;
	private PlayerState player2;

	@JsonCreator
	public GameState(
			@JsonProperty("player1") PlayerState player1,
			@JsonProperty("player2") PlayerState player2) {
		this.player1 = player1;
		this.player2 = player2;
	}

	public PlayerState getPlayer1() {
		return player1;
	}

	public PlayerState getPlayer2() {
		return player2;
	}
}
