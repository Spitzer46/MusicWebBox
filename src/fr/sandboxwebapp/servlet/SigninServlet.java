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

public class SigninServlet extends HttpServlet {

	private static final long serialVersionUID = 70478938643965922L;
	private static final String ATTRIB_SIGN_IN_ERR = "errorSignIn";
	
	@Override
	protected void doGet (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute (ATTRIB_SIGN_IN_ERR, false);
		req.getRequestDispatcher ("/WEB-INF/pages/public/signin.jsp").forward (req, resp);
	}
	
	@Override
	protected void doPost (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ServletContext context = this.getServletContext ();
		ConnectionService conService = new ConnectionService ((Connection) context.getAttribute ("con"));
		User user = conService.create (req, context);
		if (conService.hasErrors ()) {
			req.setAttribute (ATTRIB_SIGN_IN_ERR, true);
			req.setAttribute ("errors", conService.getErrors ());
			for (Exception e : conService.getErrors ())
				Logger.getLogger (SigninServlet.class.getName ()).log (Level.WARNING, null, e);
			req.getRequestDispatcher ("/WEB-INF/pages/public/signin.jsp").forward (req, resp);
		}
		else {
			req.setAttribute (ATTRIB_SIGN_IN_ERR, false);
			req.getSession ().setAttribute ("userSession", user);
			// req.getRequestDispatcher ("/WEB-INF/pages/restreint/home.jsp").forward (req, resp);
			req.getRequestDispatcher ("/WEB-INF/pages/public/email.jsp").forward (req, resp);
		}
	}
	
}
