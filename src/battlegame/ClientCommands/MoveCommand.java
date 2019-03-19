package battlegame.ClientCommands;

import java.io.Serializable;
import java.util.ArrayList;

public class MoveCommand extends ClientCommand implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3611052349385476278L;
	public ArrayList<Integer> ids;
	public int x, y;
	
	public MoveCommand(ArrayList<Integer> ids, int x, int y) {
		this.ids = ids;
		this.x = x;
		this.y = y;
	}

}
