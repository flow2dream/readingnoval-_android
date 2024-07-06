package com.homework.mynoval.tabbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.homework.mynoval.Login;
import com.homework.mynoval.MyApplication;
import com.homework.mynoval.R;

public class Profile extends Fragment {

    MyApplication app;
    TextView user_id;
    public Profile() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = MyApplication.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        user_id = view.findViewById(R.id.userId);
        user_id.setText(app.getUser_id());
        Log.d("TTT", "onCreateView: "+app.getUser_id());
        Button toLogin = view.findViewById(R.id.toLogin);
        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences share = app.getSharedPreferences();
                SharedPreferences.Editor edit = share.edit();
                edit.putBoolean("autologin", false);
                edit.apply();
                Intent intent = new Intent(getContext(), Login.class);
                startActivity(intent);
            }
        });
        return view;

    }

}