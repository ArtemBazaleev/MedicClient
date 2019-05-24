package com.example.medicapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.medicapp.R;
import com.example.medicapp.model.ResultModel;
import com.example.medicapp.adapters.ResultsAdapter;
import com.example.medicapp.presentation.presenter.ResultsFragmentPresenter;
import com.example.medicapp.presentation.view.IResultsFragmentView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ResultsFragment extends MvpAppCompatFragment
        implements ResultsAdapter.IOnResultClicked, IResultsFragmentView {

    @BindView(R.id.recycler_results) RecyclerView recyclerView;

    @InjectPresenter
    ResultsFragmentPresenter presenter;

    public ResultsFragment() { }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_results, container, false);
        ButterKnife.bind(this,v);
        presenter.onCreateView();
        return v;
    }

    @Override
    public void onResult(ResultModel model) {
        presenter.onResultClicked(model);
    }

    //MVP
    @Override
    public void showTastyMessage(String message) {
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadResults(List<ResultModel> data) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,false));
        ResultsAdapter adapter = new ResultsAdapter(getContext(), data);
        adapter.setmListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void startActivityResultView(ResultModel resultModel) {
        startActivity(new Intent(getContext(),ResultViewActivity.class));
    }
    //MVP
}
