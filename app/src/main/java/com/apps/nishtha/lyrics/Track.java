package com.apps.nishtha.lyrics;

/**
 * Created by nishtha on 20/7/17.
 */

class Track {
    private String track_share_url;
    private String track_name;
    private String artist_name;
    private String track_id;

    public String getTrack_id() {
        return track_id;
    }

    public String getArtist_name() {
        return artist_name;
    }

    public String getTrack_name() {
        return track_name;
    }

    public String getTrack_share_url() {
        return track_share_url;
    }

    public Track(String track_share_url,String track_name,String artist_name,String track_id) {
        this.track_share_url = track_share_url;
        this.track_name=track_name;
        this.artist_name=artist_name;
        this.track_id=track_id;
    }
}
