package uk.org.dulwich.mydulwich;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

// Available APIs:
// feed://mydulwich.dulwich.org.uk/_layouts/listfeed.aspx?List=%7B713BF399%2D5941%2D4B4A%2D8021%2D668E643448F3%7D
// http://web3apps.dulwich.org.uk/Calendar/TeamResults.aspx
// http://web3apps.dulwich.org.uk/Calendar/Today.aspx
// http://web3apps.dulwich.org.uk/Calendar/SPToday.aspx
// http://mydulwich.dulwich.org.uk/College%20Documents/Menus/MENU_Michaelmas_Term_2013_Week1.pdf
// http://mydulwich.dulwich.org.uk/upperschool/Pages/AssemblySchedule.aspx
// pop3://webmail.dulwich.org.uk/
// The school site's been screwed with again so there's no obvious source for timetable info

public class StudentHome extends Activity {
	//private static String login_url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studenthome);
        
    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }
    
}
