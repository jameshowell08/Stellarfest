package view;

import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import model.User;
import view.generalPages.UpdateProfile;

public class VendorComponentFactory {
	 public static ToolBar createMenuBar(BorderPane root, SceneManager sceneManager, User user) {
	        ToolBar toolBar = new ToolBar();
	        
	        Button viewHomeButton = new Button("Home");
	        //Button addGVButton = new Button("Add Guest & Vendors");
	        Button updateProfileButton = new Button("Update Profile");
	        
	        Button logoutButton = new Button("LogOut");
	        toolBar.getItems().addAll(viewHomeButton, updateProfileButton,logoutButton);
	        
	        viewHomeButton.setOnAction(e -> {
	            sceneManager.showVendorLandingPage(user);
	        });
	        
	        updateProfileButton.setOnAction(e -> {
	            UpdateProfile updateProfileView = new UpdateProfile(user.getUser_id());
	            root.setCenter(updateProfileView.getView());
	        });
	        logoutButton.setOnAction(e -> {
	        	sceneManager.showLandingPage();
	        });
	        
	        return toolBar;
	    }
}
