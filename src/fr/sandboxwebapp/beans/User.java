package fr.sandboxwebapp.beans;

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
	private boolean enabled;
	private Track lastTrack;
	
	public User () {}
	
	public static int getIdByUsername (Connection con, String username) throws Exception {
		String query = "SELECT id FROM users WHERE username = ?";
		try (PreparedStatement stmt = con.prepareStatement (query)) {
			stmt.setString (1, username);
			try (ResultSet result = stmt.executeQuery ()) {
				if (result.next ()) {
					return result.getInt ("id");
				}
				else {
					throw new Exception ("No user id has been found by this username");
				}
			}
		}
	}
	
	public static User getUser (Connection con, String username, String password) throws Exception {
		String query = "SELECT * FROM users WHERE username = ? AND password = ?";
		try (PreparedStatement stmt = con.prepareStatement (query)) {
			stmt.setString (1, username);
			stmt.setString (2, Utils.md5 (password));
			try (ResultSet result = stmt.executeQuery ()) {
				if (result.next ()) {
					User user = new User ();
					user.setUsername (username);
					user.setPassword (result.getString ("password"));
					user.setFirstname (result.getString ("firstname"));
					user.setLastname (result.getString ("lastname"));
					user.setEmail (result.getString ("email"));
					user.setEnabled (result.getBoolean ("enabled"));
					return user;
				}
			}
		}
		return null;
	}
	
	public static User getUserByTokenUrl (Connection con, String tokenUrl) throws SQLException {
		String query = "SELECT username, password, firstname, lastname, email, enabled FROM users u \n";
		query += "WHERE u.username = (SELECT username FROM tokenurltable t WHERE t.tokenUrl = ?);";
		try (PreparedStatement stmt = con.prepareStatement (query)) {
			stmt.setString (1, tokenUrl);
			try (ResultSet result = stmt.executeQuery ()) {
				if (result.next ()) {
					User user = new User ();
					user.setUsername (result.getString ("username"));
					user.setPassword (result.getString ("password"));
					user.setFirstname (result.getString ("firstname"));
					user.setLastname (result.getString ("lastname"));
					user.setEmail (result.getString ("email"));
					user.setEnabled (result.getBoolean ("enabled"));
					return user;
				}
			}
		}
		return null;
	}
	
	public static boolean userExist (Connection con, String username) throws SQLException {
		String query = "SELECT id FROM users WHERE username = ?";
		try (PreparedStatement stmt = con.prepareStatement (query)) {
			stmt.setString (1, username);
			try (ResultSet results = stmt.executeQuery ()) {
				if (results.next ()) 
					return true;
			}
		}
		return false;
	}
	
	public static boolean userExistAndActif (Connection con, String username, String email) throws SQLException {
		String query = "SELECT id FROM users WHERE username = ? AND email = ? AND enabled = 1";
		try (PreparedStatement stmt = con.prepareStatement (query)) {
			stmt.setString (1, username);
			stmt.setString (2, email);
			try (ResultSet results = stmt.executeQuery ()) {
				if (results.next ())
					return true;
			}
		}
		return false;
	}
	
	public static void save (Connection con, User user) throws SQLException {
		String query = "INSERT INTO users (username, password, firstname, lastname, email, enabled) ";
		query += "VALUES (?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE ";
		query += "username = VALUES(username),";
		query += "password = VALUES(password),";
		query += "firstname = VALUES(firstname),";
		query += "lastname = VALUES(lastname),";
		query += "email = VALUES(email),";
		query += "enabled = VALUES(enabled)";
		try (PreparedStatement stmt = con.prepareStatement (query)) {
			stmt.setString (1, user.getUsername ());
			stmt.setString (2, user.getPassword ());
			stmt.setString (3, user.getFirstname ());
			stmt.setString (4, user.getLastname ());
			stmt.setString (5, user.getEmail ());
			stmt.setBoolean (6, user.isEnabled ());
			stmt.executeUpdate ();
		}
	}
	
	public static void setEnabledByTokenUrl (Connection con, boolean enabled, String tokenUrl) throws SQLException {
		String query = "UPDATE users SET enabled = ? \n";
		query += "WHERE username = (SELECT username FROM tokenurltable WHERE tokenUrl = ?)";
		try (PreparedStatement stmt = con.prepareStatement (query)) {
			stmt.setBoolean (1, enabled);
			stmt.setString (2, tokenUrl);
			stmt.executeUpdate ();
		}
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

	public boolean isEnabled () {
		return enabled;
	}

	public void setEnabled (boolean enabled) {
		this.enabled = enabled;
	}
	
	public Track getLastTrack () {
		return lastTrack;
	}

	public void setLastTrack (Track lastTrack) {
		this.lastTrack = lastTrack;
	}
	
}
