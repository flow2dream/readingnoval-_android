package com.homework.mynoval.tabbar.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.homework.mynoval.Detail;
import com.homework.mynoval.R;
import com.homework.mynoval.tabbar.BookItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Pattern extends Fragment {
    ListView showbook;
    int type;
    List<BookItem> books = new ArrayList<>();
    List<String> urls = new ArrayList<>(), titles = new ArrayList<>();
    ProgressDialog loading;
    public Pattern() {
    }
    public Pattern(int type) {
        this.type = type;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loading = new ProgressDialog(getContext());
        loading.setCancelable(false);
        loading.setMessage("正在努力加载中···");
        loading.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect("http://www.xbiqugu.net/paihangbang/").get();
                    Element node = null;
                    if(type == 1) {
                        node = document.selectFirst("div[class=box b1]").selectFirst("ul");
                    } else if (type == 2) {
                        node = document.selectFirst("div[class=box b2]").selectFirst("ul");
                    } else if (type == 3) {
                        node = document.selectFirst("div[class=box b3]").selectFirst("ul");
                    } else if (type == 4) {
                        node = document.selectFirst("div[class=box b4]").select("ul").get(0);
                    } else if (type == 5) {
                        node = document.selectFirst("div[class=box b1]").select("ul").get(1);
                    } else if (type == 6) {
                        node = document.selectFirst("div[class=box b2]").select("ul").get(1);
                    }else if (type == 0) {
                        node = document.selectFirst("div[class=box b3]").select("ul").get(1);
                    }
                    if(node != null) {
                        Elements el = node.select("li");
                        BookItem book;
                        for(int i=0; i<el.size()-1; i++){
                            Element l = el.get(i);
                            if (l.className().equals("ltitle")){
                                continue;
                            }
                            String title = l.select("a").text();
                            String url = l.select("a").attr("abs:href");
                            String imgUrl = getCover(url);
                            book = new BookItem(title, imgUrl);
                            books.add(book);
                            urls.add(url);
                            titles.add(title);
                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                BookListAdapter adapter = new BookListAdapter(getContext(), books);
                                showbook.setAdapter(adapter);
                                loading.dismiss();
                                showbook.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        String url = urls.get(i);
                                        Intent intent = new Intent(getContext(), Detail.class);
                                        intent.putExtra("url", url);
                                        intent.putExtra("title", titles.get(i));
                                        startActivity(intent);
                                    }
                                });
                            }
                        });
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }).start();
    }
    public String getCover(String url) {
        String last = url.replace("http://www.xbiqugu.net/", "");
        String s = last.split("/")[1];
        //  String imageUrl = "http://www.xbiqugu.net/files/article/image/15/15273/15273s.jpg";
        return "http://www.xbiqugu.net/files/article/image/"+last+s+"s.jpg";
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pattern, container, false);
        showbook = view.findViewById(R.id.showbook);
        return view;
    }
}