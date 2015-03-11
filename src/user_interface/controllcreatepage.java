package user_interface;

import game.Direction;
import game.Player;
import game.Point;
import interfaces.BulletInterface;
import interfaces.PlayerInterface;

import java.util.List;
import java.io.IOException;
import java.util.ArrayList;

import network_to_game.NetworkMessage;
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
	ArrayList<Point> startCoordinates;
	ArrayList<Direction> directions;
	
	@FXML
	public void intialize() throws IOException {
		server = new network.Server(8888, this);
		listNames.setItems(names);
		setupCoordinates();
		setupDirections();
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
	
	@FXML
	private void begin() {
		ArrayList<PlayerInterface> players = new ArrayList<PlayerInterface>();
		for (int i = 0; i == names.size(); i++) {
			Player player = new Player(startCoordinates.get(i), directions.get(i));
			player.setUniqueId(i);
			players.add(player);
		}
		NetworkMessage networkMessage = new NetworkMessage(players);
		server.setNetworkMessage(networkMessage);
		server.setStarted(true);
		//TODO: new game controller with all players in the listview starts and sends a message with all initial coordinates
		openGame();
	}
	
	public void addtolist(String name) {
		names.add(name);
	}
	
	private void openGame() {
		try {
			Parent home_page_parent = FXMLLoader.load(getClass().getResource("GameScreen.fxml"));
			Scene home_page_scene = new Scene(home_page_parent);
			Stage app_stage = (Stage) Begin.getScene().getWindow();
			app_stage.setScene(home_page_scene);
			app_stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
