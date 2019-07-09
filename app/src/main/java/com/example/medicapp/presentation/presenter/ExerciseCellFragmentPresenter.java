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
import java.util.HashSet;
import java.util.LinkedList;
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

    private String mToken;
    private String mID;
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
        else if (mode == ExerciseCellFragment.MODE_SUGGESTED){
            getViewState().showLoadingIndicator();
            d.add(apiHelper.getSuggestedExercisec(mToken, mID)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(responseBodyResponse -> {
                        getViewState().hideLoadingIndicator();
                        if (responseBodyResponse.isSuccessful()){
                            Log.d("Exercice", "mode: Suggested");
                            List<ExerciseModel> models = new ArrayList<>();
                            HashSet<String> category = new HashSet<>();
                            for (Exercise i: Objects.requireNonNull(responseBodyResponse.body()).getData().getExercises()) {
                                models.add(new ExerciseModel(i));
                                category.add(i.getCategory());
                            }
                            if (models.size() == 0)
                                getViewState().showContentNotFound();
                            else
                                getViewState().hideContentNotFound();

                            List<ExerciseModel> result = new LinkedList<>();

                            for (String i: category) {
                                ExerciseModel header = new ExerciseModel();
                                header.setType(ExerciseModel.TYPE_HEADER);
                                header.setCategory(i);
                                result.add(header);
                                for (ExerciseModel j: models) {
                                    if (j.getCategory().equals(i))
                                        result.add(j);
                                }
                            }
                            getViewState().loadExerciseModels(result);
                        }
                        else Log.d("Exercise", "onError:" + responseBodyResponse.errorBody().string());
                    }, throwable -> {
                        throwable.printStackTrace();
                        getViewState().hideLoadingIndicator();
                        getViewState().showToastyMessage("Error, try later");
                    }));
        }
    }

    private void onSuccess(Response<ResponseExercise> responseBodyResponse) {
        getViewState().hideLoadingIndicator();
        if (responseBodyResponse.isSuccessful()) {
            Log.d("Exercise", "mode: All");
            List<ExerciseModel> models = new ArrayList<>();
            HashSet<String> category = new HashSet<>();
            for (Exercise i: Objects.requireNonNull(responseBodyResponse.body()).getData().getExercises()) {
                models.add(new ExerciseModel(i));
                category.add(i.getCategory());
            }
            if (models.size() == 0)
                getViewState().showContentNotFound();
            else  getViewState().hideContentNotFound();
            List<ExerciseModel> result = new LinkedList<>();

            for (String i: category) {
                ExerciseModel header = new ExerciseModel();
                header.setType(ExerciseModel.TYPE_HEADER);
                header.setCategory(i);
                result.add(header);
                for (ExerciseModel j: models) {
                    if (j.getCategory().equals(i))
                        result.add(j);
                }
            }
            getViewState().loadExerciseModels(result);
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

    public void onRefresh() {
        provideData();
    }
}
