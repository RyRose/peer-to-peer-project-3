package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import network_to_game.JSON;
import network_to_game.PlayerData;
import user_interface.GameController;

public class GameRunningThread extends Thread {

	private GameController controller;
	private Socket socket;
	
	public GameRunningThread( Socket socket, GameController controller ) {
		this.controller = controller;
		this.socket = socket;
	}
	
	@Override
    public void run() {
        try {
            BufferedReader responses = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (!responses.ready()){}
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            String s = responses.readLine();
            if ( !s.isEmpty() ) {
            	updatePlayer(s);
            }
            JSON j = new JSON();
            String network_message = j.generateJson(controller.getScreen().getMap().getPlayers());
            writer.println(network_message);
            writer.flush();
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
