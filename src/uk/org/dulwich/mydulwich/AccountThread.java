package uk.org.dulwich.mydulwich;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class AccountThread extends Thread {
	
	public Handler handler;
	
	@Override
    public void run()
	{
		Looper.prepare();
		handler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg)
			{
				switch (msg.what)
				{
					case Msg.LOGIN: return login();
					case Msg.LOGGEDIN: return onLogin();
					case Msg.GETNOTICES: return getNotices();
					default: return false;
				}
			}
		});
		handler.sendEmptyMessage(Msg.GETNOTICES);
		Looper.loop();
    }

	private boolean login() {
		StudentHome.instance.runOnUiThread(new Runnable(){
			public void run() {
				(new LoginDialog()).setHandler(handler).show(StudentHome.instance.getFragmentManager(), "login");
			}
		});
		return true;
	}
	
	private boolean onLogin() {
		return handler.sendEmptyMessage(Msg.GETNOTICES);
	}
	
	private boolean getNotices() {
		try {
        	Log.v(this.getClass().getName(), "hmm");
        	String data = (new Request(StudentHome.instance, ApiList.notices)).get();
        	Log.v(this.getClass().getName(), data);
        	StudentHome.handler.sendMessage(handler.obtainMessage(Msg.GOTNOTICES, data));
        	Log.v(this.getClass().getName(), "yup");
        } catch (BadLogin e) {
        	handler.sendEmptyMessage(Msg.LOGIN);
        } catch (Exception e) {/* ugh */
        	StudentHome.instance.finish();
        	e.printStackTrace();
        }
		return true;
	}
}