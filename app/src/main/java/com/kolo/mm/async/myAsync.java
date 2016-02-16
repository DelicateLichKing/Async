package com.kolo.mm.async;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by Administrator on 2016/2/15.
 */
public class myAsync extends AsyncTask<Void, Void, Void> {

    @Override
    //必须要重写的方法
    protected Void doInBackground(Void... params) {
        Log.e("sync","doInBackground");
        publishProgress();//手动调用onProgressUpdate方法，传入进度值
        return null;
    }

    @Override
    //预处理
    protected void onPreExecute() {
        Log.e("sync","onPreExecute");
        super.onPreExecute();
    }

    @Override
    //输出异步处理后结果
    protected void onPostExecute(Void aVoid) {
        Log.e("sync","onPostExecute");
        super.onPostExecute(aVoid);
    }

    @Override
    //获取进度，更新进度条
    protected void onProgressUpdate(Void... values) {
        Log.e("sync","onProgressUpdate");
        super.onProgressUpdate(values);
    }
}
