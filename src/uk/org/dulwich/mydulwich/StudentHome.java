package uk.org.dulwich.mydulwich;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class StudentHome extends ActionBarActivity {
	
	public static StudentHome instance;
	public static Handler handler;
	
    public SharedPreferences settings;
	private String[] drawerItems;
	private DrawerLayout drawerLayout;
	private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;
	private CharSequence title;
	private AccountThread at = new AccountThread();
    private NoticeAdapter notices;
	
	public StudentHome()
	{
		instance = this;
		handler = new Handler(new Handler.Callback() {
			@Override
			@SuppressWarnings("unchecked")
			public boolean handleMessage(Message msg)
			{
				switch (msg.what)
				{
					case Msg.LOGIN:
					case Msg.LOGGEDIN:
					case Msg.GETNOTICES:
						at.handler.sendMessage(msg);
					case Msg.GOTNOTICES: return handleNotices((List<Notice>) msg.obj);
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

		settings = getSharedPreferences("settings", Context.MODE_PRIVATE);
		
		final ListView noticeList = (ListView) findViewById(R.id.sh_Notices);
		LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View header = inflater.inflate(R.layout.header, noticeList, false);
		noticeList.addHeaderView(header);
		
		title = getTitle();
		
		RotateAnimation animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setDuration(700);
        final ImageView loadingIcon = (ImageView) findViewById(R.id.sh_LoadingIcon);
        loadingIcon.startAnimation(animation);
        
        at.start();
        
        notices = new NoticeAdapter(StudentHome.instance, R.layout.notice, new ArrayList<Notice>());
        noticeList.setAdapter(notices);
        
        drawerItems = getResources().getStringArray(R.array.panels);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);
        
        drawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.listitem_drawer, drawerItems));
        
        drawerList.setOnItemClickListener(new ListView.OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				onDrawerClick(position);
			}
        });
        
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.drawable.ic_drawer, android.R.string.yes, android.R.string.no) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(title);
                supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle("Screens");
                supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
	}

	private void onDrawerClick(int position) {
		Toast.makeText(StudentHome.instance, "# No."+position+" Click! #", Toast.LENGTH_SHORT).show();
		
	    Fragment fragment = new Fragment();

	    FragmentManager fragmentManager = getSupportFragmentManager();
	    fragmentManager.beginTransaction().replace(R.id.NoticeView, fragment).commit();

	    drawerList.setItemChecked(position, true);
	    setTitle(title);
	    drawerLayout.closeDrawer(drawerList);
	}


	@Override
	public void setTitle(CharSequence title) {
	    this.title = title;
	    getSupportActionBar().setTitle(this.title);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	
	@Override
	@SuppressLint("SetJavaScriptEnabled")
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (drawerToggle.onOptionsItemSelected(item)) {
		    return true;
		}
		switch (item.getItemId())
		{
		    case R.id.action_settings:
		        SharedPreferences.Editor editor = settings.edit();
		        editor.clear();
		        editor.commit();
		        Toast.makeText(this, "All preferences cleared!", Toast.LENGTH_LONG).show();
		        return true;
		    case R.id.action_about:
		    	startActivityForResult(new Intent(this, HelpActivity.class), 0);
		    default:
		        return super.onOptionsItemSelected(item);
		}
	}
	
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }
	
	private boolean handleNotices(List<Notice> list)
	{
        ((ImageView) findViewById(R.id.sh_LoadingIcon)).setAnimation(null);
        ((LinearLayout) findViewById(R.id.sh_Loading)).setVisibility(View.GONE);
        ((ListView) findViewById(R.id.sh_Notices)).setVisibility(View.VISIBLE);
        notices.addAll(list);
		notices.notifyDataSetChanged();
        return true;
	}
}
