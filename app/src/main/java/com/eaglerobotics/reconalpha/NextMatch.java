package com.eaglerobotics.reconalpha;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class NextMatch extends Activity {
	
	String event;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_next_match);
		event = Splash.settings.getString("event", "");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.next_match, menu);
		return true;
	}

}
