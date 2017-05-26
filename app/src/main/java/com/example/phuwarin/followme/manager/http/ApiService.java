package com.example.phuwarin.followme.manager.http;

import com.example.phuwarin.followme.dao.JoinTripMasterDao;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Phuwarin on 4/14/2017.
 */

public interface ApiService {
    @FormUrlEncoded
    @POST("check_for_start.php")
    Call<JoinTripMasterDao> loadStatusJoinTrip(
            @Field("id") String userId
    );
}
