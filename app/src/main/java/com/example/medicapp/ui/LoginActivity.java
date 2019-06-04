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
import com.example.medicapp.R;
import com.example.medicapp.model.SecuredSharedPreferences;
import com.example.medicapp.presentation.presenter.LoginPresenter;
import com.example.medicapp.presentation.view.ILoginView;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends MvpAppCompatActivity implements ILoginView {
    @BindView(R.id.registration_btn_login) Button regBtn;
    @BindView(R.id.login_act_enter) ImageButton enter;
    @BindView(R.id.login) EditText login;
    @BindView(R.id.editText3) EditText password;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.forgot_password)
    TextView forgotPassword;

    @InjectPresenter
     LoginPresenter presenter;
    private AlertDialog dialog;

    @ProvidePresenter
    LoginPresenter providePresenter(){
        return new LoginPresenter(new SecuredSharedPreferences(getApplicationContext()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);
        ButterKnife.bind(this);
        presenter.onCreate();
        regBtn.setOnClickListener(l->presenter.onRegistrationClicked());
        enter.setOnClickListener(l->presenter.onBtnLoginClicked());
        forgotPassword.setOnClickListener(l->presenter.onForgotPassportClicked());
        login.addTextChangedListener(new TextWatcher() {
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

        password.addTextChangedListener(new TextWatcher() {
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

    }

    @Override
    public void startMainActivity(String token, String id) {
        App app =  (App)getApplication();
        app.setmToken(token);
        app.setmUserID(id);
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void showToastyMessage(String message) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startRegistrationActivity() {
        Intent i = new Intent(this, RegistrationActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    @Override
    public void setEnabledLoginBtn(boolean enabled) {
        this.enter.setEnabled(enabled);
    }

    @Override
    public void showLoadingIndicator() {
        progressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void hideLoadingIndicator() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showRestorePasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = getLayoutInflater().inflate(R.layout.custom_dialog_restore_account,null);
        Button b = v.findViewById(R.id.sendSmsBtn);
        EditText editText = v.findViewById(R.id.login_phone);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.onRestoreSmsChanged(s.toString());
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
    public void hideRestorePasswordDialog() {
        dialog.hide();
    }
}
