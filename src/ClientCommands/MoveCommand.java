package ClientCommands;

import java.io.Serializable;
import java.util.ArrayList;

public class MoveCommand extends ClientCommand implements Serializable{
	public ArrayList<Integer> ids;
	public int x, y;
	
	public MoveCommand(ArrayList<Integer> ids, int x, int y) {
		this.ids = ids;
		this.x = x;
		this.y = y;
	}

}
