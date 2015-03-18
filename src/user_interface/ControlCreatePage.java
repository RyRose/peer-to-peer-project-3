package user_interface;

import game.Direction;
import game.Player;
import game.Point;
import interfaces.PlayerInterface;

import java.io.IOException;
import java.util.ArrayList;

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
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class ControlCreatePage {
	@FXML
	Button Begin;
	@FXML
	ListView<String> listNames;
	
	network.Server server;
	ObservableList<String> names = FXCollections.observableArrayList();
	ArrayList<Point> startCoordinates = new ArrayList<Point>();
	ArrayList<Direction> directions = new ArrayList<Direction>();
	ArrayList<Paint> colors = new ArrayList<Paint>();
	private int lowerXBound = 5;
	private int upperXBound = 795;
	private int lowerYBound = 5;
	private int upperYBound = 595;
	
	public ArrayList<PlayerInterface> all_players = new ArrayList<PlayerInterface>();
	public int player_id = 0;
	
	@FXML
	public void initialize() throws IOException {
		listNames.setItems(names);
		setupCoordinates();
		setupDirections();
		setupColors();
		generateHostPlayer();
		setupServer();
	}
	
	private void generateHostPlayer() {
		Player player = new Player(startCoordinates.get(player_id), directions.get(player_id));
		player.setUniqueId(player_id);
		all_players.add(player);
		player_id++;
	}
	
	private void setupServer() throws IOException {
		server = new network.Server(8888, this);
		server.isSettingUp = true;
		server.start();
	}
	
	private void setupColors() {
		colors.add(Color.BLACK);
		colors.add(Color.RED);
		colors.add(Color.BLUE);
		colors.add(Color.GREEN);
	}
	
	private void setupCoordinates() {
		startCoordinates.add(new Point(lowerXBound, lowerYBound));
		startCoordinates.add(new Point(upperXBound, upperYBound));
		startCoordinates.add(new Point(lowerXBound, upperYBound));
		startCoordinates.add(new Point(upperXBound, lowerYBound));
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
	
	public ArrayList<Paint> getColors() {
		return colors;
	}
	
	@FXML
	private void begin() {
		openGame();
	}
	
	public void addtolist(String name) {
		Platform.runLater(() -> { names.add(name); });
		
	}
	
	private void openGame() {
		try {
			server.isGameStarted = true;
			FXMLLoader cont = new FXMLLoader();
			cont.setLocation(getClass().getResource("GameScreen.fxml"));
			Parent home_page_parent = (Parent) cont.load();  
			Scene home_page_scene = new Scene(home_page_parent);
			Stage app_stage = (Stage) Begin.getScene().getWindow();
			app_stage.setScene(home_page_scene);
			GameController controller = 
					cont.<GameController>getController();
			controller.initializeGame(all_players.get(0), getPlayersDataFromPlayerInterfaces(all_players) , null);
			server.startGame(controller);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private ArrayList<PlayerData> getPlayersDataFromPlayerInterfaces( ArrayList<PlayerInterface> players ) {
		JSON j = new JSON();
		return j.parseJson(j.generateJson(players));
	}
}
