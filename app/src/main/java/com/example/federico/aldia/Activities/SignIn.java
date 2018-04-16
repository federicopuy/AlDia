package com.example.federico.aldia.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.example.federico.aldia.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignIn extends AppCompatActivity {

    @BindView(R.id.btIniciarSesion)
    Button btIniciarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
    }

@OnClick(R.id.btIniciarSesion)
    public void iniciarSesion(){

    Intent pasarAMainActivity = new Intent(SignIn.this, MainActivity.class);
    startActivity(pasarAMainActivity);

}

}
