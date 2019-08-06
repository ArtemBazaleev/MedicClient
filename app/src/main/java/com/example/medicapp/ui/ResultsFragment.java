package com.example.medicapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.example.medicapp.App;
import com.example.medicapp.R;
import com.example.medicapp.model.ResultModel;
import com.example.medicapp.adapters.ResultsAdapter;
import com.example.medicapp.presentation.presenter.ResultsFragmentPresenter;
import com.example.medicapp.presentation.view.IResultsFragmentView;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ResultsFragment extends MvpAppCompatFragment
        implements ResultsAdapter.IOnResultClicked, IResultsFragmentView {

    @BindView(R.id.recycler_results) RecyclerView recyclerView;
    @BindView(R.id.not_found_content_results) ConstraintLayout noContent;
    @BindView(R.id.results_swipe_container) SwipeRefreshLayout swipeRefreshLayout;


    @InjectPresenter
    ResultsFragmentPresenter presenter;
    @ProvidePresenter
    ResultsFragmentPresenter providePresenter(){
        App app = (App) Objects.requireNonNull(getActivity()).getApplicationContext();
        return new ResultsFragmentPresenter(app.getmToken(), app.getmUserID());
    }

    public ResultsFragment() { }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_results, container, false);
        ButterKnife.bind(this,v);
        presenter.onCreateView();
        swipeRefreshLayout.setOnRefreshListener(()->presenter.onRefresh());
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
        if (resultModel.getType() == ResultModel.TYPE_BACKBONE)
        {
            Intent i = new Intent(getContext(), BackBoneViewActivity.class);
            i.putExtra(BackBoneViewActivity.BACKBONE_ARRAY, resultModel.getBackBoneImage());
            startActivity(i);
        }
        else  {
            Intent i = new Intent(getContext(), ResultViewActivity.class);
            i.putExtra(ResultViewActivity.IMAGE_PARAM, resultModel.getUrl());
            startActivity(i);
        }
    }

    @Override
    public void showErrorNoResults() {
        noContent.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideErrorNoResults() {
        noContent.setVisibility(View.GONE);
    }

    @Override
    public void showRefreshing() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideRefreshing() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void startLoginActivityAndClearStack() {
        Intent i = new Intent(getContext(), LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    @Override
    public void startConclusionActivity(String text) {
        Intent i = new Intent(getContext(), ConclusionViewActivity.class);
        i.putExtra(ConclusionViewActivity.CONCLUSION_TEXT, text);
        startActivity(i);
    }
    //MVP
}
