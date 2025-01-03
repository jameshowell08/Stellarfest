package view.generalPages;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import view.SceneManager;

public class LandingPage {
	private Scene scene;
	private SceneManager sceneManager;
	private VBox vb;
	private Button lgn_btn,rgs_btn;
	
	public LandingPage(SceneManager sceneManager) {
		this.sceneManager = sceneManager;
		createPage();
	}
	public void createPage() {
		vb = new VBox();
		lgn_btn = new Button("LOGIN");
		rgs_btn = new Button("Sign Up");
		
		vb.getChildren().addAll(lgn_btn,rgs_btn);
		scene = new Scene(vb,1000,500);
		
		rgs_btn.setOnAction(e -> {
			sceneManager.showRegisterPage();
		});
		
		lgn_btn.setOnAction(e -> {
			sceneManager.showLoginPage();
		});
		
	}
	public Scene getScene() {
		return scene;
	}
	
}
