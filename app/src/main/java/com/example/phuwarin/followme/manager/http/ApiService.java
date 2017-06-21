package com.example.phuwarin.followme.manager.http;

import com.example.phuwarin.followme.dao.NormalDao;
import com.example.phuwarin.followme.dao.position.PositionDao;
import com.example.phuwarin.followme.dao.trip.GenerateTripDao;
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
    @GET("get_position.php")
    Call<PositionDao> loadPosition();

    @GET("generate_trip_id.php")
    Call<GenerateTripDao> generateTripId();

    @FormUrlEncoded
    @POST("check_for_start.php")
    Call<JoinTripDao> loadStatusJoinTrip(
            @Field("user_id") String userId
    );

    @FormUrlEncoded
    @POST("add_user.php")
    Call<NormalDao> addMember(
            @Field("user_id") String userId,
            @Field("user_name") String userName,
            @Field("position_id") String positionId
    );

    @FormUrlEncoded
    @POST("add_trip.php")
    Call<NormalDao> addTrip(
            @Field("leader_id") String leaderId,
            @Field("trip_id") String tripId
    );

    @FormUrlEncoded
    @POST("add_member_to_join_trip.php")
    Call<NormalDao> addMemberToJoinTrip(
            @Field("list_member") String listMember,
            @Field("trip_id") String tripId
    );
}
