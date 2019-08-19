package com.yyh.fragement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;
import com.yyh.R;
import com.yyh.adapter.itemadapter;
import com.yyh.utils.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class left_menu_fragement extends Fragment  {
    private View mView;
    private ListView function;
    private List<item> mDatas =new ArrayList<>();
    private ListAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        if (mView == null)
        {
          initView(inflater,container);

        }
        return mView;
    }


    private void initView(LayoutInflater inflater, ViewGroup container)
    {
        mView = inflater.inflate(R.layout.left_menu, container, false);
        //组件初始化
        function=(ListView)mView.findViewById(R.id.item_list);
      //  imageButton=(ImageButton)getView().findViewById(R.id.left_menu_touxiang);
      //  textView=(TextView)getView().findViewById(R.id.left_menu_name);
        initlist();
        itemadapter itemadapter=new itemadapter(getContext(),R.layout.item,mDatas);
        function.setAdapter(itemadapter);
        //添加消息响应
        function.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //判断按的是哪个
                switch (position){
                    //信息
                    case 0:
                        ShowTip("个人信息");
                        information_fragement information=new information_fragement();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.left_menu_frame, information).commit();

                        break;
                    //意见反馈
                    case 1:
                        ShowTip("反馈");
                        break;
                        //关于笔录
                    case 2:
                        ShowTip("关于");
                        statement_fragement statement_fragement=new statement_fragement();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.left_menu_frame, statement_fragement).commit();
                        break;
                        //退出登录
                    case 3:
                        ShowTip("退出");
                        Intent intent=new Intent(getActivity(),com.yyh.activity.login.class);
                        startActivity(intent);
                        break;
                        default:
                            break;
                }
            }
        });
    }
    public void initlist(){
        item item=new item("个人信息",R.drawable.information);
        mDatas.add(item);
        item a=new item("意见反馈",R.drawable.feedback);
        mDatas.add(a);
        item b=new item("关于笔录",R.drawable.about);
        mDatas.add(b);
        item c=new item("退出登录",R.drawable.out);
        mDatas.add(c);
    }
    public void ShowTip(String str){
        Toast.makeText(getActivity(),str,Toast.LENGTH_SHORT).show();
    }
}
