package com.samiapps.kv.roobaruduniya;

import java.io.Serializable;

/**
 * Created by KV on 18/6/17.
 */

public class RoobaruDuniya implements Serializable {
    String title;
    String content;
    String photo;
    String email;
    String user;
    String userProfilePhoto;
    public RoobaruDuniya()
    {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUserProfilePhoto() {
        return userProfilePhoto;
    }

    public void setUserProfilePhoto(String userProfilePhoto) {
        this.userProfilePhoto = userProfilePhoto;
    }

    public RoobaruDuniya(String title, String content, String photo, String user, String email, String userProfilePhoto, int draft, int sent) {
        this.title = title;
        this.content = content;
        this.photo = photo;
        this.user=user;
        this.email = email;
        this.userProfilePhoto=userProfilePhoto;
        this.draft = draft;
        this.sent = sent;

    }

    public String getContent() {

        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getDraft() {
        return draft;
    }

    public void setDraft(int draft) {
        this.draft = draft;
    }

    public int getSent() {
        return sent;
    }

    public void setSent(int sent) {
        this.sent = sent;
    }

    int draft;
    int sent;

}
