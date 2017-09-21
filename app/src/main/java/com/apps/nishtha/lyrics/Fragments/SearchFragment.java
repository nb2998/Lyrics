package com.apps.nishtha.lyrics.Fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.nishtha.lyrics.Adapter.TrackAdapter;
import com.apps.nishtha.lyrics.PojoForId.Details;
import com.apps.nishtha.lyrics.PojoForId.Track;
import com.apps.nishtha.lyrics.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    TextView tvInvisible;
    RecyclerView recView;
    FloatingActionButton fab;

    EditText trackNameEt,artistNameEt;
    StringBuilder trackName,artistName;

    OkHttpClient okHttpClient;
    String baseUrl="http://api.musixmatch.com/ws/1.1/track.search?apikey=0e3945b8ba5f77f377843ec4b2539360";

    ArrayList<Track> trackArrayList=new ArrayList<>();
    ArrayList<String> trackIdArrayList=new ArrayList<>();
    TrackAdapter trackAdapter;

    AdView adView;

    public SearchFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.search_fragment,container,false);

        okHttpClient=new OkHttpClient();
        tvInvisible= (TextView) v.findViewById(R.id.tvInvisible);
        recView= (RecyclerView) v.findViewById(R.id.recView);
        fab= (FloatingActionButton) v.findViewById(R.id.fab);

        recView.setLayoutManager(new LinearLayoutManager(getContext()));
        trackAdapter=new TrackAdapter(getContext(),trackArrayList);
        recView.setAdapter(trackAdapter);

        adView= (AdView) v.findViewById(R.id.adView);
        AdRequest adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        View dialogView=(LayoutInflater.from(getContext())).inflate(R.layout.dialog,null,false);
        trackNameEt= (EditText) dialogView.findViewById(R.id.trackNameEt);
        artistNameEt= (EditText) dialogView.findViewById(R.id.artistNameEt);
        final AlertDialog alertDialog=new AlertDialog.Builder(getContext())
                .setTitle("Search the lyrics")
                .setView(dialogView)
                .setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getTrackId();
                        dialog.dismiss();
                        trackNameEt.setText("");
                        artistNameEt.setText("");
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        trackNameEt.setText("");
                        artistNameEt.setText("");
                    }
                })
                .create();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.show();
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void getTrackId() {
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
                if(getActivity()!=null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvInvisible.setText("Click on any track to read the lyrics.");
                            trackAdapter.notifyDataSetChanged();
                            if (trackArrayList.size() == 0) {
                                Toast.makeText(getContext(), "No results found!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
