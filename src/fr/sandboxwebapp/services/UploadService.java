package fr.sandboxwebapp.services;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import javax.servlet.http.HttpServletRequest;
import org.apache.tomcat.util.http.fileupload.FileItemIterator;
import org.apache.tomcat.util.http.fileupload.FileItemStream;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import fr.sandboxwebapp.beans.Track;
import fr.sandboxwebapp.beans.User;
import fr.sandboxwebapp.utils.Utils;

public class UploadService extends Service {
	
	public UploadService (Connection con) {
		super (con);
	}
	
	public void upload (HttpServletRequest req, User user) {
		try {
			if (ServletFileUpload.isMultipartContent (req)) {
				Track track = new Track ();
				ServletFileUpload fileUpload = new ServletFileUpload ();
				FileItemIterator items = fileUpload.getItemIterator (req);
		        while (items.hasNext ()) {
		        	FileItemStream item = items.next ();
		            if (item.isFormField ()) {
		            	if (item.getFieldName ().equals ("duration")) {
		            	   track.setDuration (inputStreamToFloat (item.openStream ()));
		                }
		            } else {
		            	track.setTitle (getTrackTitle (item.getName ()));
		            	track.setType (getTrackType (item.getContentType ()));
		            	track.setDataIn (Utils.clone (item.openStream ()));
		            }
		        }
            	Track.save (con, track, user);
			}
		}
		catch (Exception e) {
			errors.add (e);
		}
	}
	
	private String getTrackTitle (String fullName) {
		final int liod = fullName.lastIndexOf (".");
		if (liod == -1) {
			return fullName;
		}
		return fullName.substring (0, liod);
	}
	
	private String getTrackType (String contentType) throws Exception {
		String [] part = contentType.split ("/");
		if (part.length != 2) {
			throw new Exception ("Content type is incorect, actually type is : " + contentType);
		}
		return part [1];
	}
	
	private float inputStreamToFloat (InputStream is) {
		try (BufferedReader reader = new BufferedReader (new InputStreamReader (is, "UTF8"))) {
			return Float.parseFloat (reader.readLine ());
		}
		catch (Exception e) {
			errors.add (e);
			return 0.0f;
		}
	}
	
}
