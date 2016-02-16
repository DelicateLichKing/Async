package com.kolo.mm.async;

import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.hikvision.netsdk.HCNetSDK;

import org.MediaPlayer.PlayM4.Player;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback{

    private ImageView mPlay;
    private ProgressBar mBar;
    private SurfaceView mSurfaceView = null;
    private int m_iPort = -1; //playPort
    SurfaceHolder holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!initeSdk())
        {
            this.finish();
            return;
        }

        if (!initeActivity())
        {
            this.finish();
            return;
        }


        mPlay = (ImageView) findViewById(R.id.play);
        mBar = (ProgressBar) findViewById(R.id.load);
        mSurfaceView = (SurfaceView) findViewById(R.id.s_view);
        mPlay.setOnClickListener(My_onClick);
    }

    //GUI的初始化
    private boolean initeActivity() {
        findViews();
        mSurfaceView.getHolder().addCallback(this);
    //    setListeners();
        return true;
    }

    private void findViews() {
        mPlay = (ImageView) findViewById(R.id.play);
        mBar = (ProgressBar) findViewById(R.id.load);
        mSurfaceView = (SurfaceView) findViewById(R.id.s_view);
        mPlay.setOnClickListener(My_onClick);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        new Thread(new drawThread()).start();
        Log.i("sur", "surface is created" + m_iPort);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i("sur", "Player setVideoWindow release!" + m_iPort);
        if (-1 == m_iPort)
        {
            return;
        }
        if (holder.getSurface().isValid()) {
            if (!Player.getInstance().setVideoWindow(m_iPort, 0, null)) {
                Log.e("sur", "Player setVideoWindow failed!");
            }
        }
    }

    class myAsync1 extends AsyncTask<Void, Void, Void>{
        @Override
        //必须要重写的方法
        protected Void doInBackground(Void... params) {
            Log.e("sync", "doInBackground");
            publishProgress();//手动调用onProgressUpdate方法，传入进度值
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        //预处理
        protected void onPreExecute() {
            Log.e("sync","onPreExecute");
            mBar.setVisibility(View.VISIBLE);
            mPlay.setVisibility(View.GONE);
            super.onPreExecute();
        }

        @Override
        //输出异步处理后结果
        protected void onPostExecute(Void aVoid) {
            Log.e("sync", "onPostExecute");
            mPlay.setVisibility(View.VISIBLE);
            mBar.setVisibility(View.GONE);
            super.onPostExecute(aVoid);
        }

        @Override
        //获取进度，更新进度条
        protected void onProgressUpdate(Void... values) {
            Log.e("sync","onProgressUpdate");
            super.onProgressUpdate(values);
        }
    }

    private View.OnClickListener My_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.play:
                    myAsync1 task = new myAsync1();//实例化异步任务
                    task.execute();//执行
            }
        }
    };

     class drawThread implements Runnable {
         @Override
         public void run() {
             holder = mSurfaceView.getHolder();
             holder.setFormat(PixelFormat.TRANSLUCENT);//半透明
             if (-1 == m_iPort)
             {
                 return;
             }
             Surface surface = holder.getSurface();
             if (surface.isValid()) {
                 if (!Player.getInstance().setVideoWindow(m_iPort, 0, holder)) {
                     Log.e("sur", "Player setVideoWindow failed!");
                 }
             }

         }
     }

    private boolean initeSdk()
    {
        //init net sdk
        if (!HCNetSDK.getInstance().NET_DVR_Init())
        {
            Log.e("sur", "HCNetSDK init is failed!");
            return false;
        }
        HCNetSDK.getInstance().NET_DVR_SetLogToFile(3, "/mnt/sdcard/sdklog/",true);
        return true;
    }


}
