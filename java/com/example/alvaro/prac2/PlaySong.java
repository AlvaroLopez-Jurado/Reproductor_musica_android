package com.example.alvaro.prac2;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class PlaySong extends AppCompatActivity implements View.OnClickListener{

    private ImageView mPlayPauseIv;
    private TextView mTitleTv;
    private SeekBar mSeekBar;

    private Timer mTimer;
    private TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            if (mSeekBar != null) mSeekBar.setProgress(MediaPlayerManager.getCurrentTime());
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.9), (int)(height*.25));

        mTitleTv = findViewById(R.id.track_name_tv);

        Bundle b = getIntent().getExtras();
        String nombreCancion = b.getString("NOMBRE");
        if(b!=null){
            mTitleTv.setText(nombreCancion);
        }


        mSeekBar = findViewById(R.id.seek_bar);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                MediaPlayerManager.seekTo(seekBar.getProgress());
            }
        });

        mPlayPauseIv = findViewById(R.id.play_pause_iv);
        mPlayPauseIv.setOnClickListener(this);
        /*conseguir la direccion*/
        try {
            MediaPlayerManager.stopMusic();

            GameDataHelper game = new GameDataHelper(getApplicationContext());
            SQLiteDatabase db = game.getWritableDatabase();

            Cursor cursor = db.rawQuery("SELECT " + UtilidadesSQL.CAMPO_ALBUM + " FROM " + UtilidadesSQL.TABLA_NOMBRE + " WHERE " + UtilidadesSQL.CAMPO_NOMBRE + "='" + nombreCancion + "'", null);
            cursor.moveToNext();
            String s = cursor.getString(0);
            Uri uri = Uri.parse(s);
            cursor.close();
            db.close();

            //start the new track

            MediaPlayerManager.startNewTrack(this, uri);


            //set the max value of the seek bar
            mSeekBar.setMax(MediaPlayerManager.getTotalDuration());
            mSeekBar.setProgress(0);
            mTimer = new Timer();
            mTimer.scheduleAtFixedRate(mTimerTask, 0, 1000);

            //change the button icon
            setPlayPauseImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
       if (MediaPlayerManager.isPlaying()) {    //If currently playing?? Pause.
            MediaPlayerManager.pauseMusic();

            //stop the seek bar
            if (mTimer != null) mTimer.cancel();

            setPlayPauseImage();
        } else {        //Currently in pause state. Play from where it was pause.
            MediaPlayerManager.startPlaying();
            setPlayPauseImage();

            //stop the seek bar
            mSeekBar.setMax(MediaPlayerManager.getTotalDuration());
            mSeekBar.setProgress(MediaPlayerManager.getCurrentTime());
            mTimer = new Timer();

            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    if (mSeekBar != null) mSeekBar.setProgress(MediaPlayerManager.getCurrentTime());
                }
            };

            mTimer.scheduleAtFixedRate(mTimerTask, 0, 1000);
        }
    }

    private void setPlayPauseImage() {
        mPlayPauseIv.setImageResource(MediaPlayerManager.isPlaying() ? R.drawable.baseline_pause_black_48 : R.drawable.baseline_play_arrow_black_48);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaPlayerManager.stopMusic();
    }
}
