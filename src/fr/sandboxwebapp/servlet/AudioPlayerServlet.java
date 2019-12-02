package fr.sandboxwebapp.servlet;

import java.io.IOException;
import java.sql.Connection;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Level;
import java.util.logging.Logger;
import fr.sandboxwebapp.beans.User;
import fr.sandboxwebapp.services.AudioPlayerService;

public class AudioPlayerServlet extends RestrictedServlet {

	private static final long serialVersionUID = -4789164046846528770L;

	@Override
	protected void doGet (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher ("/WEB-INF/pages/restreint/home.jsp").forward (req, resp);
	}
	
	@Override
	protected void doPost (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Audio Player API
		ServletContext context = this.getServletContext ();
		User user = (User) req.getSession ().getAttribute ("userSession");
		AudioPlayerService audioPlayerService = new AudioPlayerService ((Connection) context.getAttribute ("con"));
		
		String uri = req.getRequestURI ();
		if (uri.equals ("/SandboxWebApp/player/api/nexttracks")) {
			audioPlayerService.nextTrack (req, resp, user);
		}
		else if (uri.equals ("/SandboxWebApp/player/api/loadtrack")) {
			audioPlayerService.loadTrack (req, resp, user);
		}
		else if (uri.equals ("/SandboxWebApp/player/api/readChunk")) {
			audioPlayerService.loadChunk (resp, user);
		}
		else {
			Logger.getLogger (AudioPlayerServlet.class.getName ()).log (Level.WARNING, "Url not found");
		}
		// handler errors
		if (audioPlayerService.hasErrors ()) {
			for (Exception e : audioPlayerService.getErrors ()) {
				Logger.getLogger (AudioPlayerServlet.class.getName ()).log (Level.WARNING,  null, e);
			}
		}
	}
	
}
