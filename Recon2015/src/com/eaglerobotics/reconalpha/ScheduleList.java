package com.eaglerobotics.reconalpha;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;

import com.eaglerobotics.reconalpha.DynamoDBManager.MatchSched;
import com.eaglerobotics.reconalpha.DynamoDBManager.TeamMatch;
import com.eaglerobotics.reconalpha.adapters.CustomSchedAdapter;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ScheduleList extends Activity {

	private ArrayList<MatchSched> items = null;
	String ev;
	private ProgressDialog pd;
	private Context context;


//	private ArrayAdapter<String> arrayAdapter = null;
	private ArrayAdapter<MatchSched> arrayAdapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule_list);
		context = this;
		ev = Splash.settings.getString("event", "");
		new GetSchedListTask().execute();
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
	    	new ExportSchedData().execute();
//	    	new UpdateSchedEvent().execute();
	    	return true;
	    case R.id.action_refresh:
			new GetSchedListTask().execute();
	        return true;  
	    case R.id.action_home:
	        Intent hintent = new Intent(this, MainActivity.class);
	        startActivity(hintent);
	        return true; 
	    }
	    return false;
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
//			Collections.sort(items, new byMatchTypeComparator());
			Collections.sort(items, new byNaturalMatchTypeComparator());

			return null;
		}

		protected void onPostExecute(Void result) {

	         ListView lv = (ListView) findViewById(R.id.schedListView);
	         
			if (pd!=null) {
				pd.dismiss();
			}

	         // This is the array adapter, it takes the context of the activity as a first // parameter, the type of list view as a second parameter and your array as a third parameter

	         arrayAdapter = new CustomSchedAdapter(ScheduleList.this, items);
	         lv.setAdapter(arrayAdapter); 
	         
	         
	         lv.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
					    int position, long id) {
					

			        Intent intent = new Intent(ScheduleList.this, ScoutMatch.class);
			        String m = items.get(position).getMatchNum();
			        intent.putExtra("match", m);
			        startActivity(intent);
					
				}
	        	 
	         });
			
			

		}
	}
	/*
	private class ExportSchedData extends AsyncTask<Void, Void, Void> {

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

	        File myFile = new File(Environment.getExternalStorageDirectory().getPath()+"/ReconAlpha/"+ev+"_sched.csv");
	        try {
				myFile.createNewFile();

	        FileOutputStream fOut = new FileOutputStream(myFile);
	        OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
	        myOutWriter.append("Event,Match,MatchType,Blue1,Blue2,Blue3,Red1,Red2,Red3,BlueAuto,BlueTele,BlueFoul,BlueTotal,RedAuto,RedTele,RedFoul,RedTotal,CreateTime");
	        myOutWriter.append("\n");
	        
	        for (MatchSched citem : items) {
	    	    myOutWriter.append(citem.getEvent());
	        	myOutWriter.append(",");	
	        	myOutWriter.append(citem.getMatchNum());
	        	myOutWriter.append(",");	
	        	myOutWriter.append(citem.getMatchType());
	        	myOutWriter.append(",");	
	        	myOutWriter.append(citem.getBlue1());
	        	myOutWriter.append(",");	
	        	myOutWriter.append(citem.getBlue2());
	        	myOutWriter.append(",");	
	        	myOutWriter.append(citem.getBlue3());
	        	myOutWriter.append(",");	
	        	myOutWriter.append(citem.getRed1());
	        	myOutWriter.append(",");	
	        	myOutWriter.append(citem.getRed2());
	        	myOutWriter.append(",");	
	        	myOutWriter.append(citem.getRed3());
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getBlueAuto()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getBlueTele()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getBlueFoul()));
	        	myOutWriter.append(",");	
//	        	myOutWriter.append(Integer.toString(citem.getBlueEndGame()));
//	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getBlueTotal()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getRedAuto()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getRedTele()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getRedFoul()));
	        	myOutWriter.append(",");	
//	        	myOutWriter.append(Integer.toString(citem.getRedEndGame()));
//	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getRedTotal()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(citem.getCreateTime());	
	        	myOutWriter.append("\n");	
	        }
            myOutWriter.close();
            fOut.close();
	        
	        
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(Void result) {

	         if (pd!=null) {
				pd.dismiss();
			}
	     	Toast.makeText(getApplicationContext(), "Schedule data exported", Toast.LENGTH_SHORT).show();
		}
	}
*/
	
	private class ExportSchedData extends AsyncTask<Void, Void, Void> {

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

	        File myFile = new File(Environment.getExternalStorageDirectory().getPath()+"/ReconAlpha/"+ev+"_results.csv");
	        try {
				myFile.createNewFile();

	        FileOutputStream fOut = new FileOutputStream(myFile);
	        OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
	        myOutWriter.append("Event,Match,MatchType,Team,OAuto,OTele,OFoul,OTotal,DAuto,DTele,DFoul,DTotal");
	        myOutWriter.append("\n");
	        
	        for (MatchSched citem : items) {
	    	    myOutWriter.append(citem.getEvent());
	        	myOutWriter.append(",");	
	        	myOutWriter.append(citem.getMatchNum());
	        	myOutWriter.append(",");	
	        	myOutWriter.append(citem.getMatchType());
	        	myOutWriter.append(",");	
	        	myOutWriter.append(citem.getBlue1());
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getBlueAuto()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getBlueTele()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getBlueFoul()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getBlueTotal()));
	        	myOutWriter.append(",");		
	        	myOutWriter.append(Integer.toString(citem.getRedAuto()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getRedTele()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getRedFoul()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getRedTotal()));
	        	myOutWriter.append("\n");	

	    	    myOutWriter.append(citem.getEvent());
	        	myOutWriter.append(",");	
	        	myOutWriter.append(citem.getMatchNum());
	        	myOutWriter.append(",");	
	        	myOutWriter.append(citem.getMatchType());
	        	myOutWriter.append(",");	
	        	myOutWriter.append(citem.getBlue2());
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getBlueAuto()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getBlueTele()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getBlueFoul()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getBlueTotal()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getRedAuto()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getRedTele()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getRedFoul()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getRedTotal()));
	        	myOutWriter.append("\n");	

	    	    myOutWriter.append(citem.getEvent());
	        	myOutWriter.append(",");	
	        	myOutWriter.append(citem.getMatchNum());
	        	myOutWriter.append(",");	
	        	myOutWriter.append(citem.getMatchType());
	        	myOutWriter.append(",");	
	        	myOutWriter.append(citem.getBlue3());
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getBlueAuto()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getBlueTele()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getBlueFoul()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getBlueTotal()));
	        	myOutWriter.append(",");		
	        	myOutWriter.append(Integer.toString(citem.getRedAuto()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getRedTele()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getRedFoul()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getRedTotal()));
	        	myOutWriter.append("\n");	

	    	    myOutWriter.append(citem.getEvent());
	        	myOutWriter.append(",");	
	        	myOutWriter.append(citem.getMatchNum());
	        	myOutWriter.append(",");	
	        	myOutWriter.append(citem.getMatchType());
	        	myOutWriter.append(",");	
	        	myOutWriter.append(citem.getRed1());
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getRedAuto()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getRedTele()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getRedFoul()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getRedTotal()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getBlueAuto()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getBlueTele()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getBlueFoul()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getBlueTotal()));
	        	myOutWriter.append("\n");	
	    	    myOutWriter.append(citem.getEvent());
	        	myOutWriter.append(",");	
	        	myOutWriter.append(citem.getMatchNum());
	        	myOutWriter.append(",");	
	        	myOutWriter.append(citem.getMatchType());
	        	myOutWriter.append(",");	
	        	myOutWriter.append(citem.getRed2());
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getRedAuto()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getRedTele()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getRedFoul()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getRedTotal()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getBlueAuto()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getBlueTele()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getBlueFoul()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getBlueTotal()));
	        	myOutWriter.append("\n");	
	    	    myOutWriter.append(citem.getEvent());
	        	myOutWriter.append(",");	
	        	myOutWriter.append(citem.getMatchNum());
	        	myOutWriter.append(",");	
	        	myOutWriter.append(citem.getMatchType());
	        	myOutWriter.append(",");	
	        	myOutWriter.append(citem.getRed3());
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getRedAuto()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getRedTele()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getRedFoul()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getRedTotal()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getBlueAuto()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getBlueTele()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getBlueFoul()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getBlueTotal()));
	        	myOutWriter.append("\n");	
	        }
            myOutWriter.close();
            fOut.close();
	        
	        
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(Void result) {

	         if (pd!=null) {
				pd.dismiss();
			}
	     	Toast.makeText(getApplicationContext(), "Schedule data exported", Toast.LENGTH_SHORT).show();
		}
	}
	
	private class UpdateSchedEvent extends AsyncTask<Void, Void, Void> {

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

			for (MatchSched citem : items) {
				citem.setEvent(ev+"x");
				Log.i("key",citem.getMatchType()+citem.getMatchNum());
				DynamoDBManager.updateMatchSched(citem);
			}
			return null;
		}

		protected void onPostExecute(Void result) {

	         // This is the array adapter, it takes the context of the activity as a first // parameter, the type of list view as a second parameter and your array as a third parameter	
	         if (pd!=null) {
				pd.dismiss();
			}

		}
	}
	
}
