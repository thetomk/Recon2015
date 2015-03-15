package com.eaglerobotics.reconalpha;

import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Prefs extends Activity {
    public static final String PREFS_NAME = "ReconPrefsFile";
    SharedPreferences settings;
    EditText etuser;
    EditText etpass;
    EditText etevent;
    TextView tvversion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		String p ="",v="";
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prefs);
		
	    // Restore preferences
	    settings = getSharedPreferences(PREFS_NAME, 0);
	    String username = settings.getString("user", "");
	    String pass = settings.getString("pwd", "");
	    String event = settings.getString("event", "");
	    
	     etuser = (EditText)findViewById(R.id.editTextUI); 
	     etpass = (EditText)findViewById(R.id.editTextPw); 
	     etevent = (EditText)findViewById(R.id.editTextEv); 
	     tvversion = (TextView)findViewById(R.id.version);

	    
	    etuser.setText(username);
	    etpass.setText(pass);
	    etevent.setText(event);
	    
	    try {
			 v = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
			 p = getPackageManager().getPackageInfo(getPackageName(), 0).packageName;
			
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    tvversion.setText(p+": v"+v);
	    
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.prefs, menu);
		return true;
	}
	
	public void setPrefs(View view) {
	      // We need an Editor object to make preference changes.
	      // All objects are from android.context.Context
	      settings = getSharedPreferences(PREFS_NAME, 0);
	      SharedPreferences.Editor editor = settings.edit();
	      editor.putString("user", etuser.getText().toString());
	      editor.putString("pwd", etpass.getText().toString());
	      editor.putString("event", etevent.getText().toString());

          Splash.settings.edit().putString("event", etevent.getText().toString());

	      // Commit the edits!
	      editor.commit();
	      Toast.makeText(getApplicationContext(), "Preferences saved", Toast.LENGTH_SHORT).show();
	}

}
