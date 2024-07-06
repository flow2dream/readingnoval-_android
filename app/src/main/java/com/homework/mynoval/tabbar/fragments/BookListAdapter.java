package com.homework.mynoval.tabbar.fragments;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.homework.mynoval.R;
import com.homework.mynoval.tabbar.BookItem;

import java.util.List;

public class BookListAdapter extends BaseAdapter {
    List<BookItem> bookItems;
    Context context;

    public BookListAdapter(Context context, List<BookItem> bookItems) {
        this.context = context;
        this.bookItems = bookItems;
    }

    @Override
    public int getCount() {
        return bookItems.size();
    }

    @Override
    public Object getItem(int i) {
        return bookItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Book book;
        if(view ==null){
            book = new Book();
            view = LayoutInflater.from(context).inflate(R.layout.book_item, viewGroup, false);
            book.name = view.findViewById(R.id.bookName);
            book.imageUrl = view.findViewById(R.id.list_item);
            view.setTag(book);
        }else {
            book = (Book) view.getTag();
        }
        book.name.setText(bookItems.get(i).name);
        Glide.with(view.getContext()).load(bookItems.get(i).imageUrl).into(book.imageUrl);
        return view;
    }
}
class Book {
    TextView name;
    ImageView imageUrl;
}
