package test;

import static org.junit.Assert.*;
import game.Bullet;
import game.Direction;
import game.Map;
import game.Player;
import game.Point;
import game.ScreenBuffer;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ScreenBufferTest {

	@Test
	public void test() {
		List<Player> players = new ArrayList<Player>();
		players.add(new Player(new Point(0,0), Direction.UP));
		players.add(new Player(new Point(1,0), Direction.DOWN));
		
		ArrayList<Point> obstacles = new ArrayList<Point>();
		obstacles.add(new Point(0,1));
		
		Map map = new Map(players , obstacles);
		ScreenBuffer screen = new ScreenBuffer(map);
		assertTrue(screen.isConsistent());
		
		screen.myPlayer = new Player();
		screen.myPlayer.setHeading(Direction.UP);
		checkKillPlayer(screen);
		checkShootBullet(screen);
		checkMove(screen);
		
		
	}
	
	private void checkKillPlayer(ScreenBuffer screen) {
		screen.killPlayer();
		assertFalse(screen.myPlayer.isAlive());
		assertTrue(screen.isConsistent());
	}
	
	private void checkShootBullet(ScreenBuffer screen) {
		ArrayList<Bullet> old = screen.getMe().getBullets();
		screen.shootBullet();
		ArrayList<Bullet> cur = screen.getMe().getBullets();
		assertTrue(cur.size() == old.size() + 1);
		assertTrue(screen.isConsistent());
	}
	
	private void checkMove(ScreenBuffer screen) {
		Point oldPosition = screen.getMe().getCoordinates();
		screen.move(Direction.DOWN);
		Point newPosition = screen.getMe().getCoordinates();
		assertTrue((newPosition.getX() == oldPosition.getX()) && (newPosition.getY() == oldPosition.getY() + 5));
		assertTrue(screen.getMe().getHeading() == Direction.DOWN);
		oldPosition = newPosition;
		assertTrue(screen.isConsistent());
		
		screen.move(Direction.UP);
		newPosition = screen.getMe().getCoordinates();
		assertTrue((newPosition.getX() == oldPosition.getX()) && (newPosition.getY() == oldPosition.getY() - 5));
		assertTrue(screen.getMe().getHeading() == Direction.UP);
		oldPosition = newPosition;
		assertTrue(screen.isConsistent());
		
		screen.move(Direction.LEFT);
		newPosition = screen.getMe().getCoordinates();
		assertTrue((newPosition.getX() == oldPosition.getX() - 5) && (newPosition.getY() == oldPosition.getY()));
		assertTrue(screen.getMe().getHeading() == Direction.LEFT);
		oldPosition = newPosition;
		assertTrue(screen.isConsistent());
		
		screen.move(Direction.RIGHT);
		newPosition = screen.getMe().getCoordinates();
		assertTrue((newPosition.getX() == oldPosition.getX() + 5) && (newPosition.getY() == oldPosition.getY()));
		assertTrue(screen.getMe().getHeading() == Direction.RIGHT);
		oldPosition = newPosition;
		assertTrue(screen.isConsistent());
	}

}
