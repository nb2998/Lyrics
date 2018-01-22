package com.apps.nishtha.lyrics.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apps.nishtha.lyrics.FavLyricsDB;
import com.apps.nishtha.lyrics.Model.FavModel;
import com.apps.nishtha.lyrics.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class DisplayLyricsActivity extends AppCompatActivity implements View.OnClickListener{

    TextView tvLyrics, tvTitle;
    AdView adView1;
    FloatingActionButton fabAddToFav;
    String lyrics,title,artist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_lyrics);

        ((RelativeLayout)findViewById(R.id.relativeLayout)).getBackground().setAlpha(50);
        tvLyrics = (TextView) findViewById(R.id.textView);
        tvTitle = (TextView) findViewById(R.id.title);
        fabAddToFav= (FloatingActionButton) findViewById(R.id.fabAddToFav);
        fabAddToFav.setOnClickListener(this);

        adView1= (AdView) findViewById(R.id.adView1);
        AdRequest adRequest=new AdRequest.Builder().build();
        adView1.loadAd(adRequest);

        Intent i=getIntent();
        lyrics = i.getStringExtra(getString(R.string.lyrics));
        title=i.getStringExtra(getString(R.string.songName));
        setTitle(title);
        tvTitle.setText(title);
        tvLyrics.setText(lyrics);
        artist=i.getStringExtra(getString(R.string.artistName));
    }

    public void exit(View view) {
        finish();
    }

    @Override
    public void onClick(View v) {
        Log.d("TAG", "onClick: "+title+"---- " +lyrics );
        if(v.getId()==R.id.fabAddToFav){
            if(!lyrics.equals(getString(R.string.incorrect_song_name))) {
                FavLyricsDB favLyricsDB = new FavLyricsDB(DisplayLyricsActivity.this);
                favLyricsDB.insertInFav(new FavModel(lyrics, title, artist));
                Snackbar.make(v, R.string.added_to_fav, Snackbar.LENGTH_SHORT).show();
            } else{
                Snackbar.make(v, R.string.fav_cannot_add, Snackbar.LENGTH_SHORT).show();
            }
        }
    }
}
