package network;

import java.net.*;
import java.util.ArrayList;
import java.io.*;

import user_interface.controllcreatepage;
import network_to_game.NetworkMessage;

public class Server extends Thread {
	private ServerSocket accepter;
	private controllcreatepage controller;
	
	public boolean isSettingUp;
	public boolean isGameStarted;

	public Server(int port, controllcreatepage controller) throws IOException {
		accepter = new ServerSocket(port);
		this.controller = controller;
		System.out.println("Server IP address: " + accepter.getInetAddress());
	}

	public void listen() throws IOException {
		for (;;) {
			Socket s = accepter.accept();
			
			if( isSettingUp ) {
				GameSetupThread gameSetup = new GameSetupThread(s, controller);

				System.out.println("Connection accepted from " + s.getInetAddress());
				gameSetup.start();
			} else {
				
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

}
