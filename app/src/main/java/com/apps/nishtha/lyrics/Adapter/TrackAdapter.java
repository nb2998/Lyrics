package com.apps.nishtha.lyrics.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apps.nishtha.lyrics.Activities.DisplayLyricsActivity;
import com.apps.nishtha.lyrics.PojoForId.Details;
import com.apps.nishtha.lyrics.PojoForId.Track;
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

/**
 * Created by nishtha on 20/7/17.
 */

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.TrackHolder> {
    Context context;
    ArrayList<Track> trackArrayList;
    ArrayList<String> trackIdArrayList = new ArrayList<>();
    OkHttpClient okHttpClient;
    String trackName, artistName;
    String baseUrl = "http://api.musixmatch.com/ws/1.1/track.search?apikey=0e3945b8ba5f77f377843ec4b2539360";

    public TrackAdapter(Context context, ArrayList<Track> trackArrayList) {
        this.context = context;
        this.trackArrayList = trackArrayList;
        okHttpClient = new OkHttpClient();
    }

    @Override
    public TrackHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = (LayoutInflater.from(context)).inflate(R.layout.single_track, parent, false);
        TrackHolder trackHolder = new TrackHolder(view);
        return trackHolder;
    }

    @Override
    public void onBindViewHolder(final TrackHolder holder, int position) {
        final Track track = trackArrayList.get(position);
        holder.nameTv.setText("Track Name : " + track.getTrack_name());
        holder.artistTv.setText("Artist Name : " + track.getArtist_name());
        holder.trackCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trackName = track.getTrack_name();
                artistName = track.getArtist_name();
                getTrackId(trackName, artistName);
            }
        });
    }

    @Override
    public int getItemCount() {
        return trackArrayList.size();
    }

    class TrackHolder extends RecyclerView.ViewHolder {
        TextView nameTv, artistTv;
        CardView trackCardView;

        public TrackHolder(View itemView) {
            super(itemView);
            nameTv = (TextView) itemView.findViewById(R.id.nameTv);
            artistTv = (TextView) itemView.findViewById(R.id.artistTv);
            trackCardView = (CardView) itemView.findViewById(R.id.trackCardView);
        }
    }

    StringBuilder url = new StringBuilder();
    String lyrics;

    public void getTrackId(String trackName, String artistName) {
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

    public void getLyrics() {
        url.append("https://api.musixmatch.com/ws/1.1/track.lyrics.get?apikey="+context.getResources().getString(R.string.api_key)+"&track_id=");
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

                            Intent displayIntent = new Intent(context, DisplayLyricsActivity.class);
                            displayIntent.putExtra("lyrics", lyrics);
                            displayIntent.putExtra("songName", trackName.toString());
                            displayIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            displayIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            displayIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                            context.startActivity(displayIntent);

                        }
                    } catch (Exception e) {
                        Intent displayIntent = new Intent(context, DisplayLyricsActivity.class);
                        displayIntent.putExtra("lyrics", "Sorry! Lyrics Unavailable for the song");
                        displayIntent.putExtra("songName", "Unavailable");
                        displayIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        displayIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        displayIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                        context.startActivity(displayIntent);
                    }
                }
            });

        } else {
            Intent displayIntent = new Intent(context, DisplayLyricsActivity.class);
            displayIntent.putExtra("lyrics", "Sorry! Song name or artist name seems to be incorrect!");
            displayIntent.putExtra("songName", "Unavailable");
            displayIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            displayIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            displayIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            context.startActivity(displayIntent);
        }
    }
}
