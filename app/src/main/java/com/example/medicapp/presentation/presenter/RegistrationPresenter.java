package com.example.medicapp.presentation.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.medicapp.ITimerListener;
import com.example.medicapp.ITimerSms;
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
public class RegistrationPresenter extends MvpPresenter<IRegistrationView> implements ITimerListener {

    private String login ="";
    private String password = "";
    private String confirmPassword = "";
    private String sms = "";
    private boolean isLoading = false;
    private RegistrationHelper helper = new RegistrationHelper();
    private CompositeDisposable disposables = new CompositeDisposable();
    private ITimerSms timer;
    private boolean isTicking = false;

    public RegistrationPresenter(ITimerSms timerSms) {
        timer = timerSms;
        timer.setTimerListener(this);
        isTicking = timer.isTicking();
    }

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

    private boolean isNumeric(String login) {
        try {
            Double.parseDouble(login);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    private void onSuccess(Response<ResponseSignIn> responseSignInResponse) {
        getViewState().setEnabledSubmitBtn(true);
        getViewState().hideLoadingIndicator();
        if (responseSignInResponse.isSuccessful()){
            if (timer.isTicking())
                timer.stopTimer();
            timer.startTimer(30000);
            isTicking = true;
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
        if (sms.length() == 5 && !isLoading){
            isLoading = true;
            getViewState().showProgressDialogWindow();
            Disposable  d =  helper.confirm(login,sms)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(responseBodyResponse -> { //Success
                        getViewState().hideProgressDialogWindow();
                        isLoading = false;
                        if (responseBodyResponse.isSuccessful()) {
                            getViewState().showToastyMessage("Аккаунт зарегистрирован");
                            getViewState().startLoginActivity();
                        }
                        else{
                            JSONObject jObjError = new JSONObject(Objects.requireNonNull(responseBodyResponse.errorBody()).string());
                            getViewState().showToastyMessage(jObjError.getJSONObject("data").getString("error"));
                        }
                    }, throwable -> {  //Timeout err, no internet, bad internet error
                        throwable.printStackTrace();
                        getViewState().showToastyMessage("Error, try later");
                        getViewState().hideProgressDialogWindow();
                        isLoading = false;
                    });
            disposables.add(d);
        }
    }

    public void moreSms() {
        if (!isLoading && !isTicking) {
            isTicking = true;
            timer.startTimer(30000);
            getViewState().showProgressDialogWindow();
            isLoading =true;
            Disposable  d = helper.signUp(login, password)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(responseSignInResponse -> {
                                isLoading = false;
                                getViewState().hideProgressDialogWindow();
                                if (responseSignInResponse.isSuccessful()) {
                                    getViewState().showToastyMessage("Повторное смс отправлено");
                                }
                                else {
                                    JSONObject jObjError = new JSONObject(Objects.requireNonNull(responseSignInResponse.errorBody()).string());
                                    getViewState().showToastyMessage(jObjError.getJSONObject("data").getString("error"));
                                }
                            },
                            throwable -> {
                                isLoading = false;
                                throwable.printStackTrace();
                                getViewState().hideProgressDialogWindow();
                                getViewState().showToastyMessage("Error, try later");
                            });
            disposables.add(d);
        }
    }

    @Override
    public void onTick(long mills) {
        getViewState().setMoreSmsText("Повторное смс через " + mills/1000 + " секунд");
    }

    @Override
    public void onTimerFinished() {
        getViewState().setMoreSmsText("Отправить повторное смс");
        isTicking = false;
    }
}
