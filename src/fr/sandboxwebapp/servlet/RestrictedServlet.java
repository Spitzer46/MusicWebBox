package fr.sandboxwebapp.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public abstract class RestrictedServlet extends Servlet {

	private static final long serialVersionUID = -5476149738807718749L;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession ();
		if (session.getAttribute ("userSession") == null) {
			resp.sendError (HttpServletResponse.SC_UNAUTHORIZED);
		}
		else {
			super.service (req, resp);
		}
	}
	
}
