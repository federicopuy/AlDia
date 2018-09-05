package com.example.federico.aldiaapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.federico.aldiaapp.R;
import com.example.federico.aldiaapp.model.Shift;
import com.example.federico.aldiaapp.utils.Constants;
import com.example.federico.aldiaapp.utils.Utils;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EntryExitActivity extends AppCompatActivity {

    private static final String TAG = "Entry Exit Activity";
    @BindView(R.id.tvEntryExit)
    TextView tvEntryExit;
    @BindView(R.id.tvBusinessName)
    TextView tvBusinessName;
    @BindView(R.id.tvEntryHour)
    TextView tvEntryHour;
    @BindView(R.id.tvExitHour)
    TextView tvExitHour;
    @BindView(R.id.tvCategory)
    TextView tvCategory;
    @BindView(R.id.exitHourTv)
    TextView exitHourTv;
    @BindView(R.id.appBarEntryExit)
    View appBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_exit);
        ButterKnife.bind(this);
        Shift shift = getShiftFromIntent();

        if (shift == null) {
            Log.e(TAG, "Error when retrieving shift from intent");
            Intent returnIntent = getIntent();
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        } else {
            //check if employee is exiting or entering
            if (shift.getHoraFin() != null) {
                //employee is exiting
                appBar.setBackground(ContextCompat.getDrawable(EntryExitActivity.this, R.drawable.salida_background));
                tvEntryExit.setText(R.string.salida);
                tvExitHour.setVisibility(View.VISIBLE);
                tvExitHour.setText(Utils.getHour(shift.getHoraFin()));
                exitHourTv.setVisibility(View.VISIBLE);
            } else {
                //employee is entering
                tvEntryExit.setText(R.string.Ingreso);
            }
            tvCategory.setText(shift.getPosition().getNombre());
            tvBusinessName.setText(shift.getUser());
            tvEntryHour.setText(Utils.getHour(shift.getHoraInicio()));
        }
    }

    private Shift getShiftFromIntent() {
        Intent intent = getIntent();
        Shift shiftFromIntent = null;
        if (intent.hasExtra(Constants.KEY_INTENT_SHIFT_ENTRY_EXIT)) {
            String JsonObject = intent.getStringExtra(Constants.KEY_INTENT_SHIFT_ENTRY_EXIT);
            Gson gson = new Gson();
            shiftFromIntent = gson.fromJson(JsonObject, Shift.class);
        }
        return shiftFromIntent;
    }

    @OnClick(R.id.fabAccept)
    public void backToMainActivity() {
        Intent returnIntent = getIntent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
