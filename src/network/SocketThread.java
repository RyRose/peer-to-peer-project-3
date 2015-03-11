package network;

import java.net.*;
import java.io.*;

import network_to_game.NetworkMessage;
import user_interface.controllcreatepage;
import javafx.fxml.FXMLLoader;

public class SocketThread extends Thread {
    private Socket socket;
    private Boolean started;
    private NetworkMessage networkMessage;
    private controllcreatepage controller;
    
    public SocketThread(Socket socket, Boolean started, NetworkMessage networkMessage, controllcreatepage controller) {
        this.socket = socket;
        this.started = started;
        this.networkMessage = networkMessage;
        this.controller = controller;
    }

    public void run() {
        try {
            BufferedReader responses = 
                new BufferedReader
                (new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            System.out.println(started.toString());
            if (!started) {
            	writer.print("Waiting");
            }
            else {
            	writer.print(networkMessage.getAllPlayerJson());
            }
            writer.flush();
            while (!responses.ready()){}
            while (responses.ready()) {
            	if (started) {
            	//TODO: each "responses" will be one player's data.all responses should edit the json
            	//that next call to PrintWriter will output;
            	}
            	else {
            		updateController(responses);
            		System.out.println(responses.readLine());
            	}
            }
            socket.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } 
    }
    
    private void updateController(BufferedReader names) throws IOException {
		controller.addtolist(names.readLine());
    }
   
}
