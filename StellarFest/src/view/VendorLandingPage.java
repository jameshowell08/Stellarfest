package view;

import java.util.Arrays;
import java.util.HashSet;

import controller.InvitationController;
import controller.VendorController;
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
import model.Product;
import model.User;

public class VendorLandingPage implements VendorController.ValidationErrorCallback {
	private Scene scene;
	private SceneManager sceneManager;
	private VBox vb,vb1,vb2,vbPro,vbAll;
	private BorderPane root;
	private TableView<Event> tableEvent;
	private TableView<Event> tableAcc;
	private TableView<Product> tableProduct;
	private HashSet<Event> selectedEvent;
	private InvitationController iCon;
	private ScrollPane sp1,sp2,spAll,spProduct;
	private Button accInvBtn;
	private Label name_lbl,date_lbl,loc_lbl,desc_lbl,product_name_lbl,product_desc_lbl;
	private TextField name_tf,loc_tf,date_tf,product_name_tf;
	
	
	private TextArea desc_ta,product_desc_ta;
	private HBox all,HProduct;
	private VendorController vCon;
	private Button createProductBtn;
	
	public VendorLandingPage(SceneManager sceneManager,User user) {
		this.sceneManager = sceneManager;
		vCon = new VendorController(this);
		createPage(user);
	}
	private void createPage(User user) {
		root = new BorderPane();
		scene = new Scene(root,1000,500);
		productsTable(user.getUser_id());
		invTable(user.getUser_id(),user.getUser_email());
		acceptedInvitationsTable(user.getUser_id(),user.getUser_email());
		all = new HBox();
		all.getChildren().addAll(vb1,vb2);
		vbAll = new VBox();
		vbAll.getChildren().addAll(HProduct,all);
		spAll = new ScrollPane();
		spAll.setContent(vbAll);
		root.setTop(VendorComponentFactory.createMenuBar(root, sceneManager,user ));
		root.setCenter(spAll);
		
	}
	
	private void productsTable(String userId) {
		spProduct = new ScrollPane();
		
		tableProduct = new TableView<>();
		
		TableColumn<Product, String> idCol = new TableColumn<>("ProductId");
		idCol.setCellValueFactory(new PropertyValueFactory<>("product_id"));
		idCol.setMinWidth(100);
		
		TableColumn<Product, String> nameCol = new TableColumn<>("ProductName");
		nameCol.setCellValueFactory(new PropertyValueFactory<>("product_name"));
		nameCol.setMinWidth(100);
		
		TableColumn<Product, String> locCol = new TableColumn<>("ProductDescription");
		locCol.setCellValueFactory(new PropertyValueFactory<>("product_description"));
		locCol.setMinWidth(100);
		
		
		tableProduct.getColumns().addAll(FXCollections.observableArrayList(Arrays.asList(idCol,nameCol,locCol)));
		tableProduct.setMaxWidth(500);
		tableProduct.setMaxHeight(500);
		
		refreshProduct(userId);
		
		spProduct.setContent(tableProduct);
		
		vbPro = new VBox();
		product_name_lbl = new Label("Product Name");
		product_name_tf = new TextField();
		
		product_desc_lbl = new Label("Event Description");
		product_desc_ta = new TextArea();
		
		createProductBtn = new Button("Create Product");
		createProductBtn.setOnAction(e -> {
			String name = product_name_tf.getText();
			String desc = product_desc_ta.getText();
			System.out.println(name);
			System.out.println(desc);
			vCon.createProduct(name, desc, userId);
			refreshProduct(userId);
			textFieldProductRefresh();
		});
		
		vbPro.getChildren().addAll(product_name_lbl,product_name_tf,product_desc_lbl,product_desc_ta,createProductBtn);
		
		HProduct = new HBox();
		HProduct.getChildren().addAll(spProduct,vbPro);
	}
	
	private void invTable(String userId,String email) {
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
				vCon.accInvitation(userId, event.getEvent_id());
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
		tableAcc.setMaxWidth(400);
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
	    ObservableList<Event> evntObs = vCon.getAcceptedInvitations(email);
	    tableAcc.setItems(evntObs);
	    selectedEvent.clear();
	}
	private void refreshProduct(String userId) {
		tableProduct.refresh();
		ObservableList<Product> productObs = vCon.getProducts(userId);
		tableProduct.setItems(productObs);
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
	public void textFieldProductRefresh() {
		product_name_tf.setText(null);
		product_desc_ta.setText(null);
	}
}
