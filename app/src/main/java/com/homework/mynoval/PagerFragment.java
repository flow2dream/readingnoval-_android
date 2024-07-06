package com.homework.mynoval;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PagerFragment extends Fragment {

    String content;
    String title = "";
    int id=0;
    int total_page=0;
    public PagerFragment() {

    }
    public PagerFragment(String content, int id, int total_page) {
        this.content = content;
        this.id=id;
        this.total_page = total_page;
    }
    public PagerFragment(String content, String title, int id, int total_page) {
        this.content = content;
        this.title = title;
        this.id = id;
        this.total_page = total_page;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pager, container);
        TextView tv_content = view.findViewById(R.id.page);
        TextView tv_title = view.findViewById(R.id.title);
        TextView page_id = view.findViewById(R.id.page_num);
        if (!title.equals("")){
            tv_title.setText(this.title);
        } else {
            tv_title.setVisibility(View.GONE);
        }
        tv_content.setText(this.content);
        String pager = String.format("%d/%d", this.id+1, this.total_page);
        page_id.setText(pager);
        return view;
    }
}