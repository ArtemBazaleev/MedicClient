package com.example.medicapp.networking.data;

import com.example.medicapp.Constants;
import com.example.medicapp.model.ProfileModel;
import com.example.medicapp.networking.DataApi;
import com.example.medicapp.networking.response.advice.AdviceResponse;
import com.example.medicapp.networking.response.date.ResponseAvailableDate;
import com.example.medicapp.networking.response.exercise.ResponseExercise;
import com.example.medicapp.networking.response.reservations.ResponseReservations;
import com.example.medicapp.networking.response.results.ResponseResult;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataApiHelper {
    private Retrofit provideRetrofit(){
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    public Observable<Response<ResponseExercise>> getExercises(String token, String id){
        String idAndToken  = "token=" + token + "; " + "id=" + id;
        Retrofit retrofit = provideRetrofit();
        DataApi api = retrofit.create(DataApi.class);
        return api.getExercise(idAndToken, 0, 300);
    }

    public Observable<Response<ResponseExercise>> getSuggestedExercisec(String token, String id){
        String idAndToken  = "token=" + token + "; " + "id=" + id;
        Retrofit retrofit = provideRetrofit();
        DataApi api = retrofit.create(DataApi.class);
        return api.getSuggestedExercises(idAndToken, id,0,300);
    }

    public Observable<Response<ResponseBody>> getProfile(String token, String id){
        String idAndToken = "token=" + token + "; " + "id=" + id;
        Retrofit retrofit = provideRetrofit();
        DataApi api = retrofit.create(DataApi.class);
        return api.getProfile(idAndToken, id);
    }

    public Observable<Response<ResponseBody>> setProfile(String token, String id, ProfileModel model){
        Retrofit retrofit = provideRetrofit();
        String idAndToken = "token=" + token + "; " + "id=" + id;
        DataApi api = retrofit.create(DataApi.class);
        Profile profile = new Profile(
                model.getAge(),
                model.getHeight(),
                model.getName(),
                model.isMale() ? "male" : "female",
                model.isDoSport(),
                model.getSurname(),
                model.getWeight(),
                model.isLazyJob() ? "sedentary":"active"
        );
        return api.setProfile(idAndToken, id, new ProfileBody(profile));
    }

    public Observable<Response<ResponseAvailableDate>> getAvailableDatesForDay(String token,
                                                                               String id,
                                                                               String date,
                                                                               boolean reserved){
        Retrofit retrofit = provideRetrofit();
        String idAndToken = "token=" + token + "; " + "id=" + id;
        DataApi api = retrofit.create(DataApi.class);
        return api.getAvailableDate(idAndToken, date, reserved);
    }

    public Observable<Response<ResponseBody>> reserveData(String token,
                                                          String id,
                                                          String date,
                                                          String time,
                                                          String name,
                                                          String surname){
        Retrofit retrofit = provideRetrofit();
        String idAndToken = "token=" + token + "; " + "id=" + id;
        DataApi api = retrofit.create(DataApi.class);
        return api.reserveData(idAndToken, id, new ReservationBody(date, time, name, surname));
    }

    public Observable<Response<ResponseBody>> getDiagnosticInfo(String token, String id){
        Retrofit retrofit = provideRetrofit();
        String idAndToken = "token=" + token + "; " + "id=" + id;
        DataApi api= retrofit.create(DataApi.class);
        return api.getDiagnostics(idAndToken, id);
    }

    public Observable<Response<ResponseReservations>> getReservations(String token, String id){
        Retrofit retrofit = provideRetrofit();
        String idAndToken = "token=" + token + "; " + "id=" + id;
        DataApi api= retrofit.create(DataApi.class);
        return api.getReservedDates(idAndToken, id);
    }

    public Observable<Response<AdviceResponse>> getAdvice(String token, String id, int skip, int limit){
        Retrofit retrofit = provideRetrofit();
        String idAndToken = "token=" + token + "; " + "id=" + id;
        DataApi api= retrofit.create(DataApi.class);
        return api.getAdvice(idAndToken, skip, limit);
    }

    public Observable<Response<ResponseBody>> addMessages(String token, String id, Long count){
        Retrofit retrofit = provideRetrofit();
        String idAndToken = "token=" + token + "; " + "id=" + id;
        DataApi api= retrofit.create(DataApi.class);
        return api.requestMoreMessages(idAndToken, id, new MoreMessagesBody(count));
    }

    public Observable<Response<ResponseBody>> logOut(String token){
        Retrofit retrofit = provideRetrofit();
        DataApi api= retrofit.create(DataApi.class);
        return api.logOut(new LogOutBody(token));
    }

}
