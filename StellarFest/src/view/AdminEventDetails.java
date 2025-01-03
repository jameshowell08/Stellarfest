package view;

import java.util.Arrays;

import controller.AdminController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Event;
import model.Guest;
import model.Vendor;

public class AdminEventDetails implements AdminController.ValidationErrorCallback{
	
	private ScrollPane sp3,sp4,spAll;
	private TableView<Guest> tableGuestAttendee;
	private TableView<Vendor> tableVendorAttendee;
	private AdminController aCon;
	private VBox vb3,vb4,vbText,vbAttendee;
	private Label name_lbl,date_lbl,loc_lbl,desc_lbl,guest_lbl,vendor_lbl;
	private TextField name_tf,loc_tf,date_tf;
	private TextArea desc_ta;
	private BorderPane container ;
	private HBox all ;
		
	public AdminEventDetails(String eventId) {
		container = new BorderPane();
		aCon = new AdminController(this);
		eventDetails(eventId);
		guestAttendeeTable(eventId);
		vendorAttendeeTable(eventId);
		
		vbAttendee = new VBox();
		vbAttendee.getChildren().addAll(vb3,vb4);
		all = new HBox();
		all.getChildren().addAll(vbText,vbAttendee);
		spAll = new ScrollPane();
		spAll.setContent(all);
		container.setCenter(spAll);
		
	}
	private void eventDetails(String eventId) {
		Event event = aCon.getEventById(eventId);
		vbText = new VBox();
		name_lbl = new Label("Event Name");
		name_tf = new TextField();
		name_tf.setText(event.getEvent_name());
		
		date_lbl = new Label("Event Date");
		date_tf = new TextField();
		date_tf.setText(event.getEvent_date());
		
		loc_lbl = new Label("Event Location");
		loc_tf = new TextField();
		loc_tf.setText(event.getEvent_location());
		
		desc_lbl = new Label("Event Description");
		desc_ta = new TextArea();
		desc_ta.setText(event.getEvent_description());
		vbText.getChildren().addAll(name_lbl,name_tf,date_lbl,date_tf,loc_lbl,loc_tf,desc_lbl,desc_ta);
	}
	
	private void guestAttendeeTable(String eventId) {
		
		sp3 = new ScrollPane();

		tableGuestAttendee = new TableView<>();
		
		TableColumn<Guest, String> idCol = new TableColumn<>("Id");
		idCol.setCellValueFactory(new PropertyValueFactory<>("user_id"));
		idCol.setMinWidth(100);
		
		TableColumn<Guest, String> emailCol = new TableColumn<>("Email");
		emailCol.setCellValueFactory(new PropertyValueFactory<>("user_email"));
		emailCol.setMinWidth(100);
		
		TableColumn<Guest, String> nameCol = new TableColumn<>("Name");
		nameCol.setCellValueFactory(new PropertyValueFactory<>("user_name"));
		nameCol.setMinWidth(100);
		
		tableGuestAttendee.getColumns().addAll(FXCollections.observableArrayList(Arrays.asList(idCol,emailCol,nameCol)));
		tableGuestAttendee.setMaxWidth(400);
		tableGuestAttendee.setMaxHeight(300);
		
		refreshGuestAttendee(eventId);
		
		sp3.setContent(tableGuestAttendee);
		sp3.setMaxHeight(400);
		guest_lbl = new Label("GUEST Attendee :");
		vb3=new VBox();
		vb3.getChildren().addAll(guest_lbl,sp3);
		
	}
	private void vendorAttendeeTable(String eventId) {
	
		sp4 = new ScrollPane();

		tableVendorAttendee = new TableView<>();
		
		TableColumn<Vendor, String> idCol = new TableColumn<>("Id");
		idCol.setCellValueFactory(new PropertyValueFactory<>("user_id"));
		idCol.setMinWidth(100);
		
		TableColumn<Vendor, String> emailCol = new TableColumn<>("Email");
		emailCol.setCellValueFactory(new PropertyValueFactory<>("user_email"));
		emailCol.setMinWidth(100);
		
		TableColumn<Vendor, String> nameCol = new TableColumn<>("Name");
		nameCol.setCellValueFactory(new PropertyValueFactory<>("user_name"));
		nameCol.setMinWidth(100);
		
		tableVendorAttendee.getColumns().addAll(FXCollections.observableArrayList(Arrays.asList(idCol,emailCol,nameCol)));
		tableVendorAttendee.setMaxWidth(400);
		tableVendorAttendee.setMaxHeight(300);
		
		refreshVendorAttendee(eventId);
		
		sp4.setContent(tableVendorAttendee);	
		vendor_lbl = new Label("VENDOR Attendee:");
		vb4=new VBox();
		vb4.getChildren().addAll(vendor_lbl,sp4);
	}
	
	private void refreshGuestAttendee(String eventId) {
		tableGuestAttendee.refresh();
		ObservableList<Guest> guestObs = aCon.getGuestAttendes(eventId);
		tableGuestAttendee.setItems(guestObs);
	}
	private void refreshVendorAttendee(String eventId) {
		tableGuestAttendee.refresh();
		ObservableList<Vendor> vendorObs = aCon.getVendorAttendees(eventId);
		tableVendorAttendee.setItems(vendorObs);
	}
	public BorderPane getView() {
		return container;
	}
	@Override
	public void displayError(String errorMessage) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setHeaderText(null);
		alert.setContentText(errorMessage);
		alert.showAndWait();
	}
}
