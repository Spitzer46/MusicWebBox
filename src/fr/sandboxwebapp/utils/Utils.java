package fr.sandboxwebapp.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.apache.commons.io.IOUtils;

public class Utils {
	
	private Utils () {}
	
	public static String md5 (String input) throws NoSuchAlgorithmException {
		if(null == input) {
			return null;	
		}
		MessageDigest digest = MessageDigest.getInstance ("MD5");
		digest.update(input.getBytes (), 0, input.length ());
		return new BigInteger(1, digest.digest ()).toString (16);
	}
	
    public static InputStream clone (final InputStream is) throws Exception {
        return new ByteArrayInputStream (IOUtils.toByteArray (is));
    }
    
    public static String genUUID () {
    	return UUID.randomUUID ().toString ().replace ("-", "");
    }
    
}
