package user_interface;

import game.Direction;
import game.Player;
import game.ScreenBuffer;
import interfaces.BulletInterface;
import interfaces.PlayerInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;

import network.TalkerThread;
import network_to_game.JSON;
import network_to_game.PlayerData;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.input.KeyCode;

public class GameController {
	@FXML
	Pane canvas;
	
	Circle mySprite;
	
	ScreenBuffer screen;
	int my_id;
	private ArrayBlockingQueue<String> channel = new ArrayBlockingQueue<String>(2);
	HashMap<Integer, Circle> bulletSprites = new HashMap<Integer, Circle>();
	HashMap<Integer, Circle> playerSprites = new HashMap<Integer, Circle>();
	private TalkerThread talker;
	private String host;
	private int port = 8888;

	@FXML
	private void initialize() {
		Timer timer = new Timer();
		
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				if (screen != null && !playerSprites.isEmpty()) {
					Platform.runLater( () -> {
						drawScreen();
					});
					screen.updateBullets();
				}
				if (host != null){
					send();
				}
			}
			
		}, 0, 25);
		
		canvas.setOnKeyPressed(event -> {
			double x = 0;
			double y = 0;
			if (event.getCode() == KeyCode.UP) {
				y = -5;
				screen.move(Direction.UP);
			} else if (event.getCode() == KeyCode.DOWN) {
				y = 5;
				screen.move(Direction.DOWN);
			} else if (event.getCode() == KeyCode.LEFT) {
				x = -5;
				screen.move(Direction.LEFT);
			} else if (event.getCode() == KeyCode.RIGHT) {
				x = 5;
				screen.move(Direction.RIGHT);
			} else if (event.getCode() == KeyCode.SPACE) {
				Circle bullet = new Circle(mySprite.getTranslateX() + mySprite.getLayoutX() + 5, mySprite.getTranslateY() + mySprite.getLayoutY() + 5, 5);
				bulletSprites.put(my_id, bullet);
				screen.shootBullet();
			}
			move(x,y);
		});
		
	}
	
	@FXML
	public void grabKeys() {
		canvas.requestFocus();
	}	
	
	public void initializeGame(PlayerInterface my_player, List<PlayerData> players, String host) {
		screen = new ScreenBuffer(players, my_id);
		screen.myPlayer = (Player) my_player;
		mySprite = new Circle(screen.getMe().getCoordinates().getX(), screen.getMe().getCoordinates().getY(), 20);
		canvas.getChildren().add(mySprite);
		my_id = my_player.getUniqueId();
		playerSprites.put(my_id, mySprite);
		if (host != null) {
			this.host = host;
			send();
		} 
	}
	
	private void drawScreen() {
		ArrayList<PlayerInterface> players = screen.getPlayers();
		// System.out.println("drawScreen-playerSprites: " + playerSprites);
		// System.out.println("drawScreen-players: " + players);
		System.out.println("drawScreen-bulletSprites: " + bulletSprites);
		canvas.getChildren().clear();
		canvas.getChildren().add(mySprite);
		for (PlayerInterface player : players) {
			Circle playerSprite;
			if ( !playerSprites.containsKey(player.getUniqueId())) {
				playerSprite = new Circle(player.getCoordinates().getX(), player.getCoordinates().getY(), 20);
				playerSprites.put(player.getUniqueId(), playerSprite);
				
				canvas.getChildren().add(playerSprite);
				
				for( BulletInterface bullet : player.getBullets() ) {
					Circle bulletSprite = new Circle(bullet.getCoordinates().getX(), bullet.getCoordinates().getY(), 5);
					bulletSprites.put(player.getUniqueId(), bulletSprite);
					canvas.getChildren().add(bulletSprite);
				}
			} else if (!(player.getUniqueId() == screen.getMe().getUniqueId())){
				playerSprite = playerSprites.get(player.getUniqueId());
				playerSprites.put(player.getUniqueId(), new Circle(player.getCoordinates().getX(), player.getCoordinates().getY(), 20));
				canvas.getChildren().add(playerSprite);
			}
			
			for ( int i = 0; i < player.getBullets().size(); i++) {
				BulletInterface bullet = player.getBullets().get(i);
				Circle bulletSprite = bulletSprites.get(player.getUniqueId());
				// System.out.println("bulletSprite: " + bulletSprite);
				bulletSprites.put(player.getUniqueId(), new Circle(bullet.getCoordinates().getX(), bullet.getCoordinates().getY(), 5));
				canvas.getChildren().add(bulletSprite);
			}
		}
		screen.updateMyPlayer();
		// System.out.println("drawScreen: " + players);
	}
	
	public ScreenBuffer getScreen() {
		return screen;
	}
	
	private void move(double x, double y) {
		mySprite.setTranslateX(mySprite.getTranslateX() + x);
		mySprite.setTranslateY(mySprite.getTranslateY() + y);
	}
	
	private void update(List<PlayerData> players, int port) {
		screen.updatePlayers(players);
		screen.updateBullets();
		screen.updateMyPlayer();
	}
	
	public void updatePlayer(PlayerData player) {
		screen.updatePlayer(player);
		screen.updateMyPlayer();
	}
	
	private void send() { 
		System.out.println("send in GameController");
		if (talker != null && talker.isGoing()) {
			talker.halt();
		}
		JSON j = new JSON();
		String json = j.generateJson(screen.myPlayer);
		talker = new TalkerThread(json, host, port, channel);
		new GameReceiver().start();
		talker.start();
	}
	
	private class GameReceiver extends Thread {
		public void run() {
			while (talker.isGoing()) {
				String line;
				try {
					line = channel.take();
					if (line.endsWith("}}]}")) {
						Platform.runLater( () -> { 
							JSON j = new JSON();
							System.out.println(line);
							update( j.parseJson(line), port);
							System.out.println("screen updated");
						} );
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
