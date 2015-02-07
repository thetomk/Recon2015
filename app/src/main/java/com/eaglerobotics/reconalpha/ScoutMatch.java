package com.eaglerobotics.reconalpha;

import java.util.ArrayList;
import java.util.Collections;

import com.eaglerobotics.reconalpha.DynamoDBManager.MatchSched;
import com.eaglerobotics.reconalpha.DynamoDBManager.TeamMatch;
import com.eaglerobotics.reconalpha.adapters.CustomMatchAdapter;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class ScoutMatch extends Activity {

	private ArrayList<TeamMatch> items = null;
	private ArrayAdapter<TeamMatch> arrayAdapter = null;
	private MatchSched sched;
	private EditText match_param;
	private String match;
	private String event;
	private ProgressDialog pd;
	private Context context;
	private String mat;

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scout_match);
		context = this;

		match_param = (EditText)findViewById(R.id.editText1); 
		event = Splash.settings.getString("event", "");
		
		Intent intent = getIntent();
		mat = intent.getStringExtra("match");
		if (mat != null) {
			match_param.setText(mat);
		     match_param.setFocusableInTouchMode(false);
		     match_param.setFocusable(false);
		     match_param.setFocusableInTouchMode(true);
		     match_param.setFocusable(true);
			getMatchData(this.findViewById(R.layout.activity_scout_match));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings_refresh_home_menu, menu);
		return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
	    switch(item.getItemId()){
	    case R.id.action_settings:
	        Intent intent = new Intent(this, Prefs.class);
	        startActivity(intent);
	        return true; 
	    case R.id.action_refresh:
			getMatchData(this.findViewById(R.layout.activity_scout_match));
	        return true;  
	    case R.id.action_home:
	        Intent hintent = new Intent(this, MainActivity.class);
	        startActivity(hintent);
	        return true; 
	    }
	    return false;
	}
	
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {

	    View v = getCurrentFocus();
	    boolean ret = super.dispatchTouchEvent(event);

	    if (v instanceof EditText) {
	        View w = getCurrentFocus();
	        int scrcoords[] = new int[2];
	        w.getLocationOnScreen(scrcoords);
	        float x = event.getRawX() + w.getLeft() - scrcoords[0];
	        float y = event.getRawY() + w.getTop() - scrcoords[1];

	        if (event.getAction() == MotionEvent.ACTION_UP && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom()) ) { 

	            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	            imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
	        }
	    }
	return ret;
	}
	
	public void getMatchData(View view) {
		match =  match_param.getText().toString();
		new GetTeamListTask().execute();
	}
	

	
	private class GetTeamListTask extends AsyncTask<Void, Void, Void> {
		
		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(context);
//			pd.setTitle("Processing...");
			pd.setMessage("Please wait.");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}

		protected Void doInBackground(Void... inputs) {

			items = DynamoDBManager.getTeamsforMatch(event, match);
			sched = DynamoDBManager.getSchedMatch(event, match);
			
			for (TeamMatch curritem : items) {
		    	curritem.setAlliance("black");
		    	String s = curritem.getTeamNum();
		    	if (sched != null) {
		    		if ((s.equals(sched.getBlue1())) || (s.equals(sched.getBlue2())) || (s.equals(sched.getBlue3()))) {
		    			curritem.setAlliance("blue");
		    		}
		    		if ((s.equals(sched.getRed1())) || (s.equals(sched.getRed2())) || (s.equals(sched.getRed3()))) {
		    			curritem.setAlliance("red");
		    		}
		    	}
			}

			Collections.sort(items, new byAllianceComparator());


			return null;
		}

		protected void onPostExecute(Void result) {

	         ListView lv = (ListView) findViewById(R.id.teamList);
	         
			if (pd!=null) {
				pd.dismiss();
			}
		     match_param.setFocusableInTouchMode(false);
		     match_param.setFocusable(false);
		     match_param.setFocusableInTouchMode(true);
		     match_param.setFocusable(true);
				
	         if (items.size() == 0 && sched == null) {
	         	Toast.makeText(getApplicationContext(), "No teams found for that match", Toast.LENGTH_SHORT).show();
	         } else {
	        	 arrayAdapter = new CustomMatchAdapter(ScoutMatch.this, items, sched);
	        	 lv.setAdapter(arrayAdapter); 
	         }
	         
	         lv.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
					    int position, long id) {

			        Intent intent = new Intent(ScoutMatch.this, ViewTeam.class);
			        String t = items.get(position).getTeamNum();

			        intent.putExtra("team",t);
			        startActivity(intent);
					
				}
	        	 
	         });
	         
		}
		

	
	

	}


}


