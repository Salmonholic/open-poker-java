package main.ui.graphical.states.game;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class PlayerInfo extends HBox {
	
	private CardInfo cardInfo1;
	private CardInfo cardInfo2;
	private VBox infoVBox;
	private Text usernameText;
	private Text moneyText;
	
	public PlayerInfo() {
		setPadding(new Insets(20));
		setSpacing(5);
		cardInfo1 = new CardInfo();
		cardInfo2 = new CardInfo();
		infoVBox = new VBox();
		usernameText = new Text("Username");
		moneyText = new Text("?");
		infoVBox.getChildren().addAll(usernameText, moneyText);
		infoVBox.setSpacing(5);
		
		getChildren().addAll(cardInfo1, cardInfo2, infoVBox);
	}
	
}
