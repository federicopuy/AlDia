package com.example.federico.aldia.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.federico.aldia.R;
import com.example.federico.aldia.model.Constantes;
import com.example.federico.aldia.model.Periodo;
import com.example.federico.aldia.utils.Utils;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IngresoEgreso extends AppCompatActivity {



    @BindView(R.id.fabAceptarIngresoEgreso)
    FloatingActionButton fabAceptarIngresoEgreso;

    @BindView(R.id.tvIngresoEgreso)
    TextView tvIngresoEgreso;

    @BindView(R.id.tvNombreComercio)
    TextView tvNombreComercio;

    @BindView(R.id.tvHoraIngreso)
    TextView tvHoraIngreso;

    @BindView(R.id.tvHoraEgreso)
    TextView tvHoraEgreso;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso_egreso);
        ButterKnife.bind(this);

        Periodo periodo = obtenerPeriodoDeIntent();

//todo try catch

        if (periodo==null){

            //todo a main activity con cancel

        }
        if (periodo.getHoraFin()!=null){

            tvIngresoEgreso.setText(R.string.egreso);

            tvHoraEgreso.setVisibility(View.VISIBLE);

            tvHoraEgreso.setText(Utils.obtenerHora(periodo.getHoraFin()));

        } else {

            tvIngresoEgreso.setText(R.string.Ingreso);

        }

        tvNombreComercio.setText(periodo.getUser());

        tvHoraIngreso.setText(Utils.obtenerHora(periodo.getHoraInicio()));

    }

    private Periodo obtenerPeriodoDeIntent() {

        Intent intent = getIntent();

        Periodo periodo = null;

        if (intent.hasExtra(Constantes.KEY_INTENT_PERIODO_INGRESO_EGRESO)){

            String objetoJSON = intent.getStringExtra(Constantes.KEY_INTENT_PERIODO_INGRESO_EGRESO);

            Gson gson = new Gson();

             periodo = gson.fromJson(objetoJSON, Periodo.class);

        }

        return periodo;


    }

    @OnClick (R.id.fabAceptarIngresoEgreso)
    public void volverAMain(){

        Intent returnIntent = getIntent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();

    }


}
