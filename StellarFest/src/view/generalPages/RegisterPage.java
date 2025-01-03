package view.generalPages;


import controller.UserController;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import model.User;
import view.SceneManager;

public class RegisterPage implements UserController.ValidationErrorCallback {
	private SceneManager sceneManager;
	private Scene scene;
	private VBox vb;
	private Label name_lbl,email_lbl,password_lbl,role_lbl,info_lbl;
	private TextField name_tf, email_tf;
	private PasswordField password_pf;
	private Button signup_btn;
	private UserController ucon;
	
	
	public RegisterPage(SceneManager sceneManeger) {
		this.sceneManager = sceneManeger;
		this.ucon = new UserController(sceneManager,this);
		createPage();
		}
	
	public void createPage() {
		vb = new VBox();
		name_lbl = new Label("Name");
		name_tf = new TextField();

		email_lbl = new Label("Email");
		email_tf = new TextField();
		
		role_lbl = new Label("Role");
		ComboBox<String> role_cb = new ComboBox<String>();
		role_cb.getItems().addAll("Organizer","Vendor","Guest");
		
		password_lbl = new Label("Password");
		password_pf = new PasswordField();
		
		signup_btn = new Button("SignUp");
		info_lbl = new Label("");
		
		vb.getChildren().addAll(name_lbl,name_tf,email_lbl,email_tf,role_lbl,role_cb,password_lbl,password_pf,signup_btn,info_lbl);
		scene = new Scene(vb,1000,500);
		
		
		
		signup_btn.setOnAction(e -> {
			String name = name_tf.getText();
			String email = email_tf.getText();
			String role = role_cb.getValue();
			String password = password_pf.getText();
			
			ucon.Register(name, email, role, password);
			
		});
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
	public void navigateToLandingPage() {
        sceneManager.showLandingPage();
    }
		
}
