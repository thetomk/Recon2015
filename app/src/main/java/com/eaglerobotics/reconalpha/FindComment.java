package com.eaglerobotics.reconalpha;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eaglerobotics.reconalpha.DynamoDBManager.TeamMatch;
import com.eaglerobotics.reconalpha.adapters.CustomTeamInfoAdapter;
import com.eaglerobotics.reconalpha.util.Team;
import com.eaglerobotics.reconalpha.util.TeamInfo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;



public class FindComment extends Activity {
	private ArrayList<TeamInfo> titems = null;
	private ArrayList<TeamMatch> allitems = null;
	private EditText commentparam;
	private String comment;
	String ev;
	private ProgressDialog pd;
	private Context context;

    TeamInfo currTeam;


	static List<String> getTeamInDescendingFreqOrder(Map<String, Integer> teamCount) {

	    // Convert map to list of <String,Integer> entries
	    List<Map.Entry<String, Integer>> list = 
	        new ArrayList<Map.Entry<String, Integer>>(teamCount.entrySet());

	    // Sort list by integer values
	    Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
	        public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
	            // compare o2 to o1, instead of o1 to o2, to get descending freq. order
	            return (o2.getValue()).compareTo(o1.getValue());
	        }
	    });

	    // Populate the result into a list
	    List<String> result = new ArrayList<String>();
	    for (Map.Entry<String, Integer> entry : list) {
	        result.add(entry.getKey());
	    }
	    return result;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_comment);
		context = this;
		commentparam = (EditText)findViewById(R.id.editText1);
		ev = Splash.settings.getString("event", "");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings_refresh_home_menu, menu);
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
			getTeamList(this.findViewById(R.layout.activity_find_comment));
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
	
	public void getTeamList(View view) {
		comment =  commentparam.getText().toString();
		new GetMatchListTask().execute();
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

		    HashMap<String, Integer> countMap = new HashMap<String, Integer>();
		    List<String> teamList;

			allitems = DynamoDBManager.getMatchList(ev);
			
			for (TeamMatch curritem : allitems) {
				if (curritem.getComments() != null) {
					if (curritem.getComments().toLowerCase().contains(comment.toLowerCase())) {

						if (!countMap.containsKey(curritem.getTeamNum())) {
							countMap.put(curritem.getTeamNum(), 1);
						} else {
							Integer count = countMap.get(curritem.getTeamNum());
							count = count + 1;
							countMap.put(curritem.getTeamNum(), count);
						}
					}
				}
			}
			
			teamList = getTeamInDescendingFreqOrder(countMap);
			titems = new ArrayList<TeamInfo>();
			for (String cteam : teamList) {
				currTeam = new TeamInfo();
				currTeam = Team.getTeamSynopsis(ev, cteam);
				currTeam.setCommentRank(countMap.get(cteam));
				titems.add(currTeam);

			}

			return null;
		}
		

		protected void onPostExecute(Void result) {

	         ListView lv = (ListView) findViewById(R.id.listView1);
	         
				if (pd!=null) {
					pd.dismiss();
				}
				
	         // This is the array adapter, it takes the context of the activity as a first // parameter, the type of list view as a second parameter and your array as a third parameter

//	         arrayAdapter = new CustomArrayAdapter(FindComment.this, items);
	         CustomTeamInfoAdapter arrayAdapterT = new CustomTeamInfoAdapter(FindComment.this, titems);
	         lv.setAdapter(arrayAdapterT); 
	         lv.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
					    int position, long id) {

			        Intent intent = new Intent(FindComment.this, ViewTeam.class);
			        String t = titems.get(position).getTeamNum();

			        intent.putExtra("team",t);
			        startActivity(intent);
					
				}
	        	 
	         });
			
			

		}

	}

}
