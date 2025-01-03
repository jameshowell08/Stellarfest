package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import database.Connect;

public class Guest extends User{
	private static Connect connect = Connect.getInstance();
	
	public Vector<Guest> getGuests(String eventId){
		Vector<Guest> guests = new Vector<>();
		String query = "SELECT * FROM user u LEFT JOIN Invitation iv ON u.user_id = iv.user_id AND iv.event_id = ? WHERE u.user_role = ? AND iv.user_id IS NULL;";
		PreparedStatement pst = connect.prepareStatement(query);
		try {
		
			pst.setString(1, eventId);
			pst.setString(2, "Guest");
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
}
