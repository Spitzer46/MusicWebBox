package fr.sandboxwebapp.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import fr.sandboxwebapp.utils.Utils;

public class TokenUrl {

	private String tokenUrl;
	private Timestamp timestampCreation;
	
	public TokenUrl () {
		timestampCreation = new Timestamp (System.currentTimeMillis ());
	}
	
	public static void save (Connection con, TokenUrl tokenUrl, String username) throws Exception {
		String query = "INSERT INTO tokenurltable (tokenUrl, dateTimeCreation, username) ";
		query += "VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE ";
		query += "tokenUrl = VALUES(tokenUrl), ";
		query += "dateTimeCreation = VALUES(dateTimeCreation), ";
		query += "username = VALUES(username)";
		try (PreparedStatement stmt = con.prepareStatement (query)) {
			stmt.setString (1, tokenUrl.getTokenUrl ());
			stmt.setTimestamp (2, tokenUrl.getTimestampCreation ());
			stmt.setString (3, username);
			stmt.executeUpdate ();
		}
	}
	
	public static TokenUrl get (Connection con, User user) throws SQLException {
		String query = "SELECT * FROM tokenurltable WHERE username = ?";
		try (PreparedStatement stmt = con.prepareStatement (query)) {
			stmt.setString (1, user.getUsername ());
			try (ResultSet result = stmt.executeQuery ()) {
				if (result.next ()) {
					TokenUrl tokenUrl = new TokenUrl ();
					tokenUrl.setTokenUrl (result.getString ("tokenUrl"));
					tokenUrl.setTimestampCreation (result.getTimestamp ("dateTimeCreation"));
					return tokenUrl;
				}
			}
		}
		return null;
	}
	
	public static Timestamp fetchTimestampByTokenUrl (Connection con, String tokenUrl) throws SQLException {
		String query = "SELECT dateTimeCreation FROM tokenurltable WHERE tokenUrl = ?";
		try (PreparedStatement stmt = con.prepareStatement (query)) {
			stmt.setString (1, tokenUrl);
			try (ResultSet result = stmt.executeQuery ()) {
				if (result.next ()) {
					return result.getTimestamp ("dateTimeCreation");
				}
			}
		}
		return null;
	}
	
	public void genTokenUrl () {
		tokenUrl = Utils.genUUID ();
	}

	public String getTokenUrl () {
		return tokenUrl;
	}

	public void setTokenUrl (String tokenUrl) {
		this.tokenUrl = tokenUrl;
	}

	public Timestamp getTimestampCreation () {
		return timestampCreation;
	}

	public void setTimestampCreation (Timestamp timestampCreation) {
		this.timestampCreation = timestampCreation;
	}

	
}
