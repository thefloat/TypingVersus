package org.btt_javafx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;

public class BttNetwork {
	volatile boolean exitSnt = false;
	
	String ServiceName = "ServiceUrl";
	String UUIDStrng = "c5cf8d582b8046e5b44d2a9da34ef4b2";
	UUID uuid = new UUID(UUIDStrng, false);
	
	String UUID2 = "d6df8d582b8046e5b44d2a9da34ef4b2";
	UUID uuid2 = new UUID(UUID2, false);
	
	String rdStrng = null;
	String ldStrng = null;
	
	PrintWriter prntOut = null;
	
	Scanner scanner = null;
	
	volatile StreamConnectionNotifier scn = null;
	volatile StreamConnectionNotifier scn2 = null;
	
	volatile LocalDevice localDevice = null;
	
	volatile ServiceRecord sr = null;
	volatile ServiceRecord sr2 = null;
	
//	Service for creating a server
	Service<Void> service1 = new Service<Void>() {

		@Override
		protected Task<Void> createTask() {
			return new Task<Void>() {
				boolean waiting = true;

				StreamConnection sc = null;
				
				@Override
				protected Void call() throws Exception {
			    	try {
						localDevice = LocalDevice.getLocalDevice();
//						localDevice.setDiscoverable(DiscoveryAgent.GIAC);
					} catch (BluetoothStateException e) {
						e.printStackTrace();
					}
					
					String connUrl = "btspp://localhost:" + uuid.toString() + ";name= ServiceName";
					
					
//					Create temporary service record
					String connUrl2 = "btspp://localhost:" + uuid2.toString() + ";name= ServiceName2";
					
					scn2 = (StreamConnectionNotifier)Connector.open(connUrl2);
					
					sr2 = localDevice.getRecord(scn2);
					
					Service<Void> serviceConn = new Service<Void>() {
						@Override
						protected Task<Void> createTask() {
							return new Task<Void>() {
								@Override
								protected Void call() throws Exception {
									try {
//										Open connection and wait for and connect to client
										scn = (StreamConnectionNotifier)Connector.open(connUrl);
										sc = scn.acceptAndOpen();
										
//										Get remote device friendly name
										RemoteDevice rd = RemoteDevice.getRemoteDevice(sc);
										rdStrng = rd.getFriendlyName(false);
									} catch (IOException e) {
										e.printStackTrace();
									}
									
//									Get local device friendly name
									try {
										ldStrng = LocalDevice.getLocalDevice().getFriendlyName();
									} catch (BluetoothStateException e1) {
										e1.printStackTrace();
									}
									
//									Open Output Stream
									try {
										prntOut = new PrintWriter(sc.openOutputStream(), true);
									} catch (IOException e) {
										e.printStackTrace();
									}
									
//									Open Input Stream
									try {
										scanner = new Scanner(new BufferedReader(new InputStreamReader(sc.openInputStream())));
									} catch (IOException e) {
										e.printStackTrace();
									}

									waiting = false;
									return null;
								}
							};
						}
					};
					serviceConn.restart();

					while (waiting) {
						if(isCancelled()) {
							
//							localDevice.setDiscoverable(DiscoveryAgent.NOT_DISCOVERABLE);
//							sc.close();
							scn.close();
							
							waiting = true;
							
							break;
						}
						
					}
					
					waiting = true;
					
			    	
					return null;
				}
			};
		}
	};
	
//			Service for joining a server
		Service<Void> service2 = new Service<Void>() {

			@Override
			protected Task<Void> createTask() {
				return new Task<Void>() {
					StreamConnection sc = null;

					@Override
					protected Void call() throws Exception {
//						Select service
						String connUrl = null;
			            try {
							connUrl = LocalDevice.getLocalDevice().getDiscoveryAgent().selectService(uuid,ServiceRecord.NOAUTHENTICATE_NOENCRYPT,false);
						} catch (BluetoothStateException e) {
							e.printStackTrace();
						}
			            
//			          Open connection to service
			            try {
							sc = (StreamConnection)Connector.open(connUrl);
							
//							Get remote device friendly name
							RemoteDevice rd = RemoteDevice.getRemoteDevice(sc);
							rdStrng = rd.getFriendlyName(false);
//							rdStrng = "Server boy";
						} catch (IOException e) {
							e.printStackTrace();
						}
			            
//			            Get local device friendly name
			            try {
							ldStrng = LocalDevice.getLocalDevice().getFriendlyName();
						} catch (BluetoothStateException e1) {
							e1.printStackTrace();
						}
//			            ldStrng = "Client boy";
			            
//			            Open output stream
			            try {
							prntOut = new PrintWriter(sc.openOutputStream(), true);
						} catch (IOException e) {
							e.printStackTrace();
						}
			            
//						Open Input Stream
						try {
							scanner = new Scanner(new BufferedReader(new InputStreamReader(sc.openInputStream())));
						} catch (IOException e) {
							e.printStackTrace();
						}
				    	
					return null;}
				};
			}
		};
				
