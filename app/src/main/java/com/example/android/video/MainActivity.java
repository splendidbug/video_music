package com.example.android.video;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    Button playVid, playMusic, pauseMusic;
    VideoView videoView;
    MediaController mediaController;
    MediaPlayer mediaPlayer;
    SeekBar seekBar;
    AudioManager audioManager; //for everything in android we have an object
    SeekBar seekBar2;
    Timer timer;
    Handler handler;
    Runnable runnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playVid = findViewById(R.id.playVid);
        videoView= findViewById(R.id.videoView);
        playMusic = findViewById(R.id.playMusic);
        pauseMusic = findViewById(R.id.pauseMusic);
        seekBar = findViewById(R.id.seekBar); // change volume
        seekBar2 = findViewById(R.id.seekBar2); // seek music

        mediaController = new MediaController(MainActivity.this);// context is passed here in order for the
        //MediaController to know that we want to use this object inside main activity

        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.znmd);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);


        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);// as audio manager is a service and the return type is an object

        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC); // since the maximum volumes of devices are different
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        seekBar.setMax(maxVolume);
        seekBar.setProgress(currentVolume);



       // mediaPlayer.setOnCompletionListener(this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                seekBar2.setMax(mediaPlayer.getDuration());
                playCycle();
                mediaPlayer.start();
            }
        });

        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


                 mediaPlayer.seekTo(seekBar2.getProgress()); // not doing this will not change the position of the track,
                // you can use this feature just for the show purpose
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            mediaPlayer.pause();
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            mediaPlayer.start();
            }
        });


        playVid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri videoUri= Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video);
                videoView.setVideoURI(videoUri);
                videoView.setMediaController(mediaController);
                mediaController.setAnchorView(videoView); // sets anchor view to the required view;
                videoView.start();
            }
        });
        playMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.start();
                //  timer = new Timer(); // we use timer object to update the ui component or thread.
                // We created a new thread because the following task is heavy,
                // we don't want to over load the main UI, hence we create a new thread,
                // where the following task gets performed, not overloading the main UI

         /*       timer.scheduleAtFixedRate(new TimerTask() { // here we have a timer object that
                    // we want to schedule at a fixed rate a new timer task
                    @Override
                    public void run() {
                    seekBar2.setProgress(mediaPlayer.getCurrentPosition());
                    }
                },0, 1000);*/ // here we want the task to get updated over the period
            }
        });

        pauseMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
            }
        });

    }
    public void playCycle(){
        seekBar2.setProgress(mediaPlayer.getCurrentPosition());
        if(mediaPlayer.isPlaying()){
            runnable = new Runnable() {
                @Override
                public void run() {
                playCycle();
                }
            };
            handler.postDelayed(runnable, 1000);
        }
    }


}


   /* @Override
    public void onCompletion(MediaPlayer mp) {

        timer.cancel(); // cancels the thread.

    }*/

