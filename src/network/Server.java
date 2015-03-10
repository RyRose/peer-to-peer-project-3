package network;

import java.net.*;
import java.io.*;

public class Server {
	private ServerSocket accepter;
	private Boolean started = false;

	public Server(int port) throws IOException {
		accepter = new ServerSocket(port);
		System.out.println("Server IP address: " + accepter.getInetAddress());
	}

	public void listen() throws IOException {
		for (;;) {
			Socket s = accepter.accept();
			SocketThread echoer = new SocketThread(s, started);
			System.out.println("Connection accepted from " + s.getInetAddress());
			echoer.start();
		}
	}
	
	public void setStarted(Boolean started) {
		this.started = started;
	}

	public static void main(String[] args) throws IOException {
		Server s = new Server(Integer.parseInt(args[0]));
		s.listen();
	}
}
