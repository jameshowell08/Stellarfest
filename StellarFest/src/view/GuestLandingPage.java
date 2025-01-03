package view;

import java.util.Arrays;
import java.util.HashSet;

import controller.GuestController;
import controller.InvitationController;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Event;
import model.User;

public class GuestLandingPage implements GuestController.ValidationErrorCallback{
	private Scene scene;
	private SceneManager sceneManager;
	private VBox vb,vb1,vb2;
	private BorderPane root;
	private TableView<Event> tableEvent;
	private TableView<Event> tableAcc;
	private HashSet<Event> selectedEvent;
	private InvitationController iCon;
	private ScrollPane sp1,sp2,spAll;
	private Button accInvBtn;
	private GuestController gCon;
	private Label name_lbl,date_lbl,loc_lbl,desc_lbl;
	private TextField name_tf,loc_tf,date_tf;
	private TextArea desc_ta;
	private HBox all ;
	
	public GuestLandingPage(SceneManager sceneManager,User user) {
		this.sceneManager = sceneManager;
		gCon = new GuestController(this);
		createPage(user);
	}
	private void createPage(User user) {
		root = new BorderPane();
		scene = new Scene(root,1000,500);
		invTable(user.getUser_id(),user.getUser_email());
		acceptedInvitationsTable(user.getUser_id(),user.getUser_email());
		all = new HBox();
		all.getChildren().addAll(vb1,vb2);
		spAll = new ScrollPane();
		spAll.setContent(all);
		root.setCenter(spAll);
		root.setTop(GuestComponentFactory.createMenuBar(root, sceneManager, user));
		
	}
	private void invTable(String userId, String email) {
		tableEvent = new TableView<>();
		selectedEvent = new HashSet<>();
		TableColumn<Event, Integer> nameCol = new TableColumn<>("EventName");
		nameCol.setCellValueFactory(new PropertyValueFactory<>("event_name"));
		nameCol.setMinWidth(100);
		TableColumn<Event, CheckBox> inviteCol = new TableColumn<>("Accept");
		inviteCol.setCellValueFactory( cellData -> {
			Event event = cellData.getValue();
			CheckBox checkBox = new CheckBox();
			checkBox.setOnAction(e->{
				if(checkBox.isSelected()) {
					selectedEvent.add(event);
				}else {
					selectedEvent.remove(event);
				}
			});
			return new ReadOnlyObjectWrapper<>(checkBox);
			
		});
		inviteCol.setMinWidth(50);
		
		tableEvent.getColumns().addAll(FXCollections.observableArrayList(Arrays.asList(nameCol,inviteCol)));
		tableEvent.setMaxWidth(400);
		tableEvent.setMaxHeight(400);
		
		refreshInvTable(email);
		
		accInvBtn = new Button("Accept Invitation");
		accInvBtn.setOnAction(e -> {
			for (Event event : selectedEvent) {
				gCon.accInvitation(userId, event.getEvent_id());
			}
			refreshInvTable(email);
			refreshAcctable(email);
		});
		sp1 = new ScrollPane();
		sp1.setContent(tableEvent);
		vb1 = new VBox();
		vb1.getChildren().addAll(sp1,accInvBtn);
		
	}
	
	private void acceptedInvitationsTable(String userId,String email) {
		tableAcc = new TableView<>();
		
		TableColumn<Event, String> nameCol = new TableColumn<>("EventName");
		nameCol.setCellValueFactory(new PropertyValueFactory<>("event_name"));
		nameCol.setMinWidth(100);
		
		tableAcc.getColumns().addAll(FXCollections.observableArrayList(Arrays.asList(nameCol)));
		tableAcc.setMaxWidth(100);
		tableAcc.setMaxHeight(400);
		
		refreshAcctable(email);
		vb2 = new VBox();
	
		sp2 = new ScrollPane();
		sp2.setContent(tableAcc);
		
		vb = new VBox();
		name_lbl = new Label("Event Name");
		name_tf = new TextField();
		
		date_lbl = new Label("Event Date");
		date_tf = new TextField();
		
		loc_lbl = new Label("Event Location");
		loc_tf = new TextField();
		
		desc_lbl = new Label("Event Description");
		desc_ta = new TextArea();
		
		vb.getChildren().addAll(name_lbl,name_tf,date_lbl,date_tf,loc_lbl,loc_tf,desc_lbl,desc_ta);
		
		name_lbl.setVisible(false);
		name_tf.setVisible(false);
		date_lbl.setVisible(false);
		date_tf.setVisible(false);
		loc_lbl.setVisible(false);
		loc_tf.setVisible(false);
		desc_lbl.setVisible(false);
		desc_ta.setVisible(false);
		
		tableAcc.setOnMouseClicked(e -> {
			TableSelectionModel<Event> tableSelectionModel = tableAcc.getSelectionModel();
			tableSelectionModel.setSelectionMode(SelectionMode.SINGLE);
			
			Event selectedEvent = tableSelectionModel.getSelectedItem();
			name_tf.setText(selectedEvent.getEvent_name());
			date_tf.setText(selectedEvent.getEvent_date());
			loc_tf.setText(selectedEvent.getEvent_location());
			desc_ta.setText(selectedEvent.getEvent_description());
			name_lbl.setVisible(true);
			name_tf.setVisible(true);
			date_lbl.setVisible(true);
			date_tf.setVisible(true);
			loc_lbl.setVisible(true);
			loc_tf.setVisible(true);
			desc_lbl.setVisible(true);
			desc_ta.setVisible(true);
		});
		
		
		vb2.getChildren().addAll(sp2,vb);
		
	}
	
	private void refreshInvTable(String email) {
	    iCon = new InvitationController();
	    ObservableList<Event> evntObs = iCon.getInvitations(email);
	    tableEvent.setItems(evntObs);
	    selectedEvent.clear(); // Clear the selected events
	}

	private void refreshAcctable(String email) {
	    ObservableList<Event> evntObs = gCon.getAcceptedInvitations(email);
	    tableAcc.setItems(evntObs);
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
