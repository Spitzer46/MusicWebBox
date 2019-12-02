package fr.sandboxwebapp.services;

import java.sql.Connection;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import fr.sandboxwebapp.beans.Track;
import fr.sandboxwebapp.beans.User;

public class AudioPlayerService extends Service {
	
	public AudioPlayerService (Connection con) {
		super (con);
	}
	
	@SuppressWarnings("unchecked")
	public void nextTrack (HttpServletRequest req, HttpServletResponse resp, User user) {
		try (ServletOutputStream sos = resp.getOutputStream ()) {
			final int startId = Integer.parseInt ((String) req.getParameter ("start"));
			if (startId < 0) {
				throw new Exception ("StartId must not be less than zero");
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
			errors.add (e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void loadTrack (HttpServletRequest req, HttpServletResponse resp, User user) {
		try (ServletOutputStream sos = resp.getOutputStream ()) {
			final int trackId = Integer.parseInt ((String) req.getParameter ("id"));
			Track lastTrack = Track.getTrack (con, trackId);
			if (lastTrack == null) {
				throw new Exception ("Track with this id was no found");
			}
			user.setLastTrack (lastTrack);
			JSONObject infos = new JSONObject ();
			infos.put ("duration", lastTrack.getDuration ());
			sos.print (infos.toJSONString ());
		}
		catch (Exception e) {
			errors.add (e);
		}
	}
	
	public void loadChunk (HttpServletResponse resp, User user) {
		try (ServletOutputStream sos = resp.getOutputStream ()) {
			Track track = user.getLastTrack ();
			byte [] chunk = track.getNextChunk ();
			if (chunk != null) {
				sos.write (chunk);
			}
		}
		catch (Exception e) {
			errors.add (e);
		}
	}
	
}
