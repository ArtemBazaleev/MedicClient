package com.example.medicapp.presentation.presenter;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import com.example.medicapp.model.AdviceModel;
import com.example.medicapp.networking.data.DataApiHelper;
import com.example.medicapp.networking.response.advice.Advice;
import com.example.medicapp.presentation.view.AdviceActivityView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class AdvicesActivityPresenter extends MvpPresenter<AdviceActivityView> {

    private String mToken;
    private String mID;
    private CompositeDisposable d = new CompositeDisposable();
    private DataApiHelper apiHelper;

    public AdvicesActivityPresenter(String token, String id) {
        this.mToken = token;
        this.mID = id;
        apiHelper = new DataApiHelper();
    }

    public void onCreate(){
        requestAdvice();
    }

    private void requestAdvice() {
        getViewState().showLoadingIndicator();
        d.add(apiHelper.getAdvice(mToken, mID,0, 2000)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    getViewState().hideLoadingIndicator();
                    if (responseBodyResponse.isSuccessful()) {
                        List<AdviceModel> models = new ArrayList<>();

                        for (Advice i:responseBodyResponse.body().getData().getAdvices()) {
                            if (i.getImages().size()!=0){
                                Log.d("Advice","Images not Empty");

                                models.add(new AdviceModel(i, AdviceModel.MODE_IMAGES));
                            }
                            else if (i.getVideos().size()!=0){
                                Log.d("Advice","Videos not Empty");
                                models.add(new AdviceModel(i, AdviceModel.MODE_VIDEO));
                            }else if (!i.getText().equals("")){
                                Log.d("Advice","Text not Empty");
                                models.add(new AdviceModel(i, AdviceModel.MODE_TXT));
                            }
                        }
                        getViewState().loadData(models);
                    }
                    else{
                        Log.d("Exercise", "onError:" + responseBodyResponse.errorBody().string());
                        getViewState().startLoginActivityAndClearStack();
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    getViewState().hideLoadingIndicator();
                    getViewState().showToastyMessage("Error, try later");
                }));

    }

}
