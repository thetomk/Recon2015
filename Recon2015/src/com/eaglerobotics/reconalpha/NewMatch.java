package com.eaglerobotics.reconalpha;


import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.eaglerobotics.reconalpha.DynamoDBManager.TeamData;
import com.eaglerobotics.reconalpha.DynamoDBManager.TeamMatch;
import com.google.gson.Gson;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;



public class NewMatch extends Activity {

	int tcnum = 0;
	int bcnum = 0;
	int ncnum = 0;
	TextView tc;
	TextView bc;
	TextView nc;
	EditText comments;
	TextView matchNum;
	TextView teamNum;
	TeamMatch teamMatch = new TeamMatch();
	TeamMatch origTeamMatch = new TeamMatch();
	TeamData teamData = new TeamData();
	String ev;
	String createdby;
	ToggleButton bt_automove;
	ToggleButton bt_autotote;
	ToggleButton bt_autostack;
	ToggleButton bt_autobin;
	ToggleButton bt_fast;
	ToggleButton bt_stacktote;
	ToggleButton bt_stackbin;
	ToggleButton bt_driver;
	ToggleButton bt_carry;
	ToggleButton bt_noodlebin;
	ToggleButton bt_coopstack;
	ToggleButton bt_cooptote;
	ToggleButton bt_died;
	ToggleButton bt_noodlefloor;
	ToggleButton bt_noodlethrow;
	ToggleButton bt_pickable;
	Boolean editmode;
	String formattedDate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_match);
		
		Intent intent = getIntent();
		origTeamMatch = (new Gson()).fromJson(intent.getStringExtra("TeamMatch"),TeamMatch.class);
		if (origTeamMatch != null) {
			editmode = true;
		} else {editmode = false;}
		
		ev = Splash.settings.getString("event", "");
		createdby = Splash.settings.getString("user", "");
		
	    tc = (TextView)findViewById(R.id.toteCount); 
	    tc.setText(Integer.toString(tcnum));
	    bc = (TextView)findViewById(R.id.binCount); 
	    bc.setText(Integer.toString(bcnum));
	    nc = (TextView)findViewById(R.id.noodleCount); 
	    nc.setText(Integer.toString(ncnum));
	    comments = (EditText)findViewById(R.id.comments); 
		matchNum = (TextView)findViewById(R.id.editMatch);
		teamNum = (TextView)findViewById(R.id.editTeam);
		
		bt_automove = (ToggleButton)findViewById(R.id.toggleAutoMove);
		bt_autotote = (ToggleButton)findViewById(R.id.toggleAutoTote);
		bt_autobin = (ToggleButton)findViewById(R.id.toggleAutoBin);
		bt_autostack = (ToggleButton)findViewById(R.id.toggleAutoStack);
		bt_fast = (ToggleButton)findViewById(R.id.toggleFast);
		
		bt_stacktote = (ToggleButton)findViewById(R.id.toggleStackTote);
		bt_stackbin = (ToggleButton)findViewById(R.id.toggleStackBin);
		bt_carry = (ToggleButton)findViewById(R.id.toggleCarry);
		bt_noodlebin = (ToggleButton)findViewById(R.id.toggleNoodleBin);
		bt_driver = (ToggleButton)findViewById(R.id.toggleDriver);
		
		bt_cooptote = (ToggleButton)findViewById(R.id.toggleCoopTote);
		bt_coopstack = (ToggleButton)findViewById(R.id.toggleCoopStack);
		bt_died = (ToggleButton)findViewById(R.id.toggleDied);
		bt_noodlefloor = (ToggleButton)findViewById(R.id.toggleNoodleFloor);
		bt_noodlethrow = (ToggleButton)findViewById(R.id.toggleNoodleThrow);
		bt_pickable = (ToggleButton)findViewById(R.id.togglePick);
		
		if (editmode) {
			fillInFields();
		}
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
	
    public void countTote(View view) {
        tcnum++;
	    tc.setText(Integer.toString(tcnum));
	}
    public void countBin(View view) {
       bcnum++;
	   bc.setText(Integer.toString(bcnum));
	}
    public void countNoodle(View view) {
       ncnum++;
 	   nc.setText(Integer.toString(ncnum));
 	}
    
    public void saveData(View view) {
    	

    	Calendar c = Calendar.getInstance();
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	formattedDate = df.format(c.getTime());    
    	
    	String key = teamNum.getText().toString() + "-" + matchNum.getText().toString();
    	Log.i("NewMatch","new match for key: "+key);
    	
    	teamMatch.setKey(key);
    	teamMatch.setMatchNum(matchNum.getText().toString());
    	teamMatch.setTeamNum(teamNum.getText().toString());
    	teamMatch.setTotes(tcnum);
    	teamMatch.setBins(bcnum);
    	teamMatch.setNoodles(ncnum);
    	teamMatch.setComments(comments.getText().toString());
    	teamMatch.setCreateTime(formattedDate);
    	teamMatch.setEventID(ev);
    	teamMatch.setCreator(createdby);
    	teamMatch.setAutoMove(bt_automove.isChecked());
    	teamMatch.setAutoTote(bt_autotote.isChecked());
    	teamMatch.setAutoStack(bt_autostack.isChecked());
    	teamMatch.setAutoBin(bt_autobin.isChecked());
    	teamMatch.setFast(bt_fast.isChecked());
    	teamMatch.setStackTote(bt_stacktote.isChecked());
    	teamMatch.setStackBin(bt_stackbin.isChecked());
    	teamMatch.setCarry(bt_carry.isChecked());
    	teamMatch.setNoodleBin(bt_noodlebin.isChecked());
    	teamMatch.setDriver(bt_driver.isChecked());
    	teamMatch.setCoopTote(bt_cooptote.isChecked());
    	teamMatch.setCoopStack(bt_coopstack.isChecked());
    	teamMatch.setDied(bt_died.isChecked());
    	teamMatch.setNoodleFloor(bt_noodlefloor.isChecked());
    	teamMatch.setNoodleThrow(bt_noodlethrow.isChecked());
    	teamMatch.setPickable(bt_pickable.isChecked());

         
    	new UpdateDBTask().execute();
    	finish();
    	Toast.makeText(getApplicationContext(), "Match saved", Toast.LENGTH_SHORT).show();
    	
    }
    
	private class UpdateDBTask extends AsyncTask<Void, Void, Void> {

		protected Void doInBackground(Void... voids) {
			int numMatches;
			float oldpct;

			DynamoDBManager.updateTeamMatch(teamMatch);
			
			teamData = DynamoDBManager.getTeamData(teamMatch.getEventID(), teamMatch.getTeamNum());
			
			if (editmode) {
				backOutTeamData();
				if (!(teamMatch.getKey().equals(origTeamMatch.getKey()))) {
					DynamoDBManager.deleteTeamMatch(origTeamMatch);
				}

			}
			
			if (teamData == null) {
				numMatches = 1;
				oldpct = 0;
				teamData = new TeamData();
			} else {
				numMatches = teamData.getNumMatches() + 1;
//				oldpct = teamData.getGoodPct() * (numMatches - 1);
			}
			
//			float gp = (float) teamMatch.getGoodPts();
//			float bp = (float) teamMatch.getBadPts();
//			float gpct = (gp / (gp+ bp));

//			float newpct = (oldpct + gpct)/(numMatches);

			
			teamData.setEventID(ev);
			teamData.setTeamNum(teamNum.getText().toString());
			teamData.setNumMatches(numMatches);
			teamData.setTotes(teamData.getTotes() + (teamMatch.getTotes()));
			teamData.setBins(teamData.getBins() + (teamMatch.getBins()));
			teamData.setNoodles(teamData.getNoodles() + (teamMatch.getNoodles()));
			teamData.setAutoMove(teamData.getAutoMove() + (teamMatch.getAutoMove()?1:0));
			teamData.setAutoTote(teamData.getAutoTote() + (teamMatch.getAutoTote()?1:0));
			teamData.setAutoBin(teamData.getAutoBin() + (teamMatch.getAutoBin()?1:0));
			teamData.setAutoStack(teamData.getAutoStack() + (teamMatch.getAutoStack()?1:0));
			teamData.setFast(teamData.getFast() + (teamMatch.getFast()?1:0));
			teamData.setStackTote(teamData.getStackTote() + (teamMatch.getStackTote()?1:0));
			teamData.setStackBin(teamData.getStackBin() + (teamMatch.getStackBin()?1:0));
			teamData.setCarry(teamData.getCarry() + (teamMatch.getCarry()?1:0));
			teamData.setNoodleBin(teamData.getNoodleBin() + (teamMatch.getNoodleBin()?1:0));
			teamData.setDriver(teamData.getDriver() + (teamMatch.getDriver()?1:0));
			teamData.setCoopTote(teamData.getCoopTote() + (teamMatch.getCoopTote()?1:0));
			teamData.setCoopStack(teamData.getCoopStack() + (teamMatch.getCoopStack()?1:0));
			teamData.setDied(teamData.getDied() + (teamMatch.getDied()?1:0));
			teamData.setNoodleFloor(teamData.getNoodleFloor() + (teamMatch.getNoodleFloor()?1:0));
			teamData.setNoodleThrow(teamData.getNoodleThrow() + (teamMatch.getNoodleThrow()?1:0));
			teamData.setPickable(teamData.getPickable() + (teamMatch.getPickable()?1:0));

			teamData.setCreator(createdby);
			teamData.setCreateTime(formattedDate);
			
			DynamoDBManager.updateTeamData(teamData);
			
			return null;
		}
	}
	
	private void fillInFields() {
		
		tc.setText(Integer.toString(origTeamMatch.getTotes()));
		bc.setText(Integer.toString(origTeamMatch.getBins()));
		nc.setText(Integer.toString(origTeamMatch.getNoodles()));
		tcnum = origTeamMatch.getTotes();
		bcnum = origTeamMatch.getBins();
		ncnum = origTeamMatch.getNoodles();
		comments.setText(origTeamMatch.getComments());
		matchNum.setText(origTeamMatch.getMatchNum());
		teamNum.setText(origTeamMatch.getTeamNum());
		bt_automove.setChecked(origTeamMatch.getAutoMove());
		bt_autostack.setChecked(origTeamMatch.getAutoStack());
		bt_autotote.setChecked(origTeamMatch.getAutoTote());
		bt_autobin.setChecked(origTeamMatch.getAutoBin());
		bt_fast.setChecked(origTeamMatch.getFast());
		bt_carry.setChecked(origTeamMatch.getCarry());
		bt_noodlebin.setChecked(origTeamMatch.getNoodleBin());
		bt_driver.setChecked(origTeamMatch.getDriver());
		bt_stacktote.setChecked(origTeamMatch.getStackTote());
		bt_stackbin.setChecked(origTeamMatch.getStackBin());
		bt_noodlefloor.setChecked(origTeamMatch.getNoodleFloor());
		bt_cooptote.setChecked(origTeamMatch.getCoopTote());
		bt_died.setChecked(origTeamMatch.getDied());
		bt_coopstack.setChecked(origTeamMatch.getCoopStack());
		bt_noodlethrow.setChecked(origTeamMatch.getNoodleThrow());
		bt_pickable.setChecked(origTeamMatch.getPickable());

	}
	
	private void backOutTeamData() {
		int numMatches;
		float oldpct, newpct;

		numMatches = teamData.getNumMatches() - 1;
/*		
		oldpct = teamData.getGoodPct() * (numMatches + 1);
		float gp = (float) origTeamMatch.getGoodPts();
		float bp = (float) origTeamMatch.getBadPts();
		float gpct = (gp / (gp+ bp));
		
		if (numMatches != 0) {
			newpct = (oldpct - gpct)/(numMatches);
		} else { newpct = 0; }
		
		teamData.setGoodPct(newpct);
*/
		
		teamData.setNumMatches(numMatches);
		teamData.setAutoMove(teamData.getAutoMove() - (origTeamMatch.getAutoMove()?1:0));
		teamData.setAutoTote(teamData.getAutoTote() - (origTeamMatch.getAutoTote()?1:0));
		teamData.setAutoBin(teamData.getAutoBin() - (origTeamMatch.getAutoBin()?1:0));
		teamData.setAutoStack(teamData.getAutoStack() - (origTeamMatch.getAutoStack()?1:0));
		teamData.setFast(teamData.getFast() - (origTeamMatch.getFast()?1:0));
		teamData.setStackTote(teamData.getStackTote() - (origTeamMatch.getStackTote()?1:0));
		teamData.setStackBin(teamData.getStackBin() - (origTeamMatch.getStackBin()?1:0));
		teamData.setCarry(teamData.getCarry() - (origTeamMatch.getCarry()?1:0));
		teamData.setNoodleBin(teamData.getNoodleBin() - (origTeamMatch.getNoodleBin()?1:0));
		teamData.setDriver(teamData.getDriver() - (origTeamMatch.getDriver()?1:0));
		teamData.setCoopTote(teamData.getCoopTote() - (origTeamMatch.getCoopTote()?1:0));
		teamData.setCoopStack(teamData.getCoopStack() - (origTeamMatch.getCoopStack()?1:0));
		teamData.setDied(teamData.getDied() - (origTeamMatch.getDied()?1:0));
		teamData.setNoodleFloor(teamData.getNoodleFloor() - (origTeamMatch.getNoodleFloor()?1:0));
		teamData.setNoodleThrow(teamData.getNoodleThrow() - (origTeamMatch.getNoodleThrow()?1:0));
		teamData.setPickable(teamData.getPickable() - (origTeamMatch.getPickable()?1:0));

		
	}
    
}
