package com.yyh.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.yyh.R;
import com.yyh.db.Apartment;
import com.yyh.db.InitDB;
import com.yyh.db.Major;
import com.yyh.db.User;
import com.yyh.utils.BaseActivity;

import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class signup extends Activity {
    private Button sign;
    private EditText tel;
    private EditText pwd;
    private EditText name;
    private  EditText email;
    private EditText name1;
    private CheckBox ok;
    private EditText msg;
    private Button getMsg;
    private Spinner groupid;
    private Spinner group;
    private Spinner apartment;
    private TextView checkmsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.signup);
        init();

    }

    public void init(){
        Log.d("test","进入初始化");
        tel = findViewById(R.id.tel);
        pwd = findViewById(R.id.pwd);
        name = findViewById(R.id.name);
        name1=findViewById(R.id.name1);
        email=findViewById(R.id.email);
        ok = findViewById(R.id.ok);
        msg = findViewById(R.id.msg);
        getMsg = findViewById(R.id.getMsg);
        sign=findViewById(R.id.register);
        checkmsg=findViewById(R.id.checkmsg);
        group=findViewById(R.id.group);
        apartment=findViewById(R.id.apartment);
        groupid=findViewById(R.id.groupid);

        //初始化下拉列表
        //首先初始化部门
        ArrayList<String> list= new ArrayList<String>();
        list.add("1134901");

        //为下拉列表定义一个适配器
        ArrayAdapter<String> ad = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        //设置下拉菜单样式。
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        groupid.setAdapter(ad);
        //为groupid 添加消息响应
        groupid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               initspinner();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //注册两个点击事件
        sign.setOnClickListener(view -> register());
        getMsg.setOnClickListener(view -> getMsg());
    }
    //获得一个四位的随机数作为验证码
 public  void  getMsg(){
     int a = (int)(Math.random()*(9999-1000+1))+1000;//产生1000-9999的随机数
     String number=a+"";
    checkmsg.setText(number);
 }

 //注册事件
 public void register(){
     //用户信息
     String telitem=tel.getText().toString();
     String pwditem=pwd.getText().toString();
     String nameitem=name.getText().toString();
     String emailitem=email.getText().toString();
     String groupiditem=groupid.getSelectedItem().toString();
     String groupitem=group.getSelectedItem().toString();
     String apartmentitem=apartment.getSelectedItem().toString();
     String name1item=name1.getText().toString();
     boolean okitem=ok.isChecked();
     //验证码
     String msitem=msg.getText().toString();
     //首先先判断是否有为空的
     if(telitem.equals("")||pwditem.equals("")||nameitem.equals("")||emailitem.equals("")||msitem.equals("")||name1item.equals("")){
         showTip("不允许有空，请认真填完整");

     }
     else if(!msitem.equals(checkmsg.getText().toString())){
         showTip("验证码错误");
     }

     else{
         //查看主键是否已经存在
         List<User> checkUsers = LitePal.where("User_name = ?", nameitem).find(User.class);
         if(!checkUsers.isEmpty()){
             showTip("ID已存在");
         }
         else {
             Log.d("测试","进入创建");
             AlertDialog.Builder builder = new AlertDialog.Builder(this);
             builder.setTitle("提示");
             builder.setMessage("是否进行注册");
             builder.setPositiveButton("注册", new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialog, int which) {
                     //新建对象
                     User user = new User();
                     user.setUser_name(nameitem);
                     user.setUser_name1(name1item);
                     user.setUser_phone(telitem);
                     user.setUser_email(emailitem);
                     user.setUser_apart_code(groupiditem);
                     user.setUser_apartment(apartmentitem);
                     user.setUser_organ(groupitem);
                     user.setUser_isadmin(okitem);
                     user.setUser_pwd(pwditem);
                     //保存
                     user.save();
                     showTip("保存成功");
                     //切换回登录界面
                     Intent intent=new Intent(com.yyh.activity.signup.this,login.class);
                     startActivity(intent);

                 }
             });
             builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialog, int which) {
                     showTip("取消");
                 }
             });
             builder.show();
         }
     }
 }

 private  void initspinner(){
     String ID=groupid.getSelectedItem().toString();
     //然后进行数据库的查询
     List<Apartment> groupname=LitePal.where("Apartment_code=?",ID).find(Apartment.class);
     ArrayList<String> list1= new ArrayList<String>();
     for(Apartment a:groupname){
         list1.add(a.getApartment_from_organ());
     }

     //组织名称然后就是初始化下拉菜单
     ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list1);
     adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
     group.setAdapter(adapter);


     ArrayList<String> list= new ArrayList<String>();
     for(Apartment a:groupname){
         list.add(a.getApartment_name());
     }
     //然后就是初始化下拉菜单
     ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
     adapter1.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
     apartment.setAdapter(adapter1);

 }



 private void showTip(String str){
        Toast.makeText(this,str,Toast.LENGTH_LONG).show();
 }

}
