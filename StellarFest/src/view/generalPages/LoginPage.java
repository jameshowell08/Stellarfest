package view.generalPages;

import controller.UserController;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import view.SceneManager;


public class LoginPage implements  UserController.ValidationErrorCallback{
	private SceneManager sceneManager;
	private VBox vb;
	private Scene scene;
	private Label email_lbl,password_lbl;
	private TextField email_tf;
	private PasswordField password_pf;
	private Button login_btn;
	private UserController ucon;
	
	
	public LoginPage(SceneManager sceneManager) {
		this.sceneManager = sceneManager;
		this.ucon = new UserController(sceneManager,this);
		createPage();
	}
	private void createPage() {
		vb = new VBox();
		email_lbl = new Label("Email");
		email_tf = new TextField();
		password_lbl = new Label("Password");
		password_pf = new PasswordField();
		login_btn = new Button("Login");
		
		login_btn.setOnAction(e -> {
			String email = email_tf.getText();
			String password = password_pf.getText();
			
			ucon.Login(email, password);
		});
		
		vb.getChildren().addAll(email_lbl,email_tf,password_lbl,password_pf,login_btn);
		scene = new Scene(vb,1000,500);
	}
	public Scene getScene() {
		return scene;
	}
	@Override
	public void displayError(String errorMessage) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setHeaderText(null);
		alert.setContentText(errorMessage);
		alert.showAndWait();
	}
}
