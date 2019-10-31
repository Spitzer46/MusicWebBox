package fr.sandboxwebapp.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import fr.sandboxwebapp.beans.User;

public class ConnectionService extends Service {

	private Connection con;
	
	public ConnectionService(Connection con) {
		super ();
		this.con = con;
	}
	
	public User connecting (HttpServletRequest req) {
		try {
			String username = ((String) req.getParameter ("username")).trim ();
			if (username.isEmpty ()) {
				throw new Exception ("Le nom d'utilisateur doit être présent");
			}
			String password = ((String) req.getParameter ("password")).trim ();
			if (password.isEmpty ()) {
				throw new Exception ("Le mot de passe doit être présent");
			}
			if (!User.userExist (con, username)) {
				throw new Exception ("Aucun utilisateur existe pour ce nom");
			}
			User user = User.getUser (con, username, password);
			if (user == null) {
				throw new Exception ("Nom d'utilisateur ou mot de passe incorrect");
			}
			return user;
		}
		catch (SQLException e) {
			System.err.println ("Error about data base : " + e.getMessage ());
			return null;
		}
		catch (Exception e) {
			errors.add (e.getMessage ());
			return null;
		}
	}
	
	public User create (HttpServletRequest req) {
		try {
			String firstname = ((String) req.getParameter ("firstname")).trim ();
			if (firstname.isEmpty ()) {
				throw new Exception ("Le prénom doit être présent");
			}
			String lastname = ((String) req.getParameter ("lastname")).trim ();
			if (lastname.isEmpty ()) {
				throw new Exception ("Le nom doit être présent");
			}
			String username = ((String) req.getParameter ("username")).trim ();
			if (username.isEmpty()) {
				throw new Exception ("Un pseudo doit être choisi");
			}
			String email = ((String) req.getParameter ("email")).trim ();
			if (email.isEmpty ()) {
				throw new Exception ("Un email doit être présent");
			}
			String password = ((String) req.getParameter ("password")).trim ();
			String confirmPassword = ((String) req.getParameter ("confirmPassword")).trim ();
			if (password.isEmpty() || !password.equals (confirmPassword)) {
				throw new Exception ("Le mot de passe n'est pas le même");
			}
			if (User.userExist (con, username)) {
				throw new Exception ("Un utilisateur existe déja avec ce speudo");
			}
			User user = new User ();
			user.setFirstname (firstname);
			user.setLastname (lastname);
			user.setUsername (username);
			user.setEmail (email);
			User.create (con, password, user);
			return user;
		}
		catch (SQLException e) {
			System.err.println ("Error about data base : " + e.getMessage ());
			return null;
		}
		catch (Exception e) {
			errors.add (e.getMessage ());
			return null;
		}
	}
	
}
