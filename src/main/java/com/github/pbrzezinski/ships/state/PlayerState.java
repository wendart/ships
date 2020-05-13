package com.github.pbrzezinski.ships.state;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.pbrzezinski.ships.Board;

import java.util.List;

public class PlayerState {
	private String name;
	private List<Board.State> board;
	private List<Board.State> radar;
	private Boolean active = false;

	@JsonCreator
	public PlayerState(
			@JsonProperty("name") String name,
			@JsonProperty("board") List<Board.State> board,
			@JsonProperty("radar") List<Board.State> radar,
			@JsonProperty("active") Boolean active) {
		this.name = name;
		this.board = board;
		this.radar = radar;
		this.active = active;
	}

	public String getName() {
		return name;
	}

	public List<Board.State> getBoard() {
		return board;
	}

	public List<Board.State> getRadar() {
		return radar;
	}

	public Boolean getActive() {
		return active;
	}
}
