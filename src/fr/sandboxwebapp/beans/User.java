package fr.sandboxwebapp.beans;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import fr.sandboxwebapp.utils.Utils;

public class User {

	private String username;
	private String password;
	private String firstname;
	private String lastname;
	private String email;
	
	private Track lastTrack;
	
	public User () {}
	
	public static User getUser (Connection con, String username, String password) throws SQLException, NoSuchAlgorithmException {
		String query = "SELECT * FROM users WHERE username = ? AND password = ?";
		User user = null;
		PreparedStatement stmt = con.prepareStatement (query);
		stmt.setString (1, username);
		stmt.setString (2, Utils.md5 (password));
		ResultSet results = stmt.executeQuery ();
		if (results.next ()) {
			user = new User ();
			user.setUsername (username);
			user.setPassword (results.getString ("password"));
			user.setFirstname (results.getString ("firstname"));
			user.setLastname (results.getString ("lastname"));
			user.setEmail (results.getString ("email"));
		}
		results.close ();
		stmt.close ();
		return user;
	}
	
	public static boolean userExist (Connection con, String username) throws SQLException {
		String query = "SELECT id FROM users WHERE username = ?";
		PreparedStatement stmt = con.prepareStatement (query);
		stmt.setString (1, username);
		ResultSet results = stmt.executeQuery ();
		boolean exist = false;
		if (results.next ()) {
			exist = true;
		}
		results.close ();
		stmt.close ();
		return exist;
	}
	
	public static void save (Connection con, User user) throws SQLException {
		String query = "INSERT INTO users (username, password, firstname, lastname, email) ";
		query += "VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE ";
		query += "username = VALUES(username), ";
		query += "firstname = VALUES(firstname), ";
		query += "lastname = VALUES(lastname), ";
		query += "email = VALUES(email)";
		PreparedStatement stmt = con.prepareStatement (query);
		stmt.setString (1, user.getUsername ());
		stmt.setString (2, user.getPassword ());
		stmt.setString (3, user.getFirstname ());
		stmt.setString (4, user.getLastname ());
		stmt.setString (5, user.getEmail ());
		stmt.executeUpdate ();
		stmt.close ();
	}

	public String getUsername () {
		return username;
	}

	public void setUsername (String username) {
		this.username = username;
	}
	
	public String getPassword () {
		return password;
	}

	public void setPassword (String password) {
		this.password = password;
	}

	public String getFirstname () {
		return firstname;
	}
	
	public void setFirstname (String firstname) {
		this.firstname = firstname;
	}
	
	public String getLastname () {
		return lastname;
	}
	
	public void setLastname (String lastname) {
		this.lastname = lastname;
	}
	
	public String getEmail () {
		return email;
	}
	
	public void setEmail (String email) {
		this.email = email;
	}

	public Track getLastTrack () {
		return lastTrack;
	}

	public void setLastTrack (Track lastTrack) {
		this.lastTrack = lastTrack;
	}
	
}
