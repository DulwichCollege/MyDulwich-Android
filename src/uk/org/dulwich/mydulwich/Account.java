package uk.org.dulwich.mydulwich;

import android.app.Activity;

public class Account
{
	private static String username = "olagbaiyeb2";
	private static String password = "dyne";
	
	public Account()
	{
		; // TODO: Try fetching credentials from SharedPreferences?
	}
	
	public static void setCredentials(String username, String password)
	{
		Account.username = username;
		Account.password = password;
	}
	
	public static String getUsername(Activity caller)
	{
		return Account.username;
	}
	
	public static String getPassword(Activity caller)
	{
		return Account.password;
	}
}
