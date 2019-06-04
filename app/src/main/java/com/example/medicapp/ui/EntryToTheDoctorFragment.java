package com.example.medicapp.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.archit.calendardaterangepicker.customviews.DateRangeCalendarView;
import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.medicapp.model.EmptyDateModel;
import com.example.medicapp.R;
import com.example.medicapp.adapters.EmptyDateAdapter;
import com.example.medicapp.presentation.presenter.EntryToTheDoctorFragmentPresenter;
import com.example.medicapp.presentation.view.IEntryToTheDoctorFragmentView;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


public class EntryToTheDoctorFragment extends MvpAppCompatFragment
        implements EmptyDateAdapter.OnItemClicked, IEntryToTheDoctorFragmentView {

    @InjectPresenter
    EntryToTheDoctorFragmentPresenter presenter;

    @BindView(R.id.chose_date_btn_entry) Button choseDate;
    @BindView(R.id.recyclerView_empty_date) RecyclerView recyclerView;
    @BindView(R.id.submit_btn_entry) Button submitBtn;
    @BindView(R.id.editText3)
    EditText nameTxt;

    private boolean isShownDialog = false;

    private AlertDialog alertDialog;

    public EntryToTheDoctorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_entry_to_the_doctor, container, false);
        ButterKnife.bind(this, v);
        recyclerView.setVisibility(View.GONE);
        choseDate.setOnClickListener(l->presenter.onChoseDateClicked());
        submitBtn.setOnClickListener(l-> presenter.onSubmitBtnClicked());
        nameTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.onNameChanged(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return v;
    }

    //MVP
    @Override
    public void showToastyMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void loadAvailableDate(List<EmptyDateModel> emptyDateModel) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL,false));
        EmptyDateAdapter adapter = new EmptyDateAdapter(getContext(), emptyDateModel);
        adapter.setmListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.VISIBLE);
        choseDate.setBackgroundResource(R.drawable.calendar_chosen_bg);
    }

    @Override
    public void setDateText(String txt) {
        choseDate.setText(String.format("%s%s", getString(R.string.your_date), txt));
        choseDate.setTextColor(getResources().getColor(R.color.color_white));
    }

    @Override
    public void setActiveSubmitBtn(boolean active) {
        submitBtn.setEnabled(active);
    }

    @Override
    public void showDatePickerDialog() {
        if (isShownDialog)
            return;
        AlertDialog.Builder mBuild = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        View v = getLayoutInflater().inflate(R.layout.date_picker_dialog, null);
        DateRangeCalendarView dateRangeCalendarView = v.findViewById(R.id.calendar);
        dateRangeCalendarView.setCalendarListener(new DateRangeCalendarView.CalendarListener() {
            @Override
            public void onFirstDateSelected(Calendar startDate) {
                presenter.onDateSelected(startDate);
            }

            @Override
            public void onDateRangeSelected(Calendar startDate, Calendar endDate) {
                hideDatePickerDialog();
            }
        });
        mBuild.setView(v);
        mBuild.setOnDismissListener(dialog -> isShownDialog = false);
        alertDialog = mBuild.create();
        alertDialog.show();
        isShownDialog = true;
    }

    @Override
    public void hideDatePickerDialog() {
        if (alertDialog!=null){
            isShownDialog = false;
            alertDialog.hide();
        }
    }
//MVP
    @Override
    public void onItemClicked(EmptyDateModel model) {

    }
}