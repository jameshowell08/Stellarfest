package view;

import java.util.Arrays;


import controller.AdminController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Event;
import model.User;

public class AdminLandingPage implements AdminController.ValidationErrorCallback{
	private Scene scene;
	private SceneManager sceneManager;
	private VBox vb1,vb2;
	private ScrollPane sp1,sp2;
	private TableView<User> tableUser;
	private TableView<Event> tableEvent;
	private Button deleteUser_btn,deleteEvent_btn;
	private AdminController aCon;
	private BorderPane root;
	private String tempEventId = null;
	private HBox all;
	public AdminLandingPage(SceneManager sceneManager,User user) {
		this.sceneManager = sceneManager;
		aCon = new AdminController(this);
		createPage(user);
	}
	private void createPage(User user) {
		root = new BorderPane();
		scene = new Scene(root,1000,500);
		userTable();
		eventTable();
		all = new HBox();
		all.getChildren().addAll(vb1,vb2);
		root.setTop(AdminComponentFactory.createMenuBar(root, sceneManager, user));
		root.setCenter(all);
	}
	private void userTable() {
		vb1 = new VBox();
		sp1 = new ScrollPane();
		tableUser = new TableView<>();
		
		TableColumn<User, String> nameCol = new TableColumn<>("Name");
		nameCol.setCellValueFactory(new PropertyValueFactory<>("user_name"));
		nameCol.setMinWidth(100);
		
		TableColumn<User, String> emailCol = new TableColumn<>("Email");
		emailCol.setCellValueFactory(new PropertyValueFactory<>("user_email"));
		emailCol.setMinWidth(100);
		
		TableColumn<User, String> roleCol = new TableColumn<>("Role");
		roleCol.setCellValueFactory(new PropertyValueFactory<>("user_role"));
		roleCol.setMinWidth(100);
		
		tableUser.getColumns().addAll(FXCollections.observableArrayList(Arrays.asList(nameCol,emailCol,roleCol)));
		tableUser.setMaxWidth(600);
		tableUser.setMaxHeight(600);
		
		
		
		refreshUserData();
		
		deleteUser_btn = new Button("DELETE USER");
		deleteUser_btn.setOnAction(e->{
			User selectedUser = tableUser.getSelectionModel().getSelectedItem();
			if(selectedUser!=null) {
				aCon.deleteUser(selectedUser.getUser_id());
				refreshUserData();
			}
		});
		
		sp1.setContent(tableUser);
		vb1.getChildren().addAll(sp1,deleteUser_btn);
		
		
		
	}
	
	private void eventTable() {
		sp2 = new ScrollPane();
		tableEvent = new TableView<>();
		
		TableColumn<Event, Integer> idCol = new TableColumn<>("EventID");
		idCol.setCellValueFactory(new PropertyValueFactory<>("event_id"));
		idCol.setMinWidth(100);
		
		TableColumn<Event, String> nameCol = new TableColumn<>("EventName");
		nameCol.setCellValueFactory(new PropertyValueFactory<>("event_name"));
		nameCol.setMinWidth(100);
		
		TableColumn<Event, String> dateCol = new TableColumn<>("EventDate");
		dateCol.setCellValueFactory(new PropertyValueFactory<>("event_date"));
		dateCol.setMinWidth(100);
		
		TableColumn<Event, String> locCol = new TableColumn<>("EventLoc");
		locCol.setCellValueFactory(new PropertyValueFactory<>("event_location"));
		locCol.setMinWidth(100);
		
		
		tableEvent.getColumns().addAll(FXCollections.observableArrayList(Arrays.asList(idCol,nameCol,dateCol,locCol)));
		tableEvent.setMaxWidth(500);
		tableEvent.setMaxHeight(500);
		
		tableEvent.setOnMouseClicked(e -> {
			TableSelectionModel<Event> tableSelectionModel = tableEvent.getSelectionModel();
			tableSelectionModel.setSelectionMode(SelectionMode.SINGLE);
			
			Event selectedEvent = tableSelectionModel.getSelectedItem();
			
			tempEventId = selectedEvent.getEvent_id();
		});
		tableEvent.setRowFactory(tv -> {
            TableRow<Event> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Event rowData = row.getItem();
                    AdminEventDetails evDt = new AdminEventDetails(rowData.getEvent_id());
                    root.setCenter(evDt.getView());
                }
            });
            return row;
        });
		
		
		refreshEventData();
		
		deleteEvent_btn = new Button("DELETE EVent");
		deleteEvent_btn.setOnAction(e -> {
			aCon.deleteEvent(tempEventId);
			refreshEventData();
		});
		sp2.setContent(tableEvent);
		
		vb2 = new VBox();
		vb2.getChildren().addAll(sp2,deleteEvent_btn);
		
	}
	
	private void refreshUserData() {
		tableUser.refresh();
		ObservableList<User> userObs = aCon.viewAllUsers();
		tableUser.setItems(userObs);
		System.out.println(userObs);
	}
	private void refreshEventData() {
		tableEvent.refresh();
		ObservableList<Event> eventObs = aCon.viewAllEvents();
		tableEvent.setItems(eventObs);
		System.out.println(eventObs);
	}
	public Scene getScene() {
		return scene;
	}
	public BorderPane getView() {
		return root;
	}
	@Override
	public void displayError(String errorMessage) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setHeaderText(null);
		alert.setContentText(errorMessage);
		alert.showAndWait();
	}
}
