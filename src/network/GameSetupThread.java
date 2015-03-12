package network;

import game.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import network_to_game.GameToNetworkMessage;
import network_to_game.NetworkMessage;
import user_interface.controllcreatepage;

public class GameSetupThread extends Thread {
	
	    private Socket socket;
	    public boolean started;
	    private controllcreatepage controller;
	    
	    
	    public GameSetupThread(Socket socket, controllcreatepage controller, boolean isGameStarted) {
	        this.socket = socket;
	        this.controller = controller;
	        started = isGameStarted;
	    }

	    @Override
	    public void run() {
	        try {
	        	
	            BufferedReader responses = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            	
	            while (!responses.ready()){}
	            while (responses.ready()) {
            		String s = responses.readLine();
            		if (started) {
            			PrintWriter writer = new PrintWriter(socket.getOutputStream());
        				GameToNetworkMessage message = new GameToNetworkMessage(null, controller.all_players);
        				writer.print(message.getAllPlayersJson());
        				writer.flush();
            		} else if (!s.isEmpty()) {
            			updateLobbyScreen(s);
            			Server.IPaddresses.add( socket.getInetAddress().getHostAddress() );
        	            
        	            PrintWriter writer = new PrintWriter(socket.getOutputStream());
        	            GameToNetworkMessage message = new GameToNetworkMessage(makeAnotherPlayer(), null);
                    	writer.print(message.getSingleJson());
                    	writer.flush();
            		}
	            }
	            	            
	            socket.close();
	        } catch (IOException ioe) {
	            ioe.printStackTrace();
	        }
	    }
	    
	    private Player makeAnotherPlayer() {
	    	Player player = new Player( controller.getStartCoordinates().get(controller.player_id), controller.getStartDirections().get(controller.player_id));
	    	player.setUniqueId(controller.player_id);
	    	controller.player_id++;
	    	return player;
	    }
	    
	    private void updateLobbyScreen(String name) {
			controller.addtolist(name);
	    }
	   
}
