package main.core;

import java.io.Serializable;

import handChecker.PokerCard;

public class Card implements PokerCard, Serializable{

	private static final long serialVersionUID = -6482007440355885437L;
	
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
