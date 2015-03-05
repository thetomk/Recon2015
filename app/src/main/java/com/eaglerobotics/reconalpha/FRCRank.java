package com.eaglerobotics.reconalpha;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.eaglerobotics.reconalpha.adapters.CustomRankAdapter;
import com.eaglerobotics.reconalpha.frcAPI.FRCAPIRankingInterface;
import com.eaglerobotics.reconalpha.frcAPI.FRCRankings;
import com.eaglerobotics.reconalpha.frcAPI.Ranking;
import com.eaglerobotics.reconalpha.DynamoDBManager.TeamData;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class FRCRank extends Activity {

    String ev;
    private ArrayAdapter<Ranking> arrayAdapter = null;
    List<Ranking> rlist;
    String tnum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frc_rank);

        ev = Splash.settings.getString("event", "");
        getRankAPI();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_frc_data, menu);
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
                getRankAPI();
                return true;
            case R.id.action_save:
                new saveToTeamData().execute();
                finish();
                return true;
            case R.id.action_home:
                Intent hintent = new Intent(this, MainActivity.class);
                startActivity(hintent);
                return true;
        }
        return false;
    }

    public void getRankAPI() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint("https://frc-api.usfirst.org/api/v1.0/")
                .build();

        FRCAPIRankingInterface rankingService = restAdapter.create(FRCAPIRankingInterface.class);

        rankingService.getRankings(ev, new Callback<FRCRankings>() {
            @Override
            public void success(FRCRankings rankingData, Response response) {
                consumeApiData(rankingData);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                consumeApiData(null);
                Toast.makeText(getApplicationContext(), "Error getting rankings", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void consumeApiData(final FRCRankings rankingData) {
        if (rankingData != null) {
            ListView lv = (ListView) findViewById(R.id.rankListView);

            rlist = rankingData.getRankings();
            arrayAdapter = new CustomRankAdapter(FRCRank.this, rankingData.getRankings());
            lv.setAdapter(arrayAdapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    Intent intent = new Intent(FRCRank.this, ViewTeam.class);
                    String t = Integer.toString(rankingData.getRankings().get(position).getTeamNumber());
                    intent.putExtra("team",t);
                    startActivity(intent);

                }

            });

        }
    }

    private class saveToTeamData extends AsyncTask<String, Void, Integer> {
        TeamData td;

        @Override
        protected Integer doInBackground(String... params) {

            for (Ranking ritem : rlist) {

                tnum = Integer.toString(ritem.getTeamNumber());
                td = DynamoDBManager.getTeamData(ev,tnum);

                if (td == null) {
                    td = new TeamData();
                    td.setEventID(ev);
                    td.setTeamNum(tnum);
                }

                td.setAutoPts(ritem.getAutoPoints());
                td.setTotePts(ritem.getTotePoints());
                td.setBinPts(ritem.getContainerPoints());
                td.setNoodlePts(ritem.getLitterPoints());
                td.setCoopPts(ritem.getCoopertitionPoints());
                td.setRank(ritem.getRank());
                td.setRankedMatches(ritem.getMatchesPlayed());
                td.setQualAvg(ritem.getQualAverage());

                DynamoDBManager.updateTeamData(td);
            }
            return 0;
        }

        protected void onPostExecute(Integer result) {

            Toast.makeText(getApplicationContext(), "Team data saved", Toast.LENGTH_SHORT).show();
        }


    }
}
