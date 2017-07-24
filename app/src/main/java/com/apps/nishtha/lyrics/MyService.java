package com.apps.nishtha.lyrics;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.widget.Toast;

public class MyService extends Service {

    MusicReceiver musicReceiver;
    AudioManager audioManager;

    public MyService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        musicReceiver = new MusicReceiver();

//
//        if (isNetworkAvailable(getBaseContext())) {
//            Notification notification= new NotificationCompat.Builder(getBaseContext())
//                    .setContentTitle("Click on the notification to know the lyrics")
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setContentText("")
//                    .setContentIntent(PendingIntent.getActivity(getBaseContext(),123,
//                            new Intent(getBaseContext(),DisplayLyricsActivity.class),PendingIntent.FLAG_UPDATE_CURRENT))
//                    .build();
//
//            ((NotificationManager)(getSystemService(NOTIFICATION_SERVICE))).notify(1,notification);
//        }

//        Thread thread=new Thread(new Runnable() {
//            @Override
//            public void run() {
        Intent i = new Intent();
        IntentFilter iF = new IntentFilter();

        iF.addAction("com.android.music.queuechanged");

        //HTC Music
        iF.addAction("com.htc.music.playstatechanged");
        iF.addAction("com.htc.music.playbackcomplete");
        iF.addAction("com.htc.music.metachanged");
        //MIUI Player
        iF.addAction("com.miui.player.playstatechanged");
        iF.addAction("com.miui.player.playbackcomplete");
        iF.addAction("com.miui.player.metachanged");
        //Real
        iF.addAction("com.real.IMP.playstatechanged");
        iF.addAction("com.real.IMP.playbackcomplete");
        iF.addAction("com.real.IMP.metachanged");
        //SEMC Music Player
        iF.addAction("com.sonyericsson.music.playbackcontrol.ACTION_TRACK_STARTED");
        iF.addAction("com.sonyericsson.music.playbackcontrol.ACTION_PAUSED");
        iF.addAction("com.sonyericsson.music.TRACK_COMPLETED");
        iF.addAction("com.sonyericsson.music.metachanged");
        iF.addAction("com.sonyericsson.music.playbackcomplete");
        iF.addAction("com.sonyericsson.music.playstatechanged");
        //rdio
        iF.addAction("com.rdio.android.metachanged");
        iF.addAction("com.rdio.android.playstatechanged");
        //Samsung Music Player
        iF.addAction("com.samsung.sec.android.MusicPlayer.playstatechanged");
        iF.addAction("com.samsung.sec.android.MusicPlayer.playbackcomplete");
        iF.addAction("com.samsung.sec.android.MusicPlayer.metachanged");
        iF.addAction("com.sec.android.app.music.playstatechanged");
        iF.addAction("com.sec.android.app.music.playbackcomplete");
        iF.addAction("com.sec.android.app.music.metachanged");
        //Winamp
        iF.addAction("com.nullsoft.winamp.playstatechanged");
        iF.addAction("com.nullsoft.winamp.metachanged");
        //Amazon
        iF.addAction("com.amazon.mp3.playstatechanged");
        iF.addAction("com.amazon.mp3.metachanged");
        //Rhapsody
        iF.addAction("com.rhapsody.playstatechanged");
        //PowerAmp
        iF.addAction("com.maxmpz.audioplayer.playstatechanged");
        //will be added any....
        //scrobblers detect for players (poweramp for example)
        //Last.fm
        iF.addAction("fm.last.android.metachanged");
        iF.addAction("fm.last.android.playbackpaused");
        iF.addAction("fm.last.android.playbackcomplete");
        //A simple last.fm scrobbler
        iF.addAction("com.adam.aslfms.notify.playstatechanged");
        // Others
        iF.addAction("net.jjc1138.android.scrobbler.action.MUSIC_STATUS");
        iF.addAction("com.andrew.apollo.metachanged");

        // Read action when music player changed current song
        // stock music player
//        iF.addAction("com.android.music.metachanged");
//
//        // MIUI music player
//        iF.addAction("com.miui.player.metachanged");
//
//        // HTC music player
//        iF.addAction("com.htc.music.metachanged");
//
//        // WinAmp
//        iF.addAction("com.nullsoft.winamp.metachanged");
//
//        // MyTouch4G
//        iF.addAction("com.real.IMP.metachanged");

        if(isNetworkAvailable(getBaseContext())) {
            registerReceiver(musicReceiver, iF);
        } else{
            Toast.makeText(getBaseContext(),"Sorry, check your Internet connection and restart the app!", Toast.LENGTH_LONG).show();
        }
//            }
//        });

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
