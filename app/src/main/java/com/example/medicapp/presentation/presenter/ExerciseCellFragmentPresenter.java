package com.example.medicapp.presentation.presenter;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.medicapp.model.ExerciseModel;
import com.example.medicapp.networking.data.DataApiHelper;
import com.example.medicapp.networking.response.exercise.Exercise;
import com.example.medicapp.networking.response.exercise.ResponseExercise;
import com.example.medicapp.presentation.view.IExerciseCellFragment;
import com.example.medicapp.ui.ExerciseCellFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

@InjectViewState
public class ExerciseCellFragmentPresenter extends MvpPresenter<IExerciseCellFragment> {
    private DataApiHelper apiHelper = new DataApiHelper();

    private String mToken = "";
    private String mID = "";
    private int mode;
    private CompositeDisposable d = new CompositeDisposable();

    public ExerciseCellFragmentPresenter(String token, String id){
        mID = id;
        mToken = token;
    }

    public void onViewCreated(){
        provideData();
    }

    private void provideData(){
        if (mode == ExerciseCellFragment.MODE_ALL) {
            getViewState().showLoadingIndicator();
            d.add(apiHelper.getExercises(mToken, mID)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onSuccess, throwable -> {
                        throwable.printStackTrace();
                        getViewState().hideLoadingIndicator();
                        getViewState().showToastyMessage("Error, try later");
                    }));
        }
        else {
            getViewState().showLoadingIndicator();
            d.add(apiHelper.getSuggestedExercisec(mToken, mID)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(responseBodyResponse -> {
                        if (responseBodyResponse.isSuccessful())
                            Log.d("Exercice", "mode: Suggested" + responseBodyResponse.body().string());
                        else Log.d("Exercise", "onError: " + responseBodyResponse.errorBody().string());
                    }, throwable -> {
                        throwable.printStackTrace();
                        getViewState().hideLoadingIndicator();
                        getViewState().showToastyMessage("Error, try later");
                    }));

            List<ExerciseModel> models = new ArrayList<>();
            for (int i = 0; i < 10; i++)
                models.add(new ExerciseModel());
            getViewState().loadExerciseModels(models);
        }
    }

    private void onSuccess(Response<ResponseExercise> responseBodyResponse) {
        getViewState().hideLoadingIndicator();
        if (responseBodyResponse.isSuccessful()) {
            Log.d("Exercise", "mode: All");
            List<ExerciseModel> models = new ArrayList<>();
            for (Exercise i: Objects.requireNonNull(responseBodyResponse.body()).getData().getExercises()){
                models.add(new ExerciseModel(i));
            }
            getViewState().loadExerciseModels(models);
        }
        else {
            try {
                Log.d("Exercise", "Token id: " + mToken + " " + mID);
                Log.d("Exercise", "onError: " + Objects.requireNonNull(responseBodyResponse.errorBody()).string());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void loadMoreEllements(){

    }

    public void onItemClicked(ExerciseModel exerciseModel){
        getViewState().startVideoViewActivity(exerciseModel.getUrlVideo());
    }

    public void setMode(int mMode) {
        this.mode = mMode;
    }
}
