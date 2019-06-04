package com.example.medicapp.networking.data;

import com.example.medicapp.Constants;
import com.example.medicapp.networking.DataApi;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataApiHelper {
    private Retrofit provideRetrofit(){
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public Observable<Response<ResponseBody>> getExercises(String token, String id){
        String idAndToken  = "token=" + token + "; " + "id=" + id;
        Retrofit retrofit = provideRetrofit();
        DataApi api = retrofit.create(DataApi.class);
        return api.getExercise(idAndToken);
    }
}
