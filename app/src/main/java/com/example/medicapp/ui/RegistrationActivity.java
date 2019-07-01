package com.example.medicapp.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.example.medicapp.App;
import com.example.medicapp.ITimerSms;
import com.example.medicapp.R;
import com.example.medicapp.presentation.presenter.RegistrationPresenter;
import com.example.medicapp.presentation.view.IRegistrationView;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegistrationActivity extends MvpAppCompatActivity
        implements IRegistrationView {
    @BindView(R.id.register_act_reg) ImageButton reg;
    @BindView(R.id.login_btn_reg) Button login;


    @BindView(R.id.login) EditText loginEdit;
    @BindView(R.id.editText3) EditText passwordEdit;
    @BindView(R.id.confirm_password_reg) EditText confirmPassword;
    @BindView(R.id.progressBar_registration) ProgressBar progressBar;

    private ProgressBar progressBarDialog;

    @InjectPresenter
    RegistrationPresenter presenter;
    private AlertDialog dialog;
    private TextView moreSms;

    @ProvidePresenter
    RegistrationPresenter providePresenter(){
        return new RegistrationPresenter((App) getApplicationContext());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);
        reg.setOnClickListener(v -> presenter.onRegisterClicked());
        login.setOnClickListener(v -> presenter.onLoginClicked());
        init();
    }

    private void init(){
        loginEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.onLoginChanged(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        passwordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.onPasswordChanged(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.onConfirmPasswordChanged(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void showToastyMessage(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startLoginActivity() {
        Intent i = new Intent(this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    @Override
    public void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = getLayoutInflater().inflate(R.layout.custom_dialog,null);
        EditText sms = v.findViewById(R.id.sms_code);
        progressBarDialog = v.findViewById(R.id.progressBar_custom_dialog);
        moreSms = v.findViewById(R.id.more_sms_btn);
        moreSms.setOnClickListener(l->presenter.moreSms());
        sms.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.onSmsCode(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        builder.setView(v);
        dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    @Override
    public void showLoadingIndicator() {
        progressBar.setVisibility(View.VISIBLE);
        reg.setImageResource(0);
    }

    @Override
    public void hideLoadingIndicator() {
        progressBar.setVisibility(View.GONE);
        reg.setImageResource(R.drawable.ic_arrow_forward_black_24dp);
    }

    @Override
    public void setEnabledSubmitBtn(boolean enabled) {
        reg.setEnabled(enabled);
    }

    @Override
    public void showProgressDialogWindow() {
        progressBarDialog.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressDialogWindow() {
        progressBarDialog.setVisibility(View.GONE);
    }

    @Override
    public void setMoreSmsText(String text) {
        moreSms.setText(text);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (dialog!=null)
            dialog.dismiss();
    }
}
