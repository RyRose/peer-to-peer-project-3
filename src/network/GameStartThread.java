package network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import user_interface.controllcreatepage;

public class GameStartThread extends Thread {
	
	Socket socket;
	
	controllcreatepage controller;
	
    public GameStartThread(Socket socket, controllcreatepage controller) {
        this.socket = socket;
        this.controller = controller;
    }
    
    @Override
    public void run() {
    	Socket socket = new Socket();
    	for ( String s : controller.getIPaddresses() ) {
    		try {
				socket = new Socket ( s, 8888);
				PrintWriter writer = new PrintWriter(socket.getOutputStream());
				writer.println("start");
			} 
    		catch (UnknownHostException e) { e.printStackTrace(); } 
    		catch (IOException e) { e.printStackTrace();} 
    		finally {
				try { socket.close(); } 
				catch (IOException e) { e.printStackTrace(); }
			}
    	}
    }
}
