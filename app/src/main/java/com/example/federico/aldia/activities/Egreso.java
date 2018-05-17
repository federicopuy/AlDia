package com.example.federico.aldia.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.example.federico.aldia.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Egreso extends AppCompatActivity {

    @BindView(R.id.btAceptar)
    Button btAceptar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_egreso);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.btAceptar)
    public void pasarAMain(){

        Intent pasarAMain = new Intent(Egreso.this, MainActivity.class);
        startActivity(pasarAMain);

    }
}
