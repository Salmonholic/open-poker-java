package main.connection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeMap;

import main.core.Card;
import main.core.Table;

public class ShowdownUpdate extends Update implements Serializable {

	private static final long serialVersionUID = -6296928921021225285L;

	TreeMap<Integer, ArrayList<Card>> playersCards;

	public ShowdownUpdate(Table table, int id) {
		super(table, id);
		
		playersCards = new TreeMap<>();
		for (int playerId : table.getPlayers().keySet()) {
			main.core.Player player = table.getPlayer(playerId);
			playersCards.put(playerId, player.getCards());
		}
	}

	public TreeMap<Integer, ArrayList<Card>> getPlayersCards() {
		return playersCards;
	}

}
