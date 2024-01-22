package org.btt_javafx;

import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Screen;

public class BttTextEditor {
	Boolean skip = false;
	Boolean cnscFail = false;
	
	int trckLenght = 0;
	
	int crrctLength = 0;
	
	int points = 0;
	
	String pssgText = null;
	
	int pssgLength = 0;
	
	Double maxVal = 0.0;
	
	Text nxtTxt = new Text();
	
	public void configTextEditor(ScrollPane scrollPane, TextFlow textFlow, TextArea textArea, int pssgNum, Text youWPMValTxt, Label timerLbl, BttNetwork bttNetwork) {
		pssgText = BttPassages.passages[pssgNum];
		pssgLength = pssgText.length();
		
		textFlow.getChildren().clear();
		
//		Add the passage to the TextFlow
		for(int i=0; i<pssgLength; i++) {
			Text text1 = new Text();
			text1.setText(String.valueOf(pssgText.charAt(i)));
			text1.setFill(Color.BLACK);
			text1.setFont(Font.font(35));
			
			textFlow.getChildren().add(text1);
			
		}
		
		nxtTxt.layoutYProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				Double diff = (double)arg2 - (double)arg1;
				
				Double scrollPerc = ((diff*400)/textFlow.getHeight())/100;
				
				if(((double)arg2+diff) > (Screen.getPrimary().getVisualBounds().getHeight()/2 -20)) {
					scrollPane.setVvalue(scrollPane.getVvalue()+scrollPerc);
				}
				
			}
		});
		textArea.setFont(Font.font(35));
		textArea.setOpacity(0);
		textArea.setContextMenu(new ContextMenu());
		textArea.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {

			@Override
			public void handle(ScrollEvent se) {
				se.consume();
				
			}
		});

		textArea.addEventFilter(MouseEvent.ANY, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				if(me.getButton() == MouseButton.PRIMARY) {
					textArea.end();
					
				}
			}
		});
		textArea.addEventFilter(KeyEvent.ANY, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				if(e.getCode()  == KeyCode.Z || e.getCode() == KeyCode.Y || e.getCode() == KeyCode.A || 
					e.getCode() == KeyCode.V || e.getCode() == KeyCode.C && e.isShortcutDown() || 
					e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.BACK_SPACE) {
						e.consume();
				}
				
			}
		});
		
	}
	
	public void createCrrctText(boolean serverActive, String txt, VBox statsVB, TextFlow textFlow, TextArea textArea, 
			Text youScoreTxt, Text oppScoreTxt, Text youPointsTxt, Text oppPointsTxt, Text youWPMTxt, Text youWPMValTxt, Text oppWPMValTxt, Text youWinLoseTxt, 
			Text oppWinLoseTxt, Label timerLbl, Timeline timer, Button newBtn, BttNetwork bttNetwork) 
	{
		
		Text txtCrrct = new Text();
		txtCrrct.setFill(Color.BLUE);
		txtCrrct.setFont(Font.font(35));
		txtCrrct.setText(txt);

		textFlow.getChildren().remove(trckLenght);
		textFlow.getChildren().add(trckLenght, txtCrrct);
		
		crrctLength++;
		
		if(textArea.getLength() >= pssgText.length()) {
			textArea.setEditable(false);
			
			timer.stop();
			
			youWPMTxt.setText("WPM");
			

			finishedRound(statsVB, textArea, youScoreTxt, oppScoreTxt, youWPMValTxt, oppWPMValTxt, youPointsTxt, oppPointsTxt, youWinLoseTxt, oppWinLoseTxt, timerLbl, bttNetwork);
			
			if(serverActive) {
				newBtn.setDisable(false);
			}
			
		}else {
			nxtTxt.setFill(Color.BLACK);
			nxtTxt.setFont(Font.font(35));
			nxtTxt.setText(String.valueOf(pssgText.charAt(trckLenght+1)));
			nxtTxt.setUnderline(true);
			
			textFlow.getChildren().remove(trckLenght+1);
			textFlow.getChildren().add(trckLenght+1, nxtTxt);
			
//			Increment trckLenght
			trckLenght++;
		}
		
	}
	public void createWrngText(boolean serverActive, String txt, VBox statsVB, TextFlow textFlow, TextArea textArea, 
			Text youScoreTxt, Text oppScoreTxt, Text youPointsTxt, Text oppPointsTxt, Text youWPMTxt, Text youWPMValTxt, Text oppWPMValTxt, Text youWinLoseTxt, 
			Text oppWinLoseTxt, Label timerLbl, Timeline timer, Button newBtn, BttNetwork bttNetwork) 
	{
		Text txtWrng = new Text();
		txtWrng.setFill(Color.RED);
		txtWrng.setFont(Font.font(35));
		txtWrng.setText(txt);
		
		textFlow.getChildren().remove(trckLenght);
		textFlow.getChildren().add(trckLenght, txtWrng);
		
		if(textArea.getLength() >= pssgText.length()) {
			textArea.setEditable(false);
			
			timer.stop();
			
			youWPMTxt.setText("WPM");
			
			finishedRound(statsVB, textArea, youScoreTxt, oppScoreTxt, youWPMValTxt, oppWPMValTxt, youPointsTxt, oppPointsTxt, youWinLoseTxt, oppWinLoseTxt, timerLbl, bttNetwork);
			
			if(serverActive) {
				newBtn.setDisable(false);
			}
			
		}else {
			nxtTxt.setFill(Color.BLACK);
			nxtTxt.setFont(Font.font(35));
			nxtTxt.setText(String.valueOf(pssgText.charAt(trckLenght+1)));
			nxtTxt.setUnderline(true);
			
			textFlow.getChildren().remove(trckLenght+1);
			textFlow.getChildren().add(trckLenght+1, nxtTxt);
			
//			Increment trckLenght
			trckLenght++;
		}
		
	}
	public void reconfigTE(ScrollPane scrollPane, TextFlow textFlow, TextArea textArea, int pssgNum, Text youWPMValTxt, Label timerLbl) {
//		Reset variables
		skip = false;
		cnscFail = false;
		
		trckLenght = 0;
		
		crrctLength = 0;
		
		points = 0;
		
		maxVal = 0.0;
		
		scrollPane.setVvalue(0.0);

		textArea.clear();
		
		textFlow.getChildren().clear();
		
		pssgText = BttPassages.passages[pssgNum];
		
		pssgLength = pssgText.length();
		
		for(int i=0; i<pssgLength; i++) {
			Text text1 = new Text();
			text1.setText(String.valueOf(pssgText.charAt(i)));
			text1.setFill(Color.BLACK);
			text1.setFont(Font.font(35));
			
			textFlow.getChildren().add(text1);
			
		}

	}
	public void finishedRound(VBox statsVB, TextArea textArea, Text youScoreTxt, Text oppScoreTxt, Text youWPMValTxt, Text oppWPMValTxt, Text youPointsTxt, 
			Text oppPointsTxt, Text youWinLoseTxt, Text oppWinLoseTxt, Label timerLbl, BttNetwork bttNetwork) 
	{
		double lttrs=0, words=0,
		timeSec=0, timeMin=0;
		
		int WPM=0;
				
//		lttrs = pssgLength;
		lttrs = textArea.getLength();
		
		timeSec = Double.parseDouble(timerLbl.getText());
		
		words = lttrs/4.7;
		
		timeMin = timeSec/60;
		
//		Calculate WPM
		WPM = (int) Math.round(words/timeMin);
		
//		Calculate points
		points = WPM + crrctLength;
		
//		display WPM
		youWPMValTxt.setText(WPM+"");
		
//		Send WPM
		bttNetwork.sndMssg("*w"+WPM);
		
//		display points
		youPointsTxt.setText(points+"");
		
//		Send points
		bttNetwork.sndMssg("*po"+points);
		
//		If user is done compare points to find winner
		if(!oppPointsTxt.getText().equals("")) {
//			Check win or lose
			if(Integer.parseInt(youPointsTxt.getText()) > Integer.parseInt(oppPointsTxt.getText())) {
//				Win
				youWinLoseTxt.setText(": )");
				
				oppWinLoseTxt.setText(": (");
				
				timerLbl.setText("won");
				
				youScoreTxt.setText((Integer.parseInt(youScoreTxt.getText())+1)+"");
				
			}else if(Integer.parseInt(youPointsTxt.getText()) < Integer.parseInt(oppPointsTxt.getText())) {
//				Lose
				youWinLoseTxt.setText(": (");
				
				oppWinLoseTxt.setText(": )");
				
				timerLbl.setText("lost");
				
				oppScoreTxt.setText((Integer.parseInt(oppScoreTxt.getText())+1)+"");
				
			}else {
//				Draw
				timerLbl.setText("draw");
				
			}

			statsVB.getChildren().addAll(insertStats(new Text(youWPMValTxt.getText()), new Text(youPointsTxt.getText()),
					new Text(oppWPMValTxt.getText()), new Text(oppPointsTxt.getText())), new Separator(Orientation.HORIZONTAL)); 
			
		}
		
	}
	public HBox insertStats(Text youWPMValTxt, Text youPointsTxt, Text oppWPMValTxt, Text oppPointsTxt) {
		youWPMValTxt.setFont(new Font(14));
		youPointsTxt.setFont(new Font(10));
		
		oppWPMValTxt.setFont(new Font(14));
		oppPointsTxt.setFont(new Font(10));
		
		HBox hBox = new HBox();
		
		VBox vBox;
		
		vBox = new VBox();
		
		vBox.getChildren().addAll(new Text("WPM"));
		vBox.getChildren().addAll(youWPMValTxt);
		vBox.getChildren().addAll(youPointsTxt);
		
		HBox.setHgrow(vBox, Priority.ALWAYS);
		
		hBox.getChildren().addAll(vBox);
		
		vBox = new VBox();
		
		vBox.getChildren().addAll(new Text("WPM"));
		vBox.getChildren().addAll(oppWPMValTxt);
		vBox.getChildren().addAll(oppPointsTxt);
		
		vBox.setAlignment(Pos.TOP_RIGHT);
		
		HBox.setHgrow(vBox, Priority.ALWAYS);
		
		hBox.getChildren().addAll(vBox);
		
		return hBox;

	}

}
