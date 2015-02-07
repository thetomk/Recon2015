package com.eaglerobotics.reconalpha;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Splash extends Activity {
    public static final String PREFS_NAME = "ReconPrefsFile";
    public static SharedPreferences settings;

    String pass;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
	    settings = getSharedPreferences(PREFS_NAME, 0);
	    pass = settings.getString("pwd", "");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}
	
	@SuppressWarnings("deprecation")
	public void checkPasswd(View view) {
		
		boolean good = false;
		String perms = "user";
		
	    String ipwd = "team1388";
	    String jpwd = "recon1388";
	    String kpwd = "admin1388";
	    
	    TextView pwd = (TextView)findViewById(R.id.editPass);
	    
    	pass = pwd.getText().toString();
	    if (pass == null || pass.equals("")) {
		    pass = settings.getString("pwd", "");
	    }
	    
	    if (pass.equals(ipwd)) {
	    	good = true;
	    	perms = "user";
	    } else if (pass.equals(jpwd)) {
	    	good = true;
	    	perms = "recon";
	    } else if (pass.equals(kpwd)) {
	    	good = true;
	    	perms = "admin";
	    }

//	    Log.i("PASSWORD","Password is:{"+pwd.getText()+"}");
	    
	    if (good) {
		      SharedPreferences.Editor editor = settings.edit();
		      editor.putString("perms",perms);
		      editor.commit();
	    	Intent intent = new Intent(this, MainActivity.class);
	    	startActivity(intent);
	    }
	    else {
//	    	Toast.makeText(getApplicationContext(), "Wrong password", Toast.LENGTH_SHORT).show();
	    	AlertDialog alertDialog = new AlertDialog.Builder(this).create();
	    	   
	    	alertDialog.setTitle("Failure");
	    	   
	    	alertDialog.setMessage("Do you agree that you do not know the password and are consequently irrelevant? (You may ask Recon to become relevant.)");
	    	   
	    	alertDialog.setButton("I Agree", new DialogInterface.OnClickListener() {

	    	                public void onClick(DialogInterface dialog, int which) {
	    	                    //do somthing or dismiss dialog by                dialog.dismiss();

	    	                }
	    	            });
	    	//    
//	    	alertDialog.setIcon(R.drawable.logo);
	    	   
	    	alertDialog.show();
	    }
	    
	}

}
