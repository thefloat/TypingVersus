package org.btt_javafx;

import java.util.Optional;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ArenaFxmlController {
	private BttLogic bttLogic = new BttLogic();
	
	private Stage stage;
	
	@FXML private BttTextEditorController bttTextEditorController = new BttTextEditorController();
	
	@FXML private Button chatBtn, closeChatBtn, newBtn, goBtn, sndBtn, statsBtn;
    
	@FXML private MenuItem resetScoresMI, resetStatsMI, resetAllMI, clearChatMI, exitMI; 
	
	@FXML private Label timerLbl, youNameLbl, oppNameLbl, youNameStatsLbl, oppNameStatsLbl;
    
    @FXML private Text youScoreTxt, oppScoreTxt, youWPMTxt, oppWPMTxt,
    					youWPMValTxt, oppWPMValTxt, youPointsTxt, oppPointsTxt, youWinLoseTxt, oppWinLoseTxt, arenaTxt, timerCDTxt;
    
    @FXML private TextField textField;
    
    @FXML private ProgressBar youPBar, oppPBar;
    
    @FXML private BorderPane chatBp, statsBp;
    
    @FXML private VBox chatVB, statsVB;
    
    @FXML private ScrollPane chatSp, statsSp;
    
	@FXML protected void chatBtnActn(ActionEvent event) {
		arenaTxt.setVisible(false);
		chatBp.setVisible(true);
		statsBp.setVisible(false);
		
	}
	@FXML protected void statsBtnActn(ActionEvent event) {
		arenaTxt.setVisible(false);
		chatBp.setVisible(false);
		statsBp.setVisible(true);
		
	}
	@FXML protected void resetScoresMIActn(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.initStyle(StageStyle.UTILITY);
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to reset the scores?");
		
		Optional<ButtonType> result = alert.showAndWait();
		
		if(result.get() == ButtonType.OK) {
			bttLogic.handleResetScoresMI(youScoreTxt, oppScoreTxt);
		}
	}
	@FXML protected void resetStatsMIActn(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.initStyle(StageStyle.UTILITY);
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to reset the stats?");
		
		Optional<ButtonType> result = alert.showAndWait();
		
		if(result.get() == ButtonType.OK) {
			
		}
	}
	@FXML protected void resetAllMIActn(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.initStyle(StageStyle.UTILITY);
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to reset all?");
		
		Optional<ButtonType> result = alert.showAndWait();
		
		if(result.get() == ButtonType.OK) {
			youScoreTxt.setText("0");
			oppScoreTxt.setText("0");

		}
	}
	@FXML protected void clearChatMIActn(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.initStyle(StageStyle.UTILITY);
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to clear the chat?");
		
		Optional<ButtonType> result = alert.showAndWait();
		
		if(result.get() == ButtonType.OK) {
			chatVB.getChildren().clear();
		}
	}
	@FXML protected void exitMIActn(ActionEvent event)  throws Exception {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.initStyle(StageStyle.UTILITY);
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to exit the arena?");
		
		Optional<ButtonType> result = alert.showAndWait();
		
		if(result.get() == ButtonType.OK) {
			bttLogic.handleExitMI();
			
		}
	}
	@FXML protected void closeChatBtnActn(ActionEvent event) {
		arenaTxt.setVisible(true);
		chatBp.setVisible(false);
		
	}
	@FXML protected void closeStatsBtnActn(ActionEvent event) {
		arenaTxt.setVisible(true);
		statsBp.setVisible(false);
		
	}
	@FXML protected void newBtnActn(ActionEvent event) {
		bttLogic.handleRound(false, false, bttTextEditorController.scrollPane, chatBp, statsBp, statsVB, newBtn, bttTextEditorController.textFlow, bttTextEditorController.textArea, youPBar, oppPBar, timerLbl, youNameLbl, oppNameLbl, youScoreTxt, oppScoreTxt, youWPMTxt, oppWPMTxt, youWPMValTxt, oppWPMValTxt, youPointsTxt, oppPointsTxt, youWinLoseTxt, oppWinLoseTxt, arenaTxt, timerCDTxt);

		goBtn.setDisable(false);
		
	}
	@FXML protected void goBtnActn(ActionEvent event) {
		newBtn.setDisable(true);
		
		goBtn.setDisable(true);
		
		bttLogic.handleRound(false, true, bttTextEditorController.scrollPane, chatBp, statsBp, statsVB, newBtn, bttTextEditorController.textFlow, bttTextEditorController.textArea, youPBar, oppPBar, timerLbl, youNameLbl, oppNameLbl, youScoreTxt, oppScoreTxt, youWPMTxt, oppWPMTxt, youWPMValTxt, oppWPMValTxt, youPointsTxt, oppPointsTxt, youWinLoseTxt, oppWinLoseTxt, arenaTxt, timerCDTxt);
		
	}
	@FXML protected void sndBtnActn(ActionEvent event) {
		textField.requestFocus();
		
		String mssg = textField.getText();

		bttLogic.haddleSndBtn(mssg, textField, chatVB);
		
	}
	public void initialize() {
	    chatSp.setFitToWidth(true);
	    
	    statsSp.setFitToWidth(true);
	    
	    chatVB.heightProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				chatSp.setVvalue(1.0);
				
			}
		});
	    
	    statsVB.heightProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				statsSp.setVvalue(1.0);
				
			}
		});
	    
	    textField.addEventHandler(KeyEvent.ANY, new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode() == KeyCode.ENTER) {
					String mssg = textField.getText();
					
					bttLogic.haddleSndBtn(mssg, textField, chatVB);;
				}
				
			}
		});
	    
	    bttLogic.handleCreateServer(bttLogic, bttTextEditorController.scrollPane, chatBp, statsBp, chatVB, statsVB, newBtn, bttTextEditorController.textFlow, 
	    		bttTextEditorController.textArea, youPBar, oppPBar, timerLbl, youNameLbl, oppNameLbl, youNameStatsLbl, oppNameStatsLbl,
	    		youScoreTxt, oppScoreTxt, youWPMTxt, oppWPMTxt, youWPMValTxt, oppWPMValTxt, youPointsTxt, 
	    		oppPointsTxt, youWinLoseTxt, oppWinLoseTxt, arenaTxt, timerCDTxt);
	    
	    newBtn.setDisable(false);
    }  
}
