package com.example.phuwarin.followme.util.detail;

import android.content.Context;

import com.example.phuwarin.followme.manager.ContextBuilder;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class User {

    private static User instance;
    private Context mContext;

    private String id;
    private String name;
    private String position;

    private User() {
        mContext = ContextBuilder.getInstance().getContext();
    }

    /**
     * Traditional of Singleton
     **/

    public static User getInstance() {
        if (instance == null)
            instance = new User();
        return instance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
