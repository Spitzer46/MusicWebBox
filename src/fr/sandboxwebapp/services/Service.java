package fr.sandboxwebapp.services;

import java.util.ArrayList;
import java.util.List;

public abstract class Service {

	protected List<String> errors;
	
	protected Service () {
		errors = new ArrayList<>();
	}
	
	public boolean hasErrors () {
		return !errors.isEmpty ();
	}
	
	public List<String> getErrors () {
		return errors;
	}
	
}
