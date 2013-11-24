package uk.org.dulwich.mydulwich;

import android.os.Bundle;
import android.os.Handler;
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
	private Handler handler;
	private Context context;
	private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
        context = getApplicationContext();
        settings = getSharedPreferences("settings", Context.MODE_PRIVATE);
        Account.setCredentials(this, settings.getString("lastUser", ""), settings.getString("lastPass", ""));
        switch(settings.getInt("userType", 0))
        {
        	case 0:
                setContentView(R.layout.activity_splash);
		        handler.postDelayed(new Runnable() {
		        	
		            @Override
		            public void run() {
		                ascend();
		            }
		        }, 1000);
		        break;
        	case 1:
        		is_student(null);
        		break;
        	case 2:
        		is_teacher(null);
        		break;
        	case 3:
        		is_parent(null);
        		break;
        }
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
    	if (view != null)
    	{
	    	bobDown();
    		SharedPreferences.Editor editor = settings.edit();
    		editor.putInt("userType", 1);
    		editor.commit();
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
    	else showStudenthome();
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
    	bobDown();
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("studentType", 1);
		editor.commit();
    	showStudenthome();
    }
    
    public void is_middleschool(View view)
    {
    	bobDown();
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("studentType", 2);
		editor.commit();
    	showStudenthome();
    }
    
    public void is_upperschool(View view)
    {
    	bobDown();
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("studentType", 3);
		editor.commit();
    	showStudenthome();
    }
    
    public void showStudenthome()
    {
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
