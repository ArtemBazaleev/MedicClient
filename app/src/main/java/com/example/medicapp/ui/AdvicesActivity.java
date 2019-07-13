package com.example.medicapp.ui;

import android.support.constraint.ConstraintLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.example.medicapp.App;
import com.example.medicapp.R;
import com.example.medicapp.adapters.AdviceParentAdapter;
import com.example.medicapp.model.AdviceModel;
import com.example.medicapp.presentation.presenter.AdvicesActivityPresenter;
import com.example.medicapp.presentation.view.AdviceActivityView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdvicesActivity extends MvpAppCompatActivity implements AdviceActivityView {

    @BindView(R.id.recycler_advice) RecyclerView recyclerView;
    @BindView(R.id.not_found_content_exercise) ConstraintLayout contentNotFound;
    @BindView(R.id.progressBar6) ProgressBar progressBar;


    @ProvidePresenter
    AdvicesActivityPresenter providePresenter(){
        App app = (App) getApplication();
        return new AdvicesActivityPresenter(app.getmToken(), app.getmUserID());
    }

    @InjectPresenter
    AdvicesActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advices);
        ButterKnife.bind(this);
        presenter.onCreate();
    }

    @Override
    public void showToastyMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadData(List<AdviceModel> models) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new AdviceParentAdapter(this, models));
    }

    @Override
    public void showContentNotFound() {
        contentNotFound.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideContentNotFound() {
        contentNotFound.setVisibility(View.GONE);
    }

    @Override
    public void showLoadingIndicator() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingIndicator() {
        progressBar.setVisibility(View.GONE);
    }
}