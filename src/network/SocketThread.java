package network;

import java.net.*;
import java.util.ArrayList;
import java.io.*;

import network_to_game.NetworkMessage;
import user_interface.controllcreatepage;

public class SocketThread extends Thread {
    private Socket socket;
    private Boolean started;
    private NetworkMessage networkMessage;
    private controllcreatepage controller;
    private ArrayList<String> IPaddresses;
    
    public SocketThread(Socket socket, Boolean started, NetworkMessage networkMessage, controllcreatepage controller, ArrayList<String> IPaddresses) {
        this.socket = socket;
        this.started = started;
        this.networkMessage = networkMessage;
        this.controller = controller;
        this.IPaddresses = IPaddresses;
    }

    @Override
    public void run() {
        try {
            BufferedReader responses = 
                new BufferedReader
                (new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            System.out.println(started.toString());
            if (!started) {
            	if (IPaddresses.contains(socket.getInetAddress().toString())) {
        			writer.println(getUniqueID() + "Waiting");
        		}
            	else {
            		writer.println(IPaddresses.size() + "Waiting");
            	}
            }
            else {
            	writer.print(networkMessage.getAllPlayerJson());
            }
            writer.flush();
            while (!responses.ready()){}
            while (responses.ready()) {
            	if (started) {
            		String response = responses.readLine();
            		if (response.endsWith("}}}]}")) {
            			networkMessage = new NetworkMessage(response, false);
            		}
            	}
            	else {
            		String s = responses.readLine();
            		if (s.endsWith("Player")) {
            			updateController(s);
            		}
            	}
            }
            socket.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } 
    }
    
    private String getUniqueID() {
    	return String.valueOf(IPaddresses.indexOf(socket.getInetAddress().toString()));
    }
    
    private void updateController(String names) throws IOException {
		controller.addtolist(names);
    }
   
}
