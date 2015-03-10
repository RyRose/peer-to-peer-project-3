package network;

import java.net.*;
import java.io.*;

import user_interface.controllcreatepage;
import javafx.fxml.FXMLLoader;

public class SocketThread extends Thread {
    private Socket socket;
    private Boolean started;
    
    public SocketThread(Socket socket, Boolean started) {
        this.socket = socket;
        this.started = started;
    }

    public void run() {
        try {
            BufferedReader responses = 
                new BufferedReader
                (new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            if (!started) {
            	writer.println("Waiting");
            }
            else {
            	//writer.println(//TODO: here send the json object of all player data)
            }
            while (!responses.ready()){}
            while (responses.ready()) {
            	if (started) {
            	//TODO: each "responses" will be one player's data.all responses should edit the json
            	//that next call to PrintWriter will output;
            	}
            	else {
            		updateController(responses);
            	}
            }
            writer.flush();
            socket.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } 
    }
    
    private void updateController(BufferedReader names) {
    	FXMLLoader loader = new FXMLLoader();
		controllcreatepage controller = 
				loader.<controllcreatepage>getController();
		controller.addtolist(names.toString());
    }
   
}
