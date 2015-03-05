package com.eaglerobotics.reconalpha;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.eaglerobotics.reconalpha.adapters.CustomEventAdapter;
import com.eaglerobotics.reconalpha.frcAPI.Event;
import com.eaglerobotics.reconalpha.frcAPI.FRCAPIEventInterface;
import com.eaglerobotics.reconalpha.frcAPI.FRCEvents;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class GetFRCEvents extends Activity {


    String ev;
    private ArrayAdapter<Event> arrayAdapter = null;
    List<Event> elist;
    String tnum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_frcevents);

        ev = Splash.settings.getString("event", "");
        getEventAPI();

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
                getEventAPI();
                return true;
            case R.id.action_save:
                finish();
                return true;
            case R.id.action_home:
                Intent hintent = new Intent(this, MainActivity.class);
                startActivity(hintent);
                return true;
        }
        return false;
    }

    public void getEventAPI() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint("https://frc-api.usfirst.org/api/v1.0/")
                .build();

        FRCAPIEventInterface eventService = restAdapter.create(FRCAPIEventInterface.class);

        eventService.getEvents(new Callback<FRCEvents>() {
            @Override
            public void success(FRCEvents eventData, Response response) {
                consumeApiData(eventData);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Toast.makeText(getApplicationContext(), "Error getting events", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void consumeApiData(final FRCEvents eventData) {
        if (eventData != null) {
            ListView lv = (ListView) findViewById(R.id.eventListView);

            elist = eventData.getEvents();
            arrayAdapter = new CustomEventAdapter(GetFRCEvents.this, elist);
            lv.setAdapter(arrayAdapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    String e = (elist.get(position).getCode());

                    String PREFS_NAME = "ReconPrefsFile";
                    SharedPreferences settings;
                    settings = getSharedPreferences(PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("event", e);



                }

            });

        }
    }
}
