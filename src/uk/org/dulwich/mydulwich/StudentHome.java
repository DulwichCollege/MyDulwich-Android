package uk.org.dulwich.mydulwich;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class StudentHome extends Activity {
	
	public static StudentHome instance;
	public static Handler handler;
	private AccountThread at = new AccountThread();
    private ArrayAdapter<String> notices;
	
	public StudentHome()
	{
		instance = this;
		handler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg)
			{
				switch (msg.what)
				{
					case Msg.LOGIN:
					case Msg.LOGGEDIN:
					case Msg.GETNOTICES:
						at.handler.sendMessage(msg);
					case Msg.GOTNOTICES: return handleNotices((String) msg.obj);
					default: return false;
				}
			}
		});
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.fadein, R.anim.fadeout);
		setContentView(R.layout.activity_studenthome);
		RotateAnimation animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setDuration(700);
        final ImageView loadingIcon = (ImageView) findViewById(R.id.sh_LoadingIcon);
        loadingIcon.startAnimation(animation);
        
		final ListView noticeList = (ListView) findViewById(R.id.sh_Notices);
        notices = new ArrayAdapter<String>(StudentHome.instance, android.R.layout.simple_list_item_1);
        noticeList.setAdapter(notices);
        
        at.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		    case R.id.action_settings:
		        ((ImageView) findViewById(R.id.sh_LoadingIcon)).setAnimation(null);
		        ((LinearLayout) findViewById(R.id.sh_Loading)).setVisibility(View.GONE);
		        ((ListView) findViewById(R.id.sh_Notices)).setVisibility(View.VISIBLE);
				notices.notifyDataSetChanged();
		        return true;
		    default:
		        return super.onOptionsItemSelected(item);
		}
	}
	
	private boolean handleNotices(String data)
	{
        ((ImageView) findViewById(R.id.sh_LoadingIcon)).setAnimation(null);
        ((LinearLayout) findViewById(R.id.sh_Loading)).setVisibility(View.GONE);
        ((ListView) findViewById(R.id.sh_Notices)).setVisibility(View.VISIBLE);
        notices.add(data);
		notices.notifyDataSetChanged();
        return true;
	}
}
