package com.yyh.fragement;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.yyh.R;

import java.io.BufferedWriter;

public class statement_fragement extends Fragment {
    private Button back;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view =inflater.inflate(R.layout.statement, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        back=(Button)view.findViewById(R.id.statement_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                left_menu_fragement left_menu_fragement=new left_menu_fragement();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.left_menu_frame, left_menu_fragement).commit();
            }
        });
    }
}
