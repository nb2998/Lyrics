package com.apps.nishtha.lyrics.Fragments;


import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.nishtha.lyrics.Adapter.TrackAdapter;
import com.apps.nishtha.lyrics.PojoForId.Track;
import com.apps.nishtha.lyrics.R;

import java.util.ArrayList;

import okhttp3.OkHttpClient;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllTracksFragment extends Fragment {

    String thisTitle, thisArtist;
    ArrayList<Track> trackArrayList = new ArrayList<>();
    RecyclerView recViewForTracksInStorage;
    TrackAdapter trackAdapter;

    StringBuilder trackName, artistName;
    public static final int RCODE = 123;
    OkHttpClient okHttpClient;

    public AllTracksFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_tracks_fragment, container, false);

        okHttpClient = new OkHttpClient();

        recViewForTracksInStorage = (RecyclerView) view.findViewById(R.id.recViewForTracksInStorage);
        recViewForTracksInStorage.setLayoutManager(new LinearLayoutManager(getContext()));
        trackAdapter = new TrackAdapter(getContext(), trackArrayList);
        recViewForTracksInStorage.setAdapter(trackAdapter);

            getMusic();
        return view;
    }

    public void getMusic() {
        Log.d("TAG", "getMusic: inside ");
        ContentResolver musicResolver = getContext().getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        if (musicCursor != null && musicCursor.moveToFirst()) {
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            do {
                thisTitle = musicCursor.getString(titleColumn);
                thisArtist = musicCursor.getString(artistColumn);

                trackName = new StringBuilder();
                if (thisTitle == null || thisTitle.equals("")) {
                    trackName.append("");
                } else {
                    for (int i = 0; i < thisTitle.length(); i++) {
                        char currChar = thisTitle.charAt(i);

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
                if (thisArtist == null || thisArtist.equals("")) {
                    artistName.append("");
                } else {
                    for (int i = 0; i < thisArtist.length(); i++) {
                        char currChar = thisArtist.charAt(i);

                        if (currChar == '(' || currChar == '[' || currChar == '-') {
                            artistName.append("");
                            break;
                        } else if (thisArtist.charAt(i) == ' ') {
                            artistName.append("_");
                        } else if (currChar == ',') {
                            artistName.append("&");
                        } else {
                            artistName.append(thisArtist.charAt(i));
                        }
                    }
                    if (artistName.length() >= 7) {
                        if (thisArtist.substring(0, 7).equals("Unknown")) {
                            artistName.replace(0, artistName.length(), "");
                        }
                    }
                    if (artistName.length() >= 9) {
                        if (thisArtist.substring(0, 9).equals("<unknown>")) {
                            artistName.replace(0, artistName.length(), "");
                        }
                    }
                }
                //Excluded songs starting with AUD(whatsapp)
                if(!trackName.toString().equals("AUD"))
                    trackArrayList.add(new Track(trackName.toString(), artistName.toString(), ""));
                trackAdapter.notifyDataSetChanged();
            }
            while (musicCursor.moveToNext());
        }
    }
}
