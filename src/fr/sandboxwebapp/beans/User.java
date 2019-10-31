package fr.sandboxwebapp.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import fr.sandboxwebapp.utils.Utils;

public class User {

	private String username;
	private String firstname;
	private String lastname;
	private String email;
	
	private Track lastTrack;
	
	public User () {}
	
	public static User getUser (Connection con, String username, String password) throws SQLException {
		String query = "SELECT * FROM users WHERE username = ? AND hashPassword = ?";
		PreparedStatement stmt = con.prepareStatement (query);
		stmt.setString (1, username);
		stmt.setString (2, Utils.md5 (password));
		ResultSet results = stmt.executeQuery ();
		if (results.next ()) {
			User user = new User ();
			user.setUsername (username);
			user.setFirstname (results.getString ("firstname"));
			user.setLastname (results.getString ("lastname"));
			user.setEmail (results.getString ("email"));
			return user;
		}
		else return null;
	}
	
	public static boolean userExist (Connection con, String username) throws SQLException {
		String query = "SELECT * FROM users WHERE username = ?";
		PreparedStatement stmt = con.prepareStatement (query);
		stmt.setString (1, username);
		ResultSet results = stmt.executeQuery ();
		if (results.next ()) {
			return true;
		}
		return false;
	}
	
	public static void save (Connection con, User user) throws SQLException {
		String query = "UPDATE users"
			+ " SET username = ?, firstname = ?, lastname = ?, email = ?"
			+ " WHERE id = ?";
		PreparedStatement stmt = con.prepareStatement (query);
		stmt.setString (1, user.getUsername ());
		stmt.setString (2, user.getFirstname ());
		stmt.setString (3, user.getLastname ());
		stmt.setString (4, user.getEmail ());
		stmt.executeUpdate ();
	}
	
	public static void create (Connection con, String password, User user) throws SQLException {
		String query = "INSERT INTO users VALUES (DEFAULT, ?, ?, ?, ?, ?)";
		PreparedStatement stmt = con.prepareStatement (query);
		stmt.setString (1, user.getUsername ());
		stmt.setString (2, Utils.md5 (password));
		stmt.setString (3, user.getFirstname ());
		stmt.setString (4, user.getLastname ());
		stmt.setString (5, user.getEmail ());
		stmt.executeUpdate ();
	}

	public String getUsername () {
		return username;
	}

	public void setUsername (String username) {
		this.username = username;
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
