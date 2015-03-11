package game;

import java.util.ArrayList;

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
	ArrayList<Circle> bullets;
	
	@FXML
	private void initialize() {
		
		shoot();
		screen.updateBullets();
		
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
				screen.shootBullet();
			}
			move(x,y);
		});
		
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
}