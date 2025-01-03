package controller;

import java.util.Vector;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Event;
import model.Invitation;

public class GuestController {
	private Invitation invitation = new Invitation();
	private Event event = new Event();
	
	private ValidationErrorCallback errorCallback;
	public GuestController(ValidationErrorCallback errorCallback) {
		this.errorCallback = errorCallback;
	}
	public interface ValidationErrorCallback {
        void displayError(String errorMessage);
    }
	public void accInvitation(String userId, String eventId) {
		if(invitation.acceptInv(userId, eventId)) {
			errorCallback.displayError("Invitation Accepted");
		}else {
			errorCallback.displayError("Invitation Failed to Accept");
		}
	}
	public ObservableList<Event> getAcceptedInvitations(String email){
		Vector<Event> temp = event.getAccEvents(email);
		ObservableList<Event> evntObs = FXCollections.observableArrayList(temp);
		return evntObs;
	}
}