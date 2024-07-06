package com.homework.mynoval;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.homework.mynoval.Bean.Book;
import com.homework.mynoval.DataBaseManager.DataBase;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Detail extends AppCompatActivity implements AdapterView.OnItemClickListener {
//    TextView chapter;
    ListView chapterList;
    List<String> chapterLink = new ArrayList<>();
    List<String> chapters = new ArrayList<>();
    ProgressDialog loading;
    MyApplication app;
    ImageView cover, back;
    TextView info;
    Button showBtn, closeBtn, readBtn, addBtn;
    RelativeLayout list;

    String title; // 小说名
    String coverLink; // 小说封面
    MyApplication application;
    DataBase sql;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        sql = new DataBase(Detail.this);
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        title = intent.getStringExtra("title");
        application = MyApplication.getInstance();
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        chapterList = findViewById(R.id.chapters);
        info = findViewById(R.id.info);
        info.setMovementMethod(ScrollingMovementMethod.getInstance());
        cover = findViewById(R.id.cover);
        showBtn = findViewById(R.id.showChapter);
        closeBtn = findViewById(R.id.close);
        list = findViewById(R.id.list);
        readBtn = findViewById(R.id.read);
        addBtn = findViewById(R.id.addList); // 加入书单按钮
        if(sql.isExist(application.getUser_id(), title)){
            addBtn.setText("拿出书架");
            addBtn.setBackgroundColor(getResources().getColor(R.color.red));
        } else {
            addBtn.setText("加入书架");
            addBtn.setBackgroundColor(getResources().getColor(R.color.blue));
        }
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TTT", "onClick: "+title+coverLink+application.getUser_id());
                Book book = new Book(application.getUser_id(), coverLink, title);
                if(!sql.isExist(book)) {
                    sql.addBook(book);
                    Toast.makeText(Detail.this, "添加成功", Toast.LENGTH_LONG).show();
                    addBtn.setText("拿出书架");
                    addBtn.setBackgroundColor(getResources().getColor(R.color.red));
                }else {
                    Log.d("TTT", "onClick: deling");
                    sql.deleteBook(book);
                    Log.d("TTT", "onClick: deled");
                    addBtn.setText("加入书架");
                    addBtn.setBackgroundColor(getResources().getColor(R.color.blue));
                    Toast.makeText(Detail.this, "删除成功", Toast.LENGTH_LONG).show();
                }
                Log.d("TTT", "onClick: 是否存在"+sql.isExist(book));
            }
        });

        chapterList.setOnItemClickListener(this);
        app = MyApplication.getInstance();
        loadChapter(url);
        showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.setVisibility(View.VISIBLE);
            }
        });
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.setVisibility(View.GONE);
            }
        });
        readBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Detail.this, Page.class);
                intent.putExtra("link", chapterLink.get(0));
                intent.putExtra("title", chapters.get(0));
                app.setChapter_id(0);
                startActivity(intent);
            }
        });
    }

    public void loadChapter(String url) {
        loading = new ProgressDialog(this);
        loading.setMessage("拼命加载中···");
        loading.setCancelable(false);
        loading.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc =  Jsoup.connect(url)
                            .userAgent("Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)")
                            .get();
                    Elements link = doc.select("#list>dl>dd>a");
                    coverLink = doc.select("#fmimg>img").attr("abs:src");
                    String intro = doc.select("#intro>p").get(1).text();
                    Elements title = doc.select("#list>dl>dd>a");
                    for (Element l:link){
                        chapterLink.add(l.attr("abs:href"));
                    }
                    for(Element s:title) {
                        chapters.add(s.text());
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ChapterAdapter adapter = new ChapterAdapter(Detail.this, R.layout.item, chapters);
                            chapterList.setAdapter(adapter);
                            Glide.with(getCurrentContext()).load(coverLink).into(cover);
                            info.setText(intro);
                            loading.dismiss();

                        }
                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }).start();
        app.setChpaters(chapterLink);
        app.setChapter_id(0);
        app.setTitles(chapters);
    }
    public Context getCurrentContext() {
        return this;
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent intent = new Intent(this, Page.class);
        intent.putExtra("link", chapterLink.get(position));
        intent.putExtra("title", chapters.get(position));
        app.setChapter_id(position);
        startActivity(intent);
    }
}