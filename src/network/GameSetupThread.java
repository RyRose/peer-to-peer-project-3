package network;

import game.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import network_to_game.GameToNetworkMessage;
import network_to_game.NetworkMessage;
import user_interface.controllcreatepage;

public class GameSetupThread extends Thread {
	
	    private Socket socket;
	    private Boolean started;
	    private controllcreatepage controller;
	    private ArrayList<String> IPaddresses;
	    
	    private int player_id; // increments as players are added
	    
	    public GameSetupThread(Socket socket, Boolean started, controllcreatepage controller) {
	        this.socket = socket;
	        this.started = started;
	        this.controller = controller;
	        player_id = 0;
	    }

	    @Override
	    public void run() {
	        try {
	        	
	            BufferedReader responses = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            	
	            while (!responses.ready()){}
	            while (responses.ready()) {
            		String s = responses.readLine();
	            	updateLobbyScreen(s);
	            }
	            
	            controller.getIPaddresses().add( socket.getInetAddress().getHostAddress() );
	            
	            PrintWriter writer = new PrintWriter(socket.getOutputStream());
	            
	            GameToNetworkMessage message = new GameToNetworkMessage(makeAnotherPlayer(), null);
	            
            	writer.print(message.getSingleJson());
            	writer.flush();
	            	            
	            socket.close();
	        } catch (IOException ioe) {
	            ioe.printStackTrace();
	        }
	    }
	    
	    private Player makeAnotherPlayer() {
	    	Player player = new Player( controller.getStartCoordinates().get(player_id), controller.getStartDirections().get(player_id));
	    	player.setUniqueId(player_id);
	    	player_id++;
	    	return player;
	    }
	    
	    private void updateLobbyScreen(String name) {
			controller.addtolist(name);
	    }
	   
}
