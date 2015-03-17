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
	
	public static final ArrayList<String> IPaddresses = new ArrayList<String>();
	
	public boolean isSettingUp;
	public boolean isGameStarted;

	public Server(int port, ControlCreatePage controller) throws IOException {
		accepter = new ServerSocket(port);
		this.controller = controller;
		System.out.println("Server IP address: " + accepter.getInetAddress());
	}

	public void listen() throws IOException {
		for (;;) {
			System.out.println("server is listening");
			Socket s = accepter.accept();
			System.out.println("Connection accepted from " + s.getInetAddress());

			
			if( isSettingUp ) {
				GameSetupThread gameSetup = new GameSetupThread(s, controller, isGameStarted, this);
				gameSetup.start();
			} else if (isGameStarted) {
				System.out.println("GameRunningThread starting");
				if (gameController != null) {
					GameRunningThread gameRunning = new GameRunningThread(s, IPaddresses, gameController);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void startGame( GameController controller ) {
		gameController = controller;
	}

}
