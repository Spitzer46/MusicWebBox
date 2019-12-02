package fr.sandboxwebapp.services;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public abstract class Service {

	protected List<Exception> errors;
	protected List<String> messages;
	protected Connection con;
	
	public Service () {
		errors = new ArrayList<>();
		messages = new ArrayList<>();
	}
	
	public Service (Connection con) {
		this.con = con;
		errors = new ArrayList<>();
		messages = new ArrayList<>();
	}
	
	public boolean hasErrors () {
		return !errors.isEmpty ();
	}
	
	public List<Exception> getErrors () {
		return errors;
	}
	
	public boolean hasMessages () {
		return !messages.isEmpty ();
	}
	
	public List<String> getMessages () {
		return messages;
	}
	
}
