package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import database.Connect;

public class Vendor extends User{
	private static Connect connect = Connect.getInstance();
	
	public Vector<Vendor> getVendors(String eventId){
		Vector<Vendor> vendors = new Vector<>();
		String query = "SELECT * FROM user u LEFT JOIN invitation iv ON u.user_id = iv.user_id AND iv.event_id = ? WHERE u.user_role = ? AND iv.user_id IS NULL;";
		PreparedStatement pst = connect.prepareStatement(query);
		try {
		
			pst.setString(1, eventId);
			pst.setString(2, "Vendor");
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
	public boolean createProduct(String name, String description,String userId) {
		String query = "INSERT INTO product VALUES (?, ?, ?, ?);";
		PreparedStatement pst = connect.prepareStatement(query);
		try {
			pst.setString(1, generateProductId());
			pst.setString(2, name);
			pst.setString(3, description);
			pst.setString(4, userId);
			pst.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	private static String getLastProductId() {
		String query =  "SELECT MAX(product_id) AS latest_id FROM Product";
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
	
	private static String generateProductId() {
		String latestUserId = getLastProductId();
		int newId;
        if (latestUserId == null || latestUserId.isEmpty()) {
            newId = 1;
        } else {
            int latestId = Integer.parseInt(latestUserId.substring(1));
            newId = latestId + 1;
        }
        return String.format("P%03d", newId);
	}
}
