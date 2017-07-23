package com.apps.nishtha.lyrics;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class DisplayLyricsActivity extends AppCompatActivity {

    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_lyrics);

        textView= (TextView) findViewById(R.id.textView);

        Intent i=getIntent();
        String lyrics=i.getStringExtra("lyrics");
        Log.d("TAG", "onCreate: "+lyrics);
        textView.setText(lyrics);

    }
}
