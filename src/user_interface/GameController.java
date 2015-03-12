package user_interface;

import game.Direction;
import game.Player;
import game.ScreenBuffer;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import network.TalkThread;
import network_to_game.NetworkMessage;
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
	private ArrayBlockingQueue<String> channel;
	private TalkThread talker;
	private String host;
	private NetworkMessage networkMessage; //TODO: the game controller will draw data from here and update the data through this?
	
	@FXML
	private void initialize() {
		
		screen = new ScreenBuffer(networkMessage, my_id);
		me = new Ellipse(screen.getMe().getCoordinates().getX(), screen.getMe().getCoordinates().getY());
		
		canvas.setOnKeyPressed(event -> {
			double x = 0;
			double y = 0;
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
	
	public void initializeNetworkMessage(NetworkMessage networkMessage) {
		this.networkMessage = networkMessage;
	}
	
	public void initializeHost(String host) {
		this.host = host;
	}
	
	public void initializeUniqueId(int id) {
		my_id = id;
	}
	
	public void initializePlayer( Player player ) {
		screen.me = player;
		my_id = player.getUniqueId();
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
	
	private void update(int port) {
		screen.updatePlayers(networkMessage);
		screen.updateBullets();
		shoot();
		send(port);
	}
	
	private void send(int port) { //TODO: call this periodically IF host is not null!
		if (talker != null && talker.isGoing()) {
			talker.halt();
		}
		talker = new TalkThread(networkMessage.getMyPlayerJson(), host, port, channel);
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
						networkMessage.setNetworkToGameMessage(line, true);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
