package fr.sandboxwebapp.servlet;

import java.io.IOException;
import java.sql.Connection;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
		HttpSession session = req.getSession ();
		User user = (User) session.getAttribute ("userSession");
		String uri = req.getRequestURI ();
		AudioPlayerService audioPlayerService = new AudioPlayerService ((Connection) context.getAttribute ("con"));
		if (uri.equals ("/SandboxWebApp/player/api/nexttracks")) {
			audioPlayerService.nextTrack (req, resp, user);
			if (audioPlayerService.hasErrors ()) {
				showListWarnings (audioPlayerService.getErrors ());
			}
		}
		else if (uri.equals ("/SandboxWebApp/player/api/loadtrack")) {
			audioPlayerService.loadTrack (req, resp, user);
			if (audioPlayerService.hasErrors ()) {
				showListWarnings (audioPlayerService.getErrors ());
			}
		}
		else if (uri.equals ("/SandboxWebApp/player/api/readChunk")) {
			
		}
		else {
			LOG.warn ("Url not found");
		}
	}
	
}
