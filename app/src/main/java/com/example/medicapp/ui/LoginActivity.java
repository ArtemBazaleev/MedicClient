package com.example.medicapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.example.medicapp.App;
import com.example.medicapp.R;
import com.example.medicapp.model.SecuredSharedPreferences;
import com.example.medicapp.presentation.presenter.LoginPresenter;
import com.example.medicapp.presentation.view.ILoginView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends MvpAppCompatActivity implements ILoginView {
    @BindView(R.id.registration_btn_login) Button regBtn;
    @BindView(R.id.login_act_enter) ImageButton enter;
    @BindView(R.id.login) EditText login;
    @BindView(R.id.editText3) EditText password;

    @InjectPresenter
     LoginPresenter presenter;

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
    public void startMainActivity(String token) {
        App app =  (App)getApplication();
        app.setmToken(token);
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
}
