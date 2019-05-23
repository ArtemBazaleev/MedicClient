package com.example.medicapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.medicapp.R;
import com.example.medicapp.presentation.presenter.LoginPresenter;
import com.example.medicapp.presentation.view.ILoginView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends MvpAppCompatActivity implements ILoginView {
    @BindView(R.id.login_act_registration) TextView reg;
    @BindView(R.id.login_act_enter) ImageButton enter;

    @InjectPresenter
    LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);
        ButterKnife.bind(this);
        reg.setOnClickListener(l->presenter.onRegistrationClicked());
        enter.setOnClickListener(l->presenter.onBtnLoginClicked());
    }

    @Override
    public void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void showToastyMessage(String message) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startRegistrationActivity() {
        startActivity(new Intent(this, RegistrationActivity.class));
    }
}
