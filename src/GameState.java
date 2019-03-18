import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;

public class GameState implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7821330915361618299L;
	ArrayList<Player> players;
	
	GameState() {
		players = new ArrayList<Player>();
	}

	public void generatePlayers() {
		// Placeholder for player generation
		players.add(new Player(Color.RED));
		players.add(new Player(Color.BLUE));
	}
}
