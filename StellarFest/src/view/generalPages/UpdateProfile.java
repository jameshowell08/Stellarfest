package view.generalPages;

import controller.UserController;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import model.User;

public class UpdateProfile implements UserController.ValidationErrorCallback {
	private Label name_lbl,email_lbl,password_lbl,info_lbl;
	private TextField name_tf, email_tf;
	private PasswordField password_pf;
	private VBox vb;
	private Button update_username,update_password, update_email;
	private UserController ucon = new UserController(null, this);
	public UpdateProfile(String userId) {
		
		vb = new VBox();
		User temp = new User();
		temp = ucon.getUserById(userId);
		name_lbl = new Label("Name");
		name_tf = new TextField(temp.getUser_name());

		email_lbl = new Label("Email");
		email_tf = new TextField(temp.getUser_email());
			
		password_lbl = new Label("Password");
		password_pf = new PasswordField();
		password_pf.setText(temp.getUser_password());
		
		update_username = new Button("Update username");
		update_email = new Button("Update email");
		update_password = new Button("Update button");
		
		info_lbl = new Label("");
		
		vb.getChildren().addAll(name_lbl,name_tf,email_lbl,email_tf,password_lbl,password_pf,update_username,update_email,update_password);
		
		update_username.setOnAction(e -> {
			String name = name_tf.getText();
			
			ucon.changeUserName(name, userId);	
		});
		update_email.setOnAction(e -> {
			String email = email_tf.getText();
			ucon.changeUserEmail(email, userId);	
		});
		update_password.setOnAction(e -> {
			User x = new User();
			x = ucon.getUserById(userId);
			String password = password_pf.getText();
			ucon.changeUserPassword(password, x.getUser_password(), userId);	
		});
	}
	
	public VBox getView() {
		return vb;
	}

	@Override
	public void displayError(String errorMessage) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setHeaderText(null);
		alert.setContentText(errorMessage);
		alert.showAndWait();
	}
	
}
