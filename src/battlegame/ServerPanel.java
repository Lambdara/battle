package battlegame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

class ServerPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 680263470536700273L;

	BufferedImage buffer;
	public GameState gamestate;
		
	ServerPanel(GameState gamestate, int width, int height){
		super();
		this.gamestate = gamestate;
		buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	}
		
	@Override
	public void paintComponent(Graphics g) {
		Graphics bg = buffer.getGraphics();
		bg.setColor(Color.WHITE);
		bg.fillRect(0, 0, this.getWidth(), this.getHeight());
		for (Player player : gamestate.players)
			player.paint(bg);
		g.drawImage(buffer, 0, 0, this);
	}
}