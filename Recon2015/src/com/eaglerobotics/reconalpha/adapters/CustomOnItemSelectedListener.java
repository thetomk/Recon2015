package com.eaglerobotics.reconalpha.adapters;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

public class CustomOnItemSelectedListener implements OnItemSelectedListener {

	String selected;
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		selected = parent.getItemAtPosition(pos).toString();

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}

}
