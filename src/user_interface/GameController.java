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
	private GameReceiver receiverThread;
	private String host;
	private int port;

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
					send(port);
				}
			}
			
		}, 0, 5);

		canvas.setOnKeyPressed(event -> {
			double x = 0;
			double y = 0;
			if (event.getCode() == KeyCode.UP) {
				y = -5;
				screen.move(Direction.UP);
			} else if (event.getCode() == KeyCode.DOWN) {
				y = 5;
				screen.move(Direction.DOWN);
			}else if (event.getCode() == KeyCode.LEFT) {
				x = -5;
				screen.move(Direction.LEFT);
			} else if (event.getCode() == KeyCode.RIGHT) {
				x = 5;
				screen.move(Direction.RIGHT);
			} else if (event.getCode() == KeyCode.SPACE) {
				Circle bullet = new Circle(mySprite.getTranslateX() + mySprite.getLayoutX() + 5, mySprite.getTranslateY() + mySprite.getLayoutY() + 5, 5);
				bulletSprites.put(my_id, bullet);
				canvas.getChildren().add(bullet);
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
		System.out.println("game initializing");
		screen = new ScreenBuffer(players, my_id);
		screen.myPlayer = (Player) my_player;
		mySprite = new Circle(screen.getMe().getCoordinates().getX(), screen.getMe().getCoordinates().getY(), 20);
		//TODO: set sprite color
		canvas.getChildren().add(mySprite);
		my_id = my_player.getUniqueId();
		playerSprites.put(my_id, mySprite);
		receiverThread = new GameReceiver();
		if (host != null) {
			this.host = host;
			send(port);
		} else {
			host = null;
		}
	}
	
	private void drawScreen() {
		ArrayList<PlayerInterface> players = screen.getPlayers();
		for (PlayerInterface player : players) {
			// System.out.println(players);
			// System.out.println(playerSprites);
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
				for (BulletInterface bullet : player.getBullets()) {
					Circle bulletSprite = bulletSprites.get(player.getUniqueId());
					bulletSprite.setTranslateX(bullet.getCoordinates().getX());
					bulletSprite.setTranslateY(bullet.getCoordinates().getY());
				}
			}
		}
	}
	
	public ScreenBuffer getScreen() {
		return screen;
	}
	
	private void move(double x, double y) {
		mySprite.setTranslateX(mySprite.getTranslateX() + x);
		mySprite.setTranslateY(mySprite.getTranslateY() + y);
	}
	
	private void update(List<PlayerData> players, int port) {
		System.out.println("update players");
		screen.updatePlayers(players);
		screen.updateBullets();
	}
	
	public void updatePlayer(PlayerData player) {
		screen.updatePlayer(player);
	}
	
	private void send(int port) { 
		System.out.println("send in GameController");
		if (talker != null && talker.isGoing()) {
			talker.halt();
		}
		JSON j = new JSON();
		String json = j.generateJson(screen.myPlayer);
		talker = new TalkerThread(json, host, port, channel);
		if ( !receiverThread.isAlive() )
			receiverThread.start();
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
