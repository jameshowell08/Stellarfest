package view;

import java.util.Arrays;
import java.util.HashSet;

import controller.EventOrganizerController;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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

public class OrganizerAddGV implements EventOrganizerController.ValidationErrorCallback {
	
	private ScrollPane sp1,sp2,sp3,sp4,sAll;
	private BorderPane container;
	private TableView<Guest> tableGuest;
	private TableView<Vendor> tableVendor;
	private TableView<Guest> tableGuestAttendee;
	private TableView<Vendor> tableVendorAttendee;
	private VBox vb1,vb2,vbText,vbAdders,vb3,vb4,vbleft;
	private Button addGuest_btn,addVendor_btn;
	 private HashSet<Guest> selectedGuest;
	 private HashSet<Vendor> selectedVendor;
	 private EventOrganizerController eoCon;
	 private Label name_lbl,date_lbl,loc_lbl,desc_lbl,guest_lbl,vendor_lbl;
		private TextField name_tf,loc_tf,date_tf;
		private TextArea desc_ta;
		private HBox attendee, all;
	
	public OrganizerAddGV(String eventId) {
		eoCon = new EventOrganizerController(this);
		eventDetails(eventId);
		guestTable(eventId);
		vendorTable(eventId);
		guestAttendeeTable(eventId);
		vendorAttendeeTable(eventId);
		container = new BorderPane();
		guest_lbl = new Label("GUEST :");
		vendor_lbl = new Label("VENDOR : ");
		vbAdders = new VBox();
		vbAdders.getChildren().addAll(guest_lbl, vb1,vendor_lbl,vb2);
		
		attendee = new HBox();
		attendee.getChildren().addAll(vb3,vb4);
		
		vbleft = new VBox();
		vbleft.getChildren().addAll(vbText,attendee);
		
		all = new HBox();
		all.getChildren().addAll(vbleft,vbAdders);
		
		sAll = new ScrollPane();
		sAll.setContent(all);
		
		container.setCenter(sAll);
		
		
	}
	
	private void eventDetails(String eventId) {
		Event event = eoCon.getEventById(eventId);
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
	
	private void guestTable(String eventId) {
		vb1 = new VBox();
		sp1 = new ScrollPane();
		
		selectedGuest = new HashSet<Guest>();
		tableGuest = new TableView<>();
		TableColumn<Guest, String> emailCol = new TableColumn<>("Email");
		emailCol.setCellValueFactory(new PropertyValueFactory<>("user_email"));
		emailCol.setMinWidth(150);
		
		TableColumn<Guest, CheckBox> inviteCol = new TableColumn<>("Invite");
		inviteCol.setCellValueFactory( cellData -> {
			Guest guest = cellData.getValue();
			CheckBox checkBox = new CheckBox();
			checkBox.setOnAction(e->{
				if(checkBox.isSelected()) {
					selectedGuest.add(guest);
				}else {
					selectedGuest.remove(guest);
				}
			});
			return new ReadOnlyObjectWrapper<>(checkBox);
			
		});
		inviteCol.setMinWidth(50);
		
		
		
		tableGuest.getColumns().addAll(FXCollections.observableArrayList(Arrays.asList(emailCol,inviteCol)));
		tableGuest.setMaxWidth(400);
		tableGuest.setMaxHeight(400);
		
		refreshGuestData(eventId);
		
		addGuest_btn = new Button("Add Guest");
		addGuest_btn.setOnAction(e -> {
			
			for (Guest guest : selectedGuest) {
				eoCon.checkAddGuestInput(eventId, guest.getUser_id(), guest.getUser_role());
			}
			refreshGuestData(eventId);
		});
		
		sp1.setContent(tableGuest);
		vb1.getChildren().addAll(sp1,addGuest_btn);
		System.out.println("EVENT ID" + eventId); 
	}
	
	private void vendorTable(String eventId) {
		vb2 = new VBox();
		sp2 = new ScrollPane();
		selectedVendor = new HashSet<Vendor>();
		tableVendor = new TableView<>();
		TableColumn<Vendor, String> emailCol = new TableColumn<>("Email");
		emailCol.setCellValueFactory(new PropertyValueFactory<>("user_email"));
		emailCol.setMinWidth(150);
		
		TableColumn<Vendor, CheckBox> inviteCol = new TableColumn<>("Invite");
		inviteCol.setCellValueFactory( cellData -> {
			Vendor vendor = cellData.getValue();
			CheckBox checkBox = new CheckBox();
			checkBox.setOnAction(e->{
				if(checkBox.isSelected()) {
					selectedVendor.add(vendor);
				}else {
					selectedVendor.remove(vendor);
				}
			});
			return new ReadOnlyObjectWrapper<>(checkBox);
			
		});
		inviteCol.setMinWidth(50);
		
		tableVendor.getColumns().addAll(FXCollections.observableArrayList(Arrays.asList(emailCol,inviteCol)));
		tableVendor.setMaxWidth(400);
		tableVendor.setMaxHeight(400);
		
		refreshVendorData(eventId);
		
		addVendor_btn = new Button("Add Vendor");
		addVendor_btn.setOnAction(e -> {
			for (Vendor vendor : selectedVendor) {
				eoCon.checkAddVendorInput(eventId, vendor.getUser_id(), vendor.getUser_role());
			}
			refreshVendorData(eventId);
		});
		
		sp2.setContent(tableVendor);
		sp2.setMaxHeight(400);
		vb2.getChildren().addAll(sp2,addVendor_btn);
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
		guest_lbl = new Label("GUEST :");
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
		vendor_lbl = new Label("VENDOR :");
		vb4=new VBox();
		vb4.getChildren().addAll(vendor_lbl,sp4);
	}
	
	private void refreshGuestData(String eventId) {
		tableGuest.refresh();
		ObservableList<Guest> guestObs = eoCon.getGuests(eventId);
		tableGuest.setItems(guestObs);
		System.out.println("Guests: " + guestObs);
	}
	private void refreshVendorData(String eventId) {
		tableVendor.refresh();
		ObservableList<Vendor> vendorObs = eoCon.getVendors(eventId);
		tableVendor.setItems(vendorObs);
		System.out.println("vendors: " + vendorObs);
	}
	private void refreshGuestAttendee(String eventId) {
		tableGuestAttendee.refresh();
		ObservableList<Guest> guestObs = eoCon.getGuestAttendes(eventId);
		tableGuestAttendee.setItems(guestObs);
	}
	private void refreshVendorAttendee(String eventId) {
		tableGuestAttendee.refresh();
		ObservableList<Vendor> vendorObs = eoCon.getVendorAttendees(eventId);
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
