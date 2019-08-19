package com.yyh.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import  com.yyh.R;

import com.yyh.utils.item;

import java.util.List;

public class itemadapter  extends ArrayAdapter<item> {
    private  int resourceid;
    public itemadapter(Context context, int resource, List<item> objects) {
        super(context, resource, objects);
        resourceid=resource;
    }


    @NonNull
    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {
        item item=getItem(position);//获得当前项的实例
        View view=LayoutInflater.from(getContext()).inflate(resourceid,parent,false);
        TextView textView=(TextView)view.findViewById(R.id.item_name);
        ImageView imageView=(ImageView)view.findViewById(R.id.item_image);
        textView.setText(item.getItem_name());
        imageView.setImageResource(item.getItem_image());
        return view;

    }
}
