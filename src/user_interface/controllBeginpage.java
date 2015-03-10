package user_interface;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;


public class controllBeginpage {
	
	@FXML
	Button Join;
	@FXML
	Button Create;
	
	@FXML
	private void initialize() {
	}
	
	@FXML
	private void openCreatePage() throws IOException {
		Parent home_page_parent = FXMLLoader.load(getClass().getResource("createpage.fxml"));
		Scene home_page_scene = new Scene(home_page_parent);
		Stage app_stage = (Stage) Create.getScene().getWindow();
		app_stage.setScene(home_page_scene);
		app_stage.show();
	}
	
	@FXML
	private void openJoinPage() throws IOException {
		Parent home_page_parent = FXMLLoader.load(getClass().getResource("joinpage.fxml"));
		Scene home_page_scene = new Scene(home_page_parent);
		Stage app_stage = (Stage) Create.getScene().getWindow();
		app_stage.setScene(home_page_scene);
		app_stage.show();
	}
}
