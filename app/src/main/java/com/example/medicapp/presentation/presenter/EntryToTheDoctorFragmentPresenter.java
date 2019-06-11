package com.example.medicapp.presentation.presenter;

import android.text.format.DateFormat;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.medicapp.model.EmptyDateModel;
import com.example.medicapp.networking.data.DataApiHelper;
import com.example.medicapp.networking.response.date.Time;
import com.example.medicapp.presentation.view.IEntryToTheDoctorFragmentView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class EntryToTheDoctorFragmentPresenter extends MvpPresenter<IEntryToTheDoctorFragmentView> {

    private DataApiHelper dataApiHelper = new DataApiHelper();
    private String token = "";
    private String id = "";

    private String chosenDate = "";
    private String chosenTime = "";

    private String family = "";
    private String middleName = "";
    private String name = "";


    public EntryToTheDoctorFragmentPresenter(String token, String id) {
        this.token = token;
        this.id = id;
    }

    public void onChoseDateClicked(){
        getViewState().showDatePickerDialog();
    }

    public void onNameChanged(String name){
        if (name == null)
            return;
        if (name.equals(""))
            getViewState().setActiveSubmitBtn(false);
        else {
            this.name = name;
            getViewState().setActiveSubmitBtn(true);
        }
    }

    public void onSurnameChanged(String family){
        this.family = family;
    }

    public void onMiddleNameChanged(String middleName){
        this.middleName = middleName;
    }

    public void onSubmitBtnClicked(){
        if (name.equals("")) {
            getViewState().showToastyMessage("Name empty");
            return;
        }

        Disposable d = dataApiHelper.reserveData(token, id, chosenDate, chosenTime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    if (responseBodyResponse.isSuccessful()) {
                        Log.d("Presenter", "onSubmitBtnClicked: " + responseBodyResponse.body().string());
                        JSONObject jObjError = new JSONObject(Objects.requireNonNull(responseBodyResponse.body()).string());
                        getViewState().showToastyMessage(jObjError.getString("message"));
                    }
                    else {
                        Log.d("Presenter", "onSubmitBtnClicked: " + responseBodyResponse.errorBody().string());
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    getViewState().showToastyMessage("Error, try later");
                });
    }

    public void setTime(String time){
        this.chosenTime = time;
    }

    public void onDateSelected(Calendar startDate) {
        String s = DateFormat.format("dd.MM.yyyy", startDate.getTime()).toString();
        getViewState().setDateText(s);
        this.chosenDate = s;
        List<EmptyDateModel> models = new ArrayList<>();
        Disposable d = dataApiHelper.getAvailableDatesForDay(token, id, s)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(responseAvailableDateResponse -> Observable.fromIterable(Objects.requireNonNull(responseAvailableDateResponse.body()).getData().getHours().getTimes()))
                .filter(time -> !time.getReserved())
                .toList()
                .subscribe(response -> {
                    for (Time i :response) {
                        models.add(new EmptyDateModel(i.getTime()));
                    }
                    getViewState().loadAvailableDate(models);
                },throwable -> getViewState().showToastyMessage("Error, try later"));




        getViewState().hideDatePickerDialog();
    }
}
