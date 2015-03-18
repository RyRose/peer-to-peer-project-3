package network;

import game.Player;
import interfaces.PlayerInterface;

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
	    public boolean isGameStarted;
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
	            	JSON j = new JSON();

	            	if (isGameStarted) {
	            		String json = j.generateJson(controller.all_players);
	            		writer.println(json);
	            		writer.flush();
	            		server.isSettingUp = false;
	            		break;
	            	} else {

	            		String s = responses.readLine();

	            		if (Server.IPaddresses.contains(socket.getInetAddress().toString())) {
	            			PlayerInterface player = controller.all_players.get( Integer.valueOf(getUniqueID()));
	            			String single_json = j.generateJson(player);
	            			writer.println(single_json);
	            			writer.flush();
	            		} else if ( !s.isEmpty() ){
	            			String json = j.generateJson(makeAnotherPlayer());
	            			writer.println(json);
	            			writer.flush();
	            			updateLobbyScreen(s);
	            			Server.IPaddresses.add( socket.getInetAddress().toString() );
	            		}
	            	}
	            }
	            	            
	            socket.close();
	        } catch (IOException ioe) {
	            ioe.printStackTrace();
	        }
	    }
	    
	    private String getUniqueID() {
	    	return String.valueOf(Server.IPaddresses.indexOf(socket.getInetAddress().toString()) + 1);
	    }
	    
	    private Player makeAnotherPlayer() {
	    	Player player = new Player( controller.getStartCoordinates().get(controller.player_id), controller.getStartDirections().get(controller.player_id));
	    	player.setUniqueId(controller.player_id);
	    	player.setColor(controller.getColors().get(controller.player_id));
	    	controller.player_id++;
	    	controller.all_players.add(player);
	    	return player;
	    }
	    
	    private void updateLobbyScreen(String name) {
			controller.addtolist(name);
	    }
	   
}
