package com.yyh.fragement;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yyh.R;
import com.yyh.db.User;

import org.litepal.LitePal;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class information_fragement extends Fragment implements View.OnClickListener {
    private TextView information_group;
    private TextView information_apartment;
    private TextView information_name;
    private EditText information_email;
    private TextView information_id;
    private EditText information_tel;
    private CheckBox information_ok;
    private Button update;
    private Button back;
    private String id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.information,container,false);
        return view;
    }


    public void onViewCreated( View view,  Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //获取到id
        SharedPreferences preferences = getActivity().getSharedPreferences("data", MODE_PRIVATE);
        id = preferences.getString("trueID", "");
        if (id.equals("")) {
            ShowTip("得到的账号为空请退出程序");
        } else {
            //初始化组件
            ShowTip(id);
            initview();
        }
    }
    public void initview(){
        information_group=(TextView)getView().findViewById(R.id.information_group) ;
        information_apartment=(TextView)getView().findViewById(R.id.information_apartment) ;
        information_name=(TextView)getView().findViewById(R.id.information_name) ;
        information_email=(EditText) getView().findViewById(R.id.information_email) ;
        information_id=(TextView)getView().findViewById(R.id.information_id) ;
        information_tel=(EditText)getView(). findViewById(R.id.information_tel) ;
        information_ok=(CheckBox)getView().findViewById(R.id.information_ok);
        update=(Button) getView().findViewById(R.id.update);
        back=(Button)getView().findViewById(R.id.back);
        update.setOnClickListener(this);
        back.setOnClickListener(this);
        //初始化信息
        initinformation();
    }
    //数据库初始化数据
    public void initinformation(){
        List<User> list=LitePal.where("User_name=?",id).find(User.class);
            for(User user:list) {
                information_group.setText(user.getUser_organ());
                information_apartment.setText(user.getUser_apartment());
                information_name.setText(user.getUser_name1());
                information_id.setText(user.getUser_name());
                information_ok.setChecked(user.getUser_isadmin());
                information_tel.setText(user.getUser_phone());
                information_email.setText(user.getUser_email());
            }

    }
    //更新操作
    public void update(){
        String email=information_email.getText().toString();
        String tel=information_tel.getText().toString();
        if(email.equals("")||tel.equals("")){
            ShowTip("不允许为空");
        }
        else{
            AlertDialog.Builder makesure=new AlertDialog.Builder(getActivity());
            makesure.setTitle("提示");
            makesure.setMessage("是否进行更新");
            makesure.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    User user=new User();
                    user.setUser_phone(tel);
                    user.setUser_email(email);
                    user.updateAll("User_name=?",id);
                    ShowTip("更新成功");
                }
            });
            makesure.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ShowTip("取消操作");
                }
            });
            makesure.show();


        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.update:
                update();
                break;
            case R.id.back:
                left_menu_fragement left_menu_fragement=new left_menu_fragement();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.left_menu_frame, left_menu_fragement).commit();
                break;
        }
    }
    public void ShowTip(String str){
        Toast.makeText(getContext(),str,Toast.LENGTH_SHORT).show();
    }
}
