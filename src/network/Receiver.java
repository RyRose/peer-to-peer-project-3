package network;

import game.Player;

import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;

import user_interface.ControlJoinPage;
import user_interface.GameController;
import javafx.application.Platform;
import network_to_game.JSON;

public class Receiver extends Thread {
	private ArrayBlockingQueue<String> channel;
	private TalkerThread talker;
	private int port = 8888;
	private GameController gameController = null;
	private ControlJoinPage joinController = null;
	
	public Receiver(TalkerThread talker, ArrayBlockingQueue<String> channel, GameController gameController) {
		this.talker = talker;
		this.channel = channel;
		this.gameController = gameController;
	}
	
	public Receiver(TalkerThread talker, ArrayBlockingQueue<String> channel, ControlJoinPage joinController) {
		this.talker = talker;
		this.channel = channel;
		this.joinController = joinController;
	}
	
	@Override
	public void run() {
		while (talker.isGoing()) {
			String line;
			try {
				line = channel.take();
				if (JSON.isGameJson(line)) {
					updateGameOrStartGame(line);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void updateGameOrStartGame(String line) {
		Player[] players = JSON.parseJson(line);
		if (gameController != null) {
			Platform.runLater( () -> { 
				gameController.update(players, port);
			} );
		}
		else if (joinController != null) {
			if (players.length == 1) {
				joinController.initializePlayer( players[0] );
			} else {
				Platform.runLater( () -> {joinController.startGame(Arrays.asList(players)); } );
			}
		}
	}
	
	public void updateTalker(TalkerThread talker) {
		this.talker = talker;
	}

	
	
	
}
