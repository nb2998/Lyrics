package com.apps.nishtha.lyrics.PojoForId;

import java.util.ArrayList;

/**
 * Created by nishtha on 20/7/17.
 */

public class Body {

    private ArrayList<TrackList> track_list;

    public ArrayList<TrackList> getTrack_list() {
        return track_list;
    }

    public Body(ArrayList<TrackList> track_list) {
        this.track_list = track_list;
    }
}
