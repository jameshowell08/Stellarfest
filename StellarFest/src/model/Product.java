package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import database.Connect;

public class Product {
	
	private String product_id;
	private String product_name;
	private String product_description;
	private String vendor_id;
	
	public String getVendor_id() {
		return vendor_id;
	}

	public void setVendor_id(String vendor_id) {
		this.vendor_id = vendor_id;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getProduct_description() {
		return product_description;
	}

	public void setProduct_description(String product_description) {
		this.product_description = product_description;
	}

	private static Connect connect = Connect.getInstance();
	
	
	
	public Vector<Product> getProducts(String userId){
		Vector<Product> products = new Vector<>();
		String query = "SELECT * FROM product WHERE vendor_id = ?;";
		PreparedStatement pst = connect.prepareStatement(query);
		try {
			pst.setString(1, userId);
			ResultSet rs = pst.executeQuery();
			while(rs.next()) {
				Product product = new Product();
				product.setProduct_id(rs.getString("product_id"));
				product.setProduct_name(rs.getString("product_name"));
				product.setProduct_description(rs.getString("product_description"));
				products.add(product);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return products;
	}
	
	
}
