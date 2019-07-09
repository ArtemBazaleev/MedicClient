package com.example.medicapp.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.medicapp.IOnLoadMore;
import com.example.medicapp.R;
import com.example.medicapp.adapters.ExercisePagerAdapter;
import com.example.medicapp.presentation.presenter.ExercisePresenter;
import com.example.medicapp.presentation.view.IExerciseView;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ExerciseFragment extends MvpAppCompatFragment implements IOnLoadMore, IExerciseView {

    public ExerciseFragment() {
        // Required empty public constructor
    }
    @BindView(R.id.tab_layout) TabLayout tabLayout;
    @BindView(R.id.viewPager) ViewPager viewPager;
    @BindView(R.id.buttonAdvice) Button advicesBtn;


    @InjectPresenter
    ExercisePresenter presenter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_exercise, container, false);
        ButterKnife.bind(this,v);
        init();
        return v;
    }
    private void init(){
        ExercisePagerAdapter adapter = new ExercisePagerAdapter(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        advicesBtn.setOnClickListener(l->{
            Intent i = new Intent(getContext(), AdvicesActivity.class);
            startActivity(i);
        });
    }


    @Override
    public void loadMore(int currentCount) {

    }

    @Override
    public void showToastyMessage(String message) {
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
    }
}
