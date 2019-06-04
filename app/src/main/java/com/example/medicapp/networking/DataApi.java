package com.example.medicapp.networking;


import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface DataApi {

    @GET("exercise")
    Observable<Response<ResponseBody>> getExercise(@Header("Cookie") String tokenAndId);
}