		public void recieveMssg(BttLogic bttLogic,ScrollPane scrollPane,  BorderPane chatBp, BorderPane statsBp, VBox chatVB, VBox statsVB, Button newBtn, TextFlow textFlow, TextArea textArea, 
				ProgressBar youPBar, ProgressBar oppPBar, Label timerLbl, Label youNameLbl, Label oppNameLbl, 
				Text youScoreTxt, Text oppScoreTxt, Text youWPMTxt, Text oppWPMTxt, Text youWPMValTxt, 
				Text oppWPMValTxt, Text youPointsTxt, Text oppPointsTxt,
				Text youWinLoseTxt, Text oppWinLoseTxt, Text arenaTxt, Text timerCDTxt) 
		{
			
			Task<Void> taskRecieveMssg = new Task<Void>() {

				@Override
				protected Void call() throws Exception {
					System.out.println("Waiting to recieve message(s)");
					while(scanner != null) {
						String st = scanner.nextLine();
						
						if(st.startsWith("*m")) {
//							Insert message in chat box
							Platform.runLater(new Runnable() {
							
							@Override
							public void run() {
								chatVB.getChildren().add(new BttLogic().inserMssgOpp(st.substring(2)));
								
							}
						});
						}else if(st.startsWith("*pr")) {
//							Update progress bar with the value
							Platform.runLater(new Runnable() {
								
								@Override
								public void run() {
									oppPBar.setProgress(Double.parseDouble(st.substring(3)));
									
								}
							});
						}else if(st.startsWith("*w")) {
//							Set WPM
							Platform.runLater(new Runnable() {
								
								@Override
								public void run() {
									oppWPMTxt.setText("WPM");
									
									oppWPMValTxt.setText(st.substring(2));
									
									if(!bttLogic.done) {
										bttLogic.handleRound(true, false, scrollPane, chatBp, statsBp, statsVB, newBtn, textFlow, textArea, youPBar, oppPBar, timerLbl, youNameLbl, oppNameLbl, youScoreTxt, oppScoreTxt, youWPMTxt, oppWPMTxt, youWPMValTxt, oppWPMValTxt, youPointsTxt, oppPointsTxt, youWinLoseTxt, oppWinLoseTxt, arenaTxt, timerCDTxt);
									}
									
								}
							});
						}else if(st.startsWith("*po")) {
//							Set WPM
							Platform.runLater(new Runnable() {
								
								@Override
								public void run() {
									oppPointsTxt.setText(st.substring(3));
									
//									If user is done compare points to find winner
									if(!youPointsTxt.getText().equals("")) {
//										Check win or lose
										if(Integer.parseInt(youPointsTxt.getText()) > Integer.parseInt(oppPointsTxt.getText())) {
//											Win
											youWinLoseTxt.setText(": )");

											oppWinLoseTxt.setText(": (");
											
											timerLbl.setText("won");

											youScoreTxt.setText((Integer.parseInt(youScoreTxt.getText())+1)+"");
											
										}else if(Integer.parseInt(youPointsTxt.getText()) < Integer.parseInt(oppPointsTxt.getText())) {
//											Lose
											youWinLoseTxt.setText(": (");
											
											oppWinLoseTxt.setText(": )");
											
											timerLbl.setText("lost");
											
											oppScoreTxt.setText((Integer.parseInt(oppScoreTxt.getText())+1)+"");
										
										}else {
//											Draw
											timerLbl.setText("draw");
											
										}

										statsVB.getChildren().addAll(insertStats(new Text(youWPMValTxt.getText()), new Text(youPointsTxt.getText()),
												new Text(oppWPMValTxt.getText()), new Text(oppPointsTxt.getText())), new Separator(Orientation.HORIZONTAL)); 
										
									}
									
								}
							});
						}else if (st.startsWith("*exit")) {
							exitAllServer();
						
							break;
						
						}else {
							System.out.println("Unknown message from input stream- "+st);
						}
							
					}
					
					System.out.println("Out of recieving loop");
					
					return null;
				}
					
			};
			
			Thread th2 = new Thread(taskRecieveMssg);
			th2.setDaemon(true);
			th2.start();
			
			taskRecieveMssg.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent arg0) {
					
//					Return to 'Access' page
					Stage stage = (Stage)youPBar.getScene().getWindow();
					
					try {
						Parent root = FXMLLoader.load(getClass().getResource("btt_access.fxml"));
					
						stage.getScene().setRoot(root);

					} catch (IOException e) {
						e.printStackTrace();
					}
					
					stage.setMaximized(true);
					stage.show();
					
				}
			});
		}
		public void recieveMssgClient(BttLogic bttLogic, ScrollPane scrollPane, BorderPane chatBp, BorderPane statsBp, VBox chatVB, VBox statsVB, TextFlow textFlow, TextArea textArea, 
				ProgressBar youPBar, ProgressBar oppPBar, Label timerLbl, Label youNameLbl, Label oppNameLbl, 
				Text youScoreTxt, Text oppScoreTxt, Text youWPMTxt, Text oppWPMTxt, Text youWPMValTxt, 
				Text oppWPMValTxt, Text youPointsTxt, Text oppPointsTxt, Text youWinLoseTxt, 
				Text oppWinLoseTxt, Text arenaTxt, Text timerCDTxt) 
		{
			
			Task<Void> taskRecieveMssg = new Task<Void>() {

				@Override
				protected Void call() throws Exception {
					System.out.println("Waiting to recieve message(s)");
					while(scanner != null) {
						String st = scanner.nextLine();
						
						if(st.startsWith("*m")) {
//							Insert message in chat box
							Platform.runLater(new Runnable() {
							
							@Override
							public void run() {
								chatVB.getChildren().add(new BttLogic().inserMssgOpp(st.substring(2)));
								
							}
						});
						}else if(st.startsWith("*pr")) {
//							Update progress bar with the value
							Platform.runLater(new Runnable() {
								
								@Override
								public void run() {
									oppPBar.setProgress(Double.parseDouble(st.substring(3)));
									
								}
							});
						}else if(st.startsWith("*w")) {
//							Set WPM
							Platform.runLater(new Runnable() {
								
								@Override
								public void run() {
									oppWPMTxt.setText("WPM");
									
									oppWPMValTxt.setText(st.substring(2));
									
									if(!bttLogic.done) {
										bttLogic.handleRoundClient(true, false, 0, scrollPane, chatBp, statsBp, statsVB, textFlow, textArea, youPBar, oppPBar, timerLbl, youNameLbl, oppNameLbl, youScoreTxt, oppScoreTxt, youWPMTxt, oppWPMTxt, youWPMValTxt, oppWPMValTxt, youPointsTxt, oppPointsTxt, youWinLoseTxt, oppWinLoseTxt, arenaTxt, timerCDTxt);
									}
									
								}
							});
						}else if(st.startsWith("*po")) {
//							Set WPM
							Platform.runLater(new Runnable() {
								
								@Override
								public void run() {
									oppPointsTxt.setText(st.substring(3));
									
//									If user is done compare points to find winner
									if(!youPointsTxt.getText().equals("")) {
//										Check win or lose
										if(Integer.parseInt(youPointsTxt.getText()) > Integer.parseInt(oppPointsTxt.getText())) {
//											Win
											youWinLoseTxt.setText(": )");

											oppWinLoseTxt.setText(": (");
											
											timerLbl.setText("won");

											youScoreTxt.setText((Integer.parseInt(youScoreTxt.getText())+1)+"");
											
										}else if(Integer.parseInt(youPointsTxt.getText()) < Integer.parseInt(oppPointsTxt.getText())) {
//											Lose
											youWinLoseTxt.setText(": (");
											
											oppWinLoseTxt.setText(": )");
											
											timerLbl.setText("lost");
											
											oppScoreTxt.setText((Integer.parseInt(oppScoreTxt.getText())+1)+"");
										
										}else {
//											Draw
											timerLbl.setText("draw");
											
										}
										
										statsVB.getChildren().addAll(insertStats(new Text(youWPMValTxt.getText()), new Text(youPointsTxt.getText()),
												new Text(oppWPMValTxt.getText()), new Text(oppPointsTxt.getText())), new Separator(Orientation.HORIZONTAL)); 
									}
									
								}
							});
						}else if(st.startsWith("*new")) {
							Platform.runLater(new Runnable() {
								
								@Override
								public void run() {
									bttLogic.handleRoundClient(false, false, Integer.parseInt(st.substring(4)), scrollPane, chatBp, statsBp, statsVB, textFlow, textArea, youPBar, oppPBar, timerLbl, youNameLbl, oppNameLbl, youScoreTxt, oppScoreTxt, youWPMTxt, oppWPMTxt, youWPMValTxt, oppWPMValTxt, youPointsTxt, oppPointsTxt, youWinLoseTxt, oppWinLoseTxt, arenaTxt, timerCDTxt);
								}
							});
							
						}else if(st.equals("*go")) {
							Platform.runLater(new Runnable() {
								
								@Override
								public void run() {
									bttLogic.handleRoundClient(false, true, 0, scrollPane, chatBp, statsBp, statsVB, textFlow, textArea, youPBar, oppPBar, timerLbl, youNameLbl, oppNameLbl, youScoreTxt, oppScoreTxt, youWPMTxt, oppWPMTxt, youWPMValTxt, oppWPMValTxt, youPointsTxt, oppPointsTxt, youWinLoseTxt, oppWinLoseTxt, arenaTxt, timerCDTxt);
								}
							});
							
						}else if(st.equals("*reset_scores")) {
							Platform.runLater(new Runnable() {
								
								@Override
								public void run() {
									youScoreTxt.setText("0");
									oppScoreTxt.setText("0");
								}
							});
							
						}else if (st.startsWith("*exit")) {
							exitAllClient();
						
							break;
							
						}else {
							System.out.println("Unknown message from input stream- "+st);
						}
							
					}
					
					System.out.println("Out of recieve loop");
						
					return null;
				}
					
			};
			
			Thread th2 = new Thread(taskRecieveMssg);
			th2.setDaemon(true);
			th2.start();
			
			taskRecieveMssg.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent arg0) {
					
//					Return to 'Access' page
					Stage stage = (Stage)youPBar.getScene().getWindow();
					
					try {
						Parent root = FXMLLoader.load(getClass().getResource("btt_access.fxml"));
					
						stage.getScene().setRoot(root);

					} catch (IOException e) {
						e.printStackTrace();
					}
					
					stage.setMaximized(true);
					stage.show();
					
				}
			});
			
		}
		public void sndMssg(String mssg) {
			Service<Void> serviceSndMssg = new Service<Void>() {
				
				@Override
				protected Task<Void> createTask() {
					return new Task<Void>() {

						@Override
						protected Void call() throws Exception {
							prntOut.println(mssg);
							
							return null;
						}
					};
				}
			};
			
			serviceSndMssg.restart();
			
		}
		public void createServer() {
			service1.restart();
		}
		public void joinServer() {
			service2.restart();
		
		}
		public void cancelServer() {
			service1.cancel();
		}
		public void cancelClient() {
			service2.cancel();
		}
		public void exitThd() {
			
			sndMssg("*exit");
			
			exitSnt = true;
			
		}
		private void exitAllServer() {
			if(!exitSnt) {
				sndMssg("*exit");
				
				exitSnt = true;
				
			}
			
			try {
			sr = localDevice.getRecord(scn);

			}catch(Exception e) {
				System.out.println("din't work 2\n"+e);
					
			}
			
			try {
				sr.setAttributeValue(1, sr2.getAttributeValue(1));
			}catch(Exception e) {
				System.out.println("din't work 3\n"+e);
					
			}
			
			try {
				localDevice.updateRecord(sr);
			}catch(Exception e) {
				System.out.println("din't work 4\n"+e);
					
			}

			try {
//				sc.close();
				
				scn.close();
			}catch(Exception e) {
				System.out.println("din't work 1\n"+e);
					
			}
			
		}
		private void exitAllClient() {
			if(!exitSnt) {
				sndMssg("*exit");
				
				exitSnt = true;
				
			}
			
			try {
//				sc.close();
			}catch(Exception e) {
				System.out.println("din't work 1\n"+e);
					
			}
		}
		private HBox insertStats(Text youWPMValTxt, Text youPointsTxt, Text oppWPMValTxt, Text oppPointsTxt) {
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
