package com.apps.nishtha.lyrics;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DisplayLyricsActivity extends AppCompatActivity {

    TextView textView,title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_lyrics);
        ((LinearLayout)findViewById(R.id.linearlayout)).getBackground().setAlpha(50);
        textView= (TextView) findViewById(R.id.textView);
        title= (TextView) findViewById(R.id.title);

        Intent i=getIntent();
        String lyrics=i.getStringExtra("lyrics");
        setTitle(i.getStringExtra("songName"));
        title.setText(i.getStringExtra("songName"));
        textView.setText(lyrics);

    }
}
