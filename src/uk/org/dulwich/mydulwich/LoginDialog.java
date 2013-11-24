package uk.org.dulwich.mydulwich;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginDialog extends DialogFragment {
	
	private Handler handler = new Handler();
	
	public LoginDialog setHandler(Handler handler)
	{
		this.handler = handler;
		return this;
	}
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_login, null);
        builder.setView(view)
        .setPositiveButton(R.string.login, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            	String username = null, password = null;
            	username = ((EditText) view.findViewById(R.id.Username)).getText().toString();
	            if (username.trim().length() == 0 || username.contains(" "))
	            {
	            	Toast.makeText(getActivity(), "Please enter a valid username", Toast.LENGTH_SHORT);
	            	return;
	            }
            	password = ((EditText) view.findViewById(R.id.Password)).getText().toString();
	            if (password.length() == 0 )
	            {
	            	Toast.makeText(getActivity(), "Please enter a valid password", Toast.LENGTH_SHORT);
	            	return;
	            }
            	Account.setCredentials(username, password);
            	handler.sendEmptyMessage(Msg.LOGGEDIN);
                Toast.makeText(getActivity(), "Logging in...", Toast.LENGTH_SHORT).show();
            	dialog.dismiss();
            }
        }).setCancelable(false);
        // Create the AlertDialog object and return it
        
        return builder.create();
	}
}
