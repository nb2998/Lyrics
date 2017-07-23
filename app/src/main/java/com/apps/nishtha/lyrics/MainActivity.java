package com.apps.nishtha.lyrics;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    EditText nameEt;
    Button btnSearch;
    SwitchCompat switchCompat;
    String baseUrl = "http://api.musixmatch.com/ws/1.1/track.search?apikey=0e3945b8ba5f77f377843ec4b2539360&q_track=";
    OkHttpClient okHttpClient;
    RecyclerView recView;
    TrackAdapter trackAdapter;
    ArrayList<Track> trackArrayList = new ArrayList<>();
    public static final String TAG = "TAG";
    String trackId;
    ArrayList<String> trackIdArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameEt = (EditText) findViewById(R.id.nameEt);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        recView = (RecyclerView) findViewById(R.id.recView);
        switchCompat = (SwitchCompat) findViewById(R.id.enableSwitch);
        recView.setLayoutManager(new LinearLayoutManager(this));
        trackAdapter = new TrackAdapter(this, trackArrayList);
        recView.setAdapter(trackAdapter);

        Intent serviceIntent = new Intent(MainActivity.this, MyService.class);
        startService(serviceIntent);

//        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    Intent serviceIntent=new Intent(MainActivity.this,MyService.class);
//                    startService(serviceIntent);
//                }
//                else{
//                    Toast.makeText(MainActivity.this,"Sorry! Enable the services to know the lyrics", Toast.LENGTH_LONG).show();
//                }
//            }
//        });


        okHttpClient = new OkHttpClient();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getId();
            }
        });
    }

    public void getId() {
        String search = baseUrl + nameEt.getText().toString();
        Log.d(TAG, "getId: search  " + search);
        Request request = new Request.Builder()
                .url(search)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            Details details;

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (trackArrayList.size() != 0) {
                    trackArrayList.clear();
                }
                String result = response.body().string();
                Gson gson = new Gson();

                details = gson.fromJson(result, Details.class);

                trackId = details.getMessage().getBody().getTrack_list().get(0).getTrack().getTrack_id();

//                        Log.d(TAG, "onResponse: "+details.getMessage().getBody().getTrack_list().get(0).getTrack().getTrack_name());
//                        Log.d(TAG, "onResponse: "+details.getMessage().getBody().getTrack_list().get(0).getTrack().getTrack_share_url());
                if (details != null && details.getMessage() != null && details.getMessage().getBody() != null && details.getMessage().getBody().getTrack_list() != null) {

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < details.getMessage().getBody().getTrack_list().size(); i++) {
                                trackArrayList.add(details.getMessage().getBody().getTrack_list().get(i).getTrack());
                                trackIdArrayList.add(details.getMessage().getBody().getTrack_list().get(i).getTrack().getTrack_id());
                            }
//                                        Log.d(TAG, "run: "+trackArrayList.size());
                            if (trackArrayList.size() == 0) {
                                Toast.makeText(MainActivity.this, "Lyrics not available", Toast.LENGTH_SHORT).show();
                            }
                            trackAdapter.notifyDataSetChanged();
                        }
                    });


                } else {
                    Toast.makeText(MainActivity.this, "Invalid Request", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    String lyrics;

    public void getLyrics(View view) {
        Log.d(TAG, "getLyrics: " + trackIdArrayList.get(0));
        Request request1 = new Request.Builder()
                .url("https://api.musixmatch.com/ws/1.1/track.lyrics.get?apikey=0e3945b8ba5f77f377843ec4b2539360&track_id=" + trackIdArrayList.get(0))
                .build();
        okHttpClient.newCall(request1).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Gson gson = new Gson();
                LyricsDetails lyricsDetails = gson.fromJson(result, LyricsDetails.class);
                lyrics = lyricsDetails.getMessageLyrics().getBodyLyrics().getLyrics().getLyrics_body();
                Log.d(TAG, "onResponse lyrics : " + lyrics);
                Intent displayIntent = new Intent(MainActivity.this, DisplayLyricsActivity.class);
                displayIntent.putExtra("lyrics", lyrics);
                startActivity(displayIntent);
            }
        });

    }


}
