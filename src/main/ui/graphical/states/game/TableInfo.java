package main.ui.graphical.states.game;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import main.core.Card;
import main.core.GameState;

public class TableInfo extends HBox {
	
	private CardInfo cardInfo1;
	private CardInfo cardInfo2;
	private CardInfo cardInfo3;
	private InfoBox infoBox;
	
	public TableInfo() {
		setPadding(new Insets(20));
		setSpacing(5);
		cardInfo1 = new CardInfo();
		cardInfo2 = new CardInfo();
		cardInfo3 = new CardInfo();
		infoBox = new InfoBox(3);
		
		getChildren().addAll(cardInfo1, cardInfo2, cardInfo3, infoBox);
	}
	
	public void setCurrentPot(int currentPot) {
		infoBox.setInfo(0, "Current pot:" + currentPot);
	}
	
	public void setCurrentBet(int currentBet) {
		infoBox.setInfo(0, "Current bet:" + currentBet);
	}
	
	public void setGameState(GameState gameState) {
		infoBox.setInfo(0, "Game state:" + gameState.toString());
	}
	
	public void setCards(Card card1, Card card2, Card card3) {
		cardInfo1.setCard(card1);
		cardInfo2.setCard(card2);
		cardInfo3.setCard(card3);
	}
	
}
