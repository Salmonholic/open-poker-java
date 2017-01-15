package main.ui.graphical.states.game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import main.core.Card;

public class CardInfo extends HBox {

	public static String UNKNOWN_CARD_PATH = "/unknownCard.png";
	public static Image UNKNOWN_CARD_IMAGE = new Image(UNKNOWN_CARD_PATH);

	private ImageView imageView;

	public CardInfo() {
		super();
		imageView = new ImageView();
		setUnknownCard();
		getChildren().add(imageView);
	}

	private void setImage(Image image) {
		imageView.setImage(image);
	}

	public void setCard(Card card) {
		String path = "/" + card.getColor().name().toLowerCase() + "/" + card.getValue().getInt() + ".png";
		imageView.setAccessibleText(card.getColor().name().toLowerCase() + " " + card.getValue());
		setImage(new Image(getClass().getResourceAsStream(path)));
	}

	public void setUnknownCard() {
		imageView.setAccessibleText("Unknown card");
		setImage(UNKNOWN_CARD_IMAGE);
	}

}
