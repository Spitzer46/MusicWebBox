package fr.sandboxwebapp.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import fr.sandboxwebapp.beans.User;

public abstract class RestrictedServlet extends HttpServlet {

	private static final long serialVersionUID = -5476149738807718749L;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession ();
		User user = (User) session.getAttribute ("userSession");
		if (user == null || !user.isEnabled ()) {
			resp.sendError (HttpServletResponse.SC_UNAUTHORIZED);
		}
		else {
			super.service (req, resp);
		}
	}
	
}
