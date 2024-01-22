package org.btt_javafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

public class HomeFxmlController {
	private Stage stage;
	
	@FXML private TilePane tilePane;
	
	@FXML private Button homeBtn;
	
	@FXML protected void homeBtnActn(ActionEvent event)  throws Exception {
		stage = (Stage)homeBtn.getScene().getWindow();
		
		Parent root = FXMLLoader.load(getClass().getResource("btt_access.fxml"));
		
		stage.getScene().setRoot(root);
		stage.setMaximized(true);
		stage.show();
	}
	@FXML protected void exitBtnActn(ActionEvent event)  throws Exception {
		System.exit(0);
	}
	
	public void initialize() {
	    	
	}  
}
