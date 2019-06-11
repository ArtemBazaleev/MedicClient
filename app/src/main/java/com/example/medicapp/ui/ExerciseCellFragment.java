package com.example.medicapp.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.example.medicapp.model.ExerciseModel;
import com.example.medicapp.IOnLoadMore;
import com.example.medicapp.R;
import com.example.medicapp.adapters.ExerciseAdapter;
import com.example.medicapp.presentation.presenter.ExerciseCellFragmentPresenter;
import com.example.medicapp.presentation.view.IExerciseCellFragment;

import java.util.List;
import java.util.Objects;

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
    private int mMode;

    private ExerciseAdapter adapter;
    private RecyclerView recyclerView;
    boolean isLoading = false;

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
        recyclerView = v.findViewById(R.id.recycler_exercise_cell);
        presenter.setMode(this.mMode);
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
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void addExerciseModels(List<ExerciseModel> data) {
        adapter.addData(data);
    }

    @Override
    public void startVideoViewActivity() {
        Objects.requireNonNull(getActivity()).startActivity(new Intent(getContext(), VideoViewActivity.class));
    }
    //MVP
}
