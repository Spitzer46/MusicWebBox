package fr.sandboxwebapp.servlet;

import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import fr.sandboxwebapp.beans.User;
import fr.sandboxwebapp.services.EmailService;

public class EmailServlet extends HttpServlet {

	private static final long serialVersionUID = 3369465804543805638L;

	@Override
	protected void doGet (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Email API
		ServletContext context = this.getServletContext ();
		EmailService emailService = new EmailService ((Connection) context.getAttribute ("con"));
		
		String uri = req.getRequestURI ();
		if (uri.equals ("/SandboxWebApp/email/validatemail")) {
			emailService.activeAccount (req, resp);
		}
		else if (uri.equals ("/SandboxWebApp/email/sendemailsignin")) {
			emailService.sendEmailSignin (context, (User) req.getSession ().getAttribute ("userSession"));
		}
		else if (uri.equals ("/SandboxWebApp/email/setuppassword")) {
			
		}
		else {
			Logger.getLogger (EmailServlet.class.getName ()).log (Level.WARNING, "Url not found");
		}
		// handler errors
		if (emailService.hasErrors ()) {
			for (Exception e : emailService.getErrors ())
				Logger.getLogger (EmailServlet.class.getName ()).log (Level.WARNING,  null, e);
		}
	}
	
}
