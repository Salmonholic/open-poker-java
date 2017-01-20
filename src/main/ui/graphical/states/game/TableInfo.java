package main.ui.graphical.states.game;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import main.core.Card;
import main.core.GameState;

public class TableInfo extends HBox {
	
	private ArrayList<CardInfo> cardInfos;
	private InfoBox infoBox;
	
	public TableInfo() {
		setPadding(new Insets(20));
		setSpacing(5);
		
		cardInfos = new ArrayList<>();
		
		for (int i = 0; i < 5; i++) {
			CardInfo cardInfo = new CardInfo();
			getChildren().add(cardInfo);
			cardInfos.add(cardInfo);
		}
		
		infoBox = new InfoBox(3);
		getChildren().add(infoBox);
	}
	
	public void setCurrentPot(int currentPot) {
		infoBox.setInfo(0, "Current pot:" + currentPot);
	}
	
	public void setCurrentBet(int currentBet) {
		infoBox.setInfo(1, "Current bet:" + currentBet);
	}
	
	public void setGameState(GameState gameState) {
		infoBox.setInfo(2, "Game state:" + gameState.toString());
	}
	
	public void setCards(ArrayList<Card> cards) {
		// Reset cards
		for (int i = 0; i < 5; i++) {
			cardInfos.get(i).setUnknownCard();
		}
		
		// Set new cards
		for (int i = 0; i < cards.size(); i++) {
			cardInfos.get(i).setCard(cards.get(i));
		}
	}
	
}
