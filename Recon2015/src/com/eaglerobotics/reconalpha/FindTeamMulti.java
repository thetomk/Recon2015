package com.eaglerobotics.reconalpha;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.eaglerobotics.reconalpha.DynamoDBManager.TeamData;
import com.eaglerobotics.reconalpha.DynamoDBManager.TeamMatch;
import com.eaglerobotics.reconalpha.adapters.CustomTeamDataAdapter;
import com.eaglerobotics.reconalpha.util.TeamInfo;


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
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ToggleButton;

public class FindTeamMulti extends Activity {

	private ArrayList<TeamData> items = null;
	private ArrayList<TeamInfo> teams = null;
	String ev;
	private ProgressDialog pd;
	private Context context;
	String field_choice;
	ToggleButton tb;
	Set<String> fields = new HashSet<String>();


	private ArrayAdapter<TeamInfo> arrayAdapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_team_multi);
		context = this;
		ev = Splash.settings.getString("event", "");

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
	    	new ExportTeamData().execute();
//	    	new UpdateTeamEvent().execute();
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
	
	public void doRating(View view) {
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
			
			fields.clear();
			tb = (ToggleButton)findViewById(R.id.toggleAutoMove);
			if (tb.isChecked()) { fields.add("AutoMove");}
			tb = (ToggleButton)findViewById(R.id.toggleAutoTote);
			if (tb.isChecked()) { fields.add("AutoTote");}
			tb = (ToggleButton)findViewById(R.id.toggleAutoBin);
			if (tb.isChecked()) { fields.add("AutoBin");}
			tb = (ToggleButton)findViewById(R.id.toggleAutoStack);
			if (tb.isChecked()) { fields.add("AutoStack");}
			tb = (ToggleButton)findViewById(R.id.toggleFast);
			if (tb.isChecked()) { fields.add("Fast");}
			tb = (ToggleButton)findViewById(R.id.toggleStackTote);
			if (tb.isChecked()) { fields.add("StackTote");}
			tb = (ToggleButton)findViewById(R.id.toggleStackBin);
			if (tb.isChecked()) { fields.add("StackBin");}
			tb = (ToggleButton)findViewById(R.id.toggleNoodleBin);
			if (tb.isChecked()) { fields.add("NoodleBin");}
			tb = (ToggleButton)findViewById(R.id.toggleCarry);
			if (tb.isChecked()) { fields.add("Carry");}
			tb = (ToggleButton)findViewById(R.id.toggleDriver);
			if (tb.isChecked()) { fields.add("Driver");}
			tb = (ToggleButton)findViewById(R.id.toggleCoopTote);
			if (tb.isChecked()) { fields.add("CoopTote");}
			tb = (ToggleButton)findViewById(R.id.toggleCoopStack);
			if (tb.isChecked()) { fields.add("CoopStack");}
			tb = (ToggleButton)findViewById(R.id.toggleDied);
			if (tb.isChecked()) { fields.add("Died");}
			tb = (ToggleButton)findViewById(R.id.toggleNoodleFloor);
			if (tb.isChecked()) { fields.add("NoodleFloor");}
			tb = (ToggleButton)findViewById(R.id.toggleNoodleThrow);
			if (tb.isChecked()) { fields.add("NoodleThrow");}
			tb = (ToggleButton)findViewById(R.id.togglePick);
			if (tb.isChecked()) { fields.add("Pickable");}
			tb = (ToggleButton)findViewById(R.id.toggleOAuto);
			if (tb.isChecked()) { fields.add("OAuto");}
			tb = (ToggleButton)findViewById(R.id.toggleOTele);
			if (tb.isChecked()) { fields.add("OTele");}
			tb = (ToggleButton)findViewById(R.id.toggleContrib);
			if (tb.isChecked()) { fields.add("Contrib");}
			tb = (ToggleButton)findViewById(R.id.toggleDTele);
			if (tb.isChecked()) { fields.add("DTele");}
			
		}

		protected Void doInBackground(Void... inputs) {

			int numMatches, rating, fieldCount, numScores;
			float oauto=0, otele=0, ocontrib=0, dfoul=0, dtele=0;
			float rawvalue=0;
			
			String ratingLabel;
			
			teams = new ArrayList<TeamInfo>();
			fieldCount = fields.size();

			items = DynamoDBManager.getTeamList(ev);

			for (TeamData curr : items) {
				numScores = curr.getNumScores();

				if ((float) curr.getOAuto()/(float) numScores > oauto) {
					oauto = (float)curr.getOAuto()/(float) numScores;					
				}
				if ((float) curr.getOTele()/(float) numScores > otele) {
					otele = (float)curr.getOTele()/(float) numScores;					
				}
				if ((float) curr.getDFoul()/(float) numScores > dfoul) {
					dfoul = (float)curr.getDFoul()/(float) numScores;					
				}
				if ((float) curr.getDTele()/(float) numScores > dtele) {
					dtele = (float)curr.getDTele()/(float) numScores;					
				}

				if ( (float)((curr.getOAuto()+curr.getOTele())-(curr.getDAuto()+curr.getDTele()))/(float)numScores > ocontrib) {
					ocontrib = (float)((curr.getOAuto()+curr.getOTele())-(curr.getDAuto()+curr.getDTele()))/(float)numScores;					
				}
			}
			
			
			for (TeamData curr : items) {
				TeamInfo calcTeam = new TeamInfo();
				calcTeam.setTeamNum(curr.getTeamNum());
				numMatches = curr.getNumMatches();
				numScores = curr.getNumScores();
				rawvalue = (float) 0;
				if (numMatches > 0) {
					if (fields.contains("Pickable")) {rawvalue += (float)curr.getPickable()/(float)numMatches;}
					if (fields.contains("Fast")) {rawvalue += (float)curr.getFast()/(float)numMatches;} 
					if (fields.contains("Carry")) {rawvalue += (float)curr.getCarry()/(float)numMatches;}  
					if (fields.contains("Driver")) {rawvalue += (float)curr.getDriver()/(float)numMatches;}  
					if (fields.contains("StackTote")) {rawvalue += (float)curr.getStackTote()/(float)numMatches;}  
					if (fields.contains("StackBin")) {rawvalue += (float)curr.getStackBin()/(float)numMatches;}  
					if (fields.contains("NoodleBin")) {rawvalue += (float)curr.getNoodleBin()/(float)numMatches;}  
					if (fields.contains("AutoMove")) {rawvalue += (float)curr.getAutoMove()/(float)numMatches;}  
					if (fields.contains("AutoTote")) {rawvalue += (float)curr.getAutoTote()/(float)numMatches;}  
					if (fields.contains("AutoBin")) {rawvalue += (float)curr.getAutoBin()/(float)numMatches;}  
					if (fields.contains("AutoStack")) {rawvalue += (float)curr.getAutoStack()/(float)numMatches;}  
					if (fields.contains("CoopTote")) {rawvalue += ((float)curr.getCoopTote()/(float)numMatches);}  
					if (fields.contains("Died")) {rawvalue += 1 - ((float)curr.getDied()/(float)numMatches);}  
					if (fields.contains("CoopStack")) {rawvalue += ((float)curr.getCoopStack()/(float)numMatches);}  
					if (fields.contains("NoodleFloor")) {rawvalue += ((float)curr.getNoodleFloor()/(float)numMatches);}  
					if (fields.contains("NoodleThrow")) {rawvalue += ((float)curr.getNoodleThrow()/(float)numMatches);}  
					if (fields.contains("Rating")) {rawvalue += curr.getGoodPct();}					
				}

				if (fields.contains("OAuto")) {
					rawvalue += ((float)curr.getOAuto()/(float)numScores) / oauto ;
				}
				if (fields.contains("OTele")) {
					rawvalue += ((float)curr.getOTele()/(float)numScores) / otele;
				}
				if (fields.contains("Contrib")) {
					rawvalue += ( (float)((curr.getOAuto()+curr.getOTele())-(curr.getDAuto()+curr.getDTele())) /(float)numScores) / ocontrib;
				}
				if (fields.contains("DFoul")) {
					rawvalue += 1 - (((float)curr.getDFoul()/(float)numScores) / dfoul);
				}
				if (fields.contains("DTele")) {
					rawvalue += 1 - (((float)curr.getDTele()/(float)numScores) / dtele);
				}
				
				rating = (int) ((rawvalue / fieldCount)*100);
				ratingLabel = rating + "%";
				

				calcTeam.setRating(rating);
				calcTeam.setRatingLabel(ratingLabel);
				
				teams.add(calcTeam);
			}
			Collections.sort(teams, new byRatingComparator());
//			Collections.sort(items, new byNaturalMatchTypeComparator());

			return null;
		}

		protected void onPostExecute(Void result) {

	         ListView lv = (ListView) findViewById(R.id.teamListView);
	         
			if (pd!=null) {
				pd.dismiss();
			}

	         // This is the array adapter, it takes the context of the activity as a first // parameter, the type of list view as a second parameter and your array as a third parameter

	         arrayAdapter = new CustomTeamDataAdapter(FindTeamMulti.this, teams);
	         lv.setAdapter(arrayAdapter); 
	         
	         
	         lv.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
					    int position, long id) {
					

			        Intent intent = new Intent(FindTeamMulti.this, ViewTeam.class);
			        String m = teams.get(position).getTeamNum();
			        intent.putExtra("team", m);
			        startActivity(intent);
					
				}
	        	 
	         });
			
			

		}
	}
	
	private class ExportTeamData extends AsyncTask<Void, Void, Void> {

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

	        File myFile = new File(Environment.getExternalStorageDirectory().getPath()+"/ReconAlpha/"+ev+"_teams.csv");
	        try {
				myFile.createNewFile();

	        FileOutputStream fOut = new FileOutputStream(myFile);
	        OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
	        myOutWriter.append("Event,Team,Matches,GoodPct,Pickable,AutoMove,AutoTote,AutoBin,AutoStack,Fast,StackTote,StackBin,"+
	        		"Driver,Carry,NoodleBin,NoodleFloor,NoodleThrow,Died,CoopTote,CoopStack,NumScores,OffAuto,OffTele,OffFoul,OffTotal,DefAuto," +
	        		"DefTele,DefFoul,DefTotal,Chosen,Creator,CreateTime");
	        myOutWriter.append("\n");
	        
	        for (TeamData citem : items) {
	    	    myOutWriter.append(citem.getEventID());
	        	myOutWriter.append(",");	
	        	myOutWriter.append(citem.getTeamNum());
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getNumMatches()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Float.toString(citem.getGoodPct()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getPickable()));
	        	myOutWriter.append(",");
	        	myOutWriter.append(Integer.toString(citem.getAutoMove()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getAutoTote()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getAutoBin()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getAutoStack()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getFast()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getStackTote()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getStackBin()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getDriver()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getCarry()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getNoodleBin()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getNoodleFloor()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getNoodleThrow()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getDied()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getCoopTote()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getCoopStack()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getNumScores()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getOAuto()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getOTele()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getOFoul()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getOTotal()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getDAuto()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getDTele()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getDFoul()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getDTotal()));
	        	myOutWriter.append(",");	
	        	myOutWriter.append(Integer.toString(citem.getChosen()));
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
	     	Toast.makeText(getApplicationContext(), "Team data exported", Toast.LENGTH_SHORT).show();
		}
	} 
	
	private class UpdateTeamEvent extends AsyncTask<Void, Void, Void> {

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

			for (TeamData citem : items) {
				citem.setEventID(ev+"x");
				Log.i("team",citem.getTeamNum());
				DynamoDBManager.updateTeamData(citem);
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
