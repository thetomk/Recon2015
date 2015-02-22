package com.eaglerobotics.reconalpha.twitter;

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

import com.eaglerobotics.reconalpha.DynamoDBManager;
import com.eaglerobotics.reconalpha.DynamoDBManager.TeamData;
import com.eaglerobotics.reconalpha.MainActivity;
import com.eaglerobotics.reconalpha.Prefs;
import com.eaglerobotics.reconalpha.R;
import com.eaglerobotics.reconalpha.Splash;
import com.eaglerobotics.reconalpha.byMatchTypeComparator;
import com.eaglerobotics.reconalpha.byNaturalMatchTypeComparator;
import com.eaglerobotics.reconalpha.DynamoDBManager.MatchSched;
import com.eaglerobotics.reconalpha.DynamoDBManager.TeamMatch;
import com.eaglerobotics.reconalpha.R.id;
import com.eaglerobotics.reconalpha.R.layout;
import com.eaglerobotics.reconalpha.R.menu;
import com.eaglerobotics.reconalpha.adapters.CustomSchedAdapter;
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


import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;



public class FRCResults extends Activity {

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
		setContentView(R.layout.activity_frcresults);
		context = this;
		ev = Splash.settings.getString("event", "");

        downloadTweets();

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
	    case R.id.action_settings:
	        Intent intent = new Intent(this, Prefs.class);
	        startActivity(intent);
	        return true; 
	    case R.id.action_refresh:
			downloadTweets();
	        return true;  
	    case R.id.action_schedsave:
			new convertTweetsToSched().execute();
			finish();
	        return true;  
	    case R.id.action_home:
	        Intent hintent = new Intent(this, MainActivity.class);
	        startActivity(hintent);
	        return true; 
	    }
	    return false;
	}
	
    // download twitter timeline after first checking to see if there is a network connection
    public void downloadTweets() {
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                    new DownloadTwitterTask().execute(ScreenName);
            } else {
                    Log.v(LOG_TAG, "No network connection available.");
            }
    }
	// Uses an AsyncTask to download a Twitter user's timeline
	private class DownloadTwitterTask extends AsyncTask<String, Void, String> {
//		final static String CONSUMER_KEY = "1A6b2WdhDkSYZN6AVIhwg";
//		final static String CONSUMER_SECRET = "JoPNllcu7QxOXulApzBLvzpJQ1Dk0bCiGcTlINI";
		final static String CONSUMER_KEY = "xBCDAI4aHJBV2qQz1zjyw";
		final static String CONSUMER_SECRET = "sTchRl28y1TaWGFs4oxQ3TKciLQgzcJIkpi3bDgNdwg";
		final static String TwitterTokenURL = "https://api.twitter.com/oauth2/token";
		final static String TwitterStreamURL = "https://api.twitter.com/1.1/statuses/user_timeline.json?count=250&screen_name=";

		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(context);
//			pd.setTitle("Processing...");
			pd.setMessage("Please wait.");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}
			
		@Override
		protected String doInBackground(String... screenNames) {
			String result = null;

			if (screenNames.length > 0) {
				result = getTwitterStream(screenNames[0]);
			}
			return result;
		}
        // converts a string of JSON data into a Twitter object
        private Twitter jsonToTwitter(String result) {
                Twitter twits = null;
                if (result != null && result.length() > 0) {
                        try {
                                Gson gson = new Gson();
                                twits = gson.fromJson(result, Twitter.class);
                        } catch (IllegalStateException ex) {
                                // just eat the exception
                        }
                }
                return twits;
        }

        // convert a JSON authentication object into an Authenticated object
        private Authenticated jsonToAuthenticated(String rawAuthorization) {
                Authenticated auth = null;
                if (rawAuthorization != null && rawAuthorization.length() > 0) {
                        try {
                                Gson gson = new Gson();
                                auth = gson.fromJson(rawAuthorization, Authenticated.class);
                        } catch (IllegalStateException ex) {
                                // just eat the exception
                        }
                }
                return auth;
        }

        private String getResponseBody(HttpRequestBase request) {
                StringBuilder sb = new StringBuilder();
                try {

                        DefaultHttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());
                        HttpResponse response = httpClient.execute(request);
                        int statusCode = response.getStatusLine().getStatusCode();
                        String reason = response.getStatusLine().getReasonPhrase();

                        if (statusCode == 200) {

                                HttpEntity entity = response.getEntity();
                                InputStream inputStream = entity.getContent();

                                BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                                String line = null;
                                while ((line = bReader.readLine()) != null) {
                                        sb.append(line);
                                }
                        } else {
                                sb.append(reason);
                        }
                } catch (UnsupportedEncodingException ex) {
                } catch (ClientProtocolException ex1) {
                } catch (IOException ex2) {
                }
                return sb.toString();
        }
		// onPostExecute convert the JSON results into a Twitter object (which is an Array list of tweets
		@Override
		protected void onPostExecute(String result) {
			
//			Log.i(LOG_TAG,result);
			Twitter twits = jsonToTwitter(result);

			// lets write the results to the console as well
			items = new ArrayList<MatchSched>();

			for (Tweet tweet : twits) {
//				Log.i(LOG_TAG, tweet.getText());
		        String[] tweetStrings = tweet.getText().split(" "); 
//		        for (int i = 0; i<tweetStrings.length; i++) {
//		        	Log.i(LOG_TAG,tweetStrings[i]);
//		        }
		        String mnum = "";
		        if (tweetStrings[0].equals(ev)) {
				newSched = new MatchSched();
		        newSched.setEvent(tweetStrings[0]);
		        newSched.setMatchType(tweetStrings[2]);
		        if (tweetStrings[2].equals("E")) {
		        	mnum = tweetStrings[4];
		        	if (Integer.parseInt(tweetStrings[4]) <= 12) {
		        		mnum = "QF"+mnum;
		        	} else if (Integer.parseInt(tweetStrings[4]) <= 18) {
		        		mnum = "SF"+mnum;
		        	} else {
		        		mnum = "xF"+mnum;
		        	}
		        } else if (tweetStrings[2].equals("P")) {
		        	mnum = "0." + tweetStrings[4];
		        } else {
		        	mnum = tweetStrings[4];
		        }
		        newSched.setMatchNum(mnum);
		        newSched.setRedTotal(Integer.parseInt(tweetStrings[6]));
		        newSched.setBlueTotal(Integer.parseInt(tweetStrings[8]));
		        newSched.setRed1(tweetStrings[10]);
		        newSched.setRed2(tweetStrings[11]);
		        newSched.setRed3(tweetStrings[12]);
		        newSched.setBlue1(tweetStrings[14]);
		        newSched.setBlue2(tweetStrings[15]);
		        newSched.setBlue3(tweetStrings[16]);
		        newSched.setRedFoul(Integer.parseInt(tweetStrings[18]));
		        newSched.setBlueFoul(Integer.parseInt(tweetStrings[20]));
		        newSched.setRedAuto(Integer.parseInt(tweetStrings[22]));
		        newSched.setBlueAuto(Integer.parseInt(tweetStrings[24]));
		        newSched.setRedTele(Integer.parseInt(tweetStrings[26]));
		        newSched.setBlueTele(Integer.parseInt(tweetStrings[28]));
		        
		        items.add(newSched);
		        }
		        
			}

			if (pd!=null) {
				pd.dismiss();
			}
			
			Collections.sort(items, new byMatchTypeComparator());
			
			// send the tweets to the adapter for rendering
			ListView lv = (ListView) findViewById(R.id.TweetList);
//			ArrayAdapter<Tweet> adapter = new ArrayAdapter<Tweet>(FRCResults.this, android.R.layout.simple_list_item_1, twits);
	        ArrayAdapter<MatchSched> arrayAdapter = new CustomSchedAdapter(FRCResults.this, items);
			lv.setAdapter(arrayAdapter);
		}
	private String getTwitterStream(String screenName) {
		String results = null;

		// Step 1: Encode consumer key and secret
		try {
			// URL encode the consumer key and secret
			String urlApiKey = URLEncoder.encode(CONSUMER_KEY, "UTF-8");
			String urlApiSecret = URLEncoder.encode(CONSUMER_SECRET, "UTF-8");

			// Concatenate the encoded consumer key, a colon character, and the
			// encoded consumer secret
			String combined = urlApiKey + ":" + urlApiSecret;

			// Base64 encode the string
			String base64Encoded = Base64.encodeToString(combined.getBytes(), Base64.NO_WRAP);

			// Step 2: Obtain a bearer token
			HttpPost httpPost = new HttpPost(TwitterTokenURL);
			httpPost.setHeader("Authorization", "Basic " + base64Encoded);
			httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			httpPost.setEntity(new StringEntity("grant_type=client_credentials"));
			String rawAuthorization = getResponseBody(httpPost);
//			Log.i(LOG_TAG,rawAuthorization);
			Authenticated auth = jsonToAuthenticated(rawAuthorization);

			// Applications should verify that the value associated with the
			// token_type key of the returned object is bearer
			if (auth != null && auth.token_type.equals("bearer")) {

				// Step 3: Authenticate API requests with bearer token
				HttpGet httpGet = new HttpGet(TwitterStreamURL + screenName);

				// construct a normal HTTPS request and include an Authorization
				// header with the value of Bearer <>
				httpGet.setHeader("Authorization", "Bearer " + auth.access_token);
				httpGet.setHeader("Content-Type", "application/json");
				// update the results with the body of the response
				results = getResponseBody(httpGet);
				
			}
		} catch (UnsupportedEncodingException ex) {
		} catch (IllegalStateException ex1) {
		}
		return results;
	}	
	}
	
	private class convertTweetsToSched extends AsyncTask<String, Void, String> {

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
	     	Toast.makeText(getApplicationContext(), "ScheduleAdd data saved", Toast.LENGTH_SHORT).show();
		}

		
	}

}
