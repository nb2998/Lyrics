package com.apps.nishtha.lyrics;

import java.util.ArrayList;

/**
 * Created by nishtha on 20/7/17.
 */

class Body {

    private ArrayList<TrackList> track_list;

    public ArrayList<TrackList> getTrack_list() {
        return track_list;
    }

    public Body(ArrayList<TrackList> track_list) {
        this.track_list = track_list;
    }
}
