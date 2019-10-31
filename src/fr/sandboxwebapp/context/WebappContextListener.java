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
		    // int majorVersion = driver.getMajorVersion (); 
		    // int minorVersion = driver.getMinorVersion (); 
		    // System.out.println ("Driver = " + driver.getClass () + " v"+majorVersion + "." + minorVersion); 
			// Connection con = DriverManager.getConnection (urlDataBase, user, password);
			// DriverPropertyInfo [] props = driver.getPropertyInfo (urlDataBase, null);
			// for(int i=0 ;i<props.length;i++){ 
			//     DriverPropertyInfo prop = props[i]; 
			//     System.out.println("Prop name = "+prop.name); 
			//     System.out.println("Prop description = "+prop.description); 
			//     System.out.println("Prop value = "+prop.value); 
			//     if(prop.choices!=null){ 
			//         for(int j=0;j<prop.choices.length;j++){ 
			//             System.out.println("prop choice "+j+" = "+prop.choices[j]); 
			//         } 
			//     } 
			// }
		    Properties setup = new Properties ();
		    setup.setProperty ("user", user);
		    setup.setProperty ("password", password);
		    setup.setProperty ("autoReconnect", "true");
		    Connection con = driver.connect (urlDataBase, setup);
			sce.getServletContext ().setAttribute ("con", con);
			LOG.info ("Database connector has been configured");
		}
		catch (Exception e) {
			LOG.warn (e.toString ());
		}
	}
	
	@Override
	public void contextDestroyed (ServletContextEvent sce) {
		try {
			((Connection) sce.getServletContext ().getAttribute ("con")).close ();;
		} 
		catch (SQLException e) {
			LOG.warn (e.toString ());
		}
	}
	
}
