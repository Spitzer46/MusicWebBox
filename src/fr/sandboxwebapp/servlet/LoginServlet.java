package fr.sandboxwebapp.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import fr.sandboxwebapp.beans.User;
import fr.sandboxwebapp.services.ConnectionService;

public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 2375996470246548054L;
	private static final String ATTRIB_ERR_CON = "errorConnection";

	@Override
	protected void doGet (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute (ATTRIB_ERR_CON, false);
		req.getRequestDispatcher ("/WEB-INF/pages/public/entry.jsp").forward (req, resp);
	}
	
	@Override
	protected void doPost (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ServletContext context = this.getServletContext ();
		ConnectionService conService = new ConnectionService ((Connection) context.getAttribute ("con"));
		User user = conService.connecting (req);
		if (conService.hasErrors ()) {
			req.setAttribute (ATTRIB_ERR_CON, true);
			req.setAttribute ("errors", conService.getErrors ());
			for (Exception e : conService.getErrors ())
				Logger.getLogger (LoginServlet.class.getName ()).log (Level.WARNING, null, e);
			req.getRequestDispatcher ("/WEB-INF/pages/public/entry.jsp").forward (req, resp);
		}
		else {
			req.setAttribute (ATTRIB_ERR_CON, false);
			req.getSession ().setAttribute ("userSession", user);
			resp.sendRedirect ("player");
		}
	}
	
}
