package com.example.medicapp.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.bumptech.glide.Glide;
import com.example.medicapp.R;
import com.example.medicapp.model.ProfileModel;
import com.example.medicapp.presentation.presenter.ProfileFragmentPresenter;
import com.example.medicapp.presentation.view.IProfileFragmentView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends MvpAppCompatFragment implements IProfileFragmentView {

    public static final int GALLERY_REQUEST_CODE = 64897;

    @BindView(R.id.profile_name) EditText nameTxt;
    @BindView(R.id.profile_surname) EditText surname;
    @BindView(R.id.profile_image) CircleImageView profileImage;
    @BindView(R.id.profile_age_edittext) EditText ageEditText;
    @BindView(R.id.profile_weight_edittext) EditText weightEditText;
    @BindView(R.id.profile_height_edittext) EditText heightEditText;
    @BindView(R.id.profile_save_btn) Button submit;
    @BindView(R.id.profile_radiogroup_sex) RadioGroup radioGroupSex;
    @BindView(R.id.profile_radiogroup_lazy) RadioGroup radioGroupLazy;
    @BindView(R.id.profile_radiogroup_sport) RadioGroup radioGroupSport;



    @InjectPresenter
    ProfileFragmentPresenter presenter;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this,v);
        init();
        return v;
    }

    private void init(){
        nameTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.setName(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        surname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.setSurname(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        ageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()==0)
                    presenter.setAge(0);
                else presenter.setAge(Integer.parseInt(s.toString()));
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        weightEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()==0)
                    presenter.setWeight(0.0);
                else presenter.setWeight(Double.parseDouble(s.toString()));
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        heightEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()==0)
                    presenter.setHeight(0.0);
                else presenter.setHeight(Double.parseDouble(s.toString()));
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        submit.setOnClickListener(l->presenter.onSubmitBtnClicked());
        radioGroupLazy.setOnCheckedChangeListener(radioGroupListener);
        radioGroupSex.setOnCheckedChangeListener(radioGroupListener);
        radioGroupSport.setOnCheckedChangeListener(radioGroupListener);
        profileImage.setOnClickListener(l->presenter.onPhotoClicked());
    }
//MVP
    @Override
    public void showToastyMessage(String message) {
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setProfileData(ProfileModel model) {
        nameTxt.setText(model.getName());
        surname.setText(model.getSurname());
        ageEditText.setText(String.valueOf(model.getAge()));
        weightEditText.setText(String.valueOf(model.getWeight()));
        heightEditText.setText(String.valueOf(model.getHeight()));
    }

    @Override
    public void chosePhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    @Override
    public void setProfilePhoto(Uri uri) {
        Glide.with(this)
                .load(uri)
                .into(profileImage);
    }

    @Override
    public void setEnabledSubmitBtn(boolean enabled) {
        submit.setEnabled(enabled);
    }

    //MVP
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode){
                case GALLERY_REQUEST_CODE:
                    Uri selectedImage = data.getData();
                    presenter.onPhotoChosen(selectedImage);
            }
    }

    RadioGroup.OnCheckedChangeListener radioGroupListener = (group, checkedId) -> {
            switch (checkedId) {
                case R.id.radio_male: presenter.setMale(true);
                    break;
                case R.id.radio_feamale: presenter.setMale(false);
                    break;
                case R.id.radio_do_sport: presenter.setDoSport(true);
                    break;
                case R.id.radio_dont_do_sport: presenter.setDoSport(false);
                    break;
                case R.id.radio_seat_job: presenter.setLazyJob(true);
                    break;
                case R.id.radio_dont_seat_job: presenter.setLazyJob(false);
                    break;
                default:
                    break;
            }
    };
}
