package com.example.medicapp.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.example.medicapp.App;
import com.example.medicapp.model.ExerciseModel;
import com.example.medicapp.IOnLoadMore;
import com.example.medicapp.R;
import com.example.medicapp.adapters.ExerciseAdapter;
import com.example.medicapp.presentation.presenter.ExerciseCellFragmentPresenter;
import com.example.medicapp.presentation.view.IExerciseCellFragment;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExerciseCellFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExerciseCellFragment extends MvpAppCompatFragment
        implements IOnLoadMore, IExerciseCellFragment {

    private static final String ARG_MODE = "param1";
    public static final int MODE_ALL  = 0;
    public static  final int MODE_SUGGESTED = 1;
    public static final int MODE_ADVICE = 2;
    private int mMode;

    private ExerciseAdapter adapter;
    private RecyclerView recyclerView;
    boolean isLoading = false;
    @BindView(R.id.progressBar3) ProgressBar progressBar;
    @BindView(R.id.not_found_content_exercise) ConstraintLayout contentNotFound;
    @BindView(R.id.layoutRefreshExercise) SwipeRefreshLayout refreshLayout;

    @InjectPresenter
    ExerciseCellFragmentPresenter presenter;

    @ProvidePresenter
    ExerciseCellFragmentPresenter providePresenter(){
        App app = (App) Objects.requireNonNull(getActivity()).getApplicationContext();
        return new ExerciseCellFragmentPresenter(
                app.getmToken(),
                app.getmUserID()
        );
    }

    public ExerciseCellFragment() {
    }


    public static ExerciseCellFragment newInstance(int param) {
        ExerciseCellFragment fragment = new ExerciseCellFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MODE, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            mMode = getArguments().getInt(ARG_MODE);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_exercise_cell, container, false);
        ButterKnife.bind(this, v);
        recyclerView = v.findViewById(R.id.recycler_exercise_cell);
        presenter.setMode(this.mMode);
        refreshLayout.setOnRefreshListener(presenter::onRefresh);
        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.onViewCreated();
    }

    @Override
    public void loadMore(int currentCount) {
        presenter.onItemClicked(new ExerciseModel());
    }
    //MVP
    @Override
    public void showToastyMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadExerciseModels(List<ExerciseModel> data) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,false));
        adapter = new ExerciseAdapter(getContext(), data,this);
        adapter.setClickedListener(model -> presenter.onItemClicked(model));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void addExerciseModels(List<ExerciseModel> data) {
        adapter.addData(data);
    }

    @Override
    public void startVideoViewActivity(String url) {
        startActivity(PlayerActivity.getVideoPlayerIntent(Objects.requireNonNull(getContext()),
                url,
                "", R.drawable.ic_play_arrow_black_24dp));
    }

    @Override
    public void showLoadingIndicator() {
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoadingIndicator() {
        refreshLayout.setRefreshing(false);
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
    public void startLoginActivityAndClearStack() {
        Intent i = new Intent(getContext(), LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
    //MVP
}
