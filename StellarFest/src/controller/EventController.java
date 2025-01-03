package controller;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import model.Event;

public class EventController {
	private ValidationErrorCallback errorCallback;
	public EventController(ValidationErrorCallback errorCallback) {
		this.errorCallback = errorCallback;
	}
	public interface ValidationErrorCallback {
        void displayError(String errorMessage);
    }
	public String CheckCreateEventInput(String name,String date,String location, String description ) {
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
	public void createEvent(String name,String date,String location, String description , String organizerId) {
		String validationError = CheckCreateEventInput(name, date, location, description);
		if (validationError != null) {
            if (errorCallback != null) {
                errorCallback.displayError(validationError);  // Send the error back to the view
            }
            return;
        }
		Event event = new Event();
		if(event.createEvent(name, date, location, description, organizerId)) {
			errorCallback.displayError("Success Created Event");
		}else {
			errorCallback.displayError("Failed Created Event");
		}
		
		
	}
}
