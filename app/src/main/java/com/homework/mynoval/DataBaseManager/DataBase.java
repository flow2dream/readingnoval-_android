package com.homework.mynoval.DataBaseManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.homework.mynoval.Bean.Book;
import com.homework.mynoval.Bean.User;
import com.homework.mynoval.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class DataBase extends SQLiteOpenHelper {

    private static final String BASENAME = "novallist";

    private static final String TABELNAME = "booklist";
    private static  final String USERID="userId";
    public static final String TITLE = "title";
    public static final String IMG = "img";

    private static final String TABLENAME="user";

    public static final String ACCOUNT = "account";
    public static final String PASSWORD = "password";

    Context context;
    public DataBase(@Nullable Context context) {
        super(context, BASENAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE IF NOT EXISTS "+TABELNAME+
                "("+USERID+" text not null, "+TITLE+" text not null, "+IMG+
                " text not null,primary key("+USERID+","+TITLE+","+IMG+"));";
        sqLiteDatabase.execSQL(sql);

        String sql1 = "CREATE TABLE IF NOT EXISTS "+TABLENAME+
                " ("+ACCOUNT+" text primary key not null, "+PASSWORD+" text not null);";
        sqLiteDatabase.execSQL(sql1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addBook(Book book) {
        // 添加书本
        ContentValues cv = new ContentValues();
        cv.put(USERID, book.getUser_id());
        cv.put(TITLE, book.getTitle());
        cv.put(IMG, book.getImg());
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        long insert = sqLiteDatabase.insert(TABELNAME,USERID ,cv);
        return insert != -1;
    }

    public boolean deleteBook(Book book) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        int num = sqLiteDatabase.delete(TABELNAME, USERID+"=? and "+TITLE+"=?", new String[]{book.getUser_id(), book.getTitle()});
        return num != 0;
    }

    public boolean isExist(Book book) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(TABELNAME, null,USERID+"=? and "+TITLE + "=? and "+IMG +"=?",
                new String[] {book.getUser_id(), book.getTitle(), book.getImg()}, null, null, null);
        return cursor.getCount() != 0;
    }

    public boolean isExist(String userid, String title) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.query(TABELNAME, null,USERID+"=? and "+TITLE + "=?",
                new String[] {userid, title}, null, null, null);
        return cursor.getCount() != 0;
    }

    public List<Book> getAll(String user_id) {
        List<Book> books = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(TABELNAME, null, USERID+"=?",
                                        new String[]{user_id},
                                        null, null, null);
        if (cursor != null) {
            while(cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndex(TITLE));
                String img = cursor.getString(cursor.getColumnIndex(IMG));
                books.add(new Book(user_id, img, title));
            }
            cursor.close();
        }
        return books;
    }

    public boolean isRight(User user) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(TABLENAME, null,ACCOUNT+"=?",
                new String[] {user.getAccount()}, null, null, null);

        if(cursor != null) {
            while(cursor.moveToNext()) {
                if (user.getPassword().equals(cursor.getString(cursor.getColumnIndex(PASSWORD)))){
                    cursor.close();
                    return true;
                }
            }
        }
        cursor.close();
        return false;
    }

    public boolean addUser(User user) {
        // 添加书本
        ContentValues cv = new ContentValues();
        cv.put(ACCOUNT, user.getAccount());
        cv.put(PASSWORD, user.getPassword());
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        long insert = sqLiteDatabase.insert(TABLENAME,ACCOUNT ,cv);

        return insert != -1;
    }

    public boolean updatePassword(String user_id, String password){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(PASSWORD, password);

        int i = sqLiteDatabase.update(TABLENAME, cv, ACCOUNT+"=?",new String[]{user_id});
        Log.d("TTT", "updatePassword: "+i);
        return i!=0;
    }
}
