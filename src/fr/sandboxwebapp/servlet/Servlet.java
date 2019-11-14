package fr.sandboxwebapp.servlet;

import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import org.apache.log4j.Logger;

public abstract class Servlet extends HttpServlet {

	private static final long serialVersionUID = -3329402360280949805L;
	
	protected Logger LOG = null;
	
	public void init () throws ServletException {
		LOG = Logger.getLogger (this.getClass ().getName ());
	}

	protected void showListWarnings (List<Exception> errors) {
		for (Exception e : errors) {
			LOG.warn (e);
		}
	}
	
}
