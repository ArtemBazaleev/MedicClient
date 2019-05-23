package com.example.medicapp.ui;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.medicapp.IOnLoadMore;
import com.example.medicapp.R;
import com.example.medicapp.adapters.ExercisePagerAdapter;
import com.example.medicapp.presentation.presenter.ExercisePresenter;
import com.example.medicapp.presentation.view.IExerciseView;

import java.util.Objects;


public class ExerciseFragment extends MvpAppCompatFragment implements IOnLoadMore, IExerciseView {

    public ExerciseFragment() {
        // Required empty public constructor
    }

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ExercisePagerAdapter adapter;
    private TabItem tabAll;
    private TabItem tabSuggeted;

    @InjectPresenter
    ExercisePresenter presenter;



//    private ExerciseAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_exercise, container, false);
        tabLayout = v.findViewById(R.id.tab_layout);
        viewPager = v.findViewById(R.id.viewPager);
        tabAll = v.findViewById(R.id.all_exercises);
        tabSuggeted = v.findViewById(R.id.suggested_exercises);
        init();
    //        RecyclerView r = v.findViewById(R.id.recycler_exercise);
//        r.setLayoutManager(new LinearLayoutManager(getContext(),
//                LinearLayoutManager.VERTICAL,false));
//        List<ExerciseModel> models = new ArrayList<>();
//        for (int i = 0; i<10; i++)
//            models.add(new ExerciseModel());
//        ExerciseModel model = new ExerciseModel();
//        model.setLoadingIndicator(true);
//        models.add(model);
//        adapter = new ExerciseAdapter(getContext(),models,this);
//        r.setAdapter(adapter);
//        r.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//                int a = linearLayoutManager.findLastCompletelyVisibleItemPosition();
//                if (!isLoading) {
//                    if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == adapter.getItemCount() - 1) {
//                        //bottom of list!
//                        isLoading = true;
//                        List<ExerciseModel> list = new ArrayList<>();
//                        for (int i = 0; i<10; i++)
//                            list.add(new ExerciseModel());
//                        isLoading = false;
//                        adapter.addData(list);
//                    }
//                }
//
//            }
//        });
        return v;
    }
    private void init(){
        adapter = new ExercisePagerAdapter(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), tabLayout.getTabCount());
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
    }


    @Override
    public void loadMore(int currentCount) {

    }

    @Override
    public void showToastyMessage(String message) {
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
    }
}
