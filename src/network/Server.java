package network;

import java.net.*;
import java.io.*;

import user_interface.controllcreatepage;
import network_to_game.NetworkMessage;

public class Server extends Thread {
	private ServerSocket accepter;
	private Boolean started = false;
	private NetworkMessage networkMessage;
	private controllcreatepage controller;

	public Server(int port, controllcreatepage controller) throws IOException {
		accepter = new ServerSocket(port);
		this.controller = controller;
		System.out.println("Server IP address: " + accepter.getInetAddress());
	}

	public void listen() throws IOException {
		for (;;) {
			System.out.println("here");
			Socket s = accepter.accept();
			SocketThread echoer = new SocketThread(s, started, networkMessage, controller);
			System.out.println("Connection accepted from " + s.getInetAddress());
			echoer.start();
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
	
	public void setStarted(Boolean started) {
		this.started = started;
	}
	
	public void setNetworkMessage(NetworkMessage networkMessage) {
		this.networkMessage = networkMessage;
	}
}
