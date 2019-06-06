package com.example.medicapp.networking;

import com.example.medicapp.networking.registration.ConfirmBody;
import com.example.medicapp.networking.registration.RequestRestoreBody;
import com.example.medicapp.networking.registration.RestorePasswordBody;
import com.example.medicapp.networking.registration.SignInBody;
import com.example.medicapp.networking.registration.response.ResponseSignIn;


import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
public interface RegistrationApi {

    @Headers("Content-Type: application/json")
    @POST("auth/signin")
    Observable<Response<ResponseSignIn>> signIn(@Body SignInBody body);

    @Headers("Content-Type: application/json")
    @POST("auth/signup")
    Observable<Response<ResponseSignIn>> signUp(@Body SignInBody body);

    @Headers("Content-Type: application/json")
    @POST("auth/signup/confirm")
    Observable<Response<ResponseBody>> confirm(@Body ConfirmBody body);

    @Headers("Content-Type: application/json")
    @POST("auth/password/reset/request")
    Observable<Response<ResponseBody>> requestRestore(@Body RequestRestoreBody body);

    @Headers("Content-Type: application/json")
    @POST("auth/password/reset/receive")
    Observable<Response<ResponseBody>> restorePassword(@Body RestorePasswordBody body);


}
