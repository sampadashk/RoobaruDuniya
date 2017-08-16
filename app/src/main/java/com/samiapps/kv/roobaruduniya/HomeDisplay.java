package com.samiapps.kv.roobaruduniya;

/**
 * Created by KV on 11/8/17.
 */

public class HomeDisplay {
    String title;
    String photo;


    public HomeDisplay() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public HomeDisplay(String title, String photo) {
        this.title = title;
        this.photo = photo;
    }
}
