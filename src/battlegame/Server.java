package battlegame;

import java.awt.Dimension;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JFrame;

import battlegame.networking.ServerConnection;

public class Server {
	public double tickspeed = 30;
	public GameState gameState;
	private boolean visual = true;
	int width = 800, height = 600;
	
	JFrame serverFrame;
	ServerPanel serverPanel;
	ServerSocket serverSocket;
	ArrayList<ServerConnection> connections;
	
	public static void main(String[] args) {
		new Server();
	}
	
	Server() {
		gameState = new GameState();
		gameState.generatePlayers();
		connections = new ArrayList<ServerConnection>();
		
		if(visual) {
			serverFrame = new JFrame();
			serverFrame.setSize(width, height);
			serverFrame.setResizable(false);
			serverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			serverFrame.setAlwaysOnTop(true);
			serverFrame.setVisible(true);
			serverPanel = new ServerPanel(gameState, width, height);
			serverFrame.add(serverPanel);
			serverPanel.setPreferredSize(new Dimension(width,height));
			serverFrame.pack();
		}

		try {
			serverSocket = new ServerSocket(8000);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		new Thread(new Connector(this)).start();
		
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
		for (Player player : gameState.players) {
			player.update(delta);
		}
		for (Player player : gameState.players) {
			player.attack(delta, gameState.players);
		}
		for (Player player : gameState.players) {
			player.units.removeIf(unit -> unit.health <= 0);
		}
	}
	
	class Connector implements Runnable {
		
		Server server;
		
		Connector(Server server) {
			this.server = server;
		}

		@Override
		public void run() {
			while(true) {
				try {
					Socket socket = serverSocket.accept();
					ServerConnection serverConnection = new ServerConnection(socket, server);
					connections.add(serverConnection);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	
}
