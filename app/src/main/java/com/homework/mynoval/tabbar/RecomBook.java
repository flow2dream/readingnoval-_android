package com.homework.mynoval.tabbar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.homework.mynoval.R;
import com.homework.mynoval.tabbar.fragments.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class RecomBook extends Fragment {
    String[] titles = {"推荐","玄幻", "修真", "都市", "历史", "网游", "科幻"};
    List<Pattern> fragments = new ArrayList<>();
    TabLayout tab;
    ViewPager2 viewPager;
    MyAdapter madapter;
    View rootView;
    List<String> names = new ArrayList<>(), urls = new ArrayList<>(), imgs = new ArrayList<>();
    List<BookItem> books = new ArrayList<>();
    public RecomBook() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_reom_book, container, false);
        tab = rootView.findViewById(R.id.tabLayout);

        for(int i=0; i<titles.length; i++) {
            fragments.add(new Pattern(i));
            tab.addTab(tab.newTab());
        }
        viewPager = rootView.findViewById(R.id.view_pager);
        madapter = new MyAdapter(getChildFragmentManager(), getLifecycle(), fragments);
        viewPager.setAdapter(madapter);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tab, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tabItem, int position) {
                tabItem.setText(titles[position]);
            }
        });
        tabLayoutMediator.attach();
        return rootView;
    }

}

class MyAdapter extends FragmentStateAdapter {

    List<Pattern> list;


    public MyAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, List<Pattern> list) {
        super(fragmentManager, lifecycle);
        this.list = list;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return list.get(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}