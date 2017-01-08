package main.ui.graphical.states.game;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class TableInfo extends HBox {
	
	private CardInfo cardInfo1;
	private CardInfo cardInfo2;
	private CardInfo cardInfo3;
	private VBox infoVBox;
	private Text currentPotText;
	private Text currentBetText;
	private Text gameStateText;
	
	public TableInfo() {
		setPadding(new Insets(20));
		setSpacing(5);
		cardInfo1 = new CardInfo();
		cardInfo2 = new CardInfo();
		cardInfo3 = new CardInfo();
		infoVBox = new VBox();
		currentPotText = new Text("?");
		currentBetText = new Text("?");
		gameStateText = new Text("?");
		infoVBox.getChildren().addAll(currentPotText, currentBetText, gameStateText);
		infoVBox.setSpacing(5);
		
		getChildren().addAll(cardInfo1, cardInfo2, cardInfo3, infoVBox);
	}
	
}
