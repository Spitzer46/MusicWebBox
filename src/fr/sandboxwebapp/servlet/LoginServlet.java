package fr.sandboxwebapp.servlet;

import java.io.IOException;
import java.sql.Connection;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import fr.sandboxwebapp.beans.User;
import fr.sandboxwebapp.services.ConnectionService;

public class LoginServlet extends Servlet {

	private static final long serialVersionUID = 2375996470246548054L;

	@Override
	protected void doGet (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute ("errorConnection", false);
		req.getRequestDispatcher ("/WEB-INF/pages/public/entry.jsp").forward (req, resp);
	}
	
	@Override
	protected void doPost (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ServletContext context = this.getServletContext ();
		ConnectionService conService = new ConnectionService ((Connection) context.getAttribute ("con"));
		User user = conService.connecting (req);
		HttpSession session = req.getSession ();
		if (conService.hasErrors ()) {
			req.setAttribute ("errorConnection", true);
			req.setAttribute ("errors", conService.getErrors ());
			showListWarnings (conService.getErrors ());
			req.getRequestDispatcher ("/WEB-INF/pages/public/entry.jsp").forward (req, resp);
		}
		else {
			req.setAttribute ("errorConnection", false);
			session.setAttribute ("userSession", user);
			resp.sendRedirect ("player");
		}
	}
	
}
