package com.yyh.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.yyh.R;
import com.yyh.db.Apartment;
import com.yyh.db.InitDB;
import com.yyh.db.User;
import com.yyh.utils.BaseActivity;

import org.litepal.LitePal;

import java.util.List;


public class login extends BaseActivity {
private  EditText  id_Edit;
private  EditText password_Edit;
private CheckBox  remenber;
private SharedPreferences pref;
private  SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化数据库
        InitDB.createInitDB(this);
        setContentView(R.layout.activity_test);



        //得到 pref
        pref=PreferenceManager.getDefaultSharedPreferences(this);
        //找到对应的编辑框
       id_Edit=(EditText) findViewById(R.id.ID);
       password_Edit=(EditText) findViewById(R.id.password);
        //找到对应的checkbox按钮
        remenber=(CheckBox)findViewById(R.id.remenber);
        //首先我们先进行是否记住密码的初始化
        boolean isRemenber=pref.getBoolean("remenber_password",false);
        if(isRemenber){
        String account=pref.getString("account","");
        String password=pref.getString("password","");

        id_Edit.setText(account);
        password_Edit.setText(password);
        remenber.setChecked(isRemenber);
        }
        //登录界面
        final Button login =(Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ID;
                String password;
                boolean istrue = true;
                ID = id_Edit.getText().toString();
                password = password_Edit.getText().toString();
                //判断是否为空
                if (ID.equals("") || password.equals("")) {
                    showTip("输入不允许为空");
                }
                else {
                    //判断是否记住密码
                    remenberpsw(ID,password);
                    //进行数据库判断
                    //首先判断账号是否存在
                    List<User> list=LitePal.where("User_name=?",ID).find(User.class);
                    if(list.isEmpty()){
                        istrue=false;
                        showTip("不存在此账号，请进行注册");
                    }
                    else{
                    //判断密码是否相符了
                    //有且仅有一个
                    User user=list.get(0);
                    if(password.equals(user.getUser_pwd())){
                     istrue=true;
                    }
                    else{
                       istrue=false;
                     showTip("密码错误");
                    }
                    }



                    //如果密码正确
                    if (istrue) {
                        Intent intent = new Intent(com.yyh.activity.login.this, Main2Activity.class);
                        //把登录成功的值传入pref中
                        SharedPreferences.Editor editor=getSharedPreferences("data",MODE_PRIVATE).edit();
                        editor.putString("trueID",ID);
                        editor.apply();
                        //转移
                        startActivity(intent);
                    }


                }
            }
        });
        //注册
        Button sign =(Button) findViewById(R.id.sign);
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //跳转到注册界面
            Intent intent=new Intent(com.yyh.activity.login.this,signup.class);
            startActivity(intent);

            }
        });
        //找回密码
        Button forget =(Button) findViewById(R.id.login_error);
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //看密码
        Button check=(Button) findViewById(R.id.check);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            password_Edit.setInputType(InputType.TYPE_CLASS_TEXT);

            }
        });



    }
    private void remenberpsw(String ID,String password){

        editor = pref.edit();
        //判断是否要进行记住账号密码
        if (remenber.isChecked()) {
            editor.putBoolean("remenber_password", true);
            editor.putString("account", ID);
            editor.putString("password", password);
        } else {
            editor.clear();
        }
        //执行输入
        editor.apply();

    }
    private void showTip(String str){
        Toast.makeText(this,str,Toast.LENGTH_LONG).show();
    }
}
