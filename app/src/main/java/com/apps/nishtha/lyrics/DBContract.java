package com.apps.nishtha.lyrics;

/**
 * Created by nishtha on 25/10/17.
 */

public interface DBContract {
    String TABLE_NAME="Saved_Lyrics";
    String COLUMN_LYRICS="lyrics";
    String COLUMN_NAME="title";
    String COLUMN_ID="id";
    String COLUMN_ARTIST="artist";

    String CREATE=" CREATE TABLE ";  //leave spaces
    String COMMA=" , ";
    String LBR=" ( ";
    String RBR=" ) ";
    String TERMINATE=" ; ";
    String INT_PK_AUTOIC = " INTEGER PRIMARY KEY AUTOINCREMENT ";
    String TYPE_TEXT=" TEXT ";
    String TYPE_INTEGER = " INTEGER ";
}
