package fr.sandboxwebapp.services;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import fr.sandboxwebapp.beans.TokenUrl;
import fr.sandboxwebapp.beans.User;

public class EmailService extends Service {
	
	public static final long ACTIVATING_LAPTING = 3600L; // 1H
	
	private static final String MSG_EMAIL_AUTOMATIC = "Ceci est un message automatique, veuillez ne pas y repondre.";

	public EmailService (Connection con) {
		super (con);
	}
	
	public void sendEmailSignin (ServletContext context, User user) {
		try {
			Session emailSession = (Session) context.getAttribute ("emailSession");
			String emailSender = (String) context.getAttribute ("accountEmailSender");
			String emailRecipient = user.getEmail ();
			TokenUrl tokenUrl = TokenUrl.get (con, user);
			
			Message message = new MimeMessage (emailSession);
			message.setFrom (new InternetAddress (emailSender));
			message.setRecipient (Message.RecipientType.TO, new InternetAddress (emailRecipient));
			message.setSubject ("Creation de compte");
			String body = "Bonjour " + user.getUsername () + "\n\n";
			body += "Veuillez cliquer sur le lien ci-dessous pour activer le compte et se connecter.\n";
			body += "http://localhost:8080/SandboxWebApp/email/validatemail?id=" + tokenUrl.getTokenUrl () + "\n\n";
			body += MSG_EMAIL_AUTOMATIC;
			message.setText (body);
			Transport.send (message);
		} 
		catch (Exception e) {
			errors.add (e);
		} 	
	}
	
	public void sendEmailRecoverPassword (ServletContext context, String emailRecipient, String tokenUrl, String username) {
		try {
			Session emailSession = (Session) context.getAttribute ("emailSession");
			String emailSender = (String) context.getAttribute ("accountEmailSender");
			
			Message message = new MimeMessage (emailSession);
			message.setFrom (new InternetAddress (emailSender));
			message.setRecipient (Message.RecipientType.TO, new InternetAddress (emailRecipient));
			message.setSubject ("Recupération du compte");
			String body = "Nous avons bien reçu vôtre demande de changement de mot de passe " + username + ".\n\n";
			body += "Veuillez cliquer sur le lien ci-dessous pour effectuer le changement.\n";
			body += "http://localhost:8080/SandboxWebApp/setuppassword?id=" + tokenUrl + "\n\n";
			body += MSG_EMAIL_AUTOMATIC;
			message.setText (body);
			Transport.send (message);
		}
		catch (Exception e) {
			errors.add (e);
		}
	}
	
	public void activeAccount (HttpServletRequest req, HttpServletResponse resp) {
		try {
			String tokenUrl = (String) req.getParameter ("id");
			Timestamp dateTimeCreation = TokenUrl.fetchTimestampByTokenUrl (con, tokenUrl);
			if (dateTimeCreation != null) {
				long seconds = (new Date ().getTime () - dateTimeCreation.getTime ()) / 1000;
				if (seconds > ACTIVATING_LAPTING) {
					Logger.getLogger (EmailService.class.getName ()).log (Level.INFO, "Email d'activation périmé de " + (seconds + ACTIVATING_LAPTING) + " secondes");
					req.getRequestDispatcher ("/WEB-INF/pages/public/expiration.jsp").forward (req, resp);
				}
				else {
					User.setEnabledByTokenUrl (con, true, tokenUrl);
					resp.sendRedirect ("/SandboxWebApp/login");
				}
			}
			else {
				throw new Exception ("TokenUrl non trouvé");
			}
		} 
		catch (Exception e) {
			errors.add (e);
		} 
	}
	
}
