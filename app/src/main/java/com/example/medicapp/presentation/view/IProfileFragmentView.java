package com.example.medicapp.presentation.view;

import android.net.Uri;

import com.arellomobile.mvp.MvpView;
import com.example.medicapp.model.ProfileModel;

public interface IProfileFragmentView extends MvpView {

    void showToastyMessage(String message);

    void setProfileData(ProfileModel model);

    void chosePhoto();

    void setProfilePhoto(Uri uri);

    void setEnabledSubmitBtn(boolean enabled);

    void showProgress();

    void hideProgress();

    void startLoginActivityAndClearStack();
}
