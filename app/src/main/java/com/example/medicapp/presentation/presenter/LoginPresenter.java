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
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;


@InjectViewState
public class LoginPresenter extends MvpPresenter<ILoginView> {
    private String TAG = "LOGIN PRESENTER";
    private String login = "";
    private String password = "";
    private RegistrationHelper helper = new RegistrationHelper();
    private SecuredSharedPreferences preferences;

    private String restoreLogin = "";
    private String restorePassword = "";
    private String restoreCode = "";
    private boolean flag = false;

    private CompositeDisposable disposables = new CompositeDisposable();

    public LoginPresenter(SecuredSharedPreferences preferences){
        this.preferences = preferences;
    }

    public void onPasswordChanged(String password){
        this.password = password;
    }

    public void onCreate(){
//        if (!preferences.getToken().equals(""))
//            getViewState().startMainActivity(preferences.getToken(), preferences.getUserID());
    }

    public void onLoginChanged(String login){
        this.login = login;
    }

    public void onBtnLoginClicked(){
        if (login.equals("") || password.equals("")) {
            getViewState().showToastyMessage("Заполните все поля");
            return;
        }
        if (login.length() < 3) {
            getViewState().showToastyMessage("Проверьте логин");
            return;
        }
        if (isNumeric(login)){
            if (login.charAt(0) != '+')
                login = "+" + login;
            if (login.charAt(1) == '8')
                login = "+7" + login.substring(2);
        }
        getViewState().setEnabledLoginBtn(false);
        getViewState().showLoadingIndicator();
        disposables.add(helper.signIn(login,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccess, throwable -> {
                    throwable.printStackTrace();
                    getViewState().setEnabledLoginBtn(true);
                    getViewState().hideLoadingIndicator();
                    getViewState().showToastyMessage("Error, try later");
                }));
    }

    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }

    }
    private void onSuccess(Response<ResponseSignIn> response) {
        getViewState().hideLoadingIndicator();
        getViewState().setEnabledLoginBtn(true);
        if (response.isSuccessful()){
            if (!Objects.requireNonNull(response.body()).getData().getRole().equals("patient")){
                getViewState().showToastyMessage("Доступно только для пациента");
                return;
            }
            preferences.setToken(Objects.requireNonNull(response.body()).getData().getToken());
            preferences.setUserID(Objects.requireNonNull(response.body()).getData().getId());
            preferences.setLogin(login);
            preferences.setPassword(password);
            getViewState().startMainActivity(
                    Objects.requireNonNull(response.body()).getData().getToken(),
                    Objects.requireNonNull(response.body()).getData().getId()
            );
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

    public void onForgotPassportClicked() {
        getViewState().showRestorePasswordDialog();
    }

    public void onRestoreSmsChanged(String s) {
        restoreCode = s;
    }

    public void onRestoreLoginChanged(String s){
        if (!flag)
            restoreLogin = s;
    }

    public void onDialogBtnClicked(){
        if (restoreLogin.length() < 3) {
            getViewState().showToastyMessage("Проверьте логин");
            return;
        }
        if (isNumeric(restoreLogin)){
            if (restoreLogin.charAt(0) != '+')
                restoreLogin = "+" + restoreLogin;
            if (restoreLogin.charAt(1) == '8')
                restoreLogin = "+7" + restoreLogin.substring(2);
        }
        getViewState().showProgressDialog();
        Disposable d = helper.requestRestore(restoreLogin)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    getViewState().hideProgressDialog();
                    if (responseBodyResponse.isSuccessful()){
                        getViewState().showSmsFieldDialog();
                        getViewState().showToastyMessage("Код отправлен");
                    }else {
                        JSONObject jObjError = new JSONObject(Objects.requireNonNull(responseBodyResponse.errorBody()).string());
                        getViewState().showToastyMessage(jObjError.getJSONObject("data").getString("error"));
                    }
                }, throwable -> {
                    getViewState().hideProgressDialog();
                    throwable.printStackTrace();
                    getViewState().showToastyMessage("Error, try later");
                });
        disposables.add(d);
    }

    public void onNewPasswordChanged(String newPassword) {
        this.restorePassword = newPassword;
    }

    public void onDialogRestoreBtnClicked(){
        if (restoreCode.equals("") || restorePassword.equals("")){
            getViewState().showToastyMessage("Заполните все поля");
            return;
        }
        getViewState().showProgressDialog();
        Disposable d = helper.restorePassword(restoreLogin, restorePassword, restoreCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    getViewState().hideProgressDialog();
                    if (responseBodyResponse.isSuccessful()){
                        getViewState().showToastyMessage("Восстановление прошло успешно");
                        getViewState().hideRestorePasswordDialog();
                    }else {
                        JSONObject jObjError = new JSONObject(Objects.requireNonNull(responseBodyResponse.errorBody()).string());
                        getViewState().showToastyMessage(jObjError.getJSONObject("data").getString("error"));
                    }

                }, throwable -> {
                    getViewState().hideProgressDialog();
                    throwable.printStackTrace();
                    getViewState().showToastyMessage("Error, try later");
                });
        disposables.add(d);
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }
}
