package uk.org.dulwich.mydulwich;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.app.Activity;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.FrameLayout.LayoutParams;

public class Splash extends Activity {
	private static int SPLASH_TIMEOUT = 3000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
        	
            @Override
            public void run() {// X/(1+e^(t-5))

                final LinearLayout layout = (LinearLayout) findViewById(R.id.SplashLayout);
                Animation animation = new Animation() {
                	float start;
                	
                	@Override
                	protected void applyTransformation(float i, Transformation t)
                	{
                        LayoutParams lp = (LayoutParams) layout.getLayoutParams();
                        if(i == 0) start = lp.bottomMargin * 1.006738f; // (1+e^-5)
	                	lp.bottomMargin = (int) (start/(1+(Math.exp((i*10)-5))));
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
        }, SPLASH_TIMEOUT);
    }
    

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.splash, menu);
//        return true;
//    }
    
}
