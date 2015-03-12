package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import network_to_game.NetworkMessage;
import user_interface.controllcreatepage;

public class GameSetupThread extends Thread {
	
	    private Socket socket;
	    private Boolean started;
	    private NetworkMessage networkMessage;
	    private controllcreatepage controller;
	    private ArrayList<String> IPaddresses;
	    
	    private int player_id;
	    
	    public GameSetupThread(Socket socket, Boolean started, NetworkMessage networkMessage, controllcreatepage controller, ArrayList<String> IPaddresses) {
	        this.socket = socket;
	        this.started = started;
	        this.networkMessage = networkMessage;
	        this.controller = controller;
	        this.IPaddresses = IPaddresses;
	    }

	    @Override
	    public void run() {
	        try {
	            BufferedReader responses = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	            PrintWriter writer = new PrintWriter(socket.getOutputStream());
	            if (!started) {
	            	if (IPaddresses.contains(socket.getInetAddress().toString())) {
	        			writer.println(getUniqueID() + "Waiting");
	        		}
	            	else {
	            		System.out.println("in Else statment of !started");
	            		writer.println(IPaddresses.size() + "Waiting");
	            	}
	            }
	            else {
	            	writer.print(networkMessage.getAllPlayerJson());
	            }
	            writer.flush();
	            while (!responses.ready()){}
	            while (responses.ready()) {
	            	String s = responses.readLine();
	            	updateLobbyScreen(s);
	            }
	            
	            System.out.println("hello");
	            
	            socket.close();
	        } catch (IOException ioe) {
	            ioe.printStackTrace();
	        } 
	    }
	    
	    private String getUniqueID() {
	    	return String.valueOf(IPaddresses.indexOf(socket.getInetAddress().toString()));
	    }
	    
	    private void updateLobbyScreen(String name) {
			controller.addtolist(name);
	    }
	   
}
