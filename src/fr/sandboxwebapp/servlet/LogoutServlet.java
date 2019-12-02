package fr.sandboxwebapp.servlet;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogoutServlet extends HttpServlet {

	private static final long serialVersionUID = 2915796138775070420L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getSession ().invalidate ();
		Logger.getLogger (LogoutServlet.class.getName ()).log (Level.INFO, "One user is logged out");
		resp.sendRedirect ("/SandboxWebApp/login");
	}
	
}
