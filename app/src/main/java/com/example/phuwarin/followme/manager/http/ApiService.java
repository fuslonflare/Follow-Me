package com.example.phuwarin.followme.manager.http;

import com.example.phuwarin.followme.dao.CheckStatusTripDao;
import com.example.phuwarin.followme.dao.FriendLocationDao;
import com.example.phuwarin.followme.dao.NormalDao;
import com.example.phuwarin.followme.dao.position.PositionDao;
import com.example.phuwarin.followme.dao.route.RouteDao;
import com.example.phuwarin.followme.dao.trip.GenerateDao;
import com.example.phuwarin.followme.dao.trip.JoinTripDao;

import org.json.JSONObject;

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
    @POST("add_bicycle_route.php")
    Call<NormalDao> addBicycleRoute(
            @Field("route_id") String id,
            @Field("route_path") String path,
            @Field("trip_origin") String originId,
            @Field("trip_dest") String destinationId
    );

    @FormUrlEncoded
    @POST("update_route_in_trip.php")
    Call<NormalDao> updateRouteInTrip(
            @Field("route_id") String routeId,
            @Field("trip_id") String tripId
    );


    @FormUrlEncoded
    @POST("update_location_user.php")
    Call<NormalDao> updateLocation(
            @Field("user_id") String userId,
            @Field("user_lat") double lat,
            @Field("user_lng") double lng
    );

    @FormUrlEncoded
    @POST("load_location_friends.php")
    Call<FriendLocationDao> loadFriendsLocation(
            @Field("trip_id") String tripId
    );

    @FormUrlEncoded
    @POST("master-create-trip.php")
    Call<NormalDao> createTrip(
            @Field("request") JSONObject mainRequest
    );

    @FormUrlEncoded
    @POST("get_route_detail_from_trip.php")
    Call<RouteDao> loadRouteDetail(
            @Field("trip_id") String tripId
    );

    @FormUrlEncoded
    @POST("api-check-in.php")
    Call<NormalDao> insertCheckInPlace(
            @Field("user_id") String userId,
            @Field("lat") double lat,
            @Field("lng") double lng
    );

    @FormUrlEncoded
    @POST("api-finish-trip.php")
    Call<NormalDao> finishTrip(
            @Field("trip_id") String tripId
    );

    @FormUrlEncoded
    @POST("api-is-trip-finish.php")
    Call<CheckStatusTripDao> checkFinishTrip(
            @Field("trip_id") String tripId
    );

    @FormUrlEncoded
    @POST("keep_feedback.php")
    Call<NormalDao> sendFeedback(
            @Field("user_id") String userId,
            @Field("trip_id") String tripId,
            @Field("feedback") String message
    );
}
