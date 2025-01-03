package view;

import javafx.stage.Stage;
import model.User;
import view.generalPages.*;

public class SceneManager {
    private Stage primaryStage;

    public SceneManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("StellarFest");
    }

    public void showLandingPage() {
        LandingPage lp = new LandingPage(this); // Create a new instance if needed
        primaryStage.setScene(lp.getScene());
    }

    public void showRegisterPage() {
        RegisterPage rp = new RegisterPage(this); // Create a new instance if needed
        primaryStage.setScene(rp.getScene());
    }

    public void showLoginPage() {
        LoginPage login = new LoginPage(this); // Create a new instance if needed
        primaryStage.setScene(login.getScene());
    }

    public void showGuestLandingPage(User user) {
        GuestLandingPage glp = new GuestLandingPage(this, user);
        primaryStage.setScene(glp.getScene());
    }

    public void showAdminLandingPage(User user) {
        AdminLandingPage alp = new AdminLandingPage(this, user);
        primaryStage.setScene(alp.getScene());
    }
    public void showOrganizerLandingPage(User user) {
    	OrganizerLandingPage olp = new OrganizerLandingPage(this, user);
    	primaryStage.setScene(olp.getScene());
    }
    public void showVendorLandingPage(User user) {
    	VendorLandingPage vlp = new VendorLandingPage(this, user);
    	primaryStage.setScene(vlp.getScene());
    }
}
