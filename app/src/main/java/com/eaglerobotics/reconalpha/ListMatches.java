package com.eaglerobotics.reconalpha;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;

import com.eaglerobotics.reconalpha.DynamoDBManager.TeamMatch;
import com.eaglerobotics.reconalpha.adapters.CustomArrayAdapter;

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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ListMatches extends Activity {

	private ArrayList<TeamMatch> items = null;
	String ev;
	private ProgressDialog pd;
	private Context context;



	private ArrayAdapter<TeamMatch> arrayAdapter = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_match_list);
		context = this;
		ev = Splash.settings.getString("event", "");
		new GetMatchListTask().execute();
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
	    	new ExportMatchData().execute();
//			new UpdateMatchListTask().execute();
	    	return true;
	    case R.id.action_refresh:
			new GetMatchListTask().execute();
	        return true; 
	    case R.id.action_home:
	        Intent hintent = new Intent(this, MainActivity.class);
	        startActivity(hintent);
	        return true; 
	    }
	    return false;
	}
	
	private class GetMatchListTask extends AsyncTask<Void, Void, Void> {

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

			items = DynamoDBManager.getMatchList(ev);
			Collections.sort(items, new byMatchComparator());
			return null;
		}

		protected void onPostExecute(Void result) {

	         ListView lv = (ListView) findViewById(R.id.matchListView);

	         // This is the array adapter, it takes the context of the activity as a first // parameter, the type of list view as a second parameter and your array as a third parameter	
	         if (pd!=null) {
				pd.dismiss();
			}

	         arrayAdapter = new CustomArrayAdapter(ListMatches.this, items);
	         lv.setAdapter(arrayAdapter); 
	         lv.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
					    int position, long id) {

			        Intent intent = new Intent(ListMatches.this, ShowMatch.class);
			        String t = items.get(position).getTeamNum();
			        String m = items.get(position).getMatchNum();

			        intent.putExtra("team",t);
			        intent.putExtra("match", m);
			        startActivity(intent);
					
				}
	        	 
	         });
			
			

		}
	}

	private class ExportMatchData extends AsyncTask<Void, Void, Void> {

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

	        File myFile = new File(Environment.getExternalStorageDirectory().getPath()+"/ReconAlpha/"+ev+"_matches.csv");
	        try {
				myFile.createNewFile();

	        FileOutputStream fOut = new FileOutputStream(myFile);
	        OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
	        myOutWriter.append("Event,Match,Team,GoodClicks,BadClicks,Pickable,AutoMove,Auto1,Auto2,HighGoal,Fast,Defender,Inbound,Driver,Catcher,Truss,NoSHow,Flimsy,Died,Pushable,Comments,Creator,CreateTime");
	        myOutWriter.append("\n");
	        
	        for (TeamMatch citem : items) {
	    	    myOutWriter.append(citem.getEventID());
	        	myOutWriter.append(",");	
	        	myOutWriter.append(citem.getMatchNum());
	        	myOutWriter.append(",");	
	        	myOutWriter.append(citem.getTeamNum());
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getTotes()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getBins()));
	        	myOutWriter.append(",");
                myOutWriter.append(Integer.toString(citem.getNoodles()));
                myOutWriter.append(",");
                myOutWriter.append(Integer.toString(citem.getPoints()));
                myOutWriter.append(",");
	        	myOutWriter.append(Boolean.toString(citem.getPickable()));
	        	myOutWriter.append(",");
	        	myOutWriter.append(Boolean.toString(citem.getAutoMove()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Boolean.toString(citem.getAutoTote()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Boolean.toString(citem.getAutoBin()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Boolean.toString(citem.getAutoStack()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Boolean.toString(citem.getFast()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Boolean.toString(citem.getStackTote()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Boolean.toString(citem.getStackBin()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Boolean.toString(citem.getDriver()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Boolean.toString(citem.getCarry()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Boolean.toString(citem.getNoodleBin()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Boolean.toString(citem.getCoopTote()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Boolean.toString(citem.getCoopStack()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Boolean.toString(citem.getNoodleFloor()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Boolean.toString(citem.getNoodleThrow()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Boolean.toString(citem.getDied()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append('"'+citem.getComments()+'"');
	        	myOutWriter.append(",");	
	        	myOutWriter.append(citem.getCreator());
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
	     	Toast.makeText(getApplicationContext(), "Match data exported", Toast.LENGTH_SHORT).show();
		}
	}
	
	private class UpdateMatchListTask extends AsyncTask<Void, Void, Void> {

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

			items = DynamoDBManager.getMatchList(ev);
			Collections.sort(items, new byMatchComparator());
			for (TeamMatch citem : items) {
				citem.setEventID(ev+"x");
				Log.i("key",citem.getKey());
				DynamoDBManager.updateTeamMatch(citem);
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
