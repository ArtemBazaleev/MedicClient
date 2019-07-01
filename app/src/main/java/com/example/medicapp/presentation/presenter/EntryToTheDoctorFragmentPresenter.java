package com.example.medicapp.presentation.presenter;

import android.text.format.DateFormat;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.medicapp.model.EmptyDateModel;
import com.example.medicapp.model.ReservationModel;
import com.example.medicapp.networking.data.DataApiHelper;
import com.example.medicapp.networking.response.date.Time;
import com.example.medicapp.networking.response.reservations.Reservation;
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
            getViewState().showToastyMessage("Введите имя");
            return;
        }
        if (family.equals("")){
            getViewState().showToastyMessage("Введите фамилию");
            return;
        }
        if (chosenTime.equals("") || chosenDate.equals("")) {
            getViewState().showToastyMessage("Необходимо выбрать время и дату");
            return;
        }
        getViewState().showProgress();
        Disposable d = dataApiHelper.reserveData(token, id, chosenDate, chosenTime, name, family)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    getViewState().hideProgress();
                    if (responseBodyResponse.isSuccessful()) {
                        String response = Objects.requireNonNull(responseBodyResponse.body()).string();
                        Log.d("Presenter", "onSubmitBtnClicked: " + response);
                        getViewState().showAlertDialog("Запись прошла успешно", "Вы записаны на:" + chosenDate + " Время:" + chosenTime);
                    }
                    else {
                        String response = Objects.requireNonNull(responseBodyResponse.errorBody()).string();
                        Log.d("Presenter", "onSubmitBtnClicked: " + response);
                        JSONObject jsonObject = new JSONObject(response);
                        getViewState().showAlertDialog("Ошибка записи", jsonObject.getString("message"));
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    getViewState().showToastyMessage("Error, try later");
                    getViewState().hideProgress();
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

        getViewState().showProgressTime();
        Disposable d = dataApiHelper.getAvailableDatesForDay(token, id, s, false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(responseAvailableDateResponse -> Observable.fromIterable(Objects.requireNonNull(responseAvailableDateResponse.body()).getData().getHours().getTimes()))
                .filter(time -> !time.getReserved())
                .toList()
                .subscribe(response -> {
                    getViewState().hideProgressTime();
                    for (Time i :response) {
                        models.add(new EmptyDateModel(i.getTime()));
                    }
                    getViewState().loadAvailableDate(models);
                },throwable ->{
                    getViewState().showToastyMessage("Error, try later");
                    getViewState().hideProgressTime();
                });
        getViewState().hideDatePickerDialog();
    }

    public void onViewCreated() {

    }

    public void onReservationClicked() {
        getViewState().showProgress();
        Disposable d = dataApiHelper.getReservations(token, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    getViewState().hideProgress();
                    if (responseBodyResponse.isSuccessful()){
                        Log.d("onViewCreated: ", "OK");
                        ArrayList<ReservationModel> data = new ArrayList<>();
                        for (Reservation i:responseBodyResponse.body().getData().getReservations()) {
                            data.add(new ReservationModel(i.getDate(), i.getTime()));
                        }
                        getViewState().showAlertReservations(data);
                    }else {
                        Log.d("onViewCreated: ", "NEOK");
                        getViewState().showToastyMessage("Error, try to relogin");
                    }
                },throwable -> {
                    throwable.printStackTrace();
                    getViewState().hideProgress();
                    getViewState().showToastyMessage("Error, try later");
                });
    }
}