package com.apps.nishtha.lyrics;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.apps.nishtha.lyrics.PojoForId.Details;
import com.apps.nishtha.lyrics.PojoLyrics.LyricsDetails;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    EditText trackNameEt, artistNameEt;
    Button btnSearch;
    SwitchCompat switchCompat;
    String baseUrl = "http://api.musixmatch.com/ws/1.1/track.search?apikey=0e3945b8ba5f77f377843ec4b2539360";
    OkHttpClient okHttpClient;
    public static final String TAG = "TAG";
    ArrayList<String> trackIdArrayList = new ArrayList<>();

    boolean switchIsChecked;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        okHttpClient = new OkHttpClient();
        trackNameEt = (EditText) findViewById(R.id.trackNameEt);
        artistNameEt = (EditText) findViewById(R.id.artistNameEt);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        switchCompat = (SwitchCompat) findViewById(R.id.enableSwitch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getId();
            }
        });
    }

    @Override
    protected void onResume() {
//         TODO: 24/7/17 Not working : Trying to give a provision to enable/disable the services
        sharedPreferences = getPreferences(MODE_PRIVATE);
        editor = sharedPreferences.edit();

        serviceIntent = new Intent(MainActivity.this, MyService.class);

        if (sharedPreferences.getBoolean("switchIsChecked", true)) {
            switchCompat.setChecked(true);
            startService(serviceIntent);
        } else {
            switchCompat.setChecked(false);
            Toast.makeText(MainActivity.this, "Service disabled! Please enable the services to enjoy the lyrics!", Toast.LENGTH_LONG).show();
        }

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switchCompat.isChecked()) {
                    editor.putBoolean("switchIsChecked", true);
                    editor.apply();
                    startService(serviceIntent);
                } else {
                    editor.putBoolean("switchIsChecked", false);
                    editor.apply();
                    stopService(serviceIntent);
                    Toast.makeText(MainActivity.this, "Sorry! Enable the services to know the lyrics", Toast.LENGTH_LONG).show();
                }
            }
        });
        super.onResume();
    }

    StringBuilder trackName;
    StringBuilder artistName;

    public void getId() {
        String track = trackNameEt.getText().toString();
        String artist = artistNameEt.getText().toString();

        trackName = new StringBuilder();
        if (track == null || track.equals("")) {
            trackName.append("");
        } else {
            for (int i = 0; i < track.length(); i++) {
                char currChar = track.charAt(i);

                if (currChar == '(' || currChar == '[' || currChar == '-') {
                    trackName.append("");
                    break;
                } else if (currChar == ' ') {
                    trackName.append("_");
                } else if (currChar == ',') {
                    trackName.append("&");
                } else {
                    trackName.append(currChar);
                }
            }
        }

        artistName = new StringBuilder();
        if (artist == null || artist.equals("")) {
            artistName.append("");
        } else {
            for (int i = 0; i < artist.length(); i++) {
                char currChar = artist.charAt(i);

                if (currChar == '(' || currChar == '[' || currChar == '-') {
                    artistName.append("");
                    break;
                } else if (artist.charAt(i) == ' ') {
                    artistName.append("_");
                } else if (currChar == ',') {
                    artistName.append("&");
                } else {
                    artistName.append(artist.charAt(i));
                }
            }
            if (artistName.length() >= 7) {
                if (artist.substring(0, 7).equals("Unknown")) {
                    artistName.replace(0, artistName.length(), "");
                }
            }
        }
        String search = baseUrl + "&q_track=" + trackName + "&q_artist=" + artistName;

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
                String result = response.body().string();
                Gson gson = new Gson();

                if (trackIdArrayList.size() != 0) {
                    trackIdArrayList.clear();
                }

                details = gson.fromJson(result, Details.class);
                for (int i = 0; i < details.getMessage().getBody().getTrack_list().size(); i++) {
                    trackIdArrayList.add(details.getMessage().getBody().getTrack_list().get(i).getTrack().getTrack_id());
                }


                getLyrics();
            }
        });
    }


    String lyrics;
    StringBuilder url = new StringBuilder();

    public void getLyrics() {
        url.append("https://api.musixmatch.com/ws/1.1/track.lyrics.get?apikey=0e3945b8ba5f77f377843ec4b2539360&track_id=");
        if (trackIdArrayList.size() != 0) {
            url.append(trackIdArrayList.get(0));

            Request request1 = new Request.Builder()
                    .url(url.toString())
                    .build();
            okHttpClient.newCall(request1).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    Gson gson = new Gson();

                    try {
                        LyricsDetails lyricsDetails = gson.fromJson(result, LyricsDetails.class);

                        if (lyricsDetails != null && lyricsDetails.getMessageLyrics() != null && lyricsDetails.getMessageLyrics().getBodyLyrics() != null && lyricsDetails.getMessageLyrics().getBodyLyrics().getLyrics() != null && lyricsDetails.getMessageLyrics().getBodyLyrics().getLyrics().getLyrics_body() != null) {
                            lyrics = lyricsDetails.getMessageLyrics().getBodyLyrics().getLyrics().getLyrics_body();

                            Intent displayIntent = new Intent(MainActivity.this, DisplayLyricsActivity.class);
                            displayIntent.putExtra("lyrics", lyrics);
                            displayIntent.putExtra("songName", trackName.toString());
                            displayIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            displayIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            displayIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                            startActivity(displayIntent);
                        }
                    } catch (Exception e) {
                        Intent displayIntent = new Intent(MainActivity.this, DisplayLyricsActivity.class);
                        displayIntent.putExtra("lyrics", "Sorry! Lyrics Unavailable for the song");
                        displayIntent.putExtra("songName", "Unavailable");
                        displayIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        displayIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        displayIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                        startActivity(displayIntent);
                    }
                }

            });
        } else {
            Intent displayIntent = new Intent(MainActivity.this, DisplayLyricsActivity.class);
            displayIntent.putExtra("lyrics", "Sorry! Song name or artist name seems to be incorrect!");
            displayIntent.putExtra("songName", "Unavailable");
            displayIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            displayIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            displayIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            startActivity(displayIntent);
        }
    }
}
