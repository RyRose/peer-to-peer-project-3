package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import network_to_game.GameToNetworkMessage;
import network_to_game.NetworkToGameMessage;
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
        		String s = responses.readLine();
        		NetworkToGameMessage message = new NetworkToGameMessage(s, false);
        		controller.updatePlayer(message);
        		GameToNetworkMessage message2 = new GameToNetworkMessage(null, controller.getScreen().getMap().getPlayers());
        		PrintWriter writer = new PrintWriter(socket.getOutputStream());
            	writer.println(message2.getAllPlayersJson());
            	writer.flush();
            }
            	            
            socket.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
	
}
