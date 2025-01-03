package main;

import javafx.application.Application;
import javafx.stage.Stage;
import view.SceneManager;

public class Main extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		SceneManager sceneManager = new SceneManager(primaryStage);
		sceneManager.showLandingPage();
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
