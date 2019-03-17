import java.awt.Color;
import java.util.ArrayList;

public class GameState {
	
	ArrayList<Player> players;
	
	GameState() {
		players = new ArrayList<Player>();
		generatePlayers();
	}

	private void generatePlayers() {
		// Placeholder for player generation
		players.add(new Player(Color.RED));
		players.add(new Player(Color.BLUE));
	}
}
