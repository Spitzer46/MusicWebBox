package fr.sandboxwebapp.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Test extends HttpServlet {

	private static final long serialVersionUID = 3901786252740341129L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//		Collection<Part> parts = req.getParts ();
//		for (Part part : parts) {
//			String name = part.getName ();
//			for (String field : part.getHeaderNames ()) {
//				for (String value : part.getHeaders (field)) {
//					System.out.println (name + " : " + field + " " + value);
//				}
//			}
//			System.out.print ("\n");
//		}
	}
	
}
