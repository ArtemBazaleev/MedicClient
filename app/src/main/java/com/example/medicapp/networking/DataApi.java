package com.example.medicapp.networking;


import com.example.medicapp.networking.data.LogOutBody;
import com.example.medicapp.networking.data.MoreMessagesBody;
import com.example.medicapp.networking.data.ProfileBody;
import com.example.medicapp.networking.data.ReservationBody;
import com.example.medicapp.networking.response.advice.AdviceResponse;
import com.example.medicapp.networking.response.date.ResponseAvailableDate;
import com.example.medicapp.networking.response.exercise.ResponseExercise;
import com.example.medicapp.networking.response.reservations.ResponseReservations;
import com.example.medicapp.networking.response.results.ResponseResult;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DataApi {

    @GET("exercise")
    Observable<Response<ResponseExercise>> getExercise(
            @Header("Cookie") String tokenAndId,
            @Query("skip") int skip,
            @Query("limit") int limit
    );

    @GET("patient/{patientID}/exercise")
    Observable<Response<ResponseExercise>> getSuggestedExercises(
            @Header("Cookie") String tokenAndId,
            @Path("patientID") String patientID,
            @Query("skip") int skip,
            @Query("limit") int limit
    );

    @GET("patient/{patientID}/profile")
    Observable<Response<ResponseBody>> getProfile(
            @Header("Cookie") String tokenAndId,
            @Path("patientID") String patientID
    );

    @POST("patient/{patientID}/profile/update")
    Observable<Response<ResponseBody>> setProfile(
            @Header("Cookie") String tokenAndId,
            @Path("patientID") String userID,
            @Body ProfileBody body
    );

    @GET("patient/{patientID}/diagnosticInfo")
    Observable<Response<ResponseBody>> getDiagnostics(
            @Header("Cookie") String tokenAndId,
            @Path("patientID") String userID
    );

    @GET("reservation")
    Observable<Response<ResponseAvailableDate>> getAvailableDate(
            @Header("Cookie") String tokenAndId,
            @Query("date") String date,
            @Query("reserved") boolean reserved
    );

    @POST("patient/{patientID}/reserve")
    Observable<Response<ResponseBody>> reserveData(
            @Header("Cookie") String tokenAndId,
            @Path("patientID") String userID,
            @Body ReservationBody body
    );

    @GET("patient/{patientID}/reservation")
    Observable<Response<ResponseReservations>> getReservedDates(
            @Header("Cookie") String tokenAndId,
            @Path("patientID") String userID
    );

    @GET("advice")
    Observable<Response<AdviceResponse>> getAdvice(
            @Header("Cookie") String tokenAndId,
            @Query("skip") int skip,
            @Query("limit") int limit
    );

    @POST("patient/{patientID}/purchase/messages")
    Observable<Response<ResponseBody>> requestMoreMessages(
            @Header("Cookie") String tokenAndId,
            @Path("patientID") String userID,
            @Body MoreMessagesBody body
    );

    @POST("auth/logout")
    Observable<Response<ResponseBody>> logOut(
        @Body LogOutBody body
    );
}
