package com.homework.mynoval.tabbar;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.homework.mynoval.Bean.Book;
import com.homework.mynoval.DataBaseManager.DataBase;
import com.homework.mynoval.Detail;
import com.homework.mynoval.MyApplication;
import com.homework.mynoval.R;
import com.homework.mynoval.tabbar.fragments.BookListAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyBookList extends Fragment {

    MyApplication app;
    List<Book> mybooks;
    DataBase database;
    ListView showBookList;
    List<BookItem> bookItems = new ArrayList<>();
    public MyBookList() {
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
        View view = inflater.inflate(R.layout.fragment_list_book, container, false);
        database = new DataBase(getContext());
        showBookList = view.findViewById(R.id.showBookList);
        initView();
        return view;
    }
    public void initView() {

        mybooks = database.getAll(app.getUser_id());
        for(Book book:mybooks) {
            bookItems.add(new BookItem(book.getTitle(), book.getImg()));
        }
        BookListAdapter adapter = new BookListAdapter(getContext(), bookItems);
        showBookList.setAdapter(adapter);
        showBookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), Detail.class);
                String imageUrl = mybooks.get(i).getImg();
                String pageUrl = getHtml(imageUrl);
                intent.putExtra("url", pageUrl);
                intent.putExtra("title", mybooks.get(i).getTitle());
                startActivity(intent);
            }
        });
    }
    public String getHtml(String imageUrl) {
        String id = imageUrl.split("/\\d+s.jpg")[0];
        return id.replace("http://www.xbiqugu.net/files/article/image/","http://www.xbiqugu.net/");
    }

    @Override
    public void onResume() {
        super.onResume();
        bookItems.clear();
        initView();
    }
}