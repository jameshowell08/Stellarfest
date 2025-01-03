package view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import model.User;
import view.generalPages.UpdateProfile;

public class OrganizerComponentFactory {
    public static ToolBar createMenuBar(BorderPane root, Scene scene, User user, SceneManager sceneManager) {
        ToolBar toolBar = new ToolBar();
        
        Button viewEventsButton = new Button("View Events");
        //Button addGVButton = new Button("Add Guest & Vendors");
        Button updateProfileButton = new Button("Update Profile");
        
        Button logoutButton = new Button("LogOut");
        
        toolBar.getItems().addAll(viewEventsButton, updateProfileButton,logoutButton);
        
        viewEventsButton.setOnAction(e -> {
            OrganizerEvents organizerEvents = new OrganizerEvents(user.getUser_id(),root,scene);
            root.setCenter(organizerEvents.getView());
        });
        
        updateProfileButton.setOnAction(e -> {
            UpdateProfile updateProfileView = new UpdateProfile(user.getUser_id());
            root.setCenter(updateProfileView.getView());
        });
        
        logoutButton.setOnAction(e->{
        	sceneManager.showLandingPage();
        });
        
        
        return toolBar;
    }
}
