package user_interface;

import game.Direction;
import game.Player;
import game.ScreenBuffer;
import interfaces.PlayerInterface;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import network.TalkerThread;
import network_to_game.GameToNetworkMessage;
import network_to_game.NetworkMessage;
import network_to_game.NetworkToGameMessage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.input.KeyCode;

public class GameController {
	@FXML
	Pane canvas;
	
	@FXML
	Ellipse me;
	
	ScreenBuffer screen;
	int my_id;
	ArrayList<Circle> bullets;
	ArrayList<Ellipse> players;
	private ArrayBlockingQueue<String> channel = new ArrayBlockingQueue<String>(2);
	private TalkerThread talker;
	private String host;
	private NetworkToGameMessage networkMessage; //TODO: the game controller will draw data from here and update the data through this?
	
	@FXML
	private void initialize() {
		canvas.setOnKeyPressed(event -> {
			double x = 0;
			double y = 0;
			System.out.println(screen.me);
			if (event.getCode() == KeyCode.UP) {
				y = -1;
				screen.move(Direction.UP);
			} else if (event.getCode() == KeyCode.DOWN) {
				y = 1;
				screen.move(Direction.DOWN);
			}else if (event.getCode() == KeyCode.LEFT) {
				x = -1;
				screen.move(Direction.LEFT);
			} else if (event.getCode() == KeyCode.RIGHT) {
				x = 1;
				screen.move(Direction.RIGHT);
			} else if (event.getCode() == KeyCode.SPACE) {
				Circle bullet = new Circle(me.getTranslateX(), me.getTranslateY(), 5);
				bullets.add(bullet);
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
	
	public void initializeHost(String host) {
		this.host = host;
	}
	
	public void initializePlayer( PlayerInterface playerInterface ) {
		screen.me = (Player) playerInterface;
		me = new Ellipse(screen.getMe().getCoordinates().getX(), screen.getMe().getCoordinates().getY());
		my_id = playerInterface.getUniqueId();
		
	}
	
	private void move(double x, double y) {
		me.setTranslateX(me.getTranslateX() + x);
		me.setTranslateY(me.getTranslateY() + y);
	}
	
	private void shoot() {
		double heading = screen.getMe().getHeadingAsDouble();
		for (Circle bullet : bullets) {
			bullet.setTranslateX(bullet.getTranslateX() + 10*Math.cos(heading));
			bullet.setTranslateY(bullet.getTranslateY() - 10*Math.sin(heading));
		}
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
		shoot();
		send(port);
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
