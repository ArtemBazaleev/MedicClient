package com.example.medicapp.presentation.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.medicapp.model.ResultModel;
import com.example.medicapp.presentation.view.IResultsFragmentView;

import java.util.ArrayList;
import java.util.List;

@InjectViewState
public class ResultsFragmentPresenter extends MvpPresenter<IResultsFragmentView> {

    public void onCreateView(){
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
