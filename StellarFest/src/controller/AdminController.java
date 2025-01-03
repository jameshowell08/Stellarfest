   package controller;

import java.util.Vector;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Admin;
import model.Event;
import model.Guest;
import model.User;
import model.Vendor;

public class AdminController {
	
	private Admin admin = new Admin();
	private ValidationErrorCallback errorCallback;
	public AdminController(ValidationErrorCallback errorCallback) {
		this.errorCallback = errorCallback;
	}
	public interface ValidationErrorCallback {
        void displayError(String errorMessage);
    }
	public ObservableList<User> viewAllUsers(){
		Vector<User> temp = admin.getUsers();
		ObservableList<User> userObs = FXCollections.observableArrayList(temp);
		return userObs;
	}
	public void deleteUser(String userId) {
		if (admin.deleteUser(userId)) {
			errorCallback.displayError("Success Delete User");
		}else {
			errorCallback.displayError("failed Delete User");
		}
	}
	public ObservableList<Event> viewAllEvents(){
		Vector<Event> temp = admin.getEvents();
		ObservableList<Event> eventObs = FXCollections.observableArrayList(temp);
		return eventObs;
	}
	public void deleteEvent(String eventId) {
		if (admin.deleteEvent(eventId)) {
			errorCallback.displayError("Success Delete Event");
		}else {
			errorCallback.displayError("failed Delete Event");
		}
	}
	public ObservableList<Guest> getGuestAttendes(String eventId){
		Vector<Guest> temp = admin.getGuestAttendeeByEventId(eventId);
		ObservableList<Guest> guestObs = FXCollections.observableArrayList(temp);
		return guestObs;
	}
	public ObservableList<Vendor> getVendorAttendees(String eventId){
		Vector<Vendor> temp = admin.getVendorAttendeeByEventId(eventId);
		ObservableList<Vendor> vendorObs = FXCollections.observableArrayList(temp);
		return vendorObs;
	}
	public Event getEventById(String eventId) {
		Event event = new Event();
		event = event.getEventById(eventId);
		return event;
	}
}
