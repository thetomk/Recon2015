package com.eaglerobotics.reconalpha;

import java.util.ArrayList;
import java.util.Collections;

import com.eaglerobotics.reconalpha.DynamoDBManager.TeamData;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class TeamList extends Activity {

	private ArrayList<TeamData> items = null;
	String ev;
	private ProgressDialog pd;
	private Context context;


	private ArrayAdapter<TeamData> arrayAdapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_team_list);
		context = this;
		ev = Splash.settings.getString("event", "");
		new GetTeamListTask().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.export_settings_refresh_home_menu, menu);
		return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
	    switch(item.getItemId()){
	    case R.id.action_settings:
	        Intent intent = new Intent(this, Prefs.class);
	        startActivity(intent);
	        return true; 
	    case R.id.action_export:
//	    	new ExportSchedData().execute();
	    	return true;
	    case R.id.action_refresh:
			new GetTeamListTask().execute();
	        return true;  
	    case R.id.action_home:
	        Intent hintent = new Intent(this, MainActivity.class);
	        startActivity(hintent);
	        return true; 
	    }
	    return false;
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

			items = DynamoDBManager.getTeamList(ev);
//			Collections.sort(items, new byMatchTypeComparator());

			return null;
		}

		protected void onPostExecute(Void result) {

	         ListView lv = (ListView) findViewById(R.id.teamListView);
	         
			if (pd!=null) {
				pd.dismiss();
			}

	         // This is the array adapter, it takes the context of the activity as a first // parameter, the type of list view as a second parameter and your array as a third parameter

//	         arrayAdapter = new CustomTeamListAdapter(TeamList.this, items);
	         lv.setAdapter(arrayAdapter); 
	         
	         
	         lv.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
					    int position, long id) {
					

			        Intent intent = new Intent(TeamList.this, ViewTeam.class);
			        String m = items.get(position).getTeamNum();
			        intent.putExtra("team", m);
			        startActivity(intent);
					
				}
	        	 
	         });
			
			

		}
	}
}
