import java.awt.Dimension;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;

public class Client extends JFrame{

	public static void main(String[] args) {
		new Client();
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3023027088533526718L;
	int width = 800, height = 600;
	double tickspeed = 30;
	GameState gamestate;
	ClientPanel clientPanel;
	
	Client () {
		gamestate = new GameState();

		this.setSize(width, height);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setAlwaysOnTop(true);
		this.setVisible(true);
		
		// Initialize entities

		this.setSize(width, height);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setAlwaysOnTop(true);
		this.setVisible(true);
		clientPanel = new ClientPanel(gamestate, width, height);
		this.add(clientPanel);
		clientPanel.setPreferredSize(new Dimension(width,height));
		this.pack();
		
		new Thread(new Connection("localhost",8000)).start();
		
		// Gameloop
		long time = System.nanoTime();
		boolean running = true;
		while(running) {
			clientPanel.repaint();
			long newtime = System.nanoTime();
			double delta = (newtime - time) / 1000000000;
			
			if (delta < 1/tickspeed) {
				try {
					Thread.sleep((long) ((1/tickspeed - delta)*1000));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			time = System.nanoTime();
		}
	}	

	class Connection implements Runnable {

		Socket socket;
		DataInputStream fromClient;
		DataOutputStream toClient;
		ObjectInputStream objectFromClient;
	
		Connection(String host, int port) {
			System.out.println("Connecting...");
			try {
				this.socket = new Socket(host,port);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				fromClient = new DataInputStream(socket.getInputStream());
				objectFromClient = new ObjectInputStream(fromClient);
				toClient = new DataOutputStream(socket.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("Connected");
		}
		
		@Override
		public void run() {
			try {
				while (true) {
					gamestate = (GameState) objectFromClient.readUnshared();
					clientPanel.gamestate = gamestate;
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
