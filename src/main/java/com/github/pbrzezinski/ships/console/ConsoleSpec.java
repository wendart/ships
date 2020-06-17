package com.github.pbrzezinski.ships.console;

import com.github.pbrzezinski.ships.Console;

import java.util.function.Function;
import java.util.function.Predicate;

public class ConsoleSpec<T> {

	private String message;
	private Function<String, T> mapper;
	private Predicate<T> predicate;
	private Console console = new Console();

	private ConsoleSpec(String message) {
		this.message = message;
	}

	public static <T> ConsoleSpec<T> askFor(String message) {
		return new ConsoleSpec<T>(message);
	}

	public ConsoleSpec<T> map(Function<String, T> mapper) {
		this.mapper = mapper;
		return this;
	}

	public ConsoleSpec<T> validate(Predicate<T> predicate) {
		this.predicate = predicate;
		return this;
	}

	public T execute() {
		while (true) {
			String input = console.getInput(message);
			T result = mapper.apply(input);
			if (predicate.test(result)) {
				return result;
			} else {
				console.writeMessage("error");
			}
		}
	}
}
