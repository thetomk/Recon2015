package com.eaglerobotics.reconalpha;

import com.amazonaws.cognito.AWSClientMgr;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	
//	public static AmazonClientManager clientManager = null;
    public static AWSClientMgr clientManager = null;
	String perms;
    String ev;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		Button btn;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		perms = Splash.settings.getString("perms", "");
        ev = Splash.settings.getString("event", "");
		
		if (perms.equals("user")) {
			btn=(Button)findViewById(R.id.btnSave);
			btn.setVisibility(View.INVISIBLE);
			btn=(Button)findViewById(R.id.button2);
			btn.setVisibility(View.INVISIBLE);
			btn=(Button)findViewById(R.id.button8);
			btn.setVisibility(View.INVISIBLE);
			btn=(Button)findViewById(R.id.button11);
			btn.setVisibility(View.INVISIBLE);

		}

		if (perms.equals("recon")) {
			btn=(Button)findViewById(R.id.button8);
			btn.setVisibility(View.INVISIBLE);
			btn=(Button)findViewById(R.id.button11);
			btn.setVisibility(View.INVISIBLE);
		}
		
/*		// Create AmazonClientManager with SharedPreference
		clientManager = new AmazonClientManager(getSharedPreferences(
				"com.eaglerobotics.recona",
				Context.MODE_PRIVATE));
*/
        clientManager = new AWSClientMgr(this);


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
	    switch(item.getItemId()){
	    case R.id.action_settings:
	        Intent intent = new Intent(this, Prefs.class);
	        startActivity(intent);
	        return true;            
	    }
	    return false;
	}
	
	public void newMatch(View view) {
        if (chkEvent()) {
            Intent intent = new Intent(this, SaveMatch.class);
            startActivity(intent);
        }
	}

	
	public void listMatches(View view) {
        if (chkEvent()) {
            Intent intent = new Intent(this, ListMatches.class);
            startActivity(intent);
        }
	}
	
	public void viewTeam(View view) {
        if (chkEvent()) {
            Intent intent = new Intent(this, ViewTeam.class);
            startActivity(intent);
        }
	}

	public void showRank(View view) {
        if (chkEvent()) {
            Intent intent = new Intent(this, FRCRank.class);
            startActivity(intent);
        }
	}
	
	public void editPrefs(View view) {
            Intent intent = new Intent(this, Prefs.class);
            startActivity(intent);
	}
	
	public void scoutMatch(View view) {
        if (chkEvent()) {
            Intent intent = new Intent(this, ScoutMatch.class);
            startActivity(intent);
        }
	}

	public void nextMatch(View view) {
        if (chkEvent()) {
            Intent intent = new Intent(this, TeamScheduleList.class);
            intent.putExtra("team", "1388");
            startActivity(intent);
        }
	}

	public void findTeams(View view) {
        if (chkEvent()) {
            Intent intent = new Intent(this, FindTeamMulti.class);
            startActivity(intent);
        }
	}

	public void listSched(View view) {
        if (chkEvent()) {
            Intent intent = new Intent(this, GetFRCResults.class);
            startActivity(intent);
        }
	}

	public void showTeams(View view) {
        if (chkEvent()) {
            Intent intent = new Intent(this, GetFRCEvents.class);
            startActivity(intent);
        }
	}

    public void showEvents(View view) {
        if (chkEvent()) {
            Intent intent = new Intent(this, GetFRCEvents.class);
            startActivity(intent);
        }
    }

    public boolean chkEvent () {
        if (ev == null || ev.isEmpty()) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();

            alertDialog.setTitle("Failure");

            alertDialog.setMessage("You must specify an event code in Preferences");

            alertDialog.setButton("Will do", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    //do somthing or dismiss dialog by                dialog.dismiss();

                }
            });
            //
//	    	alertDialog.setIcon(R.drawable.logo);

            alertDialog.show();
            return false;
        } else {
            return true;
        }
    }
}
