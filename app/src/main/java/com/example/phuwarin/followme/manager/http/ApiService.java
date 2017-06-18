package com.example.phuwarin.followme.manager.http;

import com.example.phuwarin.followme.dao.InsertUserDao;
import com.example.phuwarin.followme.dao.position.PositionDao;
import com.example.phuwarin.followme.dao.trip.JoinTripDao;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Phuwarin on 4/14/2017.
 */

public interface ApiService {
    @FormUrlEncoded
    @POST("check_for_start.php")
    Call<JoinTripDao> loadStatusJoinTrip(
            @Field("user_id") String userId
    );

    @GET("get_position.php")
    Call<PositionDao> loadPosition();

    @FormUrlEncoded
    @POST("insert_user.php")
    Call<InsertUserDao> addMember(
            @Field("user_id") String userId,
            @Field("user_name") String userName,
            @Field("position_id") String positionId
    );
}
