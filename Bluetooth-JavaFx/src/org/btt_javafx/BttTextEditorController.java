package org.btt_javafx;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextFlow;
import javafx.stage.Screen;

public class BttTextEditorController {
	@FXML StackPane bttTextEditor;
	
	@FXML ScrollPane scrollPane;
	
	@FXML TextFlow textFlow;
	
	@FXML TextArea textArea;
	
	Double maxVal = 1.0;

	public void initialize() {
		scrollPane.setFitToWidth(true);
		
		bttTextEditor.setPrefSize(Screen.getPrimary().getVisualBounds().getWidth()-10, Screen.getPrimary().getVisualBounds().getHeight()/2 -20);
		
		textArea.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight()/2 -20);
		
	}
}
