import java.util.HashMap;
import javax.crypto.spec.IvParameterSpec;
import javax.xml.namespace.QName;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent; //use radial button

public class GuiClient extends Application {
	
	public int port;
	public String address;
	TextField ipTextField, portTextField, betAmount;
	Text total;
	HashMap<String, Scene> sceneMap;
	Button clientChoice, betButton, playAgain, exitClient;
	RadioButton playerButton, bankerButton, drawButton;
	ToggleGroup choiceButtons;
	GridPane grid;
	HBox buttonBox;
	VBox clientBox;
	VBox choiceBox;
	Scene startScene;
	BorderPane startPane, infoPane;
	Client clientConnection;
	ListView<String> listItems, listItems2, playerView, bankerView, resultsView;
	BaccaratInfo clientInfo = new BaccaratInfo();
	EventHandler<ActionEvent> connectToServer, gameHandler;
	
	 
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("The Client GUI");

		sceneMap = new HashMap<String, Scene>();
		sceneMap.put("gameScene", getScene(primaryStage));
		listItems = new ListView<String>();
		listItems2 = new ListView<String>();

		clientChoice = new Button("Connect");
		portTextField = new TextField();
		portTextField.setPromptText("port number");
		ipTextField = new TextField();
		ipTextField.setPromptText("IP address");

		buttonBox = new HBox(2, clientChoice, ipTextField, portTextField);
		//choiceBox = new VBox(10, playerButton, bankerButton, drawButton);
		startPane = new BorderPane();
		startPane.setPadding(new Insets(200));
		startPane.setCenter(buttonBox);
		//startPane.setRight(choiceBox);

		// event handler when button is pressed for the address and port
		// save the string and then convert to int
		// pass them to the socket constructor in client class

		connectToServer = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				address = ipTextField.getText();
				port = Integer.parseInt(portTextField.getText());
				primaryStage.setScene(sceneMap.get("gameScene")); //changes to game scene when button to connect is pressed
				clientConnection = new Client(data -> {
					Platform.runLater(() -> {
						listItems2.getItems().add(data.toString()); //CLIENT null pointer exception!!!!!!!!!!!!!!!!!!
						//clientInfo = (BaccaratInfo) data; //???????
						
					});
				}, port, address);
				System.out.println(address + "  " + port);
				clientConnection.start();
			};	
		};

		startScene = new Scene(startPane, 800, 800);
		startScene.getRoot().setStyle("-fx-font-family: 'serif';" + "-fx-background-color: Blue");

		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				Platform.exit();
				System.exit(0);
			}
		});

		clientChoice.setOnAction(connectToServer);
		//if (clientConnection.isConnected() == true) {
		//primaryStage.setScene(sceneMap.get("gameScene"));
		//}
		primaryStage.setScene(startScene);
		primaryStage.show();

	}
	
	// PLaying scene showing cards dealt
	public Scene getScene(Stage primaryStage) {
		BorderPane gamePane = new BorderPane();
		total = new Text("Earnings ");
		total.setFill(Color.WHITE);
		playerView = new ListView<String>();
		bankerView = new ListView<String>();
		resultsView = new ListView<String>();
		betAmount = new TextField();
		exitClient = new Button("EXIT");
		betAmount.setPromptText("Enter betting amount:");
		betButton = new Button("Place Bet");
		choiceButtons = new ToggleGroup();
		playerButton = new RadioButton("Player");
		playerButton.setStyle("-fx-background-color: White;");
		bankerButton = new RadioButton("Banker");
		bankerButton.setStyle("-fx-background-color: White;");
		drawButton = new RadioButton("Draw");
		drawButton.setStyle("-fx-background-color: White;");
		playerButton.setToggleGroup(choiceButtons);
		bankerButton.setToggleGroup(choiceButtons);
		drawButton.setToggleGroup(choiceButtons);
		gamePane.setPadding(new Insets(40));
		playerView.setPrefWidth(300);
		bankerView.setPrefHeight(300);
		HBox gameBox = new HBox(20, playerButton, bankerButton, drawButton, betAmount, betButton, exitClient, total);
		HBox infoScreen = new HBox(40, playerView,resultsView, bankerView);

		gamePane.setTop(gameBox);
		gamePane.setCenter(infoScreen);

		//choiceButtons.setStyle("-fx-selected-color: White;"  + "-fx-unselected-color: White;");

		Scene gameScene = new Scene(gamePane, 800, 800);
		gameScene.getRoot().setStyle("-fx-font-family: 'serif';" + "-fx-background-color: Blue");

		// need to start a baccaratinfo containing all the info
		gameHandler = new EventHandler<ActionEvent> () {
			
			public void handle(ActionEvent e) {
				clientInfo = new BaccaratInfo();
				RadioButton selected = (RadioButton) choiceButtons.getSelectedToggle();
				clientInfo.betOnWho = selected.getText();
				clientInfo.currentBet = Integer.parseInt(betAmount.getText());
				clientConnection.send(clientInfo);
				clientInfo = clientConnection.getInfo();
				playerView.getItems().add("Card 1: " + clientInfo.pCardVals.get(0)); //Null pointer exception???
				playerView.getItems().add("Card 2: " + clientInfo.pCardVals.get(1));
				if (clientInfo.pCardVals.size() == 3) {
					playerView.getItems().add("Player drew another card!");
					playerView.getItems().add("Card 3: " + clientInfo.pCardVals.get(2));
				}
				playerView.getItems().add("    ");
				
				bankerView.getItems().add("Card 1: " + clientInfo.bCardVals.get(0));
				bankerView.getItems().add("Card 2: " + clientInfo.bCardVals.get(1));
				if (clientInfo.bCardVals.size() == 3) {
					bankerView.getItems().add("Banker drew another card!");
					bankerView.getItems().add("Card 3: " + clientInfo.bCardVals.get(2));
				}
				bankerView.getItems().add("      ");

				resultsView.getItems().add("Player points: " + Integer.toString(clientInfo.pPoints));
				resultsView.getItems().add("Banker points: " + Integer.toString(clientInfo.bPoints));
				resultsView.getItems().add("");
				double sum =+ clientInfo.totalEarnings;
				total.setText("Earnings: " + sum);
				//betButton.setDisable(false);
			}
		};

		
		exitClient.setOnAction(e -> primaryStage.close());
		betButton.setOnAction(gameHandler);
		return gameScene;
	}

}
