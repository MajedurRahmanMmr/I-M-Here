package com.example.project.DB;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;


@RealmClass
public class TokenFitbit implements RealmModel {


    @PrimaryKey
    String token;

    String user_id;

    public TokenFitbit() {
    }

    public TokenFitbit(String token, String user_id) {
        this.token = token;
        this.user_id = user_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
