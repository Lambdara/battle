package battlegame.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import battlegame.Server;
import battlegame.ClientCommands.ClientCommand;
import battlegame.ClientCommands.MoveCommand;

public class ServerConnection {
	Socket socket;
	DataInputStream fromClient;
	DataOutputStream toClient;
	ObjectOutputStream objectToClient;
	ObjectInputStream objectFromClient;
	Server server;
	
	public ServerConnection(Socket socket, Server server) {
		this.socket = socket;
		this.server = server;
		try {
			fromClient = new DataInputStream(socket.getInputStream());
			toClient = new DataOutputStream(socket.getOutputStream());
			objectToClient = new ObjectOutputStream(toClient);
			objectFromClient = new ObjectInputStream(fromClient);
			new Thread(new Sender()).start();
			new Thread(new Receiver()).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	class Sender implements Runnable {
		@Override
		public void run() {
			try {
				while (true) {
					objectToClient.writeUnshared(server.gameState.players);
					objectToClient.flush();
					objectToClient.reset();
					Thread.sleep((int)(1/server.tickspeed*1000));
				}
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	class Receiver implements Runnable {
		@Override
		public void run() {
			try {
				System.out.println("Started");
				while (true) {
					ClientCommand cc = (ClientCommand) objectFromClient.readObject();
					if(cc instanceof MoveCommand) {
						MoveCommand mc = (MoveCommand) cc;
						server.gameState.processMoveCommand(mc);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}