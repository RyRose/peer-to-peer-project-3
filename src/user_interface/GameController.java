package user_interface;

import game.Direction;
import game.Player;
import game.ScreenBuffer;
import interfaces.BulletInterface;
import interfaces.PlayerInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;

import network.TalkerThread;
import network_to_game.GameToNetworkMessage;
import network_to_game.NetworkToGameMessage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.input.KeyCode;

public class GameController {
	@FXML
	Pane canvas;
	
	@FXML
	Circle me;
	
	ScreenBuffer screen;
	int my_id;
	private ArrayBlockingQueue<String> channel = new ArrayBlockingQueue<String>(2);
	HashMap<Integer, Circle> bullets = new HashMap<Integer, Circle>();
	HashMap<Integer, Circle> players = new HashMap<Integer, Circle>();
	private TalkerThread talker;
	private String host;
	private NetworkToGameMessage networkMessage; //TODO: the game controller will draw data from here and update the data through this?
	
	@FXML
	private void initialize() {
		
		//initializeScreen(networkMessage, my_id);
		
		Timer timer = new Timer();
		
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				drawScreen();
				if (host != null){
					send(8888);
				}
			}
			
		}, 0, 15);

		canvas.setOnKeyPressed(event -> {
			double x = 0;
			double y = 0;
			System.out.println(screen.me);
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
				Circle bullet = new Circle(me.getTranslateX() + me.getLayoutX(), me.getTranslateY() + me.getLayoutY(), 5);
				bullets.put(my_id, bullet);
				canvas.getChildren().add(bullet);
				screen.shootBullet();
			}
			move(x,y);
		});
		
	}
	
	public void start() {
		send(8888);
	}
	
	public void initialize(NetworkToGameMessage message, int unique_id) {
		this.networkMessage = message;
		screen = new ScreenBuffer(networkMessage, my_id);
	}
	
	private void drawScreen() {
		ArrayList<PlayerInterface> players = screen.getPlayers();
		for (PlayerInterface player : players) {
			Circle playerSprite = this.players.get(player.getUniqueId());
			playerSprite.setLayoutX(player.getCoordinates().getX());
			playerSprite.setLayoutY(player.getCoordinates().getY());
			for (BulletInterface bullet : player.getBullets()) {
				Circle bulletSprite = this.bullets.get(player.getUniqueId());
				bulletSprite.setLayoutX(bullet.getCoordinates().getX());
				bulletSprite.setLayoutY(bullet.getCoordinates().getY());
			}
		}
	}
	
	public void initializeScreen(NetworkToGameMessage message, int unique_id) {
		this.networkMessage = message;
		screen = new ScreenBuffer(networkMessage, my_id);
		me = new Circle(screen.getMe().getCoordinates().getX(), screen.getMe().getCoordinates().getY(), 20);
	}
	
	@FXML
	public void grabKeys() {
		canvas.requestFocus();
	}	
	
	public void initializeHost(String host) {
		this.host = host;
	}
	
	public void initializePlayer( PlayerInterface playerInterface ) {
		screen.me = (Player) playerInterface;
		me = new Circle(screen.getMe().getCoordinates().getX(), screen.getMe().getCoordinates().getY(), 20);
		my_id = playerInterface.getUniqueId();
		
	}
	
	private void move(double x, double y) {
		me.setTranslateX(me.getTranslateX() + x);
		me.setTranslateY(me.getTranslateY() + y);
	}
	
	public ScreenBuffer getScreen() {
		return screen;
	}
	
	public void updatePlayer(NetworkToGameMessage message) {
		screen.updatePlayer(message);
	}
	
	private void update(NetworkToGameMessage message, int port) {
		screen.updatePlayers(message);
		screen.updateBullets();
	}
	
	private void send(int port) { //TODO: call this periodically IF host is not null!
		if (talker != null && talker.isGoing()) {
			talker.halt();
		}
		String json = new GameToNetworkMessage(screen.me, null).getSingleJson();
		System.out.println(json);
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
						Platform.runLater( () -> { update(new NetworkToGameMessage(line, true), 8888); } );
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
