
import java.util.HashMap;
import javax.crypto.spec.IvParameterSpec;
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
import javafx.stage.Stage;
import javafx.stage.WindowEvent; //use radial button

public class GuiClient extends Application {
	
	// QUESTIONS
	// what else needs to be impplemented in Client and Server classes?
	// How to connect server and client? -- have print statements that tell if we are connected
	// instantiate a final variable for port number???
	// How is the serverGUI supposed to look?
	// do we need separate folder for server and client??(how are ther they communicating together being in separate folders)

	public int port;
	public String address;
	TextField ipTextField, portTextField, betAmount;
	HashMap<String, Scene> sceneMap;
	Button clientChoice, betButton;
	RadioButton playerButton, bankerButton, drawButton;
	ToggleGroup choiceButtons;
	GridPane grid;
	HBox buttonBox;
	VBox clientBox;
	VBox choiceBox;
	Scene startScene;
	BorderPane startPane, infoPane;
	Client clientConnection;
	ListView<String> listItems, listItems2, playerView, bankerView;
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

		clientChoice = new Button("Press once done adding IP and port number");
		portTextField = new TextField();
		portTextField.setPromptText("port number");
		ipTextField = new TextField();
		ipTextField.setPromptText("IP address");

		buttonBox = new HBox(10, clientChoice, ipTextField, portTextField);
		//choiceBox = new VBox(10, playerButton, bankerButton, drawButton);
		startPane = new BorderPane();
		//startPane.setPadding(new Insets(70));
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

		// clientChoice.setOnAction(e -> {
		// 	//clientChoice.setPressed(true);
		// 	address = ipTextField.getText();
		// 	port = Integer.parseInt(portTextField.getText());
		// 	primaryStage.setScene(sceneMap.get("gameScene")); //changes to game scene when button to connect is pressed

		// 	clientConnection = new Client(data -> {
		// 		Platform.runLater(() -> {
		// 			listItems2.getItems().add(data.toString());
		// 		});
		// 	}, port, address);
		// 	System.out.println(address + "  " + port);
		// 	clientConnection.start();
		// });


		startScene = new Scene(startPane, 800, 800);

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
	
	public Scene getScene(Stage primaryStage) {
		BorderPane gamePane = new BorderPane(); 
		playerView = new ListView<String>();
		bankerView = new ListView<String>();
		betAmount = new TextField();
		betAmount.setPromptText("Enter betting amount:");
		// look up radio buttons, only selects one
		betButton = new Button("Bet");
		choiceButtons = new ToggleGroup();
		playerButton = new RadioButton("Player");
		bankerButton = new RadioButton("Banker");
		drawButton = new RadioButton("Draw");
		playerButton.setToggleGroup(choiceButtons);
		bankerButton.setToggleGroup(choiceButtons);
		drawButton.setToggleGroup(choiceButtons);
		HBox gameBox = new HBox(10, playerButton, bankerButton, drawButton, betAmount, betButton);
		VBox infoScreen = new VBox(10, playerView, bankerView);

		gamePane.setTop(gameBox);
		gamePane.setBottom(infoScreen);

		Scene gameScene = new Scene(gamePane, 800, 800);
		//sceneMap.put("game", gameScene);

		// need to start a baccaratinfo containing all the info
		gameHandler = new EventHandler<ActionEvent> () {
			public void handle(ActionEvent e) {
				clientInfo = new BaccaratInfo();
				RadioButton selected = (RadioButton) choiceButtons.getSelectedToggle();
				clientInfo.betOnWho = selected.getText();
				clientInfo.currentBet = Integer.parseInt(betAmount.getText());
				clientInfo = clientConnection.clientInfo; //??????
				//clientConnection.send(clientInfo);
			}
		};

		return gameScene;
	}

}