package com.example.medicapp.networking;


import com.example.medicapp.networking.data.ProfileBody;
import com.example.medicapp.networking.data.ReservationBody;
import com.example.medicapp.networking.data.ResponseProfile;

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
    Observable<Response<ResponseBody>> getExercise(@Header("Cookie") String tokenAndId);

    @GET("patient/{patientID}/exercise")
    Observable<Response<ResponseBody>> getSuggestedExercises(
            @Header("Cookie") String tokenAndId,
            @Path("patientID") String patientID
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

    @GET("reservation?")
    Observable<Response<ResponseBody>> getAvailableDate(
            @Header("Cookie") String tokenAndId,
            @Query("day") String date
    );

    @POST("patient/{patientID}/reserve")
    Observable<Response<ResponseBody>> reserveData(
            @Header("Cookie") String tokenAndId,
            @Path("patientID") String userID,
            @Body ReservationBody body
    );
}
