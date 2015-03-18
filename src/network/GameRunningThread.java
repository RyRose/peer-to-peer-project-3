package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import network_to_game.JSON;
import network_to_game.PlayerData;
import user_interface.GameController;

public class GameRunningThread extends Thread {

	private GameController controller;
	private Socket socket;
	private ArrayList<String> IPaddresses;
	
	public GameRunningThread( Socket socket, ArrayList<String> IPaddresses, GameController controller ) {
		this.controller = controller;
		this.socket = socket;
		this.IPaddresses = IPaddresses;
	}
	
	@Override
    public void run() {
        try {
        	
            BufferedReader responses = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        	
            System.out.println("Responses about to be ready");
            while (!responses.ready()){}
            while (responses.ready()) {
            	System.out.println("Responses ready!");
            	PrintWriter writer = new PrintWriter(socket.getOutputStream());
            	String s = responses.readLine();

            	System.out.println("responses: " + s + " end_responses");
            	if ( !s.isEmpty() ) {
            		updatePlayer(s);
            	}
            	JSON j = new JSON();
            	String network_message = j.generateJson(controller.getScreen().getMap().getPlayers());
            	System.out.println(network_message);
            	writer.println(network_message);
            	writer.flush();  
            }
            
            socket.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
	
	private void updatePlayer( String s ) {
		JSON j = new JSON();
		PlayerData player = j.parserSingleJson(s);
		controller.updatePlayer(player);
	} 
}
