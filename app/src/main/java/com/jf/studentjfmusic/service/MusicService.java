package com.jf.studentjfmusic.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.jf.studentjfmusic.Constant;

import java.io.IOException;

/**
 * Created by weidong on 2017/6/12.
 */

public class MusicService extends Service {
    private static final String TAG = "MusicService";

    private static MediaPlayer mMediaPlayer = new MediaPlayer();


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MusicBinder();
    }

    public class MusicBinder extends Binder {

        public void play(){
            mMediaPlayer.start();
            Intent intent = new Intent(Constant.Action.PLAY);
            LocalBroadcastManager.getInstance(MusicService.this).sendBroadcast(intent);
        }

        /**
         * 播放
         */
        public void play(String uri) {
            Log.e(TAG, "play: 开始播放啦");

//            if (mMediaPlayer == null) {
//                mMediaPlayer = new MediaPlayer();
//                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                mMediaPlayer.setLooping(false);
//            }
            mMediaPlayer.reset();
            try {
                mMediaPlayer.setDataSource(uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                mMediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mMediaPlayer.start();

            Intent intent = new Intent(Constant.Action.PLAY);
            LocalBroadcastManager.getInstance(MusicService.this).sendBroadcast(intent);



        }

        /**
         * 暂停
         */
        public void pause() {
            mMediaPlayer.pause();
        }

        /**
         * 获取播放状态
         * @return
         */
        public boolean isPlaying() {
            if (mMediaPlayer != null) {
                return mMediaPlayer.isPlaying();
            } else {
                return false;
            }
        }
    }

}
