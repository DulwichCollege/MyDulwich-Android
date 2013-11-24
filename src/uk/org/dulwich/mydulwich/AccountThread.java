package uk.org.dulwich.mydulwich;

import java.util.List;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

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
				(new LoginDialog()).setHandler(handler).show(StudentHome.instance.getSupportFragmentManager(), "login");
			}
		});
		return true;
	}
	
	private boolean onLogin() {
		return handler.sendEmptyMessage(Msg.GETNOTICES);
	}
	
	private boolean getNotices() {
		try {
        	List<Notice> data = (new Request(StudentHome.instance, ApiList.notices)).notices();
        	StudentHome.handler.sendMessage(handler.obtainMessage(Msg.GOTNOTICES, data));
        } catch (BadLogin e) {
        	handler.sendEmptyMessage(Msg.LOGIN);
        } catch (Exception e) {/* ugh */
        	StudentHome.instance.finish();
        	e.printStackTrace();
        }
		return true;
	}
}