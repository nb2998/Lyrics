package com.apps.nishtha.lyrics;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import com.apps.nishtha.lyrics.Model.FavModel;

import java.util.ArrayList;

import static com.apps.nishtha.lyrics.DBContract.COLUMN_ARTIST;
import static com.apps.nishtha.lyrics.DBContract.COLUMN_ID;
import static com.apps.nishtha.lyrics.DBContract.COLUMN_LYRICS;
import static com.apps.nishtha.lyrics.DBContract.COLUMN_NAME;
import static com.apps.nishtha.lyrics.DBContract.COMMA;
import static com.apps.nishtha.lyrics.DBContract.CREATE;
import static com.apps.nishtha.lyrics.DBContract.INT_PK_AUTOIC;
import static com.apps.nishtha.lyrics.DBContract.LBR;
import static com.apps.nishtha.lyrics.DBContract.RBR;
import static com.apps.nishtha.lyrics.DBContract.TABLE_NAME;
import static com.apps.nishtha.lyrics.DBContract.TERMINATE;
import static com.apps.nishtha.lyrics.DBContract.TYPE_TEXT;

/**
 * Created by nishtha on 25/10/17.
 */

public class FavLyricsDB extends SQLiteOpenHelper {
    public static final String DBNAME="favLyrics";
    public static final Integer VERSION=1;

    public FavLyricsDB(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE=CREATE + TABLE_NAME + LBR +
                COLUMN_ID + INT_PK_AUTOIC +COMMA + COLUMN_NAME + TYPE_TEXT + COMMA + COLUMN_ARTIST + TYPE_TEXT + COMMA + COLUMN_LYRICS + TYPE_TEXT + RBR + TERMINATE;
        Log.d("TAG", "onCreate: "+CREATE_TABLE);
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertInFav(FavModel song){
        SQLiteDatabase sqldb=getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_LYRICS, song.getLyrics());
        cv.put(COLUMN_NAME, song.getTitle());
        cv.put(COLUMN_ARTIST, song.getArtist());
//        Log.d("TAG", "insertInFav: TITLE"+song.getTitle());
//        Log.d("TAG", "insertInFav: TITLE"+cv.get("title"));
        sqldb.insert(TABLE_NAME,null,cv);
    }

    public ArrayList<FavModel> getAllFavSongs(){
        SQLiteDatabase sqldb=getReadableDatabase();
        ArrayList<FavModel> favModelArrayList=new ArrayList<>();
        Cursor c=sqldb.query(TABLE_NAME,null,null,null,null,null,null,null);
//        ReadFromDbAsyncTask readFromDbAsyncTask=new ReadFromDbAsyncTask();
//        readFromDbAsyncTask.execute();
        while(c.moveToNext()) {
//            Log.d("TAG", "getAllFavSongs: "+c.getString(c.getColumnIndex(COLUMN_LYRICS)));
//            Log.d("TAG", "getAllFavSongs: "+c.getString(c.getColumnIndex(COLUMN_NAME)));
            favModelArrayList.add(new FavModel(c.getString(c.getColumnIndex(COLUMN_LYRICS))
                    , c.getString(c.getColumnIndex(COLUMN_NAME))
                    , c.getString(c.getColumnIndex(COLUMN_ARTIST))));
        }
        return favModelArrayList;
    }

    // TODO: 28/11/17 Make db read async
    private class ReadFromDbAsyncTask extends AsyncTask<Cursor,Void,ArrayList<FavModel>>{
        ArrayList<FavModel> favModelArrayList;

        @Override
        protected ArrayList<FavModel> doInBackground(Cursor... params) {

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            favModelArrayList = new ArrayList<>();
        }

        @Override
        protected void onPostExecute(ArrayList<FavModel> favModels) {
            super.onPostExecute(favModels);
        }
    }
}
