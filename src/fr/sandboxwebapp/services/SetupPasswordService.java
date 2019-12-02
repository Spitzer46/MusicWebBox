package fr.sandboxwebapp.services;

import java.sql.Connection;
import javax.servlet.http.HttpServletRequest;
import fr.sandboxwebapp.beans.User;
import fr.sandboxwebapp.utils.Utils;

public class SetupPasswordService extends Service {

	public SetupPasswordService (Connection con) {
		super (con);
	}
	
	public void setup (HttpServletRequest req) {
		try {
			String tokenUrl = ((String) req.getParameter ("id")).trim ();
			if (tokenUrl.isEmpty ()) 
				throw new Exception ("Id non présent");
			String password = ((String) req.getParameter ("password")).trim ();
			if (password.isEmpty ())
				throw new Exception ("Le mot de passe est vide");
			String confirmation  = ((String) req.getParameter ("confirmation")).trim ();
			if (confirmation.isEmpty ())
				throw new Exception ("La confirmation est vide");
			if (!password.equals (confirmation))
				throw new Exception ("Le mot de passe ne correspond pas à celui de la confirmation");
			User user = User.getUserByTokenUrl (con, tokenUrl);
			user.setPassword (Utils.md5 (password));
			User.save (con, user);
		}
		catch (Exception e) {
			errors.add (e);
		}
	}
	
}
