package network;

import game.Player;
import interfaces.PlayerInterface;

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
	    private Server server;
	    
	    
	    public GameSetupThread(Socket socket, controllcreatepage controller, boolean isGameStarted, Server server) {
	        this.socket = socket;
	        this.controller = controller;
	        started = isGameStarted;
	        this.server = server;
	    }

	    @Override
	    public void run() {
	        try {
	        	
	            BufferedReader responses = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            	
	            while (!responses.ready()){}
	            while (responses.ready()) {
    	            PrintWriter writer = new PrintWriter(socket.getOutputStream());

            		if (started) {
        				GameToNetworkMessage message = new GameToNetworkMessage(null, controller.all_players);
        				writer.println(message.getAllPlayersJson());
        				writer.flush();
        				server.isSettingUp = false;
        				break;
            		} else {
                    	
                		String s = responses.readLine();
                    	
        	            if (Server.IPaddresses.contains(socket.getInetAddress().toString())) {
        	            	PlayerInterface player = controller.all_players.get( Integer.valueOf(getUniqueID()));
        	            	GameToNetworkMessage message = new GameToNetworkMessage(player, null);
                			writer.println(message.getSingleJson());
                			writer.flush();
                		} else {
                	        
                			if(s.isEmpty()) {
            	            	PlayerInterface player = controller.all_players.get( Integer.valueOf(getUniqueID()) );
            	            	GameToNetworkMessage message = new GameToNetworkMessage(player, null);
                    			writer.println(message.getSingleJson());
                    			writer.flush();
                			} else {
                			
                			GameToNetworkMessage message = new GameToNetworkMessage(makeAnotherPlayer(), null);
                			writer.println(message.getSingleJson());
                			writer.flush();
                    	
                			updateLobbyScreen(s);
                			Server.IPaddresses.add( socket.getInetAddress().toString() );
                			}
                		}
            		}
	            }
	            	            
	            socket.close();
	        } catch (IOException ioe) {
	            ioe.printStackTrace();
	        }
	    }
	    
	    private String getUniqueID() {
	    	return String.valueOf(server.IPaddresses.indexOf(socket.getInetAddress().toString()) + 1);
	    }
	    
	    private Player makeAnotherPlayer() {
	    	Player player = new Player( controller.getStartCoordinates().get(controller.player_id), controller.getStartDirections().get(controller.player_id));
	    	player.setUniqueId(controller.player_id);
	    	controller.player_id++;
	    	controller.all_players.add(player);
	    	return player;
	    }
	    
	    private void updateLobbyScreen(String name) {
			controller.addtolist(name);
	    }
	   
}
