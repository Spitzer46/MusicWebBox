package fr.sandboxwebapp.services;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import org.apache.tomcat.util.http.fileupload.FileItemIterator;
import org.apache.tomcat.util.http.fileupload.FileItemStream;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import fr.sandboxwebapp.beans.Track;
import fr.sandboxwebapp.beans.User;

public class UploadService extends Service {

	private Connection con;
	
	public UploadService (Connection connection) {
		super ();
		this.con = connection;
	}
	
	public void upload (HttpServletRequest req, User user) {
		try {
			if (ServletFileUpload.isMultipartContent (req)) {
				ServletFileUpload fileUpload = new ServletFileUpload ();
				FileItemIterator items = fileUpload.getItemIterator (req);
				while (items.hasNext ()) {
					FileItemStream item = items.next ();
			        if (!item.isFormField ()) {
			        	Track track = new Track ();
			        	track.setDataIn (item.openStream ());
			        	track.setTitle (item.getName ());
			        	Track.create (con, track, user);
			        }
				}
			}
		}
		catch (Exception e) {
			errors.add (e.toString ());
		}
	}
	
}
