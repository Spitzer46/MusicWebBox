package fr.sandboxwebapp.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

public class RestrictionFilter implements Filter {

	private Logger LOG = null;
	
	@Override
	public void doFilter (ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        // not filtering static resource (all things into WEB-INF/inc folder)
        String localPath = request.getRequestURI ().substring (request.getContextPath ().length ());
        if (localPath.startsWith ("/inc")) {
        	LOG.info ("Static ressource : " + localPath);
        	chain.doFilter (req, resp);
        	return;
        }
        // filtering all other resource
        HttpSession session = request.getSession ();
        if (session.getAttribute ("userSession") == null) {
          	LOG.info ("Public access : " + localPath);
          	request.getRequestDispatcher (localPath).forward (req, resp);
        }
        else {
          	LOG.info ("Private access : " + localPath);
          	chain.doFilter (req, resp);
        }
        // chain.doFilter (req, resp);
	}

	@Override
	public void init (FilterConfig conf) {
		LOG = Logger.getLogger (this.getClass ().getName ());
	}
	
}
