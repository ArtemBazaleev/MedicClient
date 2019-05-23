package com.example.medicapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.medicapp.R;
import com.example.medicapp.presentation.presenter.RegistrationPresenter;
import com.example.medicapp.presentation.view.IRegistrationView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegistrationActivity extends MvpAppCompatActivity implements IRegistrationView {
    @BindView(R.id.register_act_reg)
    ImageButton reg;
    @BindView(R.id.register_act_login)
    TextView login;

    @InjectPresenter
    RegistrationPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);
        reg.setOnClickListener(v -> presenter.onRegisterClicked());
        login.setOnClickListener(v -> presenter.onLoginClicked());
    }

    @Override
    public void showToastyMessage(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startLoginActivity() {
        startActivity(new Intent(this, LoginActivity.class));
    }
}
