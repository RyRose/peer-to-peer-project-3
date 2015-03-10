package user_interface;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class controllcreatepage {
	@FXML
	Button Begin;
	@FXML
	ListView<String> listNames;
	
	network.Server server;
	ObservableList<String> names = FXCollections.observableArrayList();
	
	@FXML
	public void intialize() throws IOException {
		server = new network.Server(8888);
		listNames.setItems(names);
	}
	
	@FXML
	private void begin(){
		server.setStarted(true);
		//TODO: new game controller with all players in the listview starts and send a message with all initial coordinates
	}
	
	public void addtolist(String name) {
		names.add(name);
	}
}
