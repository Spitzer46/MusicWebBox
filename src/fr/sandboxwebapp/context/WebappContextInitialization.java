package fr.sandboxwebapp.context;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class WebappContextInitialization implements ServletContextListener {
	
	private void initDatabaseConnector (ServletContext context) throws Exception {		
		final String dbDriver = context.getInitParameter ("db_driver");
		final String dbUrl = context.getInitParameter ("db_url");
		final String dbUser = context.getInitParameter ("db_user");
		final String dbPassword = context.getInitParameter ("db_password");
		final String dbAutoReconnect = context.getInitParameter ("db_autoReconnect");
	    Properties setup = new Properties ();
	    setup.setProperty ("user", dbUser);
	    setup.setProperty ("password", dbPassword);
	    setup.setProperty ("autoReconnect", dbAutoReconnect);
	    Driver driver = (Driver) Class.forName (dbDriver).newInstance ();
	    Connection con = driver.connect (dbUrl, setup);
		context.setAttribute ("con", con);
	}
	
	private void initEmailSender (ServletContext context) {
		final String accountEmailSender = context.getInitParameter ("email_sender_account");
		final String pwdAccountEmailSender = context.getInitParameter ("email_sender_account_password");
		final String smtpHost = context.getInitParameter ("mail.smtp.host");
		final String smtpPort = context.getInitParameter ("mail.smtp.port");
		final String smtpAuth = context.getInitParameter ("mail.smtp.auth");
		final String smtpSfPort = context.getInitParameter ("mail.smtp.socketFactory.port");
		final String smtpSfClass = context.getInitParameter ("mail.smtp.socketFactory.class");
		Properties props = new Properties ();
		props.put ("mail.smtp.host", smtpHost);
		props.put ("mail.smtp.port", smtpPort);
		props.put ("mail.smtp.auth", smtpAuth);
		props.put ("mail.smtp.socketFactory.port", smtpSfPort);
		props.put ("mail.smtp.socketFactory.class", smtpSfClass);
		Session session = Session.getInstance (props, new Authenticator () {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {	
				return new PasswordAuthentication (accountEmailSender, pwdAccountEmailSender);
			}
		});
		context.setAttribute ("emailSession", session);
		context.setAttribute ("accountEmailSender", accountEmailSender);
	}
	
	@Override
	public void contextInitialized (ServletContextEvent sce) {
		try {
			ServletContext context = sce.getServletContext ();
			initDatabaseConnector (context);
			initEmailSender (context);
		}
		catch (Exception e) {
			Logger.getLogger (WebappContextInitialization.class.getName ()).log (Level.WARNING, null, e);
		}
	}
	
	@Override
	public void contextDestroyed (ServletContextEvent sce) {
		try {
			((Connection) sce.getServletContext ().getAttribute ("con")).close ();
		} 
		catch (SQLException e) {
			Logger.getLogger (WebappContextInitialization.class.getName ()).log (Level.WARNING, null, e);
		}
	}
	
}
