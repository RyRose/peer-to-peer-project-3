package user_interface;

import game.Player;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import network.TalkerThread;
import network_to_game.JSON;
import network_to_game.PlayerData;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class ControlJoinPage {
	public static String USERNAME_HOST_ERROR = "Enter a name and host!";
	
	Player player;
	
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
	@FXML
	TextField username;
	@FXML
	TextField hostname;
	@FXML
	Label colorRegion;
	
	String filename = "friendlist.txt";
	ObservableList<String> IPAddresses = FXCollections.observableArrayList();
	private ArrayBlockingQueue<String> channel = new ArrayBlockingQueue<String>(2,true);
	private TalkerThread talker;
	private Boolean notStarted;
	private HashMap<String, String> NametoIP = new HashMap<String, String>();
	private Receiver receiverThread;
	private int port = 8888;
	private Paint color;
	
	@FXML
	private void initialize(){
		channel = new ArrayBlockingQueue<String>(2, true);
		users.setItems(IPAddresses);
		addSavedIPs();
		receiverThread = new Receiver();
	}
	
	private void addSavedIPs() {
		try {
			File f = new File(filename);
			Scanner s = new Scanner(f);
			while (s.hasNextLine()) {
				String next = s.nextLine();
				String[] comboInfo = next.split(",", 2);
				NametoIP.put(comboInfo[1], comboInfo[0]);
	        	IPAddresses.add(comboInfo[1]);
	        }  
	        s.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void addnew(){
		addHostToDict();
		IP.setText("");
		hostname.setText("");
		users.getSelectionModel().selectLast();
		saveFriendList();		
	}
	
	private void addHostToDict() {
		String tempHostIP = IP.getText();
		String tempHostName = hostname.getText();
		NametoIP.put(tempHostName, tempHostIP);
		IPAddresses.add(tempHostName);
		IP.setText("");
		hostname.setText("");
		saveFriendList();
		users.getSelectionModel().selectLast();
	}
	
	private void saveFriendList() {
		try {
			PrintWriter printWriter = new PrintWriter(filename);
			for (int i = 0; i < NametoIP.size(); i++) {
				String name = IPAddresses.get(i);
				printWriter.write(NametoIP.get(name) + "," + name + "\n");
			}
	        printWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void joinGame() throws InterruptedException {
		if (users.getSelectionModel().getSelectedIndex() != -1 && !username.getText().equals("") && !username.getText().equals(USERNAME_HOST_ERROR)) {
			send(username.getText(), NametoIP.get(users.getSelectionModel().getSelectedItem()));
			Timer timer = new Timer();
			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					if (!notStarted) {
						cancel();
					}
					send("", NametoIP.get(users.getSelectionModel().getSelectedItem()));
				}
				
			}, 0, 100);
			
			notStarted = true;
			play.setVisible(false);
		}
		else { 
			username.setText(USERNAME_HOST_ERROR);
		}
	}

	private void send(String msg, String host) {
		if (talker != null && talker.isGoing()) {
			talker.halt();
		}
		talker = new TalkerThread(msg, host, port, channel);
		
		if ( !receiverThread.isAlive() ) 
			receiverThread.start();
		
		talker.start();
	}
	
	private class Receiver extends Thread {
		public void run() {
			while (talker.isGoing()) {
				String line;
				try {
					line = channel.take();
					System.out.println("line: " + line);
					if (line.endsWith("}}]}")) {
						JSON j = new JSON();
						ArrayList<PlayerData> players = j.parseJson(line);
						if (players.size() == 1) {
							initializePlayer( players.get(0) );
						} else {
							//talker.halt();
							Platform.runLater( () -> {startGame(players); } );
						}
					} 
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void initializePlayer( PlayerData player ) {
		this.player = player.toPlayer();
		color = this.player.getColor();
		showPlayerTheirColor();
	}
	
	private void showPlayerTheirColor() {
		Platform.runLater( ()-> {
			colorRegion.setTextFill(color);
			colorRegion.setText("This is your color!");
			colorRegion.setVisible(true);
		});
	}
	
	public void startGame( ArrayList<PlayerData> players ) {
		try {
			if (notStarted == true) {
				notStarted = false;
				FXMLLoader cont = new FXMLLoader();
				cont.setLocation(getClass().getResource("GameScreen.fxml"));		
				Parent home_page_parent = (Parent) cont.load();  
				Scene home_page_scene = new Scene(home_page_parent);
				Stage app_stage = (Stage) play.getScene().getWindow();
				app_stage.setScene(home_page_scene);
				GameController controller = 
						cont.<GameController>getController();
				controller.initializeGame(player, players, NametoIP.get(users.getSelectionModel().getSelectedItem()));
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
