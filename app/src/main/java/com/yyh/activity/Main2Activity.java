package com.yyh.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yyh.R;
import com.yyh.fragement.Fix_fragement;
import com.yyh.fragement.Setting_fragement;
import com.yyh.fragement.collecting_fragement;
import com.yyh.fragement.templateFragment;
import com.yyh.fragement.voice_collect_fragement;
import com.yyh.fragement.voice_verify_fragement;
import com.yyh.utils.BaseActivity;


public class Main2Activity extends BaseActivity  {
    /*private Button set;
    private Button record;
    private Button proofread;
    private Button search;
    private Button paint;
    private Button manage;
    private Button system;*/
    private RadioButton set, record, proofread, search, print, manage, setting;
    private RadioGroup bottomView;
//    private ImageButton account;
//    private TextView account_infor;
    private  FragmentManager mFm;
    private  FragmentTransaction transaction;
    private  Fragment mContent;
   public static boolean isable=false;

   private collecting_fragement collecting_fragement;
   private Setting_fragement setting_fragement;
   private Fix_fragement fix_fragement;
private  templateFragment templateFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //对面板进行初始化
        setting_fragement=new Setting_fragement();
        collecting_fragement=new collecting_fragement();
        fix_fragement=new Fix_fragement();
        templateFragment= new templateFragment();



        //首先让fragement显示的界面
         defaultfragment(setting_fragement);
        //对上部栏目的组件进行初始化
        //account=(ImageButton)findViewById(R.id.account);
      //  account_infor=(TextView)findViewById(R.id.account_infor);

        //得到传入的账号信息
        SharedPreferences preferences=getSharedPreferences("data",MODE_PRIVATE);
        String ID=preferences.getString("trueID","");
       // account_infor.setText(ID);
        //对按钮进行注册
//        set =(Button) findViewById(R.id.set);
//        set.setOnClickListener(this);
//
//        record =(Button) findViewById(R.id.record);
//        record.setOnClickListener(this);
//
//        proofread =(Button) findViewById(R.id.Proofread);
//        proofread.setOnClickListener(this);
//
//        search =(Button) findViewById(R.id.search);
//        search.setOnClickListener(this);
//
//        paint =(Button) findViewById(R.id.paint);
//        paint.setOnClickListener(this);
//
//        manage =(Button) findViewById(R.id.manage);
//        manage.setOnClickListener(this);
//
//        system =(Button) findViewById(R.id.system);
//        system.setOnClickListener(this);
        bottomView = findViewById(R.id.bottom_view);
        set = findViewById(R.id.navigation_set);
        record = findViewById(R.id.navigation_record);
        proofread = findViewById(R.id.navigation_proofread);
        search = findViewById(R.id.navigation_search);
        print = findViewById(R.id.navigation_print);
        manage = findViewById(R.id.navigation_manage);
        setting = findViewById(R.id.navigation_setting);
        bottomView.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.navigation_set:
                        switchfragment(setting_fragement);
                        break;

                    case R.id.navigation_record:
                        start_record();
                        break;

                    case R.id.navigation_proofread:
                        switchfragment(fix_fragement);
                        break;

                    case R.id.navigation_search:
                        switchfragment(setting_fragement);
                        break;

                    case R.id.navigation_print:
                        switchfragment(setting_fragement);
                        break;

                    case R.id.navigation_manage:
                        switchfragment(templateFragment);
                        break;

                    case R.id.navigation_setting:
                        replaceFragment(new voice_verify_fragement());
                        break;

                    default:
                        break;
                }
                setTabState();
            }
        });
        set.setChecked(true);

    }

//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.set:
//                switchfragment(setting_fragement);
//                break;
//
//            case R.id.record:
//                start_record();
//                break;
//
//            case R.id.Proofread:
//                switchfragment(fix_fragement);
//                break;
//
//            case R.id.search:
//                switchfragment(setting_fragement);
//              break;
//
//            case R.id.paint:
//                switchfragment(setting_fragement);
//                break;
//
//            case R.id.manage:
//                switchfragment(templateFragment);
//
//                break;
//
//            case R.id.system:
//                replaceFragment(new voice_verify_fragement());
//                break;
//
//            default:
//                break;
//
//        }
//    }

    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.replace(R.id.right_layout,fragment);
        transaction.commit();
    }
    //设置第一个面板
    private void defaultfragment(Fragment fm){
        mFm=getSupportFragmentManager();
        transaction=mFm.beginTransaction();
        transaction.add(R.id.right_layout,fm).commit();
        mContent=fm;
    }

public void switchfragment(Fragment fragment){
        if(mContent!=fragment){
             transaction=mFm.beginTransaction();
            //判断是否被加载过
            if(!fragment.isAdded()) {
                transaction.add(R.id.right_layout, fragment).commit();
            }else{
                //已经被加载过则隐藏
                transaction.hide(mContent).show(fragment).commit();
            }
            mContent=fragment;
        }
}



        public void start_record(){
        //首先判断是否能进行录屏
        if(isable){
            switchfragment(collecting_fragement);


        }else{
            Toast.makeText(this,"请先进行笔录设置",Toast.LENGTH_LONG).show();
        }
    }
    private void setState(RadioButton b){
        if(b.isChecked()){
            b.setTextColor(ContextCompat.getColor(this, R.color.colorRadioButtonD));
        }else{
            b.setTextColor(ContextCompat.getColor(this, R.color.colorRadioButtonU));
        }
    }

    private void setTabState(){
        setState(set);
        setState(record);
        setState(proofread);
        setState(search);
        setState(print);
        setState(manage);
        setState(setting);
    }

}
