import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Server {

	public static void main(String[] args) {
		new Server();
	}
	
	double tickspeed = 30;
	ArrayList<Player> players;
	private boolean visual = true;
	int width = 800, height = 600;
	
	JFrame serverFrame;
	ServerPanel serverPanel;
	
	Server() {
		players = new ArrayList<Player>();
		generatePlayers();
		
		if(visual) {
			serverFrame = new JFrame();
			serverFrame.setSize(width, height);
			serverFrame.setResizable(false);
			serverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			serverFrame.setAlwaysOnTop(true);
			serverFrame.setVisible(true);
			serverPanel = new ServerPanel();
			serverFrame.add(serverPanel);
			serverPanel.setPreferredSize(new Dimension(width,height));
			serverFrame.pack();
		}
		
		// Gameloop
		long time = System.nanoTime();
		boolean running = true;
		while(running) {
			if(visual)
				serverPanel.repaint();
			long newtime = System.nanoTime();
			double delta = (newtime - time) / 1000000000;
			
			if (delta < 1/tickspeed) {
				this.update(1/tickspeed);
				try {
					Thread.sleep((long) ((1/tickspeed - delta)*1000));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				this.update(delta);
			}
			time = System.nanoTime();
		}
	}
	
	private void update(double delta) {
		for (Player player : players) {
			player.update(delta);
		}
		for (Player player : players) {
			player.attack(delta, players);
		}
		for (Player player : players) {
			player.units.removeIf(unit -> unit.health <= 0);
		}
	}
	
	private void generatePlayers() {
		// Placeholder for player generation
		players.add(new Player(Color.RED));
		players.add(new Player(Color.BLUE));
	}
	
	class ServerPanel extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 680263470536700273L;

		BufferedImage buffer;
		
		ServerPanel(){
			super();
			buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		}
		
		@Override
		public void paintComponent(Graphics g) {
			Graphics bg = buffer.getGraphics();
			bg.setColor(Color.WHITE);
			bg.fillRect(0, 0, this.getWidth(), this.getHeight());
			for (Player player : players)
				player.paint(bg);
			g.drawImage(buffer, 0, 0, this);
		}
	}
}
