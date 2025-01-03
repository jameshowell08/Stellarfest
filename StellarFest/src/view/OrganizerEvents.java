package view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

import controller.EventController;
import controller.EventOrganizerController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Event;

public class OrganizerEvents implements EventController.ValidationErrorCallback, EventOrganizerController.ValidationErrorCallback{
	private ScrollPane sp;
	private BorderPane container;
	private EventOrganizerController eoCon;
	private TableView<Event> table;
	private Label name_lbl,date_lbl,loc_lbl,desc_lbl,info_lbl;
	private TextField name_tf,loc_tf;
	private DatePicker dp;
	private VBox vb;
	private TextArea desc_ta;
	private Button create_btn,update_btn;
	private EventController eCon;
	private String tempEventId = null;
	private HBox btnBox;

	
	public OrganizerEvents(String OrganizerId, BorderPane root, Scene scene) {
		sp = new ScrollPane();
		container = new BorderPane();
		eCon = new EventController(this);
		eoCon = new EventOrganizerController(this);
		
		table = new TableView<>();
		
		TableColumn<Event, String> idCol = new TableColumn<>("EventID");
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
		
		
		table.getColumns().addAll(FXCollections.observableArrayList(Arrays.asList(idCol,nameCol,dateCol,locCol)));
		table.setMaxWidth(500);
		table.setMaxHeight(500);
		
		table.setOnMouseClicked(e -> {
			TableSelectionModel<Event> tableSelectionModel = table.getSelectionModel();
			tableSelectionModel.setSelectionMode(SelectionMode.SINGLE);
			
			Event selectedEvent = tableSelectionModel.getSelectedItem();
			name_tf.setText(selectedEvent.getEvent_name());
			loc_tf.setText(selectedEvent.getEvent_location());
			desc_ta.setText(selectedEvent.getEvent_description());
			try {
	            LocalDate eventDate = LocalDate.parse(selectedEvent.getEvent_date());
	            dp.setValue(eventDate);
	        } catch (DateTimeParseException ex) {
	            // Handle parsing error if date string is not in the expected format
	            System.out.println("Error parsing date: " + ex.getMessage());
	        }
			tempEventId = selectedEvent.getEvent_id();
		});
		
		table.setRowFactory(tv -> {
            TableRow<Event> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Event rowData = row.getItem();
                    OrganizerAddGV addGV = new OrganizerAddGV(rowData.getEvent_id());
                    root.setCenter(addGV.getView());
                }
            });
            return row;
        });
		
		refreshData(OrganizerId);
		
		sp.setContent(table);
		
		//create 
		vb = new VBox();
		name_lbl = new Label("Event Name");
		name_tf = new TextField();
		
		date_lbl = new Label("Event Date");
		dp = new DatePicker();
		
		loc_lbl = new Label("Event Location");
		loc_tf = new TextField();
		
		desc_lbl = new Label("Event Description");
		desc_ta = new TextArea();
		
		btnBox = new HBox();
		create_btn = new Button("Create");
		update_btn = new Button("Update");
		btnBox.getChildren().addAll(create_btn,update_btn);
		HBox.setMargin(create_btn, new Insets(10));
		HBox.setMargin(update_btn, new Insets(10));
		
		info_lbl = new Label("");
		
		vb.getChildren().addAll(name_lbl,name_tf,date_lbl,dp,loc_lbl,loc_tf,desc_lbl,desc_ta,btnBox,info_lbl);
		
		container.setRight(vb);
		
		container.setCenter(sp);
		
		
		
		
		//eCon = new EventController(this);
		create_btn.setOnAction(e -> {
			String name = name_tf.getText();
			String date = "";
		    if (dp.getValue() != null) {
		        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		        date = dp.getValue().format(formatter);
		    }
		    
			
			String loc = loc_tf.getText();
			String desc = desc_ta.getText();
			
			eCon.createEvent(name, date, loc, desc, OrganizerId);
			textFieldEventRefresh();
			refreshData(OrganizerId);
		});
		
		update_btn.setOnAction(e ->{
			String name = name_tf.getText();
			String date = "";
		    if (dp.getValue() != null) {
		        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		        date = dp.getValue().format(formatter);
		    }
		    
			String loc = loc_tf.getText();
			String desc = desc_ta.getText();
			
			eoCon.updateEvent(name, date, loc, desc, OrganizerId, tempEventId);
			textFieldEventRefresh();
			refreshData(OrganizerId);
		});
		
	}
	
	private void refreshData(String organizerId) {
		table.refresh();
		ObservableList<Event> evntObs = eoCon.viewOrganizedEvents(organizerId);
		table.setItems(evntObs);
	}
	
	public BorderPane getView() {
		return container;
	}
	private void textFieldEventRefresh() {
		name_tf.setText(null);
		dp.setValue(null);
		loc_tf.setText(null);
		desc_ta.setText(null);
	}

	@Override
	public void displayError(String errorMessage) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setHeaderText(null);
		alert.setContentText(errorMessage);
		alert.showAndWait();
	}
}
