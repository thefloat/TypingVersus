package org.btt_javafx;

import java.io.IOException;
import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class BttLogic {
	private BttNetwork bttNetwork = new BttNetwork();
	
	private BttTextEditor bttTE = new BttTextEditor();
	
	private Boolean resetPssg = false;
	
	int countdown = 3;
	int countTimer = 1;
	int rndm = 0;
	Random random = new Random();
	
	boolean done = false;
	
	boolean chatVisb = false;
	
	boolean statsVisb = false;
	
	Popup popup = new Popup();
	
//	Create the timers
	Timeline timerCD = new Timeline();
	
	Timeline timer = new Timeline();
	
	Timeline finishCD = new Timeline();
	
	public void handleCreateServer(BttLogic bttLogic, ScrollPane scrollPane, BorderPane chatBp, BorderPane statsBp, VBox chatVB, VBox statsVB, Button newBtn, TextFlow textFlow, TextArea textArea, 
			ProgressBar youPBar, ProgressBar oppPBar, Label timerLbl, Label youNameLbl, Label oppNameLbl, Label youNameStatsLbl, Label oppNameStatsLbl, 
			Text youScoreTxt, Text oppScoreTxt, Text youWPMTxt, Text oppWPMTxt, Text youWPMValTxt, Text oppWPMValTxt, 
			Text youPointsTxt, Text oppPointsTxt, Text youWinLoseTxt, Text oppWinLoseTxt, Text arenaTxt, Text timerCDTxt) 
	{	
		
		bttNetwork.createServer();

		
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
//				createPopupServer((Stage)youNameLbl.getScene().getWindow());
				
			}
		});

		bttNetwork.service1.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
				
			@Override
			public void handle(WorkerStateEvent arg0) {
				popup.hide();
						
				oppNameLbl.setText(bttNetwork.rdStrng);
						
				youNameLbl.setText(bttNetwork.ldStrng);

//				Set names in stat box
				oppNameStatsLbl.setText(bttNetwork.rdStrng);
				
				youNameStatsLbl.setText(bttNetwork.ldStrng);
				
				oppScoreTxt.setText("0");
				
				youScoreTxt.setText("0");
				
				newBtn.setDisable(false);
				
				bttNetwork.recieveMssg(bttLogic, scrollPane, chatBp, statsBp, chatVB, statsVB, newBtn, textFlow, textArea, youPBar, oppPBar, timerLbl, youNameLbl, oppNameLbl, youScoreTxt, oppScoreTxt, youWPMTxt, oppWPMTxt, youWPMValTxt, oppWPMValTxt, youPointsTxt, oppPointsTxt, youWinLoseTxt, oppWinLoseTxt, arenaTxt, timerCDTxt);
			}
		});
	}
	public void handleJoinServer(BttLogic bttLogic, ScrollPane scrollPane, BorderPane chatBp, BorderPane statsBp, VBox chatVB, VBox statsVB, TextFlow textFlow, TextArea textArea, 
			ProgressBar youPBar, ProgressBar oppPBar, Label timerLbl, Label youNameLbl, Label oppNameLbl, Label youNameStatsLbl, Label oppNameStatsLbl,
			Text youScoreTxt, Text oppScoreTxt, Text youWPMTxt, Text oppWPMTxt, Text youWPMValTxt, Text oppWPMValTxt, 
			Text youPointsTxt, Text oppPointsTxt, Text youWinLoseTxt, Text oppWinLoseTxt, Text arenaTxt, Text timerCDTxt) 
	{
	
		bttNetwork.joinServer();
		
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
//				createPopupClient((Stage)youNameLbl.getScene().getWindow());
				
			}
		});
		
		bttNetwork.service2.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
				
			@Override
			public void handle(WorkerStateEvent arg0) {
				popup.hide();
						
				oppNameLbl.setText(bttNetwork.rdStrng);
						
				youNameLbl.setText(bttNetwork.ldStrng);
				
//				Set names in stat box
				oppNameStatsLbl.setText(bttNetwork.rdStrng);
				
				youNameStatsLbl.setText(bttNetwork.ldStrng);
				
				oppScoreTxt.setText("0");
				
				youScoreTxt.setText("0");
				
				bttNetwork.recieveMssgClient(bttLogic, scrollPane, chatBp, statsBp, chatVB, statsVB, textFlow, textArea, youPBar, oppPBar, timerLbl, youNameLbl, oppNameLbl, youScoreTxt, oppScoreTxt, youWPMTxt, oppWPMTxt, youWPMValTxt, oppWPMValTxt, youPointsTxt, oppPointsTxt, youWinLoseTxt, oppWinLoseTxt, arenaTxt, timerCDTxt);
			}
		});
	}
	public void handleRound(boolean startFCD, boolean startCD, ScrollPane scrollPane, BorderPane chatBp, BorderPane statsBp, VBox statsVB, Button newBtn, TextFlow textFlow, TextArea textArea, 
			ProgressBar youPBar, ProgressBar oppPBar, Label timerLbl, Label youNameLbl, Label oppNameLbl, 
			Text youScoreTxt, Text oppScoreTxt, Text youWPMTxt, Text oppWPMTxt, Text youWPMValTxt, Text oppWPMValTxt, 
			Text youPointsTxt, Text oppPointsTxt, Text youWinLoseTxt, Text oppWinLoseTxt, Text arenaTxt, Text timerCDTxt) 
	{

		if(startFCD) {
//			Set countdown to 15
			countdown = 15;
			
			if(chatBp.isVisible()) {
				arenaTxt.setText("");
				
				arenaTxt.setVisible(true);
				chatBp.setVisible(false);
				
				chatVisb = true;
			}else if(statsBp.isVisible()) {
				arenaTxt.setText("");
				
				arenaTxt.setVisible(true);
				statsBp.setVisible(false);
				
				statsVisb = true;
			}else {
				chatVisb = false;
				statsVisb = false;
			}
			
			finishCD.play();
		}else {
			if(startCD) {
				bttNetwork.sndMssg("*go");
				
				youWPMValTxt.setText("-");
				
				youPointsTxt.setText("");
				
				youWinLoseTxt.setText("");
				
				oppWPMValTxt.setText("-");
				
				oppPointsTxt.setText("");
				
				oppWinLoseTxt.setText("");
				
	//			Set countdown to 3
				countdown = 3;
				
				timerCD.play();
				
			}else {
				if(resetPssg) {
					rndm = random.nextInt(20);
//					System.out.println(rndm);
					
					bttNetwork.sndMssg("*new"+rndm);
					
	//				reset countTimer and countTimer
					countTimer = 1;
	
					done = false;
					
					youPBar.setProgress(0);
					
					oppPBar.setProgress(0);
					
					timerLbl.setText("-");
					
					bttTE.reconfigTE(scrollPane, textFlow, textArea, rndm, youWPMValTxt, timerLbl);
					
				}else {
					timerCDTxt.setFill(Color.GRAY);
					
					timerCD.setCycleCount(4);
					timerCD.getKeyFrames().add(new KeyFrame(javafx.util.Duration.millis(1000), new EventHandler<ActionEvent>() {
						
						@Override
						public void handle(ActionEvent arg0) {
							if(countdown == 0) {
								timer.play();
								
								timerCDTxt.setVisible(false);
								
								textArea.setEditable(true);
								
	//							Set timerCD text back to three
								countdown = 3;
								
								timerCDTxt.setText(countdown+"");
								
							}else {
								timerCDTxt.setVisible(true);
								
								timerCDTxt.setText(countdown+"");
								
								countdown--;	
							}
							
							
							
						}
					}));
			        
	//				Create timer
					timer.setCycleCount(Timeline.INDEFINITE);
					timer.getKeyFrames().add(new KeyFrame(javafx.util.Duration.millis(1000), new EventHandler<ActionEvent>() {
						
						@Override
						public void handle(ActionEvent arg0) {
							timerLbl.setText(countTimer+"");
							
							countTimer++;

						}
					}));
					
	//				Create 15 seconds count down timer 
					finishCD.setCycleCount(16);
					finishCD.getKeyFrames().add(new KeyFrame(javafx.util.Duration.millis(1000), new EventHandler<ActionEvent>() {
	
						@Override
						public void handle(ActionEvent arg0) {
							if(countdown == 0) {
								textArea.setEditable(false);
								
								timer.stop();
								
								arenaTxt.setText("\\|/");
								
								if(chatVisb) {
									arenaTxt.setVisible(false);
									chatBp.setVisible(true);
									statsBp.setVisible(false);
									
								}else if(statsVisb){
									arenaTxt.setVisible(false);
									chatBp.setVisible(false);
									statsBp.setVisible(true);
								}
								
								youWPMTxt.setText("WPM");
								
								done = true;
								
								bttTE.finishedRound(statsVB, textArea, youScoreTxt, oppScoreTxt, youWPMValTxt, oppWPMValTxt, youPointsTxt, oppPointsTxt, youWinLoseTxt, oppWinLoseTxt, timerLbl, bttNetwork);
								
								newBtn.setDisable(false);
								
							}else {
								arenaTxt.setText(countdown+"");
								
								countdown--;
								
								if(done) {
									finishCD.stop();
									
									arenaTxt.setText("\\|/");
									
									if(chatVisb) {
										arenaTxt.setVisible(false);
										chatBp.setVisible(true);
										statsBp.setVisible(false);
										
									}else if(statsVisb){
										arenaTxt.setVisible(false);
										chatBp.setVisible(false);
										statsBp.setVisible(true);
									}
								}
							}
							
						}
					}));
					
					textArea.textProperty().addListener(new ChangeListener<String>() {
						@Override
						public void changed(ObservableValue<? extends String> stringProperty, String oldState, String newState) {
							if(bttTE.skip) {
								bttTE.skip = false;
							}else {
								if(textArea.getLength() == 0) {
									
								}else {
									int length = textArea.getLength();
									String lstChar = String.valueOf(newState.charAt(textArea.getLength()-1));
									String crrct = String.valueOf(bttTE.pssgText.charAt(bttTE.trckLenght));
									
									if(textArea.getText(bttTE.trckLenght, length).equals(crrct)) {
										bttTE.cnscFail = false;

//										Calculate and send out you youBar progress
										Double progressVal = ((double)(bttTE.trckLenght+1)/(double)bttTE.pssgLength);
										
				//						update your progressBar then send the progressVal to opponent
										youPBar.setProgress(progressVal);
										
										bttNetwork.sndMssg("*pr"+progressVal);
									
//										Set done to true if user is at the end of the passage
										if(textArea.getLength() == bttTE.pssgLength) {
											done = true;
											
										}
										
										bttTE.createCrrctText(true, lstChar, statsVB, textFlow, textArea, youScoreTxt, oppScoreTxt, youPointsTxt, oppPointsTxt, youWPMTxt, youWPMValTxt, oppWPMValTxt, youWinLoseTxt, oppWinLoseTxt, timerLbl, timer, newBtn, bttNetwork);
										
									}else {
										if(bttTE.cnscFail) {
											bttTE.skip = true;
										
										textArea.deleteText(bttTE.trckLenght, length);
										}else {
											bttTE.skip = true;
											
											bttTE.cnscFail = true;
											
											textArea.replaceText(bttTE.trckLenght, length, crrct);

//											Calculate and send out you youBar progress
											Double progressVal = ((double)(bttTE.trckLenght+1)/(double)bttTE.pssgLength);
											
					//						update your progressBar then send the progressVal to opponent
											youPBar.setProgress(progressVal);
											
											bttNetwork.sndMssg("*pr"+progressVal);

//											Set done to true if user is at the end of the passage
											if(textArea.getLength() == bttTE.pssgLength) {
												done = true;
												
											}
											
											bttTE.createWrngText(true, crrct, statsVB, textFlow, textArea, youScoreTxt, oppScoreTxt, youPointsTxt, oppPointsTxt, youWPMTxt, youWPMValTxt, oppWPMValTxt, youWinLoseTxt, oppWinLoseTxt, timerLbl, timer, newBtn, bttNetwork);
										}
									}
								}
							}
						}
					});
					
					rndm = random.nextInt(20);
//					System.out.println(rndm);
					
					bttNetwork.sndMssg("*new"+rndm);
					
					textArea.clear();
				
					bttTE.configTextEditor(scrollPane, textFlow, textArea, rndm, youWPMValTxt, timerLbl, bttNetwork);

					resetPssg = true;
				}
			
			}
		}
		
	}
	public void handleRoundClient(boolean startFCD, boolean startCD, int pssgNum, ScrollPane scrollPane, BorderPane chatBp, BorderPane statsBp, VBox statsVB, TextFlow textFlow, TextArea textArea, 
		ProgressBar youPBar, ProgressBar oppPBar, Label timerLbl, Label youNameLbl, Label oppNameLbl, 
		Text youScoreTxt, Text oppScoreTxt, Text youWPMTxt, Text oppWPMTxt, Text youWPMValTxt, Text oppWPMValTxt, 
		Text youPointsTxt, Text oppPointsTxt, Text youWinLoseTxt, Text oppWinLoseTxt, Text arenaTxt, Text timerCDTxt) 
	{

		if(startFCD) {
//			Set countdown to 15
			countdown = 15;
			
			if(chatBp.isVisible()) {
				arenaTxt.setText("");
				
				arenaTxt.setVisible(true);
				chatBp.setVisible(false);
				
				chatVisb = true;
			}else if(statsBp.isVisible()) {
				arenaTxt.setText("");
				
				arenaTxt.setVisible(true);
				statsBp.setVisible(false);
				
				statsVisb = true;
			}else {
				chatVisb = false;
				statsVisb = false;
			}
			
			finishCD.play();
		}else {
			if(startCD) {
				youWPMValTxt.setText("-");
				
				youPointsTxt.setText("");
				
				youWinLoseTxt.setText("");
				
				oppWPMValTxt.setText("-");
				
				oppPointsTxt.setText("");
				
				oppWinLoseTxt.setText("");
				
	//			Set countdown to 3
				countdown = 3;
				
				timerCD.play();
				
			}else {
				if(resetPssg) {
	//				reset countTimer and countTimer
					countTimer = 1;
	
					done = false;
					
					youPBar.setProgress(0);
					
					oppPBar.setProgress(0);
					
					timerLbl.setText("-");
					
					bttTE.reconfigTE(scrollPane, textFlow, textArea, pssgNum, youWPMValTxt, timerLbl);
					
				}else {
					timerCDTxt.setFill(Color.GRAY);
					
					timerCD.setCycleCount(4);
					timerCD.getKeyFrames().add(new KeyFrame(javafx.util.Duration.millis(1000), new EventHandler<ActionEvent>() {
						
						@Override
						public void handle(ActionEvent arg0) {
							if(countdown == 0) {
								timer.play();
								
								timerCDTxt.setVisible(false);
								
								textArea.setEditable(true);
								
	//							Set timerCD text back to three
								countdown = 3;
								
								timerCDTxt.setText(countdown+"");
								
							}else {
								timerCDTxt.setVisible(true);
								
								timerCDTxt.setText(countdown+"");
								
								countdown--;	
							}
							
							
							
						}
					}));
			        
	//				Create timer
					timer.setCycleCount(Timeline.INDEFINITE);
					timer.getKeyFrames().add(new KeyFrame(javafx.util.Duration.millis(1000), new EventHandler<ActionEvent>() {
						
						@Override
						public void handle(ActionEvent arg0) {
							timerLbl.setText(countTimer+"");
							
							countTimer++;

						}
					}));
					
	//				Create 15 seconds count down timer 
					finishCD.setCycleCount(16);
					finishCD.getKeyFrames().add(new KeyFrame(javafx.util.Duration.millis(1000), new EventHandler<ActionEvent>() {
	
						@Override
						public void handle(ActionEvent arg0) {
							if(countdown == 0) {
								textArea.setEditable(false);
								
								timer.stop();
								
								arenaTxt.setText("\\|/");
								
								if(chatVisb) {
									arenaTxt.setVisible(false);
									chatBp.setVisible(true);
									statsBp.setVisible(false);
									
								}else if(statsVisb){
									arenaTxt.setVisible(false);
									chatBp.setVisible(false);
									statsBp.setVisible(true);
								}
								
								youWPMTxt.setText("WPM");
								
								done = true;
								
								bttTE.finishedRound(statsVB, textArea, youScoreTxt, oppScoreTxt, youWPMValTxt, oppWPMValTxt, youPointsTxt, oppPointsTxt, youWinLoseTxt, oppWinLoseTxt, timerLbl, bttNetwork);
								
							}else {
								arenaTxt.setText(countdown+"");
								
								countdown--;
								
								if(done) {
									finishCD.stop();
									
									arenaTxt.setText("\\|/");
									
									if(chatVisb) {
										arenaTxt.setVisible(false);
										chatBp.setVisible(true);
										statsBp.setVisible(false);
										
									}else if(statsVisb){
										arenaTxt.setVisible(false);
										chatBp.setVisible(false);
										statsBp.setVisible(true);
									}
								}
							}
							
						}
					}));
					
					textArea.textProperty().addListener(new ChangeListener<String>() {
						@Override
						public void changed(ObservableValue<? extends String> stringProperty, String oldState, String newState) {
							if(bttTE.skip) {
								bttTE.skip = false;
							}else {
								if(textArea.getLength() == 0) {
									
								}else {
									int length = textArea.getLength();
									String lstChar = String.valueOf(newState.charAt(textArea.getLength()-1));
									String crrct = String.valueOf(bttTE.pssgText.charAt(bttTE.trckLenght));
									
									if(textArea.getText(bttTE.trckLenght, length).equals(crrct)) {
										bttTE.cnscFail = false;

//										Calculate and send out you youBar progress
										Double progressVal = ((double)(bttTE.trckLenght+1)/(double)bttTE.pssgLength);
										
				//						update your progressBar then send the progressVal to opponent
										youPBar.setProgress(progressVal);
										
										bttNetwork.sndMssg("*pr"+progressVal);
									
//										Set done to true if user is at the end of the passage
										if(textArea.getLength() == bttTE.pssgLength) {
											done = true;
											
										}
										
										bttTE.createCrrctText(false, lstChar, statsVB, textFlow, textArea, youScoreTxt, oppScoreTxt, youPointsTxt, oppPointsTxt, youWPMTxt, youWPMValTxt, oppWPMValTxt, youWinLoseTxt, oppWinLoseTxt, timerLbl, timer, null, bttNetwork);
										
									}else {
										if(bttTE.cnscFail) {
											bttTE.skip = true;
										
										textArea.deleteText(bttTE.trckLenght, length);
										}else {
											bttTE.skip = true;
											
											bttTE.cnscFail = true;
											
											textArea.replaceText(bttTE.trckLenght, length, crrct);

//											Calculate and send out you youBar progress
											Double progressVal = ((double)(bttTE.trckLenght+1)/(double)bttTE.pssgLength);
											
					//						update your progressBar then send the progressVal to opponent
											youPBar.setProgress(progressVal);
											
											bttNetwork.sndMssg("*pr"+progressVal);

//											Set done to true if user is at the end of the passage
											if(textArea.getLength() == bttTE.pssgLength) {
												done = true;
												
											}
											
											bttTE.createWrngText(false, crrct, statsVB, textFlow, textArea, youScoreTxt, oppScoreTxt, youPointsTxt, oppPointsTxt, youWPMTxt, youWPMValTxt, oppWPMValTxt, youWinLoseTxt, oppWinLoseTxt, timerLbl, timer, null, bttNetwork);
										}
									}
								}
							}
						}
					});
					
					textArea.clear();
				
					bttTE.configTextEditor(scrollPane, textFlow, textArea, pssgNum, youWPMValTxt, timerLbl, bttNetwork);
					
					resetPssg = true;
				}
			
			}
		}
		
	}
	public void handleExitMI() {
		bttNetwork.exitThd();
	}
	public void handleResetScoresMI(Text youScoreTxt, Text oppScoreTxt) {
		bttNetwork.sndMssg("*reset_scores");
		
		youScoreTxt.setText("0");
		oppScoreTxt.setText("0");
		
	}
	public void haddleSndBtn(String mssg, TextField textField, VBox vBox) {
		if(!textField.getText().equals("")) {
			
			textField.clear();
			
			vBox.getChildren().add(inserMssgYou(mssg));
			
			bttNetwork.sndMssg("*m"+mssg);

		}
		
		
	}
	public HBox inserMssgYou(String mssg) {
		HBox hBox = new HBox();
		hBox.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
		
		Label lbl1 = new Label(mssg);
		lbl1.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
		lbl1.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
		lbl1.setWrapText(true);
		lbl1.setPadding(new Insets(3, 3, 3, 3));
		
		hBox.getChildren().add(lbl1);
		
		VBox.setMargin(hBox, new Insets(0,0,0,20));
		
		return hBox;
	}
	public HBox inserMssgOpp(String mssg) {
		HBox hBox = new HBox();
//		hBox.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
		
		Label lbl1 = new Label(mssg);
		lbl1.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
		lbl1.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
		lbl1.setWrapText(true);
		lbl1.setPadding(new Insets(3, 3, 3, 3));
		
		hBox.getChildren().add(lbl1);
		
		VBox.setMargin(hBox, new Insets(0,20,0,0));
		
		return hBox;
	}
	public void createPopupServer(Stage stage) {
		popup = new Popup();
		popup.sizeToScene();
		
		Text txt = new Text("Relax and unwind");
		
		ProgressBar pBar = new ProgressBar();
		
		Button btn = new Button("cancel");
		
		FlowPane flowPane = new FlowPane();
		flowPane.setPrefSize(Screen.getPrimary().getVisualBounds().getWidth()-4, Screen.getPrimary().getVisualBounds().getHeight()-35);
		flowPane.setStyle("-fx-background-color: rgb(240, 240, 255)");
		flowPane.setOpacity(0.8);
		flowPane.setOrientation(Orientation.VERTICAL);
		flowPane.setAlignment(Pos.CENTER);
		flowPane.setColumnHalignment(HPos.CENTER);
		flowPane.setVgap(10);
//		FlowPane.setMargin(txt, new Insets(0,0,10,0));
		flowPane.getChildren().addAll(txt, pBar, btn);
		
		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				bttNetwork.cancelServer();
				
				try {
					Parent root = FXMLLoader.load(getClass().getResource("btt_access.fxml"));
					
					stage.getScene().setRoot(root);

				} catch (IOException e) {
					e.printStackTrace();
				}
				
				stage.setMaximized(true);
				stage.show();
				
				popup.hide();
			}
		});
		
		popup.getContent().add(flowPane);
		popup.show(stage, 2, 25);
		
	}
	public void createPopupClient(Stage stage) {
		popup = new Popup();
		popup.sizeToScene();
		
		Text txt = new Text("Relax and unwind");
		
		ProgressBar pBar = new ProgressBar();
		
		Button btn = new Button("cancel");
		
		FlowPane flowPane = new FlowPane();
		flowPane.setPrefSize(Screen.getPrimary().getVisualBounds().getWidth()-4, Screen.getPrimary().getVisualBounds().getHeight()-35);
		flowPane.setStyle("-fx-background-color: rgb(240, 240, 255)");
		flowPane.setOpacity(0.8);
		flowPane.setOrientation(Orientation.VERTICAL);
		flowPane.setAlignment(Pos.CENTER);
		flowPane.setColumnHalignment(HPos.CENTER);
		flowPane.setVgap(10);
//		FlowPane.setMargin(txt, new Insets(0,0,10,0));
		flowPane.getChildren().addAll(txt, pBar, btn);
		
		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				bttNetwork.cancelClient();
				
				try {
					Parent root = FXMLLoader.load(getClass().getResource("btt_access.fxml"));
					
					stage.getScene().setRoot(root);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				stage.setMaximized(true);
				stage.show();
				
				popup.hide();
			}
		});
		
		popup.getContent().add(flowPane);
		popup.show(stage, 2, 25);
		
	}
}
