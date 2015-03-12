package user_interface;

import game.Direction;
import game.Player;
import game.Point;
import interfaces.PlayerInterface;

import java.io.IOException;
import java.util.ArrayList;

import network_to_game.GameToNetworkMessage;
import network_to_game.NetworkMessage;
import network_to_game.NetworkToGameMessage;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class controllcreatepage {
	@FXML
	Button Begin;
	@FXML
	ListView<String> listNames;
	
	network.Server server;
	ObservableList<String> names = FXCollections.observableArrayList();
	ArrayList<Point> startCoordinates = new ArrayList<Point>();
	ArrayList<Direction> directions = new ArrayList<Direction>();
	
	public ArrayList<PlayerInterface> all_players = new ArrayList<PlayerInterface>();
	public int player_id = 0;
	
	@FXML
	public void initialize() throws IOException {
		server = new network.Server(8888, this);
		server.isSettingUp = true;
		listNames.setItems(names);
		setupCoordinates();
		setupDirections();
		Player player = new Player(startCoordinates.get(player_id), directions.get(player_id));
		player.setUniqueId(player_id);
		all_players.add(player);
		player_id++;
		server.start();
	}
	
	private void setupCoordinates() {
		startCoordinates.add(new Point(5, 5)); //TODO: change if screen size changes from 800x600
		startCoordinates.add(new Point(795, 595));
		startCoordinates.add(new Point(5, 595));
		startCoordinates.add(new Point(795, 5));
	}
	
	private void setupDirections() {
		directions.add(Direction.LEFT);
		directions.add(Direction.RIGHT);
		directions.add(Direction.DOWN);
		directions.add(Direction.UP);
	}
	
	public ArrayList<Point> getStartCoordinates() {
		return startCoordinates;
	}
	
	public ArrayList<Direction> getStartDirections() {
		return directions;
	}
	
	@FXML
	private void begin() {
		//TODO: new game controller with all players in the listview starts and sends a message with all initial coordinates
		openGame();
	}
	
	public void addtolist(String name) {
		Platform.runLater(() -> { names.add(name); });
		
	}
	
	private void openGame() {
		try {
			FXMLLoader cont = new FXMLLoader();
			cont.setLocation(getClass().getResource("GameScreen.fxml"));
			Parent home_page_parent = (Parent) cont.load();  
			Scene home_page_scene = new Scene(home_page_parent);
			Stage app_stage = (Stage) Begin.getScene().getWindow();
			app_stage.setScene(home_page_scene);
			GameController controller = 
					cont.<GameController>getController();
			
			GameToNetworkMessage message = new GameToNetworkMessage(null, all_players);
			NetworkToGameMessage message2 = new NetworkToGameMessage(message.getAllPlayersJson(), true);
			server.isGameStarted = true;
			System.out.println(message2.getAllPlayers());
			controller.initialize(message2, all_players.get(0).getUniqueId());
			controller.initializePlayer(all_players.get(0));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
