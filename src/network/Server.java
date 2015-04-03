package network;

import java.net.*;
import java.util.ArrayList;
import java.io.*;

import user_interface.GameController;
import user_interface.ControlCreatePage;

public class Server extends Thread {
	private ServerSocket accepter;
	private ControlCreatePage controller;
	private GameController gameController;
	
	private static final ArrayList<String> IPaddresses = new ArrayList<String>();
	
	private boolean isSettingUp;
	private boolean isGameStarted;

	public Server(int port, ControlCreatePage controller) throws IOException {
		accepter = new ServerSocket(port);
		this.controller = controller;
	}

	public void listen() throws IOException {
		for (;;) {
			Socket s = accepter.accept();			
			if( isSettingUp ) {
				GameSetupThread gameSetup = new GameSetupThread(s, controller, isGameStarted, this);
				gameSetup.start();
			} else if (isGameStarted) {
				if (gameController != null) {
					GameRunningThread gameRunning = new GameRunningThread(s, gameController);
					gameRunning.start();
				}
			}
		}
	}
	
	@Override
	public void run() {
		try {
			listen();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Boolean containsIPAddress(String address) {
		return IPaddresses.contains(address);
	}
	
	public void addIPAddress(String address) {
		IPaddresses.add(address);
	}
	
	public int getIPAddressIndex(String address) {
		return IPaddresses.indexOf(address);
	}
	
	public void startGame( GameController controller ) {
		gameController = controller;
	}
	
	public void setSettingUp(Boolean value) {
		isSettingUp = value;
	}
	
	public void setGameStarted(Boolean value) {
		isGameStarted = value;
	}

}
