package com.samiapps.kv.roobaruduniya;

/**
 * Created by KV on 18/6/17.
 */

public class RoobaruDuniya {
    String title;
    String content;
    String photo;
    String email;
    public RoobaruDuniya()
    {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public RoobaruDuniya(String title, String content, String photo,String user, String email, int draft, int sent) {
        this.title = title;
        this.content = content;
        this.photo = photo;
        this.email = email;
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
