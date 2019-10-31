package fr.sandboxwebapp.beans;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

public class Track {
	
	public static final int CHUNK_SIZE = 65536;
	
	private int id;
	private int userId;
	private InputStream dataIn;
	private String title;
	
	public Track () {}
	
	public static void create (Connection con, Track track, User user) throws SQLException {
		String query = "SELECT id FROM users WHERE username = ?";
		PreparedStatement stmt = con.prepareStatement (query);
		stmt.setString (1, user.getUsername ());
		ResultSet results = stmt.executeQuery ();
		if (results.next ()) {
			query = "INSERT INTO tracks VALUES (DEFAULT, ?, ?, ?, NOW()) ON DUPLICATE KEY UPDATE track = track";
			stmt = con.prepareStatement (query);
			stmt.setInt (1, results.getInt ("id"));
			stmt.setBlob (2, track.getDataIn ());
			stmt.setString (3, track.getTitle ());
			stmt.executeUpdate ();
		}
	}
	
	public static List<Track> getNextTraks (Connection con, User user, int start, int limit) throws SQLException {
		List<Track> tracks = new ArrayList<>();
		String query = "SELECT id FROM users WHERE username = ?";
		PreparedStatement stmt = con.prepareStatement (query);
		stmt.setString (1, user.getUsername ());
		ResultSet results = stmt.executeQuery ();
		if (results.next ()) {
			query = "SELECT id, title FROM tracks WHERE userId = ? AND id < ? ORDER BY lastUpdate DESC LIMIT ?";
			stmt = con.prepareStatement (query);
			final int userId = results.getInt ("id");
			stmt.setInt (1, userId);
			stmt.setInt(2, start);
			stmt.setInt(3, limit);
			results = stmt.executeQuery ();
			while (results.next ()) {
				Track track = new Track ();
				track.setId (results.getInt ("id"));
				track.setUserId (userId);
				track.setDataIn (null);
				track.setTitle (results.getString ("title"));
				tracks.add (track);
			}
		}
		return tracks;
	}
	
	public static Track getTrack (Connection con, int id) throws SQLException {
		String query = "SELECT * FROM tracks WHERE id = ?";
		PreparedStatement stmt = con.prepareStatement (query);
		stmt.setInt (1, id);
		ResultSet result = stmt.executeQuery ();
		if (result.next ()) {
			Track track = new Track ();
			track.setId (id);
			track.setUserId (result.getInt ("userId"));
			track.setDataIn (result.getBinaryStream ("track"));
			track.setTitle (result.getString ("title"));
			return track;
		}
		return null;
	}
	
	public void loadTrack (Connection con) throws SQLException {
		String query = "SELECT track FROM tracks WHERE id = ?";
		PreparedStatement stmt = con.prepareStatement (query);
		stmt.setInt (1, id);
		ResultSet results = stmt.executeQuery ();
		if (results.next ()) {
			dataIn = results.getBinaryStream ("track");
		}
	}
	
	public JSONObject toJSON () {
		JSONObject json = new JSONObject ();
		json.put ("id", id);
		json.put ("title", title);
		return json;
	}
	
	@Override
	public String toString () {
		try {
			return String.format ("id : %d, %s, %d byte available(s)", id, title, dataIn.available ());
		}
		catch (IOException e) {
			return String.format ("id : %d, %s", id, title);
		}
	}

	public int getId () {
		return id;
	}

	public void setId (int id) {
		this.id = id;
	}

	public int getUserId () {
		return userId;
	}

	public void setUserId (int userId) {
		this.userId = userId;
	}

	public InputStream getDataIn () {
		return dataIn;
	}

	public void setDataIn (InputStream dataIn) {
		this.dataIn = dataIn;
	}

	public String getTitle () {
		return title;
	}

	public void setTitle (String title) {
		this.title = title;
	}

}
