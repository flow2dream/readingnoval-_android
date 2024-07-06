package com.homework.mynoval.Bean;

public class Book {
    private String user_id, title, img;

    public Book(String user_id, String img, String title) {
        this.user_id = user_id;
        this.img = img;
        this.title = title;
    }

    public Book() {
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
