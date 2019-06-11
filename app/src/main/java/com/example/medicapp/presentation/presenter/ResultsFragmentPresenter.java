package com.example.medicapp.presentation.presenter;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.medicapp.model.ResultModel;
import com.example.medicapp.networking.data.DataApiHelper;
import com.example.medicapp.presentation.view.IResultsFragmentView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class ResultsFragmentPresenter extends MvpPresenter<IResultsFragmentView> {

    private DataApiHelper apiHelper = new DataApiHelper();

    private String mToken = "";
    private String mUserID = "";

    private String TAG = "Results";

    public ResultsFragmentPresenter(String token, String id) {
        this.mToken = token;
        mUserID = id;
    }

    public void onCreateView(){
        Disposable d =  apiHelper.getDiagnosticInfo(mToken, mUserID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    if (responseBodyResponse.isSuccessful())
                        Log.d(TAG, "onCreateView: " + responseBodyResponse.body().string());
                    else Log.d(TAG, "onCreateView: "  + responseBodyResponse.errorBody().string());
                },throwable -> {
                    getViewState().showTastyMessage("Error, try later");
                });


        getViewState().loadResults(provideData());
    }

    private List<ResultModel> provideData() {
        List<ResultModel> data = new ArrayList<>();
        for (int i = 0; i < 5; i++)
            data.add(new ResultModel());
        return data;
    }

    public void onResultClicked(ResultModel model){
        getViewState().startActivityResultView(model);
    }

}
