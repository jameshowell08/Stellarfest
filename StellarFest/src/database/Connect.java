package database;

import java.sql.*;

public class Connect {
	private static final String USERNAME = "root";
	private static final String PASSWORD = "";
	private static final String DATABASE = "stellarfest";
	private static final String HOST = "localhost:3306";
	private static final String CONNECTION = String.format("jdbc:mysql://%s/%s", HOST, DATABASE);
	
	private Connection con;
	private Statement st;
	
	private Connect() {
		try {
			con = DriverManager.getConnection(CONNECTION,USERNAME,PASSWORD);
			st = con.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static Connect instance = null;
	
	public static Connect getInstance() {
		if(instance == null) {
			return new Connect();
		}
		return instance;
	}
	
	public ResultSet rs;
	public ResultSetMetaData rsm;
	
	public ResultSet execQuery(String query) {
		try {
			rs = st.executeQuery(query);
			rsm = rs.getMetaData();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	public void execUpdate(String query) {
		try {
			st.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public PreparedStatement prepareStatement(String query) {
		PreparedStatement ps = null;
		try {
			ps=con.prepareStatement(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ps;
	}
	
	
}
