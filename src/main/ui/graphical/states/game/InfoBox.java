package main.ui.graphical.states.game;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class InfoBox extends VBox {

	public InfoBox(int infoAmount) {
		setPadding(new Insets(10));
		setSpacing(5);
		for (int i = 0; i < infoAmount; i++) {
			Text infoText = new Text("?");
			getChildren().add(infoText);
		}
	}

	public void setInfo(int index, Object info) {
		((Text) getChildren().get(index)).setText(info.toString());
	}

	public void setColor(Color color) {
		setBackground(new Background(new BackgroundFill(Color.DODGERBLUE,
				new CornerRadii(10), new Insets(0))));
	}

}
