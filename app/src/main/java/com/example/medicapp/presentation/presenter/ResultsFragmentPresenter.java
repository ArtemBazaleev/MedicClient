package com.example.medicapp.presentation.presenter;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.medicapp.Constants;
import com.example.medicapp.model.ResultModel;
import com.example.medicapp.networking.data.DataApiHelper;
import com.example.medicapp.presentation.view.IResultsFragmentView;

import org.json.JSONArray;
import org.json.JSONObject;

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
    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public ResultsFragmentPresenter(String token, String id) {
        this.mToken = token;
        mUserID = id;
    }

    public void onCreateView(){
        requestResults();
    }

    private List<ResultModel> provideData() {
        List<ResultModel> data = new ArrayList<>();
        for (int i = 0; i < 5; i++)
            data.add(new ResultModel());
        return data;
    }

    public void onResultClicked(ResultModel model){
        if (model.getType() == ResultModel.TYPE_CONCLUSION)
            getViewState().startConclusionActivity(model.getDesc());
        else
            getViewState().startActivityResultView(model);
    }

    public void onRefresh() {
        requestResults();
    }

    private void requestResults(){
        data = new ArrayList<>();
        getViewState().showRefreshing();
        Disposable d =  apiHelper.getDiagnosticInfo(mToken, mUserID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    getViewState().hideRefreshing();
                    Log.d(TAG, "onCreateView: " + responseBodyResponse.code());
                    if (responseBodyResponse.isSuccessful())
                    {
                        JSONObject jsonObject = new JSONObject(responseBodyResponse.body().string());
                        Log.d(TAG, jsonObject.toString());
                        try {
                            JSONArray infos = jsonObject.getJSONObject("data").getJSONArray("info");
                            for (int i = 0; i< infos.length(); i++) {
                                Date date = new Date(infos.getJSONObject(i).getLong("created"));
                                String today = formatter.format(date);
                                data.add(new ResultModel("",
                                        infos.getJSONObject(i).getString("conclusion"),
                                        "Заключение",
                                        ResultModel.TYPE_CONCLUSION));
                                JSONArray arrJson = infos.getJSONObject(i).getJSONArray("backbone");
                                String[] arr = new String[arrJson.length()];
                                for(int j = 0; j < arrJson.length(); j++)
                                    arr[j] = arrJson.getString(j);
                                ResultModel resultModel = new ResultModel(
                                        "",
                                        "3D модель позвоночника",
                                        today,
                                        ResultModel.TYPE_BACKBONE
                                );
                                resultModel.setBackBoneImage(arr);
                                data.add(resultModel);

                                JSONArray otherArr = infos.getJSONObject(i).getJSONArray("other");
                                for (int k=0; k < otherArr.length(); k++){
                                    data.add(new ResultModel(
                                            Constants.BASE_URL_IMAGE + otherArr.getJSONObject(k).getString("image"),
                                            otherArr.getJSONObject(k).getString("name"),
                                            today,
                                            ResultModel.TYPE_OTHER));
                                }
                            }
                            if (data.size() == 0)
                                getViewState().showErrorNoResults();
                            getViewState().loadResults(data);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    else{
                        Log.d(TAG, "onCreateView: "  + responseBodyResponse.errorBody().string());
                        getViewState().startLoginActivityAndClearStack();
                    }
                },throwable -> {
                    getViewState().hideRefreshing();
                    getViewState().showTastyMessage("Error, try later");
                });

    }
}
