package view;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import model.User;

public class OrganizerLandingPage {
	private Scene scene;
	private SceneManager sceneManager;
	private BorderPane root;
	
	public OrganizerLandingPage(SceneManager sceneManager, User user) {
		this.sceneManager = sceneManager;
		createPage(user);
	}
	private void createPage(User user) {
		root = new BorderPane();
		scene = new Scene(root,1000,500);
		root.setTop(OrganizerComponentFactory.createMenuBar(root,scene,user,sceneManager));
		System.out.println("Menu bar created successfully");
		
		OrganizerEvents oe = new OrganizerEvents(user.getUser_id(),root,scene);
		root.setCenter(oe.getView());
		System.out.println("OrganizerEvents initialized successfully");
	}
	public Scene getScene() {
		return scene;
	}
}
