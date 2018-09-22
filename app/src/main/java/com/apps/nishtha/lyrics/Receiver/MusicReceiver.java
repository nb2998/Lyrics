package com.apps.nishtha.lyrics.Receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.apps.nishtha.lyrics.Activities.DisplayLyricsActivity;
import com.apps.nishtha.lyrics.PojoForId.Details;
import com.apps.nishtha.lyrics.PojoLyrics.LyricsDetails;
import com.apps.nishtha.lyrics.R;
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
    ArrayList<String> trackIdArrayList = new ArrayList<>();
    public static final String TAG = "TAG";
    Context c;
    String track;
    String lyrics;

    StringBuilder artistName;
    StringBuilder trackName;
    NotificationManager notifManager;

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
            if (artistName.length() >= 7) {
                if (artist.substring(0, 7).equals("Unknown")||artist.substring(0, 7).equals("Unknown")) {
                    artistName.replace(0, artistName.length(), "");
                }
            }
            if (artistName.length() >= 9) {
                if (artist.substring(0, 9).equals("<unknown>")) {
                    artistName.replace(0, artistName.length(), "");
                }
            }
        }

        Notification notification = new NotificationCompat.Builder(c)
                .setContentTitle("Lyrics unavailable")
                .setContentText("Sorry!")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .build();

        if (track == null) {
            ((NotificationManager) (c.getSystemService(NOTIFICATION_SERVICE))).notify(1, notification);
        } else {
            getTrackId(trackName.toString(), artistName.toString());
        }


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
                                                      try {
                                                          details = gson.fromJson(result, Details.class);
                                                          for (int i = 0; i < details.getMessage().getBody().getTrack_list().size(); i++) {
                                                              trackIdArrayList.add(details.getMessage().getBody().getTrack_list().get(i).getTrack().getTrack_id());
                                                          }
                                                          getLyrics();
                                                      } catch (Exception e){
                                                          Notification notification = new NotificationCompat.Builder(c)
                                                                  .setContentTitle("Sorry!")
                                                                  .setContentText("Invalid request!")
                                                                  .setSmallIcon(R.mipmap.ic_launcher)
                                                                  .setAutoCancel(true)
                                                                  .build();
                                                          ((NotificationManager) (c.getSystemService(NOTIFICATION_SERVICE))).notify(1, notification);
                                                      }
                                                  }

                                              }
        );
    }

    public void getLyrics() {
        StringBuilder url = new StringBuilder();
        url.append("https://api.musixmatch.com/ws/1.1/track.lyrics.get?apikey=0e3945b8ba5f77f377843ec4b2539360");
        if (trackIdArrayList.size() != 0) {
            url.append("&track_id=" + trackIdArrayList.get(0));
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

                            Intent displayIntent = new Intent(c, DisplayLyricsActivity.class);
                            displayIntent.putExtra("lyrics", lyrics);
                            displayIntent.putExtra("songName", trackName.toString());
                            displayIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            displayIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            displayIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                            Notification notification = new NotificationCompat.Builder(c)
                                    .setContentTitle("Want to know the lyrics?")
                                    .setContentText("Current song : " + trackName)
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setAutoCancel(true)
                                    .setContentIntent(PendingIntent.getActivity(c, 123, displayIntent, PendingIntent.FLAG_UPDATE_CURRENT))
                                    .build();

                            ((NotificationManager) (c.getSystemService(NOTIFICATION_SERVICE))).notify(1, notification);
                        }
                    } catch (Exception e) {
                        Notification notification = new NotificationCompat.Builder(c)
                                .setContentTitle("Sorry!")
                                .setContentText("Lyrics Unavailable for the song")
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setAutoCancel(true)
                                .build();
                        ((NotificationManager) (c.getSystemService(NOTIFICATION_SERVICE))).notify(1, notification);
                    }
                }

            });
        } else {

            Notification notification = new NotificationCompat.Builder(c)
                    .setContentTitle("Lyrics unavailable")
                    .setContentText("Sorry! The song seems to be pirated")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(true)
                    .build();

            ((NotificationManager) (c.getSystemService(NOTIFICATION_SERVICE))).notify(1, notification);
        }
    }

}
// https://api.musixmatch.com/ws/1.1/track.lyrics.get?apikey=0e3945b8ba5f77f377843ec4b2539360&track_id=113673904
// http://api.musixmatch.com/ws/1.1/track.search?apikey=0e3945b8ba5f77f377843ec4b2539360&q_track=Perfect&q_artist=Ed