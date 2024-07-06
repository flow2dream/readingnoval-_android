package com.homework.mynoval;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.TextNode;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Page extends AppCompatActivity {
    ViewPager2 container;
    String title;
    ProgressDialog loading;
    MyPgaerAdapter myPgaerAdapter, temp;
    MyApplication app;
    boolean flag=true, isPre=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        loading = new ProgressDialog(this);
        loading.setMessage("正在获取内容中···");
        loading.setCancelable(false);
        container = findViewById(R.id.container);
        getContent(intent.getStringExtra("link"));
        title = intent.getStringExtra("title");
        app = MyApplication.getInstance();
        container.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                flag = false;
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                if(myPgaerAdapter.getItemCount() == position+1) {
                    app.addID();
                    title = app.getTitles().get(app.getChapter_id());
                    getContent(app.getChpaters().get(app.getChapter_id()));
                }
                if (position == 0 &&!flag) {
                    isPre = true;
                    app.desID();
                    title = app.getTitles().get(app.getChapter_id());
                    getContent(app.getChpaters().get(app.getChapter_id()));
                    Log.d("TTT", "onPageSelected: "+myPgaerAdapter.getItemCount());
//                    container.setCurrentItem(myPgaerAdapter.getItemCount()-3);
                }
                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }

    private void initViewPager(ViewPager2 viewPager2, String content) {
        List<PagerFragment> list = new ArrayList<>();
        List<String> split_content = stringToStringArray(content, 300);
        list.add(new PagerFragment("",0, 0)); // 用于加载上一章
        for(int i=0; i<split_content.size();i++){
            if(i==0) {
                list.add(new PagerFragment(split_content.get(i), title, i, split_content.size()));
            }
            else {
                list.add(new PagerFragment(split_content.get(i), i, split_content.size()));
            }
        }
        list.add(new PagerFragment("",0, 0)); // 用于加载下一章
        myPgaerAdapter = new MyPgaerAdapter(getSupportFragmentManager(), getLifecycle());
        myPgaerAdapter.setFragmentList(list);

        container.setAdapter(myPgaerAdapter);
        if (isPre){
            container.setCurrentItem(myPgaerAdapter.getItemCount()-2);
            isPre=false;
        }else {
            container.setCurrentItem(1);
        }

        loading.dismiss();
    }

    public List<String> stringToStringArray(String src, int length) {
        //检查参数是否合法
        if (null == src || src.equals("")) {
            return null;
        }

        if (length <= 0) {
            return null;
        }
        List<String> str = new ArrayList<>();
        String[] split = src.split("\n\n");
        String item = "";
        for (String s:split){
            if(item.length()+s.length()>length){
                str.add(item);
                item = s;
            }else {
                item += s+"\n\n";
            }
        }
        return str;
    }

    public void getContent(String url) {
        loading.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect(url).get();
                    List<TextNode> doc = document.select("#content").textNodes();
                    String content = doc.toString();
                    content = content.replaceFirst("&nbsp;&nbsp;&nbsp;&nbsp;", "");
                    content = content.replace("&nbsp;&nbsp;&nbsp;&nbsp;", "\n\n")
                            .replace("[", "").replace("]", "");
                    String finalContent = content;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initViewPager(container, finalContent);
                        }
                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
}