package fr.sandboxwebapp.servlet;

import java.io.IOException;
import java.sql.Connection;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import fr.sandboxwebapp.services.RecoverPasswordService;

public class RecoverPassword extends HttpServlet {

	private static final long serialVersionUID = -6265614601595643713L;
	
	private static final String ATTRIB_ERR_CON = "errorConnection";
	private static final String ATTRIB_ERR = "errors";
	private static final String ATTRIB_HAS_MSG = "hasMessages";
	private static final String ATTRIB_MSG = "messages";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute (ATTRIB_ERR_CON, false);
		req.setAttribute (ATTRIB_HAS_MSG, false);
		req.getRequestDispatcher ("/WEB-INF/pages/public/recoverPassword.jsp").forward (req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ServletContext context = this.getServletContext ();
		RecoverPasswordService recoverPasswordService = new RecoverPasswordService ((Connection) context.getAttribute ("con"));
		recoverPasswordService.recover (req, context);
		if (recoverPasswordService.hasErrors ()) {
			req.setAttribute (ATTRIB_ERR_CON, true);
			req.setAttribute (ATTRIB_ERR, recoverPasswordService.getErrors ());
			req.setAttribute (ATTRIB_HAS_MSG, false);
		}
		else {
			req.setAttribute (ATTRIB_ERR_CON, false);
			if (recoverPasswordService.hasMessages ()) {
				req.setAttribute (ATTRIB_HAS_MSG, true);
				req.setAttribute (ATTRIB_MSG, recoverPasswordService.getMessages ());
			}
			else {
				req.setAttribute (ATTRIB_HAS_MSG, false);
			}
		}
		req.getRequestDispatcher ("/WEB-INF/pages/public/recoverPassword.jsp").forward (req, resp);
	}
	
}
