package uk.org.dulwich.mydulwich;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Account
{
	private static String username = "";
	private static String password = "";
	
	public Account()
	{
		; // TODO: Try fetching credentials from SharedPreferences?
	}
	
	public static void setCredentials(Context context, String username, String password)
	{
		SharedPreferences.Editor editor = context.getSharedPreferences("settings", Context.MODE_PRIVATE).edit();
		editor.putString("lastUser", username);
		editor.putString("lastPass", password);
		editor.commit();
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
