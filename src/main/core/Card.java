package main.core;

import handChecker.PokerCard;

public class Card implements PokerCard {
	
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
