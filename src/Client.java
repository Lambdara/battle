import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JFrame;

import ClientCommands.*;

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
	BlockingQueue<ClientCommand> commandQueue;
	
	ArrayList<Integer> selection;

	Client () {
		gamestate = new GameState();
		selection = new ArrayList<Integer>();
		commandQueue = new LinkedBlockingQueue<ClientCommand>();

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
		clientPanel = new ClientPanel(gamestate, this, width, height);
		this.add(clientPanel);
		clientPanel.setPreferredSize(new Dimension(width,height));
		this.pack();
		
		new Connection("localhost",8000);
		
		clientPanel.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (arg0.getButton() == MouseEvent.BUTTON1) {
					selection.clear();
					selection.addAll(gamestate.getUnitsAt(arg0.getX(),arg0.getY()));
				} else if (arg0.getButton() == MouseEvent.BUTTON3) {
					ClientCommand mc = 
							new MoveCommand((ArrayList<Integer>) selection.clone(),arg0.getX(),arg0.getY());
					try {
						commandQueue.put(mc);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});

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

	class Connection {

		Socket socket;
		DataInputStream fromClient;
		DataOutputStream toClient;
		ObjectInputStream objectFromClient;
		ObjectOutputStream objectToClient;
	
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
				objectToClient = new ObjectOutputStream(toClient);
			} catch (IOException e) {
				e.printStackTrace();
			}
			new Thread(new Receiver()).start();
			new Thread(new Sender()).start();
			System.out.println("Connected");
		}

		class Receiver implements Runnable {
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				try {
					while (true) {
						gamestate.players = (ArrayList<Player>) objectFromClient.readUnshared();
						clientPanel.gamestate.players = gamestate.players;
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		class Sender implements Runnable {
			@Override
			public void run() {
				try {
					while (true) {
						int jobs = commandQueue.size();
						while(jobs > 0) {
							jobs--;
							objectToClient.writeObject(commandQueue.take());
						}
						objectToClient.flush();
						objectToClient.reset();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
