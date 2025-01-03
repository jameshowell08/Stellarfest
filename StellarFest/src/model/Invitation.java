package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;


import database.Connect;

public class Invitation {
	private String invitation_id;
	private String event_id;
	private String user_id;
	private String invitation_status;
	private String invitation_role;
	
	private static Connect connect = Connect.getInstance();
	
	
	public String getInvitation_id() {
		return invitation_id;
	}
	public void setInvitation_id(String invitation_id) {
		this.invitation_id = invitation_id;
	}
	public String getEvent_id() {
		return event_id;
	}
	public void setEvent_id(String event_id) {
		this.event_id = event_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getInvitation_status() {
		return invitation_status;
	}
	public void setInvitation_status(String invitation_status) {
		this.invitation_status = invitation_status;
	}
	public String getInvitation_role() {
		return invitation_role;
	}
	public void setInvitation_role(String invitation_role) {
		this.invitation_role = invitation_role;
	}
	
	public boolean sendInvitation(String event_id, String user_id, String invitation_role) {
		String query = "INSERT INTO invitation VALUES(?,?,?,?,?);";
		PreparedStatement pst = connect.prepareStatement(query);
		try {
			pst.setString(1, generateInvitationId());
			pst.setString(2, event_id);
			pst.setString(3, user_id);
			pst.setString(4, "Pending");
			pst.setString(5, invitation_role);
			pst.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private static String getLastInvitationId() {
		String query =  "SELECT MAX(invitation_id) AS latest_id FROM Invitation";
		PreparedStatement pst = connect.prepareStatement(query);
		try {
			ResultSet rs = pst.executeQuery();
			if(rs.next()) {
				return rs.getString("latest_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static String generateInvitationId() {
		String latestInvitationId = getLastInvitationId();
		int newId;
        if (latestInvitationId == null || latestInvitationId.isEmpty()) {
            newId = 1;
        } else {
            int latestId = Integer.parseInt(latestInvitationId.substring(1));
            newId = latestId + 1;
        }
        return String.format("I%03d", newId);
	}
	
	public Vector<Event> getInvitation(String email){
		Vector<Event> invitations = new Vector<>();
		String query = "SELECT * FROM event e LEFT JOIN invitation iv ON e.event_id = iv.event_id LEFT JOIN user u ON iv.user_id = u.user_id  WHERE u.user_email = ? AND iv.invitation_status = ?;";
		//String query = "SELECT * FROM event e LEFT JOIN invitation iv ON e.event_id = iv.event_id AND iv.user_id = ? WHERE iv.invitation_status = ?;";
		//String query = "SELECT * FROM user u LEFT JOIN invitation iv ON u.user_id = iv.user_id LEFT JOIN event e ON iv.event_id = e.event_id AND u.user_email = ? WHERE iv.invitation_status = ?;";
		PreparedStatement pst = connect.prepareStatement(query);
		try {
			pst.setString(1, email);
			pst.setString(2, "Pending");
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				 Event event = new Event();
				 event.setEvent_id(rs.getString("event_id"));
				 event.setEvent_name(rs.getString("event_name"));
				 event.setEvent_date(rs.getString("event_date"));
				 event.setEvent_location(rs.getString("event_location"));
				 event.setEvent_description(rs.getString("event_description"));
				 event.setOrganizer_id(rs.getString("organizer_id"));
				 invitations.add(event);
                
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return invitations;
	}
	
	public boolean acceptInv(String userId, String eventId) {
		String query = "UPDATE invitation SET invitation_status = ? WHERE user_id = ? AND event_id = ?;";
		PreparedStatement pst = connect.prepareStatement(query);
		try {
			pst.setString(1, "Accepted");
			pst.setString(2, userId);
			pst.setString(3, eventId);
			pst.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
}
