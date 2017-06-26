package com.example.phuwarin.followme.manager.http;

import com.example.phuwarin.followme.dao.NormalDao;
import com.example.phuwarin.followme.dao.position.PositionDao;
import com.example.phuwarin.followme.dao.trip.GenerateDao;
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
    Call<GenerateDao> generateTripId();

    @GET("generate_destination_id.php")
    Call<GenerateDao> generateDestinationId();

    @GET("generate_origin_id.php")
    Call<GenerateDao> generateOriginId();

    @GET("generate_route_id.php")
    Call<GenerateDao> generateRouteId();

    @FormUrlEncoded
    @POST("check_for_added.php")
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

    @FormUrlEncoded
    @POST("delete_user_join_trip.php")
    Call<NormalDao> deleteUserJoinTrip(
            @Field("user_id") String userId
    );

    @FormUrlEncoded
    @POST("check_for_start.php")
    Call<NormalDao> loadRouteTrip(
            @Field("trip_id") String tripId
    );

    @FormUrlEncoded
    @POST("check_user_exist.php")
    Call<NormalDao> checkUserExist(
            @Field("list_member") String listMember
    );

    @FormUrlEncoded
    @POST("add_destination.php")
    Call<NormalDao> addDestination(
            @Field("dest_id") String id,
            @Field("dest_name_en") String nameEn,
            @Field("dest_name_th") String nameTh,
            @Field("dest_lat") double lat,
            @Field("dest_lng") double lng
    );

    @FormUrlEncoded
    @POST("add_origin.php")
    Call<NormalDao> addOrigin(
            @Field("origin_id") String id,
            @Field("origin_name_en") String nameEn,
            @Field("origin_name_th") String nameTh,
            @Field("origin_lat") double lat,
            @Field("origin_lng") double lng
    );

    @FormUrlEncoded
    @POST("add_bicycle_route.php")
    Call<NormalDao> addBicycleRoute(
            @Field("route_id") String id,
            @Field("route_path") String path,
            @Field("trip_origin") String originId,
            @Field("trip_dest") String destinationId
    );
}
