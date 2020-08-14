package com.tudiby.freemusic.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import android.os.CountDownTimer;
import android.os.IBinder;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import com.tudiby.freemusic.Constants;
import com.tudiby.freemusic.MainActivity;

import java.util.ArrayList;
import java.util.List;

import ModalClass.OfflineModalClass;
import ModalClass.SongModel;

import static com.tudiby.freemusic.MainActivity.PLAYERSTATUS;
import static com.tudiby.freemusic.MainActivity.currentduraiton;
import static com.tudiby.freemusic.MainActivity.totalduration;

public class MediaPlayerService extends Service {

    //player
    private MediaPlayer mp = new MediaPlayer();
    public  static boolean LOOPINGSTATUS=false;


    public static List<SongModel> currentlist = new ArrayList<>();
    public static List<OfflineModalClass> currentlistoffline = new ArrayList<>();
    int pos=0;
    String type ="online";
    String mediaurl;
    //receiver

    @Override
    public void onCreate() {
        super.onCreate();


        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String status = intent.getStringExtra("status");

                if (status.equals("pause")){
                    mp.pause();
                }
                else if (status.equals("resume")){
                    mp.start();

                }

                else  if (status.equals("seek")){
                    int seek = intent.getIntExtra("seektime",0);

                    mp.pause();
                    mp.seekTo(seek);
                    mp.start();

                }
                else if (status.equals("stopmusic")){
                    mp.release();
                }
                else if (status.equals("getduration")){
                    totalduration=mp.getDuration();
                    currentduraiton=mp.getCurrentPosition();
                }
                else if (status.equals("settimer")){
                    Long end= intent.getLongExtra("end",0);
//                    Toast.makeText(getApplicationContext(),"Timer set : "+end,Toast.LENGTH_LONG).show();

                    new CountDownTimer(end, 1000) {


                        public void onTick(long millisUntilFinished) {

                        }

                        public void onFinish() {
                            Intent intent = new Intent("fando");
                            intent.putExtra("status", "stopmusic");
                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                            Toast.makeText(getApplicationContext(),"Timer End : ",Toast.LENGTH_LONG).show();


                        }
                    }.start();
                }




            }
        }, new IntentFilter("fando"));

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }







    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        pos=intent.getIntExtra("pos",0);
        type=intent.getStringExtra("type");

        playsong(pos,type);

        return START_STICKY;
    }


    private void playsong (final int pos, final String type){
        try {

            if (type.equals("online")){

                final SongModel modalClass = currentlist.get(pos);
                mediaurl= Constants.SERVERMUSIC+modalClass.getId();

            }
            else {
                final OfflineModalClass modalClass = currentlistoffline.get(pos);
                mediaurl= modalClass.getFilepath();
            }




            mp.stop();
            mp.reset();
            mp.release();



            Uri myUri = Uri.parse(mediaurl);
            mp = new MediaPlayer();
            mp.setDataSource(this, myUri);
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            mp.prepareAsync(); //don't use prepareAsync for mp3 playback

            mp.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {

                    return true;
                }
            });


            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp1) {


                    if (LOOPINGSTATUS){
                        playsong(pos,type);
                    }
                    else {
                        playsong(pos+1,type);
                    }



//                    playsong(pos,type);

                }



            });



            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @SuppressLint("RestrictedApi")
                @Override
                public void onPrepared(MediaPlayer mplayer) {


                    if (mplayer.isPlaying()) {
                        mp.pause();

                    } else {
                        mp.start();
                        PLAYERSTATUS="PLAYING";
                        Intent intent = new Intent("fando");
                        intent.putExtra("status", "playing");
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                        MainActivity.sessionId = mp.getAudioSessionId();





                    }

                }


            });





            mp.prepareAsync();


        }
        catch (Exception e){
            System.out.println(e);
        }


    }









}
