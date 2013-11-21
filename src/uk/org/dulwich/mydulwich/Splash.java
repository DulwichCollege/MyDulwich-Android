package uk.org.dulwich.mydulwich;

import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.Toast;

public class Splash extends Activity {
	private static CharSequence nImpl = "Not Implemented (Yet...)";
	private SharedPreferences sharedPref;
	private Handler handler;
	private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        handler = new Handler();
        context = getApplicationContext();
        setContentView(R.layout.activity_splash);
        handler.postDelayed(new Runnable() {
        	
            @Override
            public void run() {
                ascend();
            }
        }, 1000);
    }
    
    private void ascend() // X/(1+e^(t-5)) for 10t
    {// 0.993307 to 0.006693 // (x - 0.006693) * 1.013568
    	final LinearLayout layout = (LinearLayout) findViewById(R.id.SplashLayout);
        Animation animation = new Animation() {
        	float start;
        	
        	@Override
        	protected void applyTransformation(float i, Transformation t)
        	{
                LayoutParams lp = (LayoutParams) layout.getLayoutParams();
                if(i == 0) start = lp.bottomMargin;
            	lp.bottomMargin = (int) (start*((1/(1+(Math.exp((i*10)-5))))-0.006693)*1.013568);
            	lp.topMargin = (int) (start - lp.bottomMargin);
            	if(i == 1)
            	{
            		lp.bottomMargin = 0;
            		lp.topMargin = (int) start;
            	}
            	layout.setLayoutParams(lp);
        	}
        };
        
        animation.setDuration(2000);
    	layout.startAnimation(animation);
    }
    
    private void bobDown()
    {
    	final LinearLayout layout = (LinearLayout) findViewById(R.id.SplashLayout);
        Animation animation = new Animation() {
        	float start;
        	
        	@Override
        	protected void applyTransformation(float i, Transformation t)
        	{
                LayoutParams lp = (LayoutParams) layout.getLayoutParams();
                if(i == 0) start = lp.topMargin;
            	lp.topMargin = (int) (start*(Math.exp(-(i*8))-0.000335)*1.000336);
            	lp.bottomMargin = (int) (start - lp.topMargin);
            	if(i == 1)
            	{
            		lp.topMargin = 0;
            		lp.bottomMargin = (int) start;
            	}
            	layout.setLayoutParams(lp);
        	}
        };
        
        animation.setDuration(1000);
    	layout.startAnimation(animation);
    }
    
    private void bobUp()
    {
    	final LinearLayout layout = (LinearLayout) findViewById(R.id.SplashLayout);
        Animation animation = new Animation() {
        	float start;
        	
        	@Override
        	protected void applyTransformation(float i, Transformation t)
        	{
                LayoutParams lp = (LayoutParams) layout.getLayoutParams();
                if(i == 0) start = lp.bottomMargin;
            	lp.bottomMargin = (int) (start*(1-Math.exp((i*8)-8))*1.000336);
            	lp.topMargin = (int) (start - lp.bottomMargin);
            	if(i == 1)
            	{
            		lp.bottomMargin = 0;
            		lp.topMargin = (int) start;
            	}
            	layout.setLayoutParams(lp);
        	}
        };
        
        animation.setDuration(1000);
    	layout.startAnimation(animation);
    }
    
    public void is_student(View view)
    {
    	bobDown();
    	handler.postDelayed(new Runnable() {
        	
            @Override
            public void run() {
            	((Button) findViewById(R.id.b_student)).setVisibility(View.GONE);
            	((Button) findViewById(R.id.b_teacher)).setVisibility(View.GONE);
            	((Button) findViewById(R.id.b_parent)).setVisibility(View.GONE);
            	((Button) findViewById(R.id.b_guest)).setVisibility(View.GONE);
            	((Button) findViewById(R.id.b_lowerschool)).setVisibility(View.VISIBLE);
            	((Button) findViewById(R.id.b_middleschool)).setVisibility(View.VISIBLE);
            	((Button) findViewById(R.id.b_upperschool)).setVisibility(View.VISIBLE);
                bobUp();
            }
        }, 1000);
    }
    
    public void is_teacher(View view)
    {
    	Toast.makeText(context, nImpl, Toast.LENGTH_SHORT).show();
    }
    
    public void is_parent(View view)
    {
    	Toast.makeText(context, nImpl, Toast.LENGTH_SHORT).show();
    }
    
    public void is_guest(View view)
    {
    	Toast.makeText(context, "# Debug Shortcut #", Toast.LENGTH_SHORT).show();
    	showStudenthome();
    }
    
    public void is_lowerschool(View view)
    {
    	showStudenthome();
    }
    
    public void is_middleschool(View view)
    {
    	showStudenthome();
    }
    
    public void is_upperschool(View view)
    {
    	showStudenthome();
    }
    
    public void showStudenthome()
    {
    	bobDown();
    	final Intent intent = new Intent(this, StudentHome.class);
    	handler.postDelayed(new Runnable() {
        	
            @Override
            public void run() {
            	startActivityForResult(intent, 0);
            }
        }, 1000);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(resultCode)
        {
	        case RESULT_CANCELED:
	        	setResult(RESULT_CANCELED);
	        	finish();
	        case RESULT_OK:
	        	finish();
	        default:
	            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    
}
