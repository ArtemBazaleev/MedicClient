package com.example.medicapp.presentation.presenter;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.medicapp.Constants;
import com.example.medicapp.model.ResultModel;
import com.example.medicapp.networking.data.DataApiHelper;
import com.example.medicapp.networking.response.results.Info;
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
    private List<ResultModel> data;
    private String TAG = "Results";

    public ResultsFragmentPresenter(String token, String id) {
        this.mToken = token;
        mUserID = id;
    }

    public void onCreateView(){
        data = new ArrayList<>();
        Disposable d =  apiHelper.getDiagnosticInfo(mToken, mUserID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    if (responseBodyResponse.isSuccessful())
                    {
                        for (String i: responseBodyResponse.body().getData().getInfo().get(0).getImages()) {
                            data.add(new ResultModel(Constants.BASE_URL_IMAGE + i));
                        }
                        getViewState().loadResults( data);
                    }
                    else{
                        Log.d(TAG, "onCreateView: "  + responseBodyResponse.errorBody().string());
                    }
                },throwable -> {
                    getViewState().showTastyMessage("Error, try later");
                });

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
