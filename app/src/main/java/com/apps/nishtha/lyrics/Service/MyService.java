package com.apps.nishtha.lyrics.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.widget.Toast;

import com.apps.nishtha.lyrics.R;
import com.apps.nishtha.lyrics.Receiver.MusicReceiver;

public class MyService extends Service {

    MusicReceiver musicReceiver;
    AudioManager audioManager;

    public MyService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        musicReceiver = new MusicReceiver();

        Intent i = new Intent();
        IntentFilter iF = new IntentFilter();

        iF.addAction("com.android.music.queuechanged");

        iF.addAction("com.bsbportal.music.playstatechanged");
        iF.addAction("com.bsbportal.music.playbackcomplete");
        iF.addAction("com.bsbportal.music.metachanged");
        iF.addAction("com.bsbportal.music.queuechanged");

        //HTC Music
        iF.addAction("com.htc.music.playstatechanged");
        iF.addAction("com.htc.music.playbackcomplete");
        iF.addAction("com.htc.music.metachanged");
        iF.addAction("com.htc.music.queuechanged");
        //MIUI Player
        iF.addAction("com.miui.player.playstatechanged");
        iF.addAction("com.miui.player.playbackcomplete");
        iF.addAction("com.miui.player.metachanged");
        iF.addAction("com.miui.player.queuechanged");
        //Real
        iF.addAction("com.real.IMP.playstatechanged");
        iF.addAction("com.real.IMP.playbackcomplete");
        iF.addAction("com.real.IMP.metachanged");
        iF.addAction("com.real.IMP.queuechanged");
        //SEMC Music Player
        iF.addAction("com.sonyericsson.music.playbackcontrol.ACTION_TRACK_STARTED");
        iF.addAction("com.sonyericsson.music.playbackcontrol.ACTION_PAUSED");
        iF.addAction("com.sonyericsson.music.TRACK_COMPLETED");
        iF.addAction("com.sonyericsson.music.metachanged");
        iF.addAction("com.sonyericsson.music.playbackcomplete");
        iF.addAction("com.sonyericsson.music.playstatechanged");
        iF.addAction("com.sonyericsson.music.queuechanged");
        //rdio
        iF.addAction("com.rdio.android.metachanged");
        iF.addAction("com.rdio.android.playstatechanged");
        //Samsung Music Player
        iF.addAction("com.sec.android.app.music.playstatechanged");
        iF.addAction("com.sec.android.app.music.metachanged");
        iF.addAction("com.sec.android.app.music.playbackcomplete");
        iF.addAction("com.sec.android.app.music.queuechanged");
        iF.addAction("com.android.musicfx.playstatechanged");
        iF.addAction("com.android.musicfx.metachanged");
        iF.addAction("com.android.musicfx.playbackcomplete");
        iF.addAction("com.android.musicfx.queuechanged");

        iF.addAction("com.sec.android.MusicPlayer.playstatechanged");
        iF.addAction("com.sec.android.MusicPlayer.playbackcomplete");
        iF.addAction("com.sec.android.MusicPlayer.metachanged");
        iF.addAction("com.sec.android.MusicPlayer.queuechanged");
        iF.addAction("com.sec.android.app.music.playstatechanged");
        iF.addAction("com.sec.android.app.music.playbackcomplete");
        iF.addAction("com.sec.android.app.music.queuechanged");

