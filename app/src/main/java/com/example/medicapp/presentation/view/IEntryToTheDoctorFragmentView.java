package com.example.medicapp.presentation.view;

import com.arellomobile.mvp.MvpView;
import com.example.medicapp.model.EmptyDateModel;
import com.example.medicapp.model.ReservationModel;

import java.util.ArrayList;
import java.util.List;

public interface IEntryToTheDoctorFragmentView extends MvpView {
    void showToastyMessage(String message);

    void showDatePickerDialog();

    void hideDatePickerDialog();

    void loadAvailableDate(List<EmptyDateModel> EmptyDateModel);

    void setDateText(String txt);

    void setActiveSubmitBtn(boolean active);

    void showAlertDialog(String header, String message);

    void showAlertReservations(ArrayList<ReservationModel> data);

    void showProgress();

    void hideProgress();

    void showProgressTime();

    void hideProgressTime();

    void startLoginActivityAndClearStack();

}
