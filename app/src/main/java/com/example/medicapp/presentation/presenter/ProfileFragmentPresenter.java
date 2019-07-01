package com.example.medicapp.presentation.presenter;

import android.net.Uri;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.medicapp.model.ProfileModel;
import com.example.medicapp.networking.DataApi;
import com.example.medicapp.networking.data.DataApiHelper;
import com.example.medicapp.presentation.view.IProfileFragmentView;

import java.text.DecimalFormat;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.schedulers.Schedulers;


@InjectViewState
public class ProfileFragmentPresenter extends MvpPresenter<IProfileFragmentView> {

    private ProfileModel profile;
    private String token = "";
    private String userID = "";
    private DataApiHelper dataApiHelper;
    DecimalFormat df = new DecimalFormat("#.##");
    private CompositeDisposable disposables = new CompositeDisposable();

    public ProfileFragmentPresenter(String token, String userID){
        this.token = token;
        this.userID = userID;
        profile = new ProfileModel();
        dataApiHelper = new DataApiHelper();
    }

    public void onViewCreated(){
        getViewState().showProgress();
        Disposable d = dataApiHelper.getProfile(token, userID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    getViewState().hideProgress();
                    if (responseBodyResponse.isSuccessful())
                        getViewState().setProfileData(new ProfileModel(responseBodyResponse.body().string()));
                    else
                        Log.d("Profile", "onCreateView: " + responseBodyResponse.errorBody().string());
                }, throwable -> {
                    getViewState().hideProgress();
                    throwable.printStackTrace();
                    getViewState().showToastyMessage("Error, try later");
                });
        disposables.add(d);
    }

    public void onSubmitBtnClicked(){
        getViewState().showProgress();
        Log.d("USERID", " " + userID);
        Disposable d = dataApiHelper.setProfile(token, userID,profile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        responseBodyResponse -> {
                            getViewState().hideProgress();
                            if (responseBodyResponse.isSuccessful())
                                getViewState().showToastyMessage("Успех");
                            else
                                Log.d("Profile", "onSubmitBtnClicked: " + responseBodyResponse.errorBody().string());
                        },
                        throwable -> {
                            getViewState().hideProgress();
                            getViewState().showToastyMessage("Error, try later");
                        }
                );
        disposables.add(d);
    }

    public void setMale(boolean isMale){
        profile.setMale(isMale);
    }

    public void setName(String name){
        profile.setName(name);
        checkFilledData();
    }

    public void setSurname(String surname){
        profile.setSurname(surname);
        checkFilledData();
    }

    public void setWeight(float weight){
        String formatted = df.format(weight);
        formatted =  formatted.replace(",",".");
        profile.setWeight(Double.parseDouble(formatted));
    }

    public void setAge(int age){
        profile.setAge(age);
    }

    public void setHeight(float height){
        String formatted = df.format(height);
        formatted =  formatted.replace(",",".");
        profile.setHeight(Double.parseDouble(formatted));
    }

    public void setDoSport(boolean isDoSport){
        profile.setDoSport(isDoSport);
    }

    public void setLazyJob(boolean isLazy){
        profile.setLazyJob(isLazy);
    }

    public void onPhotoChosen(Uri selectedImage) {
        getViewState().setProfilePhoto(selectedImage);
    }

    public void onPhotoClicked() {
        getViewState().chosePhoto();
    }

    private void checkFilledData(){
        if (profile.getName().equals("")) {
            getViewState().setEnabledSubmitBtn(false);
            return;
        }
        if (profile.getSurname().equals("")) {
            getViewState().setEnabledSubmitBtn(false);
            return;
        }
        getViewState().setEnabledSubmitBtn(true);
    }
}
