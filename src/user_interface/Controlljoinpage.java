package user_interface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;

import network.TalkThread;
import network_to_game.NetworkMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Controlljoinpage {
	@FXML
	Pane canvas;
	@FXML
	ChoiceBox<String> users;
	@FXML
	Button add;
	@FXML
	Button play;
	@FXML
	TextField IP;
	
	String filename = "friendlist.txt";
	ObservableList<String> IPAddresses = FXCollections.observableArrayList();
	private ArrayBlockingQueue<String> channel;
	private TalkThread talker;
	private Boolean notStarted;
	
	//TODO: periodically need to check to see if the status has changed somewhere after joinGame() called. send("", users.getSelectionModel().getSelectedItem(), 8888)
	@FXML
	private void initialize(){
		users.setItems(IPAddresses);
		users.getSelectionModel().select(0);
		addSavedIPs();
		
		channel = new ArrayBlockingQueue<String>(2, true);
	}
	
	private void addSavedIPs() {
		try {
			File f = new File(filename);
			Scanner s = new Scanner(f);
			while (s.hasNextLine()) {
	        	IPAddresses.add(s.nextLine());
	        }  
	        s.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void addnew(){
		String input = IP.getText();
		IPAddresses.add(input);
		IP.setText("");
		saveFriendList();
	}
	
	private void saveFriendList() {
		try {
			PrintWriter printWriter = new PrintWriter(filename);
			for (int i = 0; i < IPAddresses.size(); i++) {
				printWriter.write(IPAddresses.get(i) + "\n");
			}
	        printWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void joinGame() throws InterruptedException {
		if (users.getSelectionModel().getSelectedIndex() != -1) {
			send("Player", users.getSelectionModel().getSelectedItem(), 8888);
			notStarted = true;
			
			Timer timer = new Timer();
			timer.scheduleAtFixedRate( new TimerTask() {

				@Override
				public void run() {
					if(!notStarted) {
						cancel();
					} else {
						send("", users.getSelectionModel().getSelectedItem(), 8888);
					}
				}
				
			}, 0, 100);
			
			//TODO: later we can change this so that it says "player name" is joining game, and that way it will pop up as their name for the creater
		}
		else {
			//TODO: maybe give an error box telling them to select a player
		}
	}
	
	private void send(String msg, String host, int port) {
		if (talker != null && talker.isGoing()) {
			talker.halt();
		}
		talker = new TalkThread(msg, host, port, channel);
		new Receiver().start();
		talker.start();		
	}
	
	private class Receiver extends Thread {
		public void run() {
			while (talker.isGoing()) {
				String line;
				try {
					line = channel.take();
					if (line.equals("Waiting")) {}
					else if (line.endsWith("}}}]}")) {
						notStarted = false;
						startGame(line);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void startGame(String line) {
		try {
			FXMLLoader cont = new FXMLLoader();
			cont.setLocation(getClass().getResource("GameScreen.fxml"));		
			Parent home_page_parent = (Parent) cont.load();  
			Scene home_page_scene = new Scene(home_page_parent);
			Stage app_stage = (Stage) canvas.getScene().getWindow();
			app_stage.setScene(home_page_scene);
			GameController controller = 
					cont.<GameController>getController();
			controller.initializeNetworkMessage(new NetworkMessage(line, true));
			controller.initializeHost(users.getSelectionModel().getSelectedItem());
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
