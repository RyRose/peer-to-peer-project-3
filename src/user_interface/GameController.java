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
	
	@FXML
	Circle me;
	
	ScreenBuffer screen;
	int my_id;
	private ArrayBlockingQueue<String> channel = new ArrayBlockingQueue<String>(2);
	HashMap<Integer, Circle> bullets = new HashMap<Integer, Circle>();
	HashMap<Integer, Circle> players = new HashMap<Integer, Circle>();
	private TalkerThread talker;
	private String host;
	
	@FXML
	private void initialize() {
				
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
	
	@FXML
	public void grabKeys() {
		canvas.requestFocus();
	}	
	
	public void initializeGame(PlayerInterface my_player, List<PlayerData> players, String host) {
		screen = new ScreenBuffer(players, my_id);
		screen.me = (Player) my_player;
		me = new Circle(screen.getMe().getCoordinates().getX(), screen.getMe().getCoordinates().getY(), 20);
		my_id = my_player.getUniqueId();
		if (host != null) {
			this.host = host;
			send(8888);
		} else {
			host = null;
		}
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
	
	public ScreenBuffer getScreen() {
		return screen;
	}
	
	public void updatePlayer(PlayerData player) {
		screen.updatePlayer(player);
	}
	
	private void move(double x, double y) {
		me.setTranslateX(me.getTranslateX() + x);
		me.setTranslateY(me.getTranslateY() + y);
	}
	
	private void update(List<PlayerData> players, int port) {
		screen.updatePlayers(players);
		screen.updateBullets();
	}
	
	private void send(int port) { //TODO: call this periodically IF host is not null!
		if (talker != null && talker.isGoing()) {
			talker.halt();
		}
		String json = JSON.generateJson(screen.me);
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
						Platform.runLater( () -> { update( JSON.parseJson(line), 8888); } );
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
