package network;

import game.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


import user_interface.ControlCreatePage;
import network_to_game.JSON;

public class GameSetupThread extends Thread {
	
	    private Socket socket;
	    private ControlCreatePage controller;
	    private boolean isGameStarted;
	    private Server server;
	    
	    
	    public GameSetupThread(Socket socket, ControlCreatePage controller, boolean isGameStarted, Server server) {
	        this.socket = socket;
	        this.controller = controller;
	        this.isGameStarted = isGameStarted;
	        this.server = server;
	    }

	    @Override
	    public void run() {
	        try {
	            BufferedReader responses = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	            while (!responses.ready()){}
	            while (responses.ready()) {
	            	PrintWriter writer = new PrintWriter(socket.getOutputStream());
	            	if (isGameStarted) {
	            		String json = JSON.generateJson(controller.all_players);
	            		writer.println(json);
	            		writer.flush();
	            		server.setSettingUp(false);
	            		break;
	            	} else {
	            		String s = responses.readLine();
	            		if (server.containsIPAddress(socket.getInetAddress().toString())) {
	            			Player player = controller.all_players.get( Integer.valueOf(getUniqueID()));
	            			String single_json = JSON.generateJson(player);
	            			writer.println(single_json);
	            			writer.flush();
	            		} else if ( !s.isEmpty() ){
	            			String json = JSON.generateJson(makeAnotherPlayer());
	            			writer.println(json);
	            			writer.flush();
	            			updateLobbyScreen(s);
	            			server.addIPAddress(socket.getInetAddress().toString());
	            		}
	            	}
	            }
	            	            
	            socket.close();
	        } catch (IOException ioe) {
	            ioe.printStackTrace();
	        }
	    }
	    
	    private String getUniqueID() {
	    	return String.valueOf(server.getIPAddressIndex(socket.getInetAddress().toString()) + 1);
	    }
	    
	    private Player makeAnotherPlayer() {
	    	Player player = new Player( controller.getStartCoordinates().get(controller.getPlayerID()), controller.getStartDirections().get(controller.getPlayerID()));
	    	player.setUniqueId(controller.getPlayerID());
	    	player.setColor(controller.getColors().get(controller.getPlayerID()));
	    	controller.addToPlayerID();
	    	controller.all_players.add(player);
	    	return player;
	    }
	    
	    private void updateLobbyScreen(String name) {
			controller.addtolist(name);
	    }
	   
}
