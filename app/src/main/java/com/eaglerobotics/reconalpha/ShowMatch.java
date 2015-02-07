package com.eaglerobotics.reconalpha;


import com.eaglerobotics.reconalpha.DynamoDBManager.TeamMatch;
import com.google.gson.Gson;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ShowMatch extends Activity {
	
String tm = null;
String m = null;
TeamMatch tmobj;
String ev;
private ProgressDialog pd;
private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.activity_show_match);
		ev = Splash.settings.getString("event", "");
		
		Intent intent = getIntent();
		tm = intent.getStringExtra("team");
		m = intent.getStringExtra("match");
		
	    TextView tmtv = (TextView)findViewById(R.id.textTeam); 
	    tmtv.setText("Team: "+tm+"    Match: "+m);
	    
	    new GetMatchDataTask().execute();
	    

	}
	
	protected void onResume(Bundle savedInstanceState) {
		super.onResume();
	    new GetMatchDataTask().execute();
	}

	
	private class GetMatchDataTask extends AsyncTask<Void, Void, Void> {

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
			tmobj = DynamoDBManager.getTeamMatch(ev, tm, m);
			return null;
		}
		protected void onPostExecute(Void result) {
			
			if (pd!=null) {
				pd.dismiss();
			}
						
	         ProgressBar synPb = (ProgressBar) findViewById(R.id.synopsisBar1);
	         TextView synTxt = (TextView) findViewById(R.id.textSynopsis);
	         TextView gdpts = (TextView) findViewById(R.id.textGreen);
	         TextView bdpts = (TextView) findViewById(R.id.textRed);
	         TextView audit = (TextView) findViewById(R.id.textAudit);
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
	         
	         int tp=0,bp = 0, np=0;
	         String allComments ="";
	         bp = tmobj.getBins();
	         tp = tmobj.getTotes();
	         np = tmobj.getNoodles();
	         allComments = tmobj.getComments();
	         if (allComments != null && !allComments.isEmpty()) {
	        	 allComments = allComments.replace("; ", "\n");
	         }

				int tot;
				float gpct=0;
				tot = tp + bp + np;

	         if (tot > 0) {
	         	gpct = (((float)bp+np / (float)tot)*100);
	         } 
	         
//		    	Log.i("MatchSynopsis","Match for tp, bp, pct: "+tp+", "+bp+", "+gpct+":"+tot);

	         synPb.setProgress((int) gpct);
	         synTxt.setText(allComments);
	         gdpts.setText(Integer.toString(bp+np));
	         bdpts.setText(Integer.toString(tp));
	         audit.setText(tmobj.getCreateTime() + " (" + tmobj.getCreator() + ")");
	         tvR1.setText((tmobj.getPickable()?"Yes":"No"));
	         tvR2.setText((tmobj.getAutoMove()?"Yes":"---"));
	         tvR3.setText((tmobj.getAutoTote()?"Yes":"---"));
	         tvR4.setText((tmobj.getAutoBin()?"Yes":"---"));
	         tvR5.setText((tmobj.getStackTote()?"Yes":" ---"));
	         tvR6.setText((tmobj.getStackBin()?"Yes":"---"));
	         tvR7.setText((tmobj.getCarry()?"Yes":" ---"));
	         tvR8.setText((tmobj.getAutoStack()?"Yes":"---"));
	         tvR9.setText((tmobj.getDriver()?"Yes":"---"));
	         tvR10.setText((tmobj.getNoodleBin()?"Yes":"---"));
	         tvR11.setText((tmobj.getFast()?"Yes":" ---"));
	         tvR12.setText((tmobj.getCoopTote()?"Yes":"---"));
	         tvR13.setText((tmobj.getNoodleThrow()?"Yes":" ---"));
	         tvR14.setText((tmobj.getCoopStack()?"Yes":"---"));
	         tvR15.setText((tmobj.getDied()?"Yes":" ---"));
	         
	         tvL1.setBackgroundColor((tmobj.getPickable()?0x7700FF00:0x77FF0000));
	         tvL2.setBackgroundColor((tmobj.getAutoMove()?0x7700FF00:0x00000000));
	         tvL3.setBackgroundColor((tmobj.getAutoTote()?0x7700FF00:0x00000000));
	         tvL4.setBackgroundColor((tmobj.getAutoBin()?0x7700FF00:0x00000000));
	         tvL5.setBackgroundColor((tmobj.getStackTote()?0x7700FF00:0x00000000));
	         tvL6.setBackgroundColor((tmobj.getStackBin()?0x7700FF00:0x00000000));
	         tvL7.setBackgroundColor((tmobj.getCarry()?0x7700FF00:0x00000000));
	         tvL8.setBackgroundColor((tmobj.getAutoStack()?0x7700FF00:0x00000000));
	         tvL9.setBackgroundColor((tmobj.getDriver()?0x7700FF00:0x00000000));
	         tvL10.setBackgroundColor((tmobj.getNoodleBin()?0x7700FF00:0x00000000));
	         tvL11.setBackgroundColor((tmobj.getFast()?0x7700FF00:0x00000000));
	         tvL12.setBackgroundColor((tmobj.getCoopTote()?0x7700FF00:0x00000000));
	         tvL13.setBackgroundColor((tmobj.getNoodleThrow()?0x7700FF00:0x00000000));
	         tvL14.setBackgroundColor((tmobj.getCoopStack()?0x77FF0000:0x00000000));
	         tvL15.setBackgroundColor((tmobj.getDied()?0x77FF0000:0x00000000));
		}	

		}
	        
		
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_settings_home_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
	    switch(item.getItemId()){
	    case R.id.action_settings:
	        Intent intent = new Intent(this, Prefs.class);
	        startActivity(intent);
	        return true; 
	    case R.id.action_edit:
	        Intent eintent = new Intent(this, NewMatch.class);
	        eintent.putExtra("TeamMatch",(new Gson()).toJson(tmobj));
	        startActivity(eintent);
	        finish();
	        return true; 
	    case R.id.action_home:
	        Intent hintent = new Intent(this, MainActivity.class);
	        startActivity(hintent);
	        return true; 
	    }
	    return false;
	}

}
