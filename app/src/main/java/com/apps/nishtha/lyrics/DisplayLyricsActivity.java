package com.apps.nishtha.lyrics;

import android.content.Intent;
import android.os.Bundle;
<<<<<<< HEAD
import android.util.Log;
import android.widget.LinearLayout;
=======
import android.support.v7.app.AppCompatActivity;
>>>>>>> 087b162883b3d1553ed39a833ba6a42a4b1cb429
import android.widget.TextView;

public class DisplayLyricsActivity extends AppCompatActivity {

    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_lyrics);
        ((LinearLayout)findViewById(R.id.linearlayout)).getBackground().setAlpha(50);
        textView= (TextView) findViewById(R.id.textView);

        Intent i=getIntent();
        String lyrics=i.getStringExtra("lyrics");
        setTitle(i.getStringExtra("songName"));
        textView.setText(lyrics);

    }
}
