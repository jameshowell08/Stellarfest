package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import database.Connect;

public class EventOrganizer extends User {
	private static Connect connect = Connect.getInstance();

	public Vector<Event> viewOrganizedEvents(String organizerID){
		Vector<Event> events = new Vector<>();
		String query = "SELECT * FROM event WHERE organizer_id = ?";
		PreparedStatement pst = connect.prepareStatement(query);
		try {
			pst.setString(1, organizerID);
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
	
	public Vector<Guest> getGuestAttendeeByEventId(String eventId) {
		Vector<Guest> guests = new Vector<>();
		String query = "SELECT * FROM user u LEFT JOIN invitation iv ON u.user_id = iv.user_id AND iv.event_id = ? WHERE u.user_role = ? AND iv.invitation_status = ?;";
		PreparedStatement pst = connect.prepareStatement(query);
		try {
			pst.setString(1, eventId);
			pst.setString(2, "Guest");
			pst.setString(3, "Accepted");
			ResultSet rs = pst.executeQuery();
			while(rs.next()) {
				Guest guest = new Guest();
				guest.setUser_email(rs.getString("user_email"));
				guest.setUser_name(rs.getString("user_name"));
				guest.setUser_password(rs.getString("user_password"));
				guest.setUser_role(rs.getString("user_role"));
				guest.setUser_id(rs.getString("user_id"));
				guests.add(guest);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return guests;
	}
	public Vector<Vendor> getVendorAttendeeByEventId(String eventId) {
		Vector<Vendor> vendors = new Vector<>();
		String query = "SELECT * FROM user u LEFT JOIN invitation iv ON u.user_id = iv.user_id AND iv.event_id = ? WHERE u.user_role = ? AND iv.invitation_status = ?;";
		PreparedStatement pst = connect.prepareStatement(query);
		try {
			pst.setString(1, eventId);
			pst.setString(2, "Vendor");
			pst.setString(3, "Accepted");
			ResultSet rs = pst.executeQuery();
			while(rs.next()) {
				Vendor vendor = new Vendor();
				vendor.setUser_email(rs.getString("user_email"));
				vendor.setUser_name(rs.getString("user_name"));
				vendor.setUser_password(rs.getString("user_password"));
				vendor.setUser_role(rs.getString("user_role"));
				vendor.setUser_id(rs.getString("user_id"));
				vendors.add(vendor);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return vendors;
	}
}
