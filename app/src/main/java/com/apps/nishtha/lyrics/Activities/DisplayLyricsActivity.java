package com.apps.nishtha.lyrics.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apps.nishtha.lyrics.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class DisplayLyricsActivity extends AppCompatActivity {

    TextView textView,title;
    AdView adView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_lyrics);

        ((LinearLayout)findViewById(R.id.linearlayout)).getBackground().setAlpha(50);
        textView= (TextView) findViewById(R.id.textView);
        title= (TextView) findViewById(R.id.title);

        adView1= (AdView) findViewById(R.id.adView1);
        AdRequest adRequest=new AdRequest.Builder().build();
        adView1.loadAd(adRequest);

        Intent i=getIntent();
        String lyrics=i.getStringExtra("lyrics");
        setTitle(i.getStringExtra("songName"));
        title.setText(i.getStringExtra("songName"));
        textView.setText(lyrics);

    }
}
