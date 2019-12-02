package fr.sandboxwebapp.services;

import java.sql.Connection;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import fr.sandboxwebapp.beans.TokenUrl;
import fr.sandboxwebapp.beans.User;

public class RecoverPasswordService extends Service {
	
	public RecoverPasswordService (Connection con) {
		super (con);
	}
	
	public void recover (HttpServletRequest req, ServletContext context) {
		try {
			String username = ((String) req.getParameter ("username")).trim ();
			if (username.isEmpty ())
				throw new Exception ("Le nom d'utilisateur doit être présent");
			String email = ((String) req.getParameter ("email")).trim ();
			if (email.isEmpty ()) 
				throw new Exception ("Un email doit être présent");
			if (User.userExistAndActif (con, username, email)) {
				TokenUrl tokenUrl = new TokenUrl ();
				tokenUrl.genTokenUrl ();
				TokenUrl.save (con, tokenUrl, username);

				(new EmailService (con)).sendEmailRecoverPassword (context, email, tokenUrl.getTokenUrl (), username);
				messages.add ("Un email vient d'être envoyer sur votre boite de reception.");
				messages.add ("Il vous permettra de changer vôtre mot de passe.");
			}
			else {
				throw new Exception ("Aucun compte n'existe");
			}
		}
		catch (Exception e) {
			errors.add (e);
		}
	}
	
}
