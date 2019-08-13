package com.yyh.fragement;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.yyh.activity.Main2Activity;
import com.yyh.R;
import com.yyh.db.Major;
import com.yyh.db.RecordSet;
import com.yyh.db.RecordType;
import com.yyh.db.User;

import org.litepal.LitePal;

import static android.content.Context.MODE_PRIVATE;

public class Setting_fragement extends Fragment {
    private  Button data;
    private  Button clear;
    private  Button collect;
    private  EditText test_name;//笔录名称
    private TextView data_set;//日期


    private Spinner spinner_type;
    private Spinner spinner_group;
    private Spinner spinner_menber;
    private Spinner spinner_charge;
    private Spinner spinner_apartment;
    private  int mYear, mMonth, mDay;

    private User user;


    @SuppressLint("WrongViewCast")
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

         View view=inflater.inflate(R.layout.fragement_setting,container,false);

        return view;
    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated( View view,  Bundle savedInstanceState) {
        //初始化组件
        init();

        //笔录按钮消息响应
        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //创建一个文件
              String date=data_set.getText().toString();
              String name=test_name.getText().toString();
              String apartmentname= spinner_apartment.getSelectedItem().toString();
              String type=spinner_type.getSelectedItem().toString();
              String group=spinner_group.getSelectedItem().toString();
              String number=spinner_menber.getSelectedItem().toString();
              String chager=spinner_charge.getSelectedItem().toString();



              if(TextUtils.isEmpty(test_name.getText())){

            ShowTip("编辑框不能为空");


              }else{


                  String filename=data+"-"+name;

                 if(isFileExists(name)){
                      //把数据存储到数据库

                     SharedPreferences preferences=getActivity().getSharedPreferences("data",MODE_PRIVATE);
                     Log.d("信息",preferences.getString("name",""));
                     Log.d("信息",preferences.getString("date",""));


                     if(preferences.getString("name","")!=""||preferences.getString("data","")!=""){
                         //设置不可切换
                         Main2Activity.isable=true;
                     }


                    //设置sharedPreference
                     SharedPreferences.Editor editor=getActivity().getSharedPreferences("data",MODE_PRIVATE).edit();
                     editor.putString("name",name);
                     editor.putString("date",date);
                     editor.apply();

                     //数据库保存操作
                     sqlsave();

                      //然后就是进行fragement之间的传值 还有自动切换
                    collecting_fragement cf=new collecting_fragement();
                     FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
//                     Bundle bundle=new Bundle();
//                     bundle.putString("name",name);
//                     bundle.putString("data",data);
//                     cf.setArguments(bundle);//将bundle绑定到cf面板中

                     fragmentTransaction.replace(R.id.right_layout,cf);
                     fragmentTransaction.commit();

                  }else{
                     System.out.print("文件已存在\n");
                      ShowTip("此文件已存在");
                  }
              }



            }
        });


        //清空按钮的消息响应
        clear.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                data_set.setText("");
                test_name.setText("");

            }
        });



        //日期的消息响应
        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dialog = new
                        DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int
                            dayOfMonth) {
                        mYear=year;
                        if(monthOfYear<=9){
                            mMonth=monthOfYear+1;
                        }else{
                            mMonth=monthOfYear+1;
                        }
                        if(dayOfMonth<=9){
                            mDay= dayOfMonth;
                        }else{
                            mDay=dayOfMonth;
                        }
                        data_set.setText(mYear+"-"+mMonth+"-"+mDay);


                    }
                },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                dialog.setCancelable(true);
                dialog.show();
            }
        });





    }
    //新建一个文件夹
    private boolean isFileExists(String name){
        try {
            //存储的地址是
            File f=new File(Environment.getExternalStorageDirectory() +"/itv/"+name);

         // Log.d("文件测试",Environment.getExternalStorageDirectory() +"/itv/"+name);

            if(f.exists()){
               return  false;
            }
            else if(!f.exists()){
                //创建文件夹
                //f.mkdirs();
             //   Log.d("文件测试","建立文件夹");
                return true;
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
    //组件初始化
    public void init(){
        //编辑页面
        data_set=(TextView)getView().findViewById(R.id.date_set);
        test_name=(EditText)getView().findViewById(R.id.itv_testname);
        //初始化日期为当前日期
        Date d=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        data_set.setText(simpleDateFormat.format(d));

        //按钮
        clear=(Button)getView().findViewById(R.id.clear);
        collect=(Button)getView().findViewById(R.id.collect);
        data=(Button)getView().findViewById(R.id.data);

        //下拉列表初始化
        inituser();//首先要获得登录用户
        initapartment();
        initcharge();
        initgroup();
        initmenber();
        inittype();
    }

    //初始化类型列表
    public void inittype(){
    List<RecordType>recordTypes=LitePal.findAll(RecordType.class);

        //笔录类型
        ArrayList<String> list = new ArrayList<String>();
       for(RecordType r:recordTypes){
           list.add(r.getRecord_type_name());
       }
        //为下拉列表定义一个适配器
        ArrayAdapter<String> ad = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);
        //设置下拉菜单样式。
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_type=(Spinner)getView().findViewById(R.id.spinner_type);
        spinner_type.setAdapter(ad);
    }
    //初始化组织列表
    public void initgroup(){
        List<String>list1=new ArrayList<>();
        list1.add(user.getUser_organ());
        //为下拉列表定义一个适配器
        ArrayAdapter<String> ad1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list1);
        //设置下拉菜单样式。
        ad1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_group=(Spinner)getView().findViewById(R.id.spinner_group);
        spinner_group.setAdapter(ad1);

    }
    //初始化部门列表
    public void initapartment(){

        List<Major> majors=LitePal.where("Major_from_apartment=?",user.getUser_apartment()).find(Major.class);

        //笔录组织机构
        ArrayList<String> list1 = new ArrayList<String>();
        for (Major m:majors){
            list1.add(m.getMajor_name());
        }
        //为下拉列表定义一个适配器
        ArrayAdapter<String> ad1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list1);
        //设置下拉菜单样式。
        ad1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_apartment=(Spinner)getView().findViewById(R.id.spinner_apartment);
        spinner_apartment.setAdapter(ad1);

    }
    //初始化参与人员列表
    public void initmenber(){
//参与人员
        ArrayList<String> list2 = new ArrayList<String>();
        list2.add("何国辉、高潮、欧阳伟军");
        list2.add("程先生、刘先生、杨先生");
        //为下拉列表定义一个适配器
        ArrayAdapter<String> ad2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list2);
        //设置下拉菜单样式。
        ad2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_menber=(Spinner)getView().findViewById(R.id.spinner_menber);
        spinner_menber.setAdapter(ad2);
    }
    //初始化负责人员列表
    public void initcharge(){
        //参与人员
        ArrayList<String> list3 = new ArrayList<String>();
        list3.add(user.getUser_name());
        //为下拉列表定义一个适配器
        ArrayAdapter<String> ad3 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list3);
        //设置下拉菜单样式。
        ad3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_charge=(Spinner)getView().findViewById(R.id.spinner_charge);
        spinner_charge.setAdapter(ad3);
    }
    //初始化用户
    public void inituser(){
        //首先获得登录进来的账号
        SharedPreferences preferences=getActivity().getSharedPreferences("data",MODE_PRIVATE);
        String ID=preferences.getString("trueID","");
        //然后就是根据账号获得本账号的基本信息
        List<User> list=LitePal.where("User_name=?",ID).find(User.class);
        if(list.isEmpty()){
            ShowTip("查无此人请重新登录");
        }
        else {
            user = list.get(0);
        }

        Log.d("测试",user.getUser_apartment());


    }

    //数据存储
    public void sqlsave(){
        //创建一个文件
        String date=data_set.getText().toString();
        String name=test_name.getText().toString();
        String apartmentname= spinner_apartment.getSelectedItem().toString();
        String type=spinner_type.getSelectedItem().toString();
        String group=spinner_group.getSelectedItem().toString();
        String menber=spinner_menber.getSelectedItem().toString();
        String chager=spinner_charge.getSelectedItem().toString();

        RecordSet recordSet=new RecordSet();
        recordSet.setRecord_t_name(type);
        recordSet.setRecord_name(name);
        recordSet.setRecord_id(date+name);
        recordSet.setRecord_date(date);
       // recordSet.setRecord_organ("五邑大学");
        recordSet.setRecord_apart(group);
        recordSet.setRecord_major(apartmentname);
        recordSet.setRecord_attend_member(menber);
        recordSet.setRecord_chair(chager);
        //保存
        recordSet.save();

    }

    public void ShowTip(String str){
        Toast.makeText(getContext(),str,Toast.LENGTH_LONG).show();
    }

}
