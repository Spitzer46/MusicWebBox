package fr.sandboxwebapp.context;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.apache.log4j.Logger;

@WebListener
public class WebappContextListener implements ServletContextListener {

	private Logger LOG = null;
	
	@Override
	public void contextInitialized (ServletContextEvent sce) {
		final String urlDataBase = "jdbc:mariadb://127.0.0.1:3306/sandboxwebapp";
		final String user = "root";
		final String password = "root";
		try {
			LOG = Logger.getLogger (this.getClass ().getName ());
			Driver driver = (Driver) Class.forName ("org.mariadb.jdbc.Driver").newInstance ();
		    Properties setup = new Properties ();
		    setup.setProperty ("user", user);
		    setup.setProperty ("password", password);
		    setup.setProperty ("autoReconnect", "true");
		    Connection con = driver.connect (urlDataBase, setup);
			sce.getServletContext ().setAttribute ("con", con);
			LOG.info ("Database connector has been configured");
		}
		catch (Exception e) {
			LOG.warn (e);
		}
	}
	
	@Override
	public void contextDestroyed (ServletContextEvent sce) {
		try {
			((Connection) sce.getServletContext ().getAttribute ("con")).close ();
		} 
		catch (SQLException e) {
			LOG.warn (e);
		}
	}
	
}
