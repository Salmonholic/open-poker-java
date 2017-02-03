package main.ui.graphical.states.game;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import main.core.Card;

public class PlayerInfo extends HBox {

	private CardInfo cardInfo1;
	private CardInfo cardInfo2;
	private InfoBox infoBox;

	private boolean fold;
	private boolean allIn;
	private boolean primaryPlayer;

	public PlayerInfo() {
		setPadding(new Insets(10));
		setSpacing(5);
		cardInfo1 = new CardInfo();
		cardInfo2 = new CardInfo();
		infoBox = new InfoBox(3);

		getChildren().addAll(cardInfo1, cardInfo2, infoBox);
		redraw();
	}

	public void setUsername(String username) {
		infoBox.setInfo(0, "Username: " + username);
	}

	public void setMoney(int money) {
		infoBox.setInfo(1, "Money: " + money);
	}

	public void setCurrentBet(int currentBet) {
		infoBox.setInfo(2, "Current bet: " + currentBet);
	}

	private void redraw() {
		if (primaryPlayer) {
			infoBox.setColor(Color.GREENYELLOW);
		} else {
			infoBox.setColor(Color.WHITE);
		}

		if (allIn) {
			setColor(Color.DODGERBLUE);
		} else if (fold) {
			setColor(Color.ORANGERED);
		} else {
			setColor(Color.WHITE);
		}
	}

	public void setPrimaryPlayer(boolean primaryPlayer) {
		this.primaryPlayer = primaryPlayer;
		redraw();
	}

	public void setAllIn(boolean allIn) {
		this.allIn = allIn;
		redraw();
	}

	public void setFold(boolean fold) {
		this.fold = fold;
		redraw();
	}

	public void setCards(Card card1, Card card2) {
		cardInfo1.setCard(card1);
		cardInfo2.setCard(card2);
	}

	public void setUnknownCards() {
		cardInfo1.setUnknownCard();
		cardInfo2.setUnknownCard();
	}
	
	private void setColor(Color color) {
		setBackground(new Background(new BackgroundFill(Color.DODGERBLUE, new CornerRadii(10), new Insets(0))));
	}

}
