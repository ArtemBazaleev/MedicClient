package com.example.medicapp.presentation.presenter;

import android.text.format.DateFormat;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.medicapp.model.EmptyDateModel;
import com.example.medicapp.presentation.view.IEntryToTheDoctorFragmentView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@InjectViewState
public class EntryToTheDoctorFragmentPresenter extends MvpPresenter<IEntryToTheDoctorFragmentView> {

    public void onChoseDateClicked(){
        getViewState().showDatePickerDialog();
    }

    public void onNameChanged(String name){}

    public void onSurnameChanged(String family){}

    public void onMiddleNameChanged(String middleName){}

    public void onEntryBtnClicked(){}

    public void onDateSelected(Calendar startDate) {
        String s = DateFormat.format("dd/MM/yyyy", startDate.getTime()).toString();
        getViewState().setDateText(s);
        List<EmptyDateModel> models = new ArrayList<>();
        for (int i =0; i<5;i++)
            models.add(new EmptyDateModel());
        getViewState().loadAvailableDate(models);
        getViewState().hideDatePickerDialog();
    }
}
