package com.apps.nishtha.lyrics.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.apps.nishtha.lyrics.Adapter.FavAdapter;
import com.apps.nishtha.lyrics.FavLyricsDB;
import com.apps.nishtha.lyrics.Model.FavModel;
import com.apps.nishtha.lyrics.R;

import java.util.ArrayList;

public class FavouritesActivity extends AppCompatActivity {

    ArrayList<FavModel> favModelArrayList=new ArrayList<>();
    RecyclerView recViewFav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        recViewFav= (RecyclerView) findViewById(R.id.recViewFav);
        recViewFav.setLayoutManager(new LinearLayoutManager(this));

        FavLyricsDB favLyricsDB=new FavLyricsDB(this);
        favModelArrayList =favLyricsDB.getAllFavSongs();
        recViewFav.setAdapter(new FavAdapter(this,favModelArrayList));

        Log.d("TAG", "onCreate: "+favModelArrayList.size());
    }
}
