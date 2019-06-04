package com.example.medicapp.presentation.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.medicapp.model.SecuredSharedPreferences;
import com.example.medicapp.networking.registration.RegistrationHelper;
import com.example.medicapp.networking.registration.response.ResponseSignIn;
import com.example.medicapp.presentation.view.ILoginView;

import org.json.JSONObject;

import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;


@InjectViewState
public class LoginPresenter extends MvpPresenter<ILoginView> {
    private String TAG = "LOGIN PRESENTER";
    private String login;
    private String password;
    private RegistrationHelper helper = new RegistrationHelper();
    private SecuredSharedPreferences preferences;

    public LoginPresenter(SecuredSharedPreferences preferences){
        this.preferences = preferences;
    }

    public void onPasswordChanged(String password){
        this.password = password;

    }

    public void onCreate(){
        if (!preferences.getToken().equals(""))
            getViewState().startMainActivity(preferences.getToken());
    }

    public void onLoginChanged(String login){
        this.login = login;
    }

    public void onBtnLoginClicked(){
        helper.signIn(login,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccess, throwable -> throwable.printStackTrace());

    }

    private void onSuccess(Response<ResponseSignIn> response) {
        if (response.isSuccessful()){
            getViewState().showToastyMessage(response.body() != null ? response.body().getMessage() : "");
            preferences.setToken(Objects.requireNonNull(response.body()).getData().getToken());
            getViewState().startMainActivity(Objects.requireNonNull(response.body()).getData().getToken());
        }
        else {
            try {
                JSONObject jObjError = new JSONObject(Objects.requireNonNull(response.errorBody()).string());
                getViewState().showToastyMessage(jObjError.getJSONObject("data").getString("error"));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void onRegistrationClicked(){
        getViewState().startRegistrationActivity();
    }

}
