package org.btt_javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BttApp extends Application{

	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("btt_home.fxml"));

		Scene scene = new Scene(root);
		
        stage.setTitle("Bt Typing");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
		
	}	
	public static void main(String[] args) {
		launch(args);
		
	}

}
