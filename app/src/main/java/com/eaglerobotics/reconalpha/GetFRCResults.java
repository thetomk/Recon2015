package com.eaglerobotics.reconalpha;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.eaglerobotics.reconalpha.DynamoDBManager.MatchSched;
import com.eaglerobotics.reconalpha.DynamoDBManager.TeamData;
import com.eaglerobotics.reconalpha.R.id;
import com.eaglerobotics.reconalpha.R.layout;
import com.eaglerobotics.reconalpha.adapters.CustomSchedAdapter;
import com.eaglerobotics.reconalpha.frcAPI.FRCAPIScheduleInterface;
import com.eaglerobotics.reconalpha.frcAPI.FRCSchedule;
import com.eaglerobotics.reconalpha.frcAPI.SchedTeam;
import com.eaglerobotics.reconalpha.frcAPI.Schedule;
import com.eaglerobotics.reconalpha.twitter.Authenticated;
import com.eaglerobotics.reconalpha.twitter.Tweet;
import com.eaglerobotics.reconalpha.twitter.Twitter;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class GetFRCResults extends Activity {

    final static String ScreenName = "frcfms";
    final static String LOG_TAG = "erb";
	private ProgressDialog pd;
	private Context context;
	private ArrayList<MatchSched> items = null;
	private MatchSched newSched;
	private ArrayList<MatchSched> scheditems = null;
	String ev;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(layout.activity_frcresults);
		context = this;
		ev = Splash.settings.getString("event", "");

        getScheduleAPI();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.savesched_settings_refresh_home_menu, menu);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item){
	    switch(item.getItemId()){
	    case id.action_settings:
	        Intent intent = new Intent(this, Prefs.class);
	        startActivity(intent);
	        return true;
	    case id.action_refresh:
			getScheduleAPI();
	        return true;
	    case id.action_schedsave:
			new saveToSched().execute();
			finish();
	        return true;
	    case id.action_home:
	        Intent hintent = new Intent(this, MainActivity.class);
	        startActivity(hintent);
	        return true;
	    }
	    return false;
	}

    public void getScheduleAPI() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint("http://private-anon-516e78132-frcevents.apiary-mock.com/api/v1.0/")
                .build();

        FRCAPIScheduleInterface rankingService = restAdapter.create(FRCAPIScheduleInterface.class);

        rankingService.getSchedule(ev, new Callback<FRCSchedule>() {
            @Override
            public void success(FRCSchedule schedData, Response response) {
                consumeApiData(schedData);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                consumeApiData(null);
                Toast.makeText(getApplicationContext(), "Error getting schedule", Toast.LENGTH_SHORT).show();
            }
        });
	}
	
	private class saveToSched extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {

			ArrayList<MatchSched> savedItems = null;
			TeamData red1, red2, red3, blu1,blu2, blu3;
			Boolean found;
			
			Calendar c = Calendar.getInstance();
	    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    	String formattedDate = df.format(c.getTime());    

			savedItems = DynamoDBManager.getSchedList(ev);
			Collections.sort(items, new byNaturalMatchTypeComparator());

			for (MatchSched curritem : items) {
				found = false;
				for (MatchSched existitem : savedItems) {
					if (curritem.getMatchNum().equals(existitem.getMatchNum())) {
						found = true;
					}
				}
				if (!found) {
					curritem.setCreateTime(formattedDate);
					DynamoDBManager.updateMatchSched(curritem);
										
					red1 = getPoints(curritem,"Red",curritem.getRed1());
					DynamoDBManager.updateTeamData(red1);
					red2 = getPoints(curritem,"Red",curritem.getRed2());
					DynamoDBManager.updateTeamData(red2);
					red3 = getPoints(curritem,"Red",curritem.getRed3());
					DynamoDBManager.updateTeamData(red3);
					blu1 = getPoints(curritem,"Blue",curritem.getBlue1());
					DynamoDBManager.updateTeamData(blu1);
					blu2 = getPoints(curritem,"Blue",curritem.getBlue2());
					DynamoDBManager.updateTeamData(blu2);
					blu3 = getPoints(curritem,"Blue",curritem.getBlue3());
					DynamoDBManager.updateTeamData(blu3);
				}
			}

			
			
			return null;
			
		}
		
		
		protected TeamData getPoints(MatchSched citem, String alliance, String tnum) {
			TeamData td;
			int oauto, otele, ofoul, otot, dauto, dtele, dfoul, dtot;
			
			if (alliance.equals("Red")) {
				oauto = citem.getRedAuto();
				otele = citem.getRedTele();
				ofoul = citem.getRedFoul();
				otot = citem.getRedTotal();
				dauto = citem.getBlueAuto();
				dtele = citem.getBlueTele();
				dfoul = citem.getBlueFoul();
				dtot = citem.getBlueTotal();
				
			} else {
				dauto = citem.getRedAuto();
				dtele = citem.getRedTele();
				dfoul = citem.getRedFoul();
				dtot = citem.getRedTotal();
				oauto = citem.getBlueAuto();
				otele = citem.getBlueTele();
				ofoul = citem.getBlueFoul();
				otot = citem.getBlueTotal();
			}
			td = DynamoDBManager.getTeamData(ev,tnum);
			if (td == null) {
				td = new TeamData();
				td.setEventID(ev);
				td.setTeamNum(tnum);
			}
			td.setOAuto(oauto + td.getOAuto());
			td.setOTele(otele + td.getOTele());
			td.setOFoul(ofoul + td.getOFoul());
			td.setOTotal(otot + td.getOTotal());
			td.setDAuto(dauto + td.getDAuto());
			td.setDTele(dtele + td.getDTele());
			td.setDFoul(dfoul + td.getDFoul());
			td.setDTotal(dtot + td.getDTotal());
			td.setNumScores(td.getNumScores()+1);
			
			return td;
		}
		
		protected void onPostExecute(Void result) {

	         if (pd!=null) {
				pd.dismiss();
			}
	     	Toast.makeText(getApplicationContext(), "Schedule data saved", Toast.LENGTH_SHORT).show();
		}

		
	}
    public void consumeApiData(FRCSchedule schedData) {
        if (schedData != null) {

            items = new ArrayList<MatchSched>();

            for(Schedule sitem : schedData.getSchedule()) {
                newSched = new MatchSched();
                newSched.setEvent(ev);
                newSched.setMatchType("Q");
                newSched.setMatchNum(Integer.toString(sitem.getMatchNumber()));
                newSched.setRedTotal(sitem.getScoreRedFinal());
                newSched.setBlueTotal(sitem.getScoreBlueFinal());
                newSched.setRedFoul(sitem.getScoreRedFoul());
                newSched.setBlueFoul(sitem.getScoreBlueFoul());
                newSched.setRedAuto(sitem.getScoreRedAuto());
                newSched.setBlueAuto(sitem.getScoreBlueAuto());
                for (SchedTeam st : sitem.getTeams()) {
                    if (st.getStation().equals("Blue1")) {
                        newSched.setBlue1(Integer.toString(st.getNumber()));
                    }
                    else if (st.getStation().equals("Blue2")) {
                        newSched.setBlue2(Integer.toString(st.getNumber()));
                    }
                    else if (st.getStation().equals("Blue3")) {
                        newSched.setBlue3(Integer.toString(st.getNumber()));
                    }
                    else if (st.getStation().equals("Red1")) {
                        newSched.setRed1(Integer.toString(st.getNumber()));
                    }
                    else if (st.getStation().equals("Red2")) {
                        newSched.setRed2(Integer.toString(st.getNumber()));
                    }
                    else if (st.getStation().equals("Red3")) {
                        newSched.setRed3(Integer.toString(st.getNumber()));
                    }
                }

                items.add(newSched);
            }


            ListView lv = (ListView) findViewById(R.id.TweetList);

            ArrayAdapter<MatchSched> arrayAdapter = new CustomSchedAdapter(GetFRCResults.this, items);
            lv.setAdapter(arrayAdapter);
        }
    }

}
