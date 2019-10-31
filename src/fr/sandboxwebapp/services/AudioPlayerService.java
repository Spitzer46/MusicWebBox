package fr.sandboxwebapp.services;

import java.sql.Connection;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import fr.sandboxwebapp.beans.Track;
import fr.sandboxwebapp.beans.User;

public class AudioPlayerService extends Service {

	private Connection con;
	
	public AudioPlayerService (Connection con) {
		super ();
		this.con = con;
	}
	
	public void nextTrack (HttpServletRequest req, HttpServletResponse resp, User user) {
		try (ServletOutputStream sos = resp.getOutputStream ()) {
			final int startId = Integer.parseInt ((String) req.getParameter ("start"));
			if (startId < 0) {
				throw new Exception ("StartId must not be lass than zero");
			}
			final int limit = Integer.parseInt ((String) req.getParameter ("limit"));
			if (limit <= 0) {
				throw new Exception ("Limit must not be equal to zero or less");
			}
			List<Track> tracks = Track.getNextTraks (con, user, startId, limit);
			JSONArray jsonTracks = new JSONArray ();
			for (Track track : tracks) {
				jsonTracks.add (track.toJSON ());
			}
			sos.print (jsonTracks.toJSONString ());
		}
		catch (Exception e) {
			errors.add (e.toString ());
		}
	}
	
	public void loadTrack (HttpServletRequest req, HttpServletResponse resp, User user) {
		try (ServletOutputStream sos = resp.getOutputStream ()) {
			final int trackId = Integer.parseInt ((String) req.getParameter ("id"));
			Track lastTrack = Track.getTrack (con, trackId);
			if (lastTrack == null) {
				throw new Exception ("Track with this id was no found");
			}
			user.setLastTrack (lastTrack);
			sos.print (lastTrack.getDataIn ().available ());
		}
		catch (Exception e) {
			errors.add (e.toString ());
		}
	}
	
}
