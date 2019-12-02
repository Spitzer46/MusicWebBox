package fr.sandboxwebapp.servlet;

import java.io.IOException;
import java.sql.Connection;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import fr.sandboxwebapp.services.SetupPasswordService;

public class SetupPasswordServlet extends HttpServlet {
	
	private static final long serialVersionUID = -676249690224148836L;
	
	private static final String ATTRIB_ERR_CON = "errorConnection";
	private static final String ATTRIB_ERR = "errors";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute (ATTRIB_ERR_CON, false);
		req.getRequestDispatcher ("/WEB-INF/pages/public/setupPassword.jsp").forward (req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ServletContext context = this.getServletContext ();
		SetupPasswordService setupPasswordService = new SetupPasswordService ((Connection) context.getAttribute ("con"));
		setupPasswordService.setup (req);
		if (setupPasswordService.hasErrors ()) {
			req.setAttribute (ATTRIB_ERR_CON, true);
			req.setAttribute (ATTRIB_ERR, setupPasswordService.getErrors ());
			req.getRequestDispatcher ("/WEB-INF/pages/public/setupPassword.jsp").forward (req, resp);
		}
		else {
			resp.sendRedirect ("login");
		}
	}
	
}
