package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import database.Connect;

public class Event {
	private String event_id;
	private String event_name;
	private String event_date;
	private String event_location;
	private String event_description;
	private String organizer_id;
	private static Connect connect = Connect.getInstance();
	public String getEvent_id() {
		return event_id;
	}
	public void setEvent_id(String event_id) {
		this.event_id = event_id;
	}
	public String getEvent_name() {
		return event_name;
	}
	public void setEvent_name(String event_name) {
		this.event_name = event_name;
	}
	public String getEvent_date() {
		return event_date;
	}
	public void setEvent_date(String event_date) {
		this.event_date = event_date;
	}
	public String getEvent_location() {
		return event_location;
	}
	public void setEvent_location(String event_location) {
		this.event_location = event_location;
	}
	public String getEvent_description() {
		return event_description;
	}
	public void setEvent_description(String event_description) {
		this.event_description = event_description;
	}
	public String getOrganizer_id() {
		return organizer_id;
	}
	public void setOrganizer_id(String organizer_id) {
		this.organizer_id = organizer_id;
	}
	public boolean createEvent(String name, String date, String location, String description, String organizerId) {
		String query = "INSERT INTO event VALUES (?, ?, ?, ?, ?, ?);";
		PreparedStatement pst = connect.prepareStatement(query);
		try {
			pst.setString(1, generateEventId());
			pst.setString(2, name);
			pst.setString(3, date);
			pst.setString(4, location);
			pst.setString(5, description);
			pst.setString(6, organizerId);
			pst.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean updateEvent(String name, String date, String location, String description, String organizerId, String eventId) {
		String query = "UPDATE event SET event_name = ? , event_date = ?, event_location =?, event_description = ?, organizer_id = ? WHERE event_id = ?;";
		PreparedStatement pst = connect.prepareStatement(query);
		try {
			pst.setString(1, name);
			pst.setString(2, date);
			pst.setString(3, location);
			pst.setString(4, description);
			pst.setString(5, organizerId);
			pst.setString(6, eventId);
			pst.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public Event getEventById(String eventId) {
		String query = "SELECT * FROM event WHERE event_id = ?";
		PreparedStatement pst = connect.prepareStatement(query);
		try {
			pst.setString(1, eventId);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				Event event = new Event();
				event.setEvent_id(rs.getString("event_id"));
				event.setEvent_name(rs.getString("event_name"));
				event.setEvent_date(rs.getString("event_date"));
				event.setEvent_location(rs.getString("event_location"));
				event.setEvent_description(rs.getString("event_description"));
				event.setOrganizer_id(rs.getString("organizer_id"));

				return event;
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	private static String getLastEventId() {
		String query = "SELECT MAX(event_id) AS latest_id FROM Event";
		PreparedStatement pst = connect.prepareStatement(query);
		try {
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				return rs.getString("latest_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String generateEventId() {
		String latestEventId = getLastEventId();
		int newId;
		if (latestEventId == null || latestEventId.isEmpty()) {
			newId = 1;
		} else {
			int latestId = Integer.parseInt(latestEventId.substring(1));
			newId = latestId + 1;
		}
		return String.format("E%03d", newId);
	}

	public Vector<Event> getAccEvents(String email) {
		Vector<Event> events = new Vector<>();
		String query = "SELECT * FROM event e LEFT JOIN invitation iv ON e.event_id = iv.event_id LEFT JOIN user u ON iv.user_id = u.user_id  WHERE u.user_email = ? AND iv.invitation_status = ?;";
		//String query = "SELECT * FROM event e LEFT JOIN invitation iv ON e.event_id = iv.event_id AND iv.user_id = ? WHERE iv.invitation_status = ?;";
		//String query = "SELECT * FROM user u LEFT JOIN invitation iv ON u.user_id = iv.user_id LEFT JOIN event e ON iv.event_id = e.event_id AND u.user_email = ? WHERE iv.invitation_status = ?;";
		
		PreparedStatement pst = connect.prepareStatement(query);
		try {
			pst.setString(1, email);
			pst.setString(2, "Accepted");
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Event event = new Event();
				event.setEvent_id(rs.getString("event_id"));
				event.setEvent_name(rs.getString("event_name"));
				event.setEvent_date(rs.getString("event_date"));
				event.setEvent_location(rs.getString("event_location"));
				event.setEvent_description(rs.getString("event_description"));
				event.setOrganizer_id(rs.getString("organizer_id"));
				events.add(event);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return events;
	}
}
