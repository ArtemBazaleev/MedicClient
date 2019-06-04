package com.example.medicapp.presentation.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.medicapp.networking.registration.RegistrationHelper;
import com.example.medicapp.networking.registration.response.ResponseSignIn;
import com.example.medicapp.presentation.view.IRegistrationView;

import org.json.JSONObject;

import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

@InjectViewState
public class RegistrationPresenter extends MvpPresenter<IRegistrationView> {

    private String login ="";
    private String password = "";
    private String confirmPassword = "";
    private String sms;
    private RegistrationHelper helper = new RegistrationHelper();
    private CompositeDisposable disposables = new CompositeDisposable();

    public void onLoginClicked(){
        getViewState().startLoginActivity();
    }

    public void onRegisterClicked(){
        if (password.equals("") || confirmPassword.equals("") || login.equals("")){
            getViewState().showToastyMessage("Заполните все поля");
            return;
        }
        if (!password.equals(confirmPassword)) {
            getViewState().showToastyMessage("Пароли не совпадают");
            return;
        }
        getViewState().setEnabledSubmitBtn(false);
        getViewState().showLoadingIndicator();
        Disposable  d = helper.signUp(login, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccess, throwable -> {
                    throwable.printStackTrace();
                    getViewState().hideLoadingIndicator();
                    getViewState().setEnabledSubmitBtn(true);
                    getViewState().showToastyMessage("Error, try later");
                });
        disposables.add(d);
    }

    private void onSuccess(Response<ResponseSignIn> responseSignInResponse) {
        getViewState().setEnabledSubmitBtn(true);
        getViewState().hideLoadingIndicator();
        if (responseSignInResponse.isSuccessful()){
            getViewState().showToastyMessage(responseSignInResponse.body() != null ? responseSignInResponse.body().getMessage() : "");
            getViewState().showAlertDialog();
        }
        else {
            try {
                JSONObject jObjError = new JSONObject(Objects.requireNonNull(responseSignInResponse.errorBody()).string());
                getViewState().showToastyMessage(jObjError.getJSONObject("data").getString("error"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onLoginChanged(String login){
        this.login = login;
    }

    public void onPasswordChanged(String password){
        this.password = password;
    }

    public void onConfirmPasswordChanged(String confirmPassword){
        this.confirmPassword = confirmPassword;
    }

    public void onSmsCode(String sms) {
        this.sms = sms;
        if (sms.length() == 5){
            Disposable  d =  helper.confirm(login,sms)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(responseBodyResponse -> {
                        if (responseBodyResponse.isSuccessful())
                            getViewState().showToastyMessage(Objects.requireNonNull(responseBodyResponse.body()).string());
                        else   getViewState().showToastyMessage(Objects.requireNonNull(responseBodyResponse.errorBody()).string());
                    }, Throwable::printStackTrace);
            disposables.add(d);
        }
    }
}
