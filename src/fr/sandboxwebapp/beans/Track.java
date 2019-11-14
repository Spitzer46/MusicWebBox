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
	private String type;
	private float duration;
	
	public Track () {}
	
	public static void save (Connection con, Track track, User user) throws SQLException {
		String query = "SELECT id FROM users WHERE username = ?";
		PreparedStatement stmt = con.prepareStatement (query);
		stmt.setString (1, user.getUsername ());
		ResultSet results = stmt.executeQuery ();
		if (results.next ()) {
			query = "INSERT INTO tracks (userId, track, title, lastUpdate, type, duration) ";
			query += "VALUES (?, ?, ?, NOW(), ?, ?) ON DUPLICATE KEY UPDATE ";
			query += "track = VALUES(track), ";
			query += "lastUpdate = VALUES(lastUpdate), ";
			query += "type = VALUES(type), ";
			query += "duration = VALUES(duration)";
			stmt = con.prepareStatement (query);
			stmt.setInt (1, results.getInt ("id"));
			stmt.setBlob (2, track.getDataIn ());
			stmt.setString (3, track.getTitle ());
			stmt.setString (4, track.getType ());
			stmt.setFloat (5, track.getDuration ());
			stmt.execute ();
		}
		results.close ();
		stmt.close ();
	}
	
	public static List<Track> getNextTraks (Connection con, User user, final int start, final int limit) throws SQLException {
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
			results.close ();
			results = stmt.executeQuery ();
			while (results.next ()) {
				Track track = new Track ();
				track.setId (results.getInt ("id"));
				track.setUserId (userId);
				track.setDataIn (null);
				track.setTitle (results.getString ("title"));
				track.setType (null);
				track.setDuration (0.0f);
				tracks.add (track);
			}
		}
		results.close ();
		stmt.close ();
		return tracks;
	}
	
	public static Track getTrack (Connection con, final int id) throws SQLException {
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
			track.setType (result.getString ("type"));
			track.setDuration (result.getFloat ("duration"));
			return track;
		}
		result.close ();
		stmt.close ();
		return null;
	}
	
	public byte [] getNextChunk () throws Exception {
		final int available = dataIn.available ();
		if (available == 0) {
			return null;
		}
		byte [] buffer = new byte [available >= CHUNK_SIZE ? CHUNK_SIZE : available];
		if (dataIn.read (buffer) == -1) {
			throw new Exception ("Chunk is empty");
		}
		return buffer;
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
			return String.format ("id : %d, %s, %s, %f secondes, %d byte available(s)", id, title, type, duration, dataIn.available ());
		}
		catch (IOException e) {
			return String.format ("id : %d, %s, %s", id, title, type);
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

	public String getType () {
		return type;
	}

	public void setType (String type) {
		this.type = type;
	}

	public float getDuration () {
		return duration;
	}

	public void setDuration (float duration) {
		this.duration = duration;
	}
	
}
