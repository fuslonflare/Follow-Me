package com.example.phuwarin.followme.manager;

import android.content.Context;

import com.example.phuwarin.followme.manager.http.ApiService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class HttpManager {

    private static final String URL1 = "https://webserv.kmitl.ac.th/parietallobe/";
    private static final String URL2 = "http://bicyclefollowme.esy.es/";

    private static HttpManager instance;
    private Context mContext;
    private ApiService service;

    private HttpManager() {
        mContext = ContextBuilder.getInstance().getContext();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL2)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(ApiService.class);
    }

    public static HttpManager getInstance() {
        if (instance == null)
            instance = new HttpManager();
        return instance;
    }

    public ApiService getService() {
        return service;
    }
}
