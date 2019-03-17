import java.awt.Dimension;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
	GameState gamestate = new GameState();
	
	Client () {
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
		ClientPanel clientPanel = new ClientPanel(gamestate, width, height);
		this.add(clientPanel);
		clientPanel.setPreferredSize(new Dimension(width,height));
		this.pack();
		
		new Thread(new Connection("localhost",8000)).run();
	}	

	class Connection implements Runnable {

		Socket socket;
		DataInputStream fromClient;
		DataOutputStream toClient;
		
		Connection(String host, int port) {
			try {
				this.socket = new Socket(host,port);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				fromClient = new DataInputStream(socket.getInputStream());
				toClient = new DataOutputStream(socket.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void run() {
		}
		
	}
}
