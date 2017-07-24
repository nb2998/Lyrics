package com.apps.nishtha.lyrics;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.NOTIFICATION_SERVICE;

public class MusicReceiver extends BroadcastReceiver {

    OkHttpClient okHttpClient = new OkHttpClient();
    ArrayList<Track> trackArrayList = new ArrayList<>();
    ArrayList<String> trackIdArrayList = new ArrayList<>();
    public static final String TAG = "TAG";
    Context c;
    String trackId;
    String track;
    String lyrics;

    StringBuilder artistName;
    StringBuilder trackName;
    AlertDialog alertDialog;

    @Override
    public void onReceive(Context context, Intent intent) {
        c = context;
        String action = intent.getAction();
        String cmd = intent.getStringExtra("command");
        Log.d("onReceive ", action + " / " + cmd);
        String artist = intent.getStringExtra("artist");
        String album = intent.getStringExtra("album");

        track = intent.getStringExtra("track");
        Log.d("Music", artist + ":" + album + ":" + track);


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
        }

        getTrackId(trackName.toString(), artistName.toString());


    }

    public void getTrackId(final String track, String artist) {
        String baseUrl = "http://api.musixmatch.com/ws/1.1/track.search?apikey=0e3945b8ba5f77f377843ec4b2539360";
        String url = baseUrl + "&q_track=" + track + "&q_artist=" + artist;
        Log.d(TAG, "getTrackId: " + url);
        Request request = new Request.Builder()
                .url(url)
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
//                                                      Log.d(TAG, "onResponse: size of tracklist " + details.getMessage().getBody().getTrack_list().size());
//                if (details != null && details.getMessage() != null && details.getMessage().getBody() != null && details.getMessage().getBody().getTrack_list() != null) {
                                                      for (int i = 0; i < details.getMessage().getBody().getTrack_list().size(); i++) {
                                                          trackIdArrayList.add(details.getMessage().getBody().getTrack_list().get(i).getTrack().getTrack_id());
//                                                          Log.d(TAG, "onResponse: size of trackidlist " + trackIdArrayList.size());
                                                      }
//                                                      Log.d(TAG, "onResponse: size of trackidlist outside for " + trackIdArrayList.size());


                                                      getLyrics();
//                                                      if(trackIdArrayList.size()!=0) {
//                                                          getLyrics();
//                                                      }
//                                                      else {
////                                                          Toast.makeText(c,"Sorry! Name of the song saved is not apt !",Toast.LENGTH_LONG).show();
//                                                      }
//
//     } else {
//                }

                                                  }

                                              }

        );

    }

    public void getLyrics() {
//        Log.d(TAG, "getLyrics: "+"https://api.musixmatch.com/ws/1.1/track.lyrics.get?apikey=0e3945b8ba5f77f377843ec4b2539360"+trackIdArrayList.get(0));
//        Log.d(TAG, "getLyrics: size of tracid list" + trackIdArrayList.size());
        String url;
        url = "https://api.musixmatch.com/ws/1.1/track.lyrics.get?apikey=0e3945b8ba5f77f377843ec4b2539360";
        if (trackIdArrayList.size() != 0) {
            Log.d(TAG, "getLyrics: " + trackIdArrayList.size());
            url = url + "&track_id=" + trackIdArrayList.get(0);
            Log.d(TAG, "getLyrics: " + url.toString());

            Request request1 = new Request.Builder()
                    .url(url)
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
                    if (lyricsDetails != null && lyricsDetails.getMessageLyrics() != null && lyricsDetails.getMessageLyrics().getBodyLyrics() != null && lyricsDetails.getMessageLyrics().getBodyLyrics().getLyrics() != null && lyricsDetails.getMessageLyrics().getBodyLyrics().getLyrics().getLyrics_body() != null) {
                        lyrics = lyricsDetails.getMessageLyrics().getBodyLyrics().getLyrics().getLyrics_body();
                        Log.d("TAG", "onResponse: " + lyrics);

                        Intent displayIntent = new Intent(c, DisplayLyricsActivity.class);
                        displayIntent.putExtra("lyrics", lyrics);
                        displayIntent.putExtra("songName",trackName.toString());
                        displayIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

//                        c.startActivity(displayIntent);

                        Notification notification = new NotificationCompat.Builder(c)
                                .setContentTitle("Want to know the lyrics?")
                                .setContentText("Current song : " + trackName)
                                .setSmallIcon(R.drawable.tune)
                                .setAutoCancel(true)
                                .setContentIntent(PendingIntent.getActivity(c, 123, displayIntent, PendingIntent.FLAG_UPDATE_CURRENT))
                                .build();

                        ((NotificationManager) (c.getSystemService(NOTIFICATION_SERVICE))).notify(1, notification);
                    }
                }
            });
        } else {
            Intent displayIntent = new Intent(c, DisplayLyricsActivity.class);
            displayIntent.putExtra("lyrics", "Sorry! The song is pirated or the name of the song saved is not apt!");
            displayIntent.putExtra("songName","Unavailable");
            displayIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            c.startActivity(displayIntent);
        }
    }


}
// https://api.musixmatch.com/ws/1.1/track.lyrics.get?apikey=0e3945b8ba5f77f377843ec4b2539360&track_id=113673904
// http://api.musixmatch.com/ws/1.1/track.search?apikey=0e3945b8ba5f77f377843ec4b2539360&q_track=Perfect&q_artist=Ed