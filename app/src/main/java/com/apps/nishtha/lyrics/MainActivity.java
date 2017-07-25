package com.apps.nishtha.lyrics;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.nishtha.lyrics.PojoForId.Details;
import com.apps.nishtha.lyrics.PojoForId.Track;
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

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Intent serviceIntent;
    StringBuilder trackName;
    StringBuilder artistName;
    RecyclerView recView;
    TrackAdapter trackAdapter;
    ArrayList<Track> trackArrayList=new ArrayList<>();
    TextView tvInvisible;
    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvInvisible= (TextView) findViewById(R.id.tvInvisible);
        recView= (RecyclerView) findViewById(R.id.recView);
        trackAdapter=new TrackAdapter(this,trackArrayList);
        recView.setLayoutManager(new LinearLayoutManager(this));
        recView.setAdapter(trackAdapter);
        okHttpClient = new OkHttpClient();
        switchCompat = (SwitchCompat) findViewById(R.id.enableSwitch);

        fab= (FloatingActionButton) findViewById(R.id.fab);
        View v=(LayoutInflater.from(this)).inflate(R.layout.dialog,null,false);
        trackNameEt= (EditText) v.findViewById(R.id.trackNameEt);
        artistNameEt= (EditText) v.findViewById(R.id.artistNameEt);
        final AlertDialog alertDialog=new AlertDialog.Builder(MainActivity.this)
                .setTitle("Search the lyrics")
                .setView(v)
                .setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getId();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.show();
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
            Toast.makeText(MainActivity.this, "Service disabled! Please enable the services to enjoy the lyrics " +
                    "while listening to music!", Toast.LENGTH_LONG).show();
        }

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switchCompat.isChecked()) {
                    editor.putBoolean("switchIsChecked", true);
                    editor.apply();
                    startService(serviceIntent);
                } else {
                    AlertDialog alertDialog=new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Are you sure you want to disable the service")
                            .setMessage("By doing so, you would stop receiving notifications to know the lyrics while listening to songs.")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    editor.putBoolean("switchIsChecked", false);
                                    editor.apply();
                                    stopService(serviceIntent);
                                    Toast.makeText(MainActivity.this, "Service disabled! Please enable the services to enjoy the lyrics while listening to music!", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switchCompat.setChecked(true);
                                    editor.putBoolean("switchIsChecked", true);
                                    editor.apply();
                                    startService(serviceIntent);
                                    dialog.dismiss();
                                }
                            })
                            .show();

                }
            }
        });
        super.onResume();
    }

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
                if (trackArrayList.size() != 0) {
                    trackArrayList.clear();
                }

                details = gson.fromJson(result, Details.class);
                for (int i = 0; i < details.getMessage().getBody().getTrack_list().size(); i++) {
                    trackArrayList.add(details.getMessage().getBody().getTrack_list().get(i).getTrack());
                    trackIdArrayList.add(details.getMessage().getBody().getTrack_list().get(i).getTrack().getTrack_id());
                }
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        tvInvisible.setVisibility(View.VISIBLE);
                        tvInvisible.setText("Click on any track to read the lyrics.");
                        trackAdapter.notifyDataSetChanged();
                        if(trackArrayList.size()==0){
                            Toast.makeText(MainActivity.this,"No results found!",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        (getMenuInflater()).inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.infoMenu){
            AlertDialog alertDialog=new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Information")
                    .setMessage("By enabling the service, you'll get a notification on playing any song in the Music Player. Click on the notification to enjoy the lyrics of the song! You can disable this service anytime using the switch/toggle above.")
                    .setPositiveButton("Got it!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        serviceIntent = new Intent(MainActivity.this, MyService.class);
        if (sharedPreferences.getBoolean("switchIsChecked", true)) {
            switchCompat.setChecked(true);
            startService(serviceIntent);
        }
        super.onDestroy();
    }
}
