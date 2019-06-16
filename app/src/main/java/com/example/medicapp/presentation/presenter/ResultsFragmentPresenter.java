package com.example.medicapp.presentation.presenter;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.medicapp.Constants;
import com.example.medicapp.model.ResultModel;
import com.example.medicapp.networking.data.DataApiHelper;
import com.example.medicapp.networking.response.results.Info;
import com.example.medicapp.presentation.view.IResultsFragmentView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
                    Log.d(TAG, "onCreateView: " + responseBodyResponse.code());
                    if (responseBodyResponse.isSuccessful())
                    {
                        Log.d(TAG, "onCreateView: "+ responseBodyResponse.body().toString());
                        Info info = responseBodyResponse.body().getData().getInfo().get(0);
                        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        Date date = new Date(info.getCreated());
                        String today = formatter.format(date);
                        data.add(new ResultModel("",info.getConclusion(),"Заключение:", ResultModel.TYPE_CONCLUSION));
                        for (String i:info.getBackbone()) {
                            data.add(new ResultModel(Constants.BASE_URL_IMAGE + i, info.getConclusion(), today, ResultModel.TYPE_BACKBONE));
                        }
                        for (String i:info.getOther()) {
                            data.add(new ResultModel(Constants.BASE_URL_IMAGE + i, "", today, ResultModel.TYPE_OTHER));
                        }
                        getViewState().loadResults(data);
                    }
                    else{
                        Log.d(TAG, "onCreateView: "  + responseBodyResponse.errorBody().string());
                    }
                },throwable -> getViewState().showTastyMessage("Error, try later"));

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
