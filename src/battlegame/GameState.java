package battlegame;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;

import battlegame.ClientCommands.MoveCommand;

public class GameState implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7821330915361618299L;
	public ArrayList<Player> players;
	
	GameState() {
		players = new ArrayList<Player>();
	}

	public void generatePlayers() {
		// Placeholder for player generation
		players.add(new Player(Color.RED));
		players.add(new Player(Color.BLUE));
	}

	public ArrayList<Integer> getUnitsAt(int x, int y) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		for (Player player : players) 
			for (Unit unit : player.units)
				if (x >= unit.x && x <= unit.x + unit.width && y >= unit.y && y <= unit.y + unit.height)
					result.add(unit.id);
		return result;
	}

	public void processMoveCommand(MoveCommand mc) {
		for (Player player : players) 
			for (Unit unit : player.units)
				if (mc.ids.contains(unit.id)) {
					unit.targetX = mc.x;
					unit.targetY = mc.y;
				}
	}
}
