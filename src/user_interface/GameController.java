package user_interface;

import game.Bullet;
import game.Direction;
import game.Player;
import game.ScreenBuffer;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;

import network.Receiver;
import network.TalkerThread;
import network_to_game.JSON;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.input.KeyCode;

public class GameController {
	
	@FXML
	Pane canvas;
	
	private Circle mySprite;
	private ScreenBuffer screen;
	private int my_id;
	private ArrayBlockingQueue<String> channel = new ArrayBlockingQueue<String>(2);
	private HashMap<Integer, Circle> bulletSprites = new HashMap<Integer, Circle>();
	private HashMap<Integer, Circle> playerSprites = new HashMap<Integer, Circle>();
	private TalkerThread talker;
	private String host;
	private int port = 8888;
	
	private network.Receiver receiver;

	@FXML
	private void initialize() {
		receiver = new network.Receiver(null, channel, this);
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
					Platform.runLater(() -> {
					send();
					} );
				}
			}
		}, 0, 15);
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
				if (screen.getMe().isAlive()) {
					Circle bullet = new Circle(mySprite.getTranslateX() + mySprite.getLayoutX() + 5, mySprite.getTranslateY() + mySprite.getLayoutY() + 5, 5);
					bulletSprites.put(my_id, bullet);
					screen.shootBullet();
				}
			}
			move(x,y);
		});
		
	}
	
	public void initializeGame(Player my_player, List<Player> players, String host) {
		canvas.requestFocus();
		screen = new ScreenBuffer(players, my_id);
		screen.setMyPlayer(my_player);
		mySprite = new Circle(screen.getMe().getCoordinates().getX(), screen.getMe().getCoordinates().getY(), 20);
		mySprite.setFill(screen.getMyPlayer().getColor());
		canvas.getChildren().add(mySprite);
		my_id = my_player.getUniqueId();
		playerSprites.put(my_id, mySprite);
		if (host != null) {
			this.host = host;
			send();
		} 
		
	}
	
	private void drawScreen() {
		List<Player> players = screen.getPlayers();
		canvas.getChildren().clear();
		for (Player player : players) {
			Circle playerSprite;
			if( player.isAlive() ) {
				if (!playerSprites.containsKey(player.getUniqueId()) ) {
					playerSprite = new Circle(player.getCoordinates().getX(), player.getCoordinates().getY(), 20);
					playerSprites.put(player.getUniqueId(), playerSprite);					
					for( Bullet bullet : player.getBullets() ) {
						Circle bulletSprite = new Circle(bullet.getX(), bullet.getY(), 5);
						bulletSprites.put(player.getUniqueId(), bulletSprite);
						canvas.getChildren().add(bulletSprite);
					}
				}
				playerSprite = playerSprites.get(player.getUniqueId());
				playerSprite.setFill(player.getColor());
				playerSprites.put(player.getUniqueId(), new Circle(player.getCoordinates().getX(), player.getCoordinates().getY(), 20));
				for ( int i = 0; i < player.getBullets().size(); i++) {
					Bullet bullet = player.getBullets().get(i);
					Circle bulletSprite = bulletSprites.get(player.getUniqueId());
					bulletSprites.put(player.getUniqueId(), new Circle(bullet.getX(), bullet.getY(), 5));
					canvas.getChildren().add(bulletSprite);
				}
				canvas.getChildren().add(playerSprite);
			} else if (playerSprites.containsKey(player.getUniqueId())) {
				playerSprites.remove(player.getUniqueId());
			}
		}
		screen.updateMyPlayer();
	}
	
	public ScreenBuffer getScreen() {
		return screen;
	}
	
	private void move(double x, double y) {
		mySprite.setTranslateX(mySprite.getTranslateX() + x);
		mySprite.setTranslateY(mySprite.getTranslateY() + y);
	}
	
	public void update(Player[] players, int port) {
		screen.updatePlayers(players);
		screen.updateBullets();
		screen.updateMyPlayer();
	}
	
	public void updatePlayer(Player player) {
		screen.updatePlayer(player);
		screen.updateBullets();
		screen.updateMyPlayer();
	}
	
	private void send() { 
		if (talker != null && talker.isGoing()) {
			talker.halt();
		}
		String json = JSON.generateJson(screen.getMyPlayer());
		talker = new TalkerThread(json, host, port, channel);
		receiver = new Receiver(talker, channel, this);
		talker.start();
		receiver.start();
	}

}
