package com.jf.studentjfmusic.activity.base;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.LayoutRes;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.jf.studentjfmusic.Constant;
import com.jf.studentjfmusic.R;
import com.jf.studentjfmusic.service.MusicService;

/**
 * 在这个类里面添加底部播放条，应该限制子Activity的布局大小
 * 该类里面处理：
 * 底部播放条所有的逻辑
 * 1. 播放按钮的状态
 *
 *
 * Created by weidong on 2017/6/16.
 */

public abstract class BaseBottomNavActivity extends AppCompatActivity {
    public static String TAG = "BaseBottomNavActivity";


    //方式一
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_base_bottom_nav);
//        FrameLayout root = (FrameLayout) findViewById(R.id.root);
//
//        //获取子类的布局文件
//        int resid = layoutResId();
//
//        //加载子类布局
//        View childView = LayoutInflater.from(this).inflate(resid,root,false);
//
//        //将子类对应的View添加到root view中
//        root.addView(childView);
//
//    }

//    /**
//     * 返回想要显示的布局
//     * @return 布局id
//     */
//    public abstract int layoutResId();


    //方式二

    protected MusicService.MusicBinder mMusicBinder;

    //播放按钮
    protected ImageView iv_playstatu;

    //监听播放状态
    private PlayBroadcastReceiver mPlayBroadcastReceiver;


    @Override
    public final void setContentView(@LayoutRes int layoutResID) {
        //加载父类的布局
        View view = LayoutInflater.from(this).inflate(R.layout.activity_base_bottom_nav,null);

        initBottomView(view);

        FrameLayout root = (FrameLayout) view.findViewById(R.id.root);
        //加载子类布局
        View childView = LayoutInflater.from(this).inflate(layoutResID,root,false);
        root.addView(childView);

        setContentView(view);
        //获取类型的名字
        TAG = getClass().getName();

        bindMusicService();

        registerBroadcast();
    }


    public void bindMusicService() {
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, mConnection, BIND_AUTO_CREATE);
    }

    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMusicBinder = (MusicService.MusicBinder) service;
            Log.e(TAG, "onServiceConnected: " + "MainActivity 服务连接啦");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    /**
     * 初始化底部按钮的点击事件
     * @param bottomView
     */
    private void initBottomView(View bottomView){
        iv_playstatu = (ImageView) bottomView.findViewById(R.id.iv_playstatu);
        iv_playstatu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMusicBinder!=null) {
                    if (mMusicBinder.isPlaying()) {
                        mMusicBinder.pause();
                        iv_playstatu.setImageResource(R.mipmap.a2s);
                    } else {
                        mMusicBinder.play();
                        iv_playstatu.setImageResource(R.mipmap.play_rdi_btn_pause);
                    }
                }
            }
        });
    }


    private void registerBroadcast() {
        mPlayBroadcastReceiver = new PlayBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.Action.PLAY);
        LocalBroadcastManager.getInstance(this).registerReceiver(mPlayBroadcastReceiver,intentFilter);
    }

    class PlayBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            iv_playstatu.setImageResource(R.mipmap.play_rdi_btn_pause);
        }
    }
}
