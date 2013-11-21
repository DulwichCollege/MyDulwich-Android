package uk.org.dulwich.mydulwich;

public class BadLogin extends Exception {

	private static final long serialVersionUID = 2274334414532030892L;

	public BadLogin()
	{
		super();
	}
	
	public BadLogin(String message)
	{
		super(message);
	}
}
