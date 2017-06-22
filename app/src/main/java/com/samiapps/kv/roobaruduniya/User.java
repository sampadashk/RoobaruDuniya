package com.samiapps.kv.roobaruduniya;

/**
 * Created by KV on 19/6/17.
 */

public class User {
    String name;
    String email;
    String articleStatus;
    //published
  //  String drafted;
   // String sent;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User(String name, String email, String articleStatus) {
        this.name = name;
        this.email = email;
        this.articleStatus = articleStatus;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getarticleStatus() {
        return articleStatus;
    }

    public void setArticleStatus(String articleStatus) {
        this.articleStatus = articleStatus;
    }


}
