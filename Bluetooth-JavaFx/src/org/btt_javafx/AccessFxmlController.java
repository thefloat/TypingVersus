package org.btt_javafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

public class AccessFxmlController {
	private Stage stage;
	
	@FXML private TilePane tilePane;
	
	@FXML private Button accessBtn1, accessBtn2;
	
	@FXML protected void accessBtnActn1(ActionEvent event) throws Exception{
		stage = (Stage)accessBtn1.getScene().getWindow();
		
		Parent root = FXMLLoader.load(getClass().getResource("btt_arena.fxml"));
		
		stage.getScene().setRoot(root);
		stage.setMaximized(true);
		stage.show();
		
	}
	@FXML protected void accessBtnActn2(ActionEvent event) throws Exception{
		stage = (Stage)accessBtn2.getScene().getWindow();
		
		Parent root = FXMLLoader.load(getClass().getResource("btt_arena_client.fxml"));

		stage.getScene().setRoot(root);
		stage.setMaximized(true);
		stage.show();
	}
	@FXML protected void accessBtnActn3(ActionEvent event) throws Exception{
		stage = (Stage)accessBtn2.getScene().getWindow();
		
		Parent root = FXMLLoader.load(getClass().getResource("btt_home.fxml"));

		stage.getScene().setRoot(root);
		stage.setMaximized(true);
		stage.show();
	}
	public void initialize() {
		
	}  

}
