package controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Vector;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Event;
import model.EventOrganizer;
import model.Guest;
import model.Invitation;
import model.Vendor;

public class EventOrganizerController {
	
	private EventOrganizer eo = new EventOrganizer();
	private Guest guest = new Guest();
	private Vendor vendor = new Vendor();
	private Invitation invitation = new Invitation();
	private ValidationErrorCallback errorCallback;
	public EventOrganizerController(ValidationErrorCallback errorCallback) {
		this.errorCallback = errorCallback;
	}
	public interface ValidationErrorCallback {
        void displayError(String errorMessage);
    }
	public ObservableList<Event> viewOrganizedEvents(String organizerID){
		Vector<Event> temp = eo.viewOrganizedEvents(organizerID);
		ObservableList<Event> evntObs = FXCollections.observableArrayList(temp);
		return evntObs;
	}
	public ObservableList<Guest> getGuests(String eventId){
		Vector<Guest> temp = guest.getGuests(eventId);
		ObservableList<Guest> guestObs = FXCollections.observableArrayList(temp);
		return guestObs;
	}
	public ObservableList<Vendor> getVendors(String eventId){
		Vector<Vendor> temp = vendor.getVendors(eventId);
		ObservableList<Vendor> vendorObs = FXCollections.observableArrayList(temp);
		return vendorObs;
	}

	public void checkAddGuestInput(String event_id, String guest_id, String role) {
		Invitation inv = new Invitation();
		if(inv.sendInvitation(event_id, guest_id, role)) {
			errorCallback.displayError("Success Send Invitation");
		}else {
			errorCallback.displayError("Failed Send Invitation");
		}
		
		
	}
	public void checkAddVendorInput(String event_id, String vendor_id, String role) {
		Invitation inv = new Invitation();
		if(inv.sendInvitation(event_id, vendor_id, role)) {
			errorCallback.displayError("Success Send Invitation");
		}else {
			errorCallback.displayError("Failed Send Invitation");
		}
	}
	public ObservableList<Guest> getGuestAttendes(String eventId){
		Vector<Guest> temp = eo.getGuestAttendeeByEventId(eventId);
		ObservableList<Guest> guestObs = FXCollections.observableArrayList(temp);
		return guestObs;
	}
	public ObservableList<Vendor> getVendorAttendees(String eventId){
		Vector<Vendor> temp = eo.getVendorAttendeeByEventId(eventId);
		ObservableList<Vendor> vendorObs = FXCollections.observableArrayList(temp);
		return vendorObs;
	}
	public void sendInvitation(String event_id, String user_id, String invitation_role) {
		if(invitation.sendInvitation(event_id, user_id, invitation_role)) {
			errorCallback.displayError("Success Send Invitation");
		}else {
			errorCallback.displayError("Failed Send Invitation");
		}
	}
	public Event getEventById(String eventId) {
		Event event = new Event();
		event = event.getEventById(eventId);
		return event;
	}
	public void updateEvent(String name,String date,String location, String description , String organizerId, String
			eventId) {
		String validationError = CheckEditEventInput(name, date, location, description);
		if (validationError != null) {
            if (errorCallback != null) {
                errorCallback.displayError(validationError);  // Send the error back to the view
            }
            return;
        }
		Event event = new Event();
		if(event.updateEvent(name, date, location, description, organizerId, eventId)) {
			errorCallback.displayError("Success");
		}else {
			errorCallback.displayError("Failed");
		}
		
		
	}
	public String CheckEditEventInput(String name,String date,String location, String description ) {
		if(name.isEmpty()) {
			return "Event Name must not be empty";
		}
		if (date.isEmpty()) {
	        return "Date must not be empty";
	    } else {
	        try {
	            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	            LocalDate eventDate = LocalDate.parse(date, formatter);
	            LocalDate today = LocalDate.now();
	            if (!eventDate.isAfter(today)) {
	                return "Date must be after today's date";
	            }
	        } catch (DateTimeParseException e) {
	            return "Date format is invalid. Use 'dd-MM-yyyy'.";
	        }
	    }
		if(location.isEmpty()||location.length()<5) {
			return "Location must be atleast 5 characters long";
		}
		if(description.isEmpty()) {
			return "Description must not be empty";
		}else if(description.length()>200) {
			return "Description must not be more than 200 characters";
		}
		return null;
	}
}
