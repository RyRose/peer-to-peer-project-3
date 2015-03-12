package network;

import java.net.Socket;
import java.util.ArrayList;

import user_interface.GameController;

public class GameRunningThread extends Thread {

	private GameController controller;
	private Socket socket;
	private ArrayList<String> IPaddresses;
	
	public GameRunningThread( Socket socket, ArrayList<String> IPaddresses, GameController controller ) {
		this.controller = controller;
		this.socket = socket;
		this.IPaddresses = IPaddresses;
	}
}
