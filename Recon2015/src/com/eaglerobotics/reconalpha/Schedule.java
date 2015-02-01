package com.eaglerobotics.reconalpha;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.eaglerobotics.reconalpha.DynamoDBManager.MatchSched;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


public class Schedule extends Activity {
	EditText r1,r2,r3,b1,b2,b3,mnum;
	RadioGroup rg;
	MatchSched matchSched = new MatchSched();
	String ev;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule);
		
		   r1 = (EditText)findViewById(R.id.red1); 
		   r2 = (EditText)findViewById(R.id.red2); 
		   r3 = (EditText)findViewById(R.id.red3); 
		   b1 = (EditText)findViewById(R.id.blue1); 
		   b2 = (EditText)findViewById(R.id.blue2); 
		   b3 = (EditText)findViewById(R.id.blue3); 
		   mnum = (EditText)findViewById(R.id.matchnum); 
		   rg = (RadioGroup)findViewById(R.id.radioGroup1); 
		   ev = Splash.settings.getString("event", "");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings_home_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
	    switch(item.getItemId()){
	    case R.id.action_settings:
	        Intent intent = new Intent(this, Prefs.class);
	        startActivity(intent);
	        return true;       
	    case R.id.action_home:
	        Intent hintent = new Intent(this, MainActivity.class);
	        startActivity(hintent);
	        return true; 
	    }
	    return false;
	}

	
    public void saveData(View view) {
    	

    	String mtype, matchnum;
    	Calendar c = Calendar.getInstance();
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String formattedDate = df.format(c.getTime());    
    	RadioButton rb =  (RadioButton)findViewById(rg.getCheckedRadioButtonId());
    	String radiovalue =rb.getText().toString();
    	Log.i("sched","radio button: ="+radiovalue);
    	matchnum = mnum.getText().toString();
    	if (radiovalue.startsWith("P")) {
    		mtype = "P";
    		matchnum = "0."+matchnum;
    	} else if (radiovalue.startsWith("Q")) {
    		mtype = "Q";
    	} else if (radiovalue.startsWith("E")) {
    		mtype = "E";
        	if (Integer.parseInt(matchnum) <= 12) {
        		matchnum = "QF"+matchnum;
        	} else if (Integer.parseInt(matchnum) <= 18) {
        		matchnum = "SF"+matchnum;
        	} else {
        		matchnum = "xF"+matchnum;
        	}
    	} else {
    		mtype = "";
    	}

    	Log.i("NewMatchSched","new sched for match: "+mtype+"-"+matchnum);
    	

    	matchSched.setMatchNum(matchnum);
    	matchSched.setRed1(r1.getText().toString());
    	matchSched.setRed2(r2.getText().toString());
    	matchSched.setRed3(r3.getText().toString());
    	matchSched.setBlue1(b1.getText().toString());
    	matchSched.setBlue2(b2.getText().toString());
    	matchSched.setBlue3(b3.getText().toString());
    	matchSched.setMatchType(mtype);
    	matchSched.setEvent(ev);
    	matchSched.setCreateTime(formattedDate);
         
    	new UpdateDBTask().execute();
    	finish();
    	Toast.makeText(getApplicationContext(), "Match saved", Toast.LENGTH_SHORT).show();
    	
    }
    
	private class UpdateDBTask extends AsyncTask<Void, Void, Void> {

		protected Void doInBackground(Void... voids) {

			DynamoDBManager.updateMatchSched(matchSched);

			return null;
		}
	}
}
