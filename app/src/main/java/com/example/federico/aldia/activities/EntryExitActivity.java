package com.example.federico.aldia.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.federico.aldia.R;
import com.example.federico.aldia.model.Periodo;
import com.example.federico.aldia.utils.Constants;
import com.example.federico.aldia.utils.Utils;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EntryExitActivity extends AppCompatActivity {

    private static final String TAG = "Entry Exit Activity";
    @BindView(R.id.textViewIngresoEgreso)
    TextView textViewIngresoEgreso;
    @BindView(R.id.fabAceptarIngresoEgreso)
    FloatingActionButton fabAceptarIngresoEgreso;
    @BindView(R.id.tvNombreComercio)
    TextView tvNombreComercio;
    @BindView(R.id.tvHoraIngreso)
    TextView tvHoraIngreso;
    @BindView(R.id.tvHoraEgreso)
    TextView tvHoraEgreso;
    @BindView(R.id.tvCategoria)
    TextView tvCategoria;
    @BindView(R.id.horaEgresoTv)
    TextView horaEgresoTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso_egreso);
        ButterKnife.bind(this);
        Periodo shift = getShiftFromIntent();

        if (shift == null) {
            Log.e(TAG, "Error when retrieving shift from intent");
            Intent returnIntent = getIntent();
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        } else {
            //check if employee is exiting or entering
            if (shift.getHoraFin() != null) {
                //employee is exiting
                View appBar = findViewById(R.id.appBarIngresoEgreso);
                appBar.setBackground(ContextCompat.getDrawable(EntryExitActivity.this, R.drawable.salida_background));
                textViewIngresoEgreso.setText(R.string.salida);
                tvHoraEgreso.setVisibility(View.VISIBLE);
                tvHoraEgreso.setText(Utils.obtenerHora(shift.getHoraFin()));
                horaEgresoTv.setVisibility(View.VISIBLE);
            } else {
                //employee is entering
                textViewIngresoEgreso.setText(R.string.Ingreso);
            }
            tvCategoria.setText(shift.getCategoria().getNombre());
            tvNombreComercio.setText(shift.getUser());
            tvHoraIngreso.setText(Utils.obtenerHora(shift.getHoraInicio()));
        }
    }

    private Periodo getShiftFromIntent() {
        Intent intent = getIntent();
        Periodo shiftFromIntent = null;
        if (intent.hasExtra(Constants.KEY_INTENT_PERIODO_INGRESO_EGRESO)) {
            String JsonObject = intent.getStringExtra(Constants.KEY_INTENT_PERIODO_INGRESO_EGRESO);
            Gson gson = new Gson();
            shiftFromIntent = gson.fromJson(JsonObject, Periodo.class);
        }
        return shiftFromIntent;
    }

    @OnClick(R.id.fabAceptarIngresoEgreso)
    public void backToMainActivity() {
        Intent returnIntent = getIntent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
