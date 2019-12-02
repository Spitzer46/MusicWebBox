package fr.sandboxwebapp.servlet;

import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import fr.sandboxwebapp.beans.User;
import fr.sandboxwebapp.services.UploadService;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UploadServlet extends RestrictedServlet {

	private static final long serialVersionUID = 8637099732701580747L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher ("/WEB-INF/pages/restreint/upload.jsp").forward (req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ServletContext context = this.getServletContext ();
		HttpSession session = req.getSession ();
		UploadService uploadService = new UploadService ((Connection) context.getAttribute ("con"));
		uploadService.upload (req, (User) session.getAttribute ("userSession"));
		if (uploadService.hasErrors ()) {
			for (Exception e : uploadService.getErrors ())
				Logger.getLogger (UploadServlet.class.getName ()).log (Level.WARNING, null, e);
		}
	}
	
}
