package com.example.medicapp.networking.registration;

import com.example.medicapp.Constants;
import com.example.medicapp.networking.RegistrationApi;
import com.example.medicapp.networking.registration.response.ResponseSignIn;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistrationHelper {
    public Observable<Response<ResponseSignIn>> signIn(String login, String password){
        Retrofit retrofit = provideRetrofit();
        RegistrationApi api = retrofit.create(RegistrationApi.class);
        return api.signIn(new SignInBody(login,password));
    }

    public Observable<Response<ResponseSignIn>> signUp(String login, String password) {
        Retrofit retrofit = provideRetrofit();
        RegistrationApi api = retrofit.create(RegistrationApi.class);
        return api.signUp(new SignInBody(login, password));
    }

    public Observable<Response<ResponseBody>> confirm(String login, String code){
        Retrofit retrofit = provideRetrofit();
        RegistrationApi api = retrofit.create(RegistrationApi.class);
        return api.confirm(new ConfirmBody(login,code));
    }

    private Retrofit provideRetrofit(){
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
}
