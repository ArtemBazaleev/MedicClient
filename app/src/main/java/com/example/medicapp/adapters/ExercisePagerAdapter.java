package com.example.medicapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.medicapp.ui.ExerciseCellFragment;

public class ExercisePagerAdapter extends FragmentStatePagerAdapter {

    private int tabsCount;
    public ExercisePagerAdapter(FragmentManager fm, int count) {
        super(fm);
        tabsCount = count;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                return ExerciseCellFragment.newInstance(ExerciseCellFragment.MODE_SUGGESTED);
            case 1:
                return ExerciseCellFragment.newInstance(ExerciseCellFragment.MODE_ALL);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabsCount;
    }
}
