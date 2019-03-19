package battlegame.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import battlegame.Client;
import battlegame.Player;

public class ClientConnection {

	Socket socket;
	DataInputStream fromClient;
	DataOutputStream toClient;
	ObjectInputStream objectFromClient;
	ObjectOutputStream objectToClient;
	Client client;

	public ClientConnection(String host, int port, Client client) {
		this.client = client;
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
					client.gamestate.players = (ArrayList<Player>) objectFromClient.readUnshared();
					client.clientPanel.gamestate.players = client.gamestate.players;
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
					int jobs = client.commandQueue.size();
					while(jobs > 0) {
						jobs--;
						objectToClient.writeObject(client.commandQueue.take());
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
