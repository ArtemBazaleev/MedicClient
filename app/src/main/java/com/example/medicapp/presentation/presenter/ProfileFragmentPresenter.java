package com.example.medicapp.presentation.presenter;

import android.net.Uri;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.medicapp.model.ProfileModel;
import com.example.medicapp.presentation.view.IProfileFragmentView;


@InjectViewState
public class ProfileFragmentPresenter extends MvpPresenter<IProfileFragmentView> {

    private ProfileModel profile;

    public ProfileFragmentPresenter(){
        profile = new ProfileModel();
    }

    public void onSubmitBtnClicked(){

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

    public void setWeight(Double weight){
        profile.setWeight(weight);
    }

    public void setAge(int age){
        profile.setAge(age);
    }

    public void setHeight(Double height){
        profile.setHeight(height);
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
