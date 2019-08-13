package com.yyh.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.yyh.activity.login;

public class BaseActivity extends AppCompatActivity {
    private  InternetRecider internetRecider;
    private IntentFilter intentFilter;
    //复写父类的Oncreate 方法
    protected  void OnCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //获得当前类名并打印出来
        ActivityCollector.addActivity(this);



    }
    protected  void OnResume(){
        super.onResume();
        //注册网络变化的接收器
        intentFilter=new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        internetRecider=new InternetRecider();
        registerReceiver(internetRecider,intentFilter);
    }

    protected void OnPause(){
        super.onPause();
        if(internetRecider!=null){
            unregisterReceiver(internetRecider);
            internetRecider=null;
        }
    }
    protected  void OnDestroy(){
        super.onDestroy();
        ActivityCollector.removeActivity(this);

    }

    class InternetRecider extends  BroadcastReceiver{

        @Override
        public void onReceive(final Context context, Intent intent) {
            ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
            if(networkInfo!=null&&networkInfo.isAvailable()){
            Toast.makeText(context,"net is available",Toast.LENGTH_SHORT).show();
            }
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("网络连接");
                builder.setMessage("网络连接失败");
                builder.setCancelable(false);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //销毁所有活动
                        ActivityCollector.finishAll();
                        Intent i=new Intent(context,login.class);
                        context.startActivity(i);
                    }
                });
                //显示
                builder.show();
            }
        }
    }
}
