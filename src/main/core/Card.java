package main.core;

import java.io.Serializable;

import handChecker.PokerCard;

public class Card implements PokerCard, Serializable{

	private Color color;
	private Value value;

	public Card(Color color, Value value) {
		this.color = color;
		this.value = value;
	}

	public Color getColor() {
		return color;
	}

	public Value getValue() {
		return value;
	}

}
