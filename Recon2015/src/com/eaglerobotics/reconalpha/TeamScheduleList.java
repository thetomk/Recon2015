package com.eaglerobotics.reconalpha;

import java.util.ArrayList;
import java.util.Collections;

import com.eaglerobotics.reconalpha.DynamoDBManager.MatchSched;
import com.eaglerobotics.reconalpha.adapters.CustomSchedAdapter;

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
import android.widget.AdapterView.OnItemClickListener;

public class TeamScheduleList extends Activity {

	private ArrayList<MatchSched> items = null;
	private ArrayList<MatchSched> tmitems = null;
	String ev;
	private ProgressDialog pd;
	private Context context;
	private EditText teamparam;
	String tm;


	private ArrayAdapter<MatchSched> arrayAdapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_team_schedule_list);
		context = this;
		
		teamparam = (EditText)findViewById(R.id.teamParam); 
		
		Intent intent = getIntent();
		tm = intent.getStringExtra("team");
		if (tm != null) {
			teamparam.setText(tm);
			getTeamData(this.findViewById(R.layout.activity_team_schedule_list));
		}
		ev = Splash.settings.getString("event", "");

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
			getTeamData(this.findViewById(R.layout.activity_team_schedule_list));
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
	
	public void getTeamData(View view) {
		tm =  teamparam.getText().toString();
		new GetSchedListTask().execute();
	}
	
	private class GetSchedListTask extends AsyncTask<Void, Void, Void> {
		
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

			items = DynamoDBManager.getSchedList(ev);
			
			tmitems = new ArrayList<MatchSched>();
			
			for (MatchSched citem : items) {
				if (citem.getBlue1().equals(tm) || citem.getBlue2().equals(tm) || citem.getBlue3().equals(tm) ||
						citem.getRed1().equals(tm) || citem.getRed2().equals(tm) ||citem.getRed3().equals(tm)) {
					tmitems.add(citem);
				}
			}

			return null;
		}

		protected void onPostExecute(Void result) {
			
			Collections.sort(tmitems, new byNaturalMatchTypeComparator());
	        ListView lv = (ListView) findViewById(R.id.schedListView);
	         
			if (pd!=null) {
				pd.dismiss();
			}
			teamparam.setFocusableInTouchMode(false);
			teamparam.setFocusable(false);
			teamparam.setFocusableInTouchMode(true);
			teamparam.setFocusable(true);
		     
	         // This is the array adapter, it takes the context of the activity as a first // parameter, the type of list view as a second parameter and your array as a third parameter

	         arrayAdapter = new CustomSchedAdapter(TeamScheduleList.this, tmitems);
	         lv.setAdapter(arrayAdapter); 
	         
	         
	         lv.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
					    int position, long id) {

			        Intent intent = new Intent(TeamScheduleList.this, ScoutOurMatch.class);

			        String m = tmitems.get(position).getMatchNum();

			        intent.putExtra("match", m);
			        startActivity(intent);
					
				}
	        	 
	         });
			
			

		}
	}

}
