package com.eaglerobotics.reconalpha;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;

import com.eaglerobotics.reconalpha.DynamoDBManager.TeamData;
import com.eaglerobotics.reconalpha.DynamoDBManager.TeamMatch;
import com.eaglerobotics.reconalpha.adapters.CustomTeamArrayAdapter;
import com.eaglerobotics.reconalpha.util.Team;
import com.eaglerobotics.reconalpha.util.TeamInfo;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ViewTeam extends Activity {

	private ArrayList<TeamMatch> items = null;
	TeamInfo teamSynopsis = null;
	TeamData tmobj = null;
	private ArrayAdapter<TeamMatch> arrayAdapter = null;
	private String team;
	private EditText teamparam;
    HashMap<String, Integer> countMap = new HashMap<String, Integer>();
    String ev;
	private ProgressDialog pd;
	private Context context;
	String tm;

	String imageName, bucketName;
	long modDate;


	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.activity_team);
		teamparam = (EditText)findViewById(R.id.teamParam); 
		
		Intent intent = getIntent();
		tm = intent.getStringExtra("team");
		if (tm != null) {
			teamparam.setText(tm);
			getTeamData(this.findViewById(R.layout.activity_team));
		}
		
		ev = Splash.settings.getString("event", "");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings_photo_refresh_home_menu, menu);
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
			getTeamData(this.findViewById(R.layout.activity_team));
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
		team =  teamparam.getText().toString();
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

			items = DynamoDBManager.getTeamList(ev, team);
			Collections.sort(items, new byMatchComparator());
			teamSynopsis = Team.getTeamSynopsis(ev, team);
			tmobj = DynamoDBManager.getTeamData(ev, team);
			
			return null;
		}

		protected void onPostExecute(Void result) {

			 if (pd!=null) {
				pd.dismiss();
			 }
		     
		     teamparam.setFocusableInTouchMode(false);
		     teamparam.setFocusable(false);
		     teamparam.setFocusableInTouchMode(true);
		     teamparam.setFocusable(true);
		     
	         if (tmobj == null) { return; }

	         ListView lv = (ListView) findViewById(R.id.teamListView);
	         ProgressBar synPb = (ProgressBar) findViewById(R.id.synopsisBar);
	         TextView tvR1 = (TextView) findViewById(R.id.TextViewR01);
	         TextView tvR2 = (TextView) findViewById(R.id.TextViewR02);
	         TextView tvR3 = (TextView) findViewById(R.id.TextViewR03);
	         TextView tvR4 = (TextView) findViewById(R.id.TextViewR04);
	         TextView tvR5 = (TextView) findViewById(R.id.TextViewR05);
	         TextView tvR6 = (TextView) findViewById(R.id.TextViewR06);
	         TextView tvR7 = (TextView) findViewById(R.id.TextViewR07);
	         TextView tvR8 = (TextView) findViewById(R.id.TextViewR08);
	         TextView tvR9 = (TextView) findViewById(R.id.TextViewR09);
	         TextView tvR10 = (TextView) findViewById(R.id.TextViewR10);
	         TextView tvR11 = (TextView) findViewById(R.id.TextViewR11);
	         TextView tvR12 = (TextView) findViewById(R.id.TextViewR12);
	         TextView tvR13 = (TextView) findViewById(R.id.TextViewR13);
	         TextView tvR14 = (TextView) findViewById(R.id.TextViewR14);
	         TextView tvR15 = (TextView) findViewById(R.id.TextViewR15);
	         TextView tvR16 = (TextView) findViewById(R.id.TextViewR16);
	         TextView tvR17 = (TextView) findViewById(R.id.TextViewR17);
	         
	         TextView tvL1 = (TextView) findViewById(R.id.TextViewL01);
	         TextView tvL2 = (TextView) findViewById(R.id.TextViewL02);
	         TextView tvL3 = (TextView) findViewById(R.id.TextViewL03);
	         TextView tvL4 = (TextView) findViewById(R.id.TextViewL04);
	         TextView tvL5 = (TextView) findViewById(R.id.TextViewL05);
	         TextView tvL6 = (TextView) findViewById(R.id.TextViewL06);
	         TextView tvL7 = (TextView) findViewById(R.id.TextViewL07);
	         TextView tvL8 = (TextView) findViewById(R.id.TextViewL08);
	         TextView tvL9 = (TextView) findViewById(R.id.TextViewL09);
	         TextView tvL10 = (TextView) findViewById(R.id.TextViewL10);
	         TextView tvL11 = (TextView) findViewById(R.id.TextViewL11);
	         TextView tvL12 = (TextView) findViewById(R.id.TextViewL12);
	         TextView tvL13 = (TextView) findViewById(R.id.TextViewL13);
	         TextView tvL14 = (TextView) findViewById(R.id.TextViewL14);
	         TextView tvL15 = (TextView) findViewById(R.id.TextViewL15);
	         TextView tvL16 = (TextView) findViewById(R.id.TextViewL16);
	         TextView tvL17 = (TextView) findViewById(R.id.TextViewL17);

	         TextView tvOA = (TextView) findViewById(R.id.textViewOA);
	         TextView tvOT = (TextView) findViewById(R.id.textViewOT);
	         TextView tvOF = (TextView) findViewById(R.id.textViewOF);
	         TextView tvOL = (TextView) findViewById(R.id.textViewOL);
	         TextView tvDA = (TextView) findViewById(R.id.textViewDA);
	         TextView tvDT = (TextView) findViewById(R.id.textViewDT);
	         TextView tvDF = (TextView) findViewById(R.id.textViewDF);
	         TextView tvDL = (TextView) findViewById(R.id.textViewDL);

	         synPb.setProgress(teamSynopsis.getGoodPct());


	         setInfo(tmobj.getPickable(),tmobj.getNumMatches() ,tvL16, tvR16, 0x7700FF00);
	         setInfo(tmobj.getAutoMove(),tmobj.getNumMatches() ,tvL1, tvR1, 0x7700FF00);
	         setInfo(tmobj.getAutoTote(),tmobj.getNumMatches() ,tvL3, tvR3, 0x7700FF00);
	         setInfo(tmobj.getAutoBin(),tmobj.getNumMatches() ,tvL4, tvR4, 0x7700FF00);
	         setInfo(tmobj.getAutoStack(),tmobj.getNumMatches() ,tvL8, tvR8, 0x7700FF00);
	         setInfo(tmobj.getFast(),tmobj.getNumMatches() ,tvL11, tvR11, 0x7700FF00);
	         setInfo(tmobj.getStackTote(),tmobj.getNumMatches() ,tvL5, tvR5, 0x7700FF00);
	         setInfo(tmobj.getStackBin(),tmobj.getNumMatches() ,tvL6, tvR6, 0x7700FF00);
	         setInfo(tmobj.getDriver(),tmobj.getNumMatches() ,tvL10, tvR10, 0x7700FF00);
	         setInfo(tmobj.getCarry(),tmobj.getNumMatches() ,tvL7, tvR7, 0x7700FF00);
	         setInfo(tmobj.getNoodleBin(),tmobj.getNumMatches() ,tvL9, tvR9, 0x7700FF00);
	         setInfo(tmobj.getCoopTote(),tmobj.getNumMatches() ,tvL12, tvR12, 0x7700FF00);
	         setInfo(tmobj.getCoopStack(),tmobj.getNumMatches() ,tvL14, tvR14, 0x7700FF00);
	         setInfo(tmobj.getNoodleFloor(),tmobj.getNumMatches() ,tvL13, tvR13, 0x7700FF00);
	         setInfo(tmobj.getNoodleThrow(),tmobj.getNumMatches() ,tvL15, tvR15, 0x7700FF00);
	         setInfo(tmobj.getDied(),tmobj.getNumMatches() ,tvL17, tvR17, 0x77FF0000);
	         
	         tvOA.setText(String.format("%.1f", (float)tmobj.getOAuto()/(float)tmobj.getNumScores() ));
	         tvOT.setText(String.format("%.1f", (float)tmobj.getOTele()/(float)tmobj.getNumScores() ));
	         tvOF.setText(String.format("%.1f", (float)tmobj.getOFoul()/(float)tmobj.getNumScores() ));
	         tvOL.setText(String.format("%.1f", (float)tmobj.getOTotal()/(float)tmobj.getNumScores() ));
	         tvDA.setText(String.format("%.1f", (float)tmobj.getDAuto()/(float)tmobj.getNumScores() ));
	         tvDT.setText(String.format("%.1f", (float)tmobj.getDTele()/(float)tmobj.getNumScores() ));
	         tvDF.setText(String.format("%.1f", (float)tmobj.getDFoul()/(float)tmobj.getNumScores() ));
	         tvDL.setText(String.format("%.1f", (float)tmobj.getDTotal()/(float)tmobj.getNumScores() ));
	         
        	 tvOL.setBackgroundColor(0x00000000);
        	 tvDL.setBackgroundColor(0x00000000);
	         if ((float)tmobj.getOTotal()/(float)tmobj.getNumScores() > (float)tmobj.getDTotal()/(float)tmobj.getNumScores()) {
	        	 tvOL.setBackgroundColor(0x7700FF00);
	         } else if ((float)tmobj.getOTotal()/(float)tmobj.getNumScores() < (float)tmobj.getDTotal()/(float)tmobj.getNumScores()) {
	        	 tvDL.setBackgroundColor(0x77FF0000);	        	 
	         }
	         
		     
		     arrayAdapter = new CustomTeamArrayAdapter(ViewTeam.this, items);
		     lv.setAdapter(arrayAdapter); 
		     
	         lv.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
					    int position, long id) {

			        Intent intent = new Intent(ViewTeam.this, ShowMatch.class);

			        String t = items.get(position).getTeamNum();
			        String m = items.get(position).getMatchNum();

			        intent.putExtra("team",t);
			        intent.putExtra("match", m);
			        startActivity(intent);
					
				}
	        	 
	         });

	      
		}
		
        protected void setInfo (int val, int max, TextView label, TextView vtext, int color) {
       	 
	         vtext.setText(val + "/" + max);
	         label.setBackgroundColor(0x00000000);
	         if ((float)val/(float)max > .7) {
	        	 label.setBackgroundColor(color);
	         }
        }
		
	}
	

}
