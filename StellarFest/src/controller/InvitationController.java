package controller;

import java.util.Vector;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Event;
import model.Invitation;

public class InvitationController {
	private Invitation invitation = new Invitation();
//	private ValidationErrorCallback errorCallback;
//	public InvitationController(ValidationErrorCallback errorCallback) {
//		this.errorCallback = errorCallback;
//	}
//	public interface ValidationErrorCallback {
//        void displayError(String errorMessage);
//    }
	public ObservableList<Event> getInvitations(String email){
		Vector<Event> temp = invitation.getInvitation(email);
		ObservableList<Event> evntObs = FXCollections.observableArrayList(temp);
		return evntObs;
	}
}
