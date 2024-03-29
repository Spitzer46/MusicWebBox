package fr.sandboxwebapp.services;

import java.sql.Connection;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import fr.sandboxwebapp.beans.TokenUrl;
import fr.sandboxwebapp.beans.User;
import fr.sandboxwebapp.utils.Utils;

public class ConnectionService extends Service {
	
	public ConnectionService(Connection con) {
		super (con);
	}
	
	public User connecting (HttpServletRequest req) {
		try {
			String username = ((String) req.getParameter ("username")).trim ();
			if (username.isEmpty ())
				throw new Exception ("Le nom d'utilisateur doit �tre pr�sent");
			String password = ((String) req.getParameter ("password")).trim ();
			if (password.isEmpty ()) 
				throw new Exception ("Le mot de passe doit �tre pr�sent");
			if (!User.userExist (con, username)) 
				throw new Exception ("Aucun utilisateur existe pour ce nom");
			User user = User.getUser (con, username, password);
			if (user == null) 
				throw new Exception ("Nom d'utilisateur ou mot de passe incorrect");
			if (!user.isEnabled ()) 
				throw new Exception ("Il se peut que ce compte ne soit pas activ�");
			return user;
		}
		catch (Exception e) {
			errors.add (e);
			return null;
		}
	}
	
	public User create (HttpServletRequest req, ServletContext context) {
		try {
			String firstname = ((String) req.getParameter ("firstname")).trim ();
			if (firstname.isEmpty ()) 
				throw new Exception ("Le pr�nom doit �tre pr�sent");
			String lastname = ((String) req.getParameter ("lastname")).trim ();
			if (lastname.isEmpty ()) 
				throw new Exception ("Le nom doit �tre pr�sent");
			String username = ((String) req.getParameter ("username")).trim ();
			if (username.isEmpty()) 
				throw new Exception ("Un pseudo doit �tre choisi");
			String email = ((String) req.getParameter ("email")).trim ();
			if (email.isEmpty ()) 
				throw new Exception ("Un email doit �tre pr�sent");
			String password = ((String) req.getParameter ("password")).trim ();
			String confirmPassword = ((String) req.getParameter ("confirmPassword")).trim ();
			if (password.isEmpty() || !password.equals (confirmPassword))
				throw new Exception ("Le mot de passe n'est pas le m�me");
			if (User.userExist (con, username))
				throw new Exception ("Un utilisateur existe d�ja avec ce pseudo");
			// Create user in db
			User user = new User ();
			user.setFirstname (firstname);
			user.setLastname (lastname);
			user.setUsername (username);
			user.setPassword (Utils.md5 (password));
			user.setEmail (email);
			user.setEnabled (false);
			User.save (con, user);
			// create token url in db
			TokenUrl tokenUrl = new TokenUrl ();
			tokenUrl.genTokenUrl ();
			TokenUrl.save (con, tokenUrl, user.getUsername ());
			// Send email to confirm
			(new EmailService (con)).sendEmailSignin (context, user);
			return user;
		}
		catch (Exception e) {
			errors.add (e);
			return null;
		}
	}
	
}