        iF.addAction("com.google.android.music.metachanged");
        iF.addAction("com.google.android.music.playbackcomplete");
        iF.addAction("com.google.android.music.metachanged");
        iF.addAction("com.google.android.music.queuechanged");
        //Winamp
        iF.addAction("com.nullsoft.winamp.playstatechanged");
        iF.addAction("com.nullsoft.winamp.metachanged");
        iF.addAction("com.nullsoft.winamp.queuechanged");
        //Amazon
        iF.addAction("com.amazon.mp3.playstatechanged");
        iF.addAction("com.amazon.mp3.metachanged");
        iF.addAction("com.amazon.mp3.queuechanged");
        //Rhapsody
        iF.addAction("com.rhapsody.playstatechanged");
        iF.addAction("com.rhapsody.metachanged");
        iF.addAction("com.rhapsody.queuechanged");
        //PowerAmp
        iF.addAction("com.maxmpz.audioplayer.playstatechanged");
        iF.addAction("com.maxmpz.audioplayer.metachanged");
        iF.addAction("com.maxmpz.audioplayer.queuechanged");
        //will be added any....
        //scrobblers detect for players (poweramp for example)
        //Last.fm
        iF.addAction("fm.last.android.metachanged");
        iF.addAction("fm.last.android.playbackpaused");
        iF.addAction("fm.last.android.playbackcomplete");
        iF.addAction("fm.last.android.queuechanged");
        //A simple last.fm scrobbler
        iF.addAction("com.adam.aslfms.notify.playstatechanged");
        iF.addAction("com.adam.aslfms.notify.queuechanged");
        // Others
        iF.addAction("net.jjc1138.android.scrobbler.action.MUSIC_STATUS");
        iF.addAction("com.andrew.apollo.metachanged");

        // Read action when music player changed current song
        // stock music player
        iF.addAction("com.android.music.metachanged");

        // MyTouch4G
        iF.addAction("com.real.IMP.metachanged");

        iF.addAction("com.spotify.music.playbackstatechanged");
        iF.addAction("com.spotify.music.metadatachanged");
        iF.addAction("com.spotify.music.queuechanged");

       iF.addAction("com.jrtstudio.music.metachanged");
       iF.addAction("com.jrtstudio.music.playstatechanged");
       iF.addAction("com.jrtstudio.music.playbackcomplete");
       iF.addAction("com.jrtstudio.music.queuechanged");

        iF.addAction("com.android.mediacenter.playstatechanged");
        iF.addAction("com.android.mediacenter.playbackcomplete");
        iF.addAction("com.android.mediacenter.queuechanged");
        iF.addAction("com.android.mediacenter.metachanged");

        iF.addAction("com.jetappfactory.jetaudio.playstatechanged");
        iF.addAction("com.jetappfactory.jetaudio.metachanged");
        iF.addAction("com.jetappfactory.jetaudio.playbackcomplete");
        iF.addAction("com.jetappfactory.jetaudio.queuechanged");
        iF.addAction("com.jetappfactory.jetaudioplus.playstatechanged");
        iF.addAction("com.jetappfactory.jetaudioplus.metachanged");
        iF.addAction("com.jetappfactory.jetaudioplus.playbackcomplete");
        iF.addAction("com.jetappfactory.jetaudioplus.queuechanged");

        iF.addAction("com.e8tracks.playstatechanged");
        iF.addAction("com.e8tracks.playbackcomplete");
        iF.addAction("com.e8tracks.metachanged");
        iF.addAction("com.e8tracks.queuechanged");

        iF.addAction("com.doubleTwist.androidPlayer.metachanged");
        iF.addAction("com.doubleTwist.androidPlayer.playstatechanged");
        iF.addAction("com.doubleTwist.androidPlayer.playbackcomplete");
        iF.addAction("com.doubleTwist.androidPlayer.queuechanged");

        iF.addAction("com.tbig.playerpro.playstatechanged");
        iF.addAction("com.tbig.playerpro.playbackcomplete");
        iF.addAction("com.tbig.playerpro.metachanged");
        iF.addAction("com.tbig.playerpro.queuechanged");
        iF.addAction("com.tbig.playerpro.playstatechanged");
        iF.addAction("com.tbig.playerpro.metachanged");
        iF.addAction("com.tbig.playerpro.playbackcomplete");
        iF.addAction("com.tbig.playerpro.queuechanged");


        if(isNetworkAvailable(getBaseContext())) registerReceiver(musicReceiver, iF);
        else
            Toast.makeText(getBaseContext(), R.string.enable_internet, Toast.LENGTH_LONG).show();
        return START_STICKY_COMPATIBILITY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}