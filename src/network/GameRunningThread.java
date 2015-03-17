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
        	
            while (!responses.ready()){}
            while (responses.ready()) {
        		PrintWriter writer = new PrintWriter(socket.getOutputStream());

        		String s = "hello";
        		
        		System.out.println("responses: " + s);
        		updatePlayer(s);
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
		PlayerData player = j.parseJson(s).get(0);
		controller.updatePlayer(player);
	} 
}
