import java.awt.Color;
import java.awt.Graphics;

public class ClientPanel extends ServerPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5569551038806808925L;
	private Client client;

	ClientPanel(GameState gamestate, Client client, int width, int height) {
		super(gamestate, width, height);
		this.client = client;
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics bg = buffer.getGraphics();
		bg.setColor(Color.WHITE);
		bg.fillRect(0, 0, this.getWidth(), this.getHeight());
		for (Player player : gamestate.players)
			player.paint(bg,client.selection);
		g.drawImage(buffer, 0, 0, this);
	}
}
