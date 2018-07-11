package com.example.federico.aldia.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.example.federico.aldia.adapters.PeriodoAdapter;
import com.example.federico.aldia.R;
import com.example.federico.aldia.utils.Constantes;
import com.example.federico.aldia.model.Periodo;
import com.example.federico.aldia.network.APIInterface;
import com.example.federico.aldia.network.RetrofitClient;
import com.example.federico.aldia.network.URLs;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PeriodosActivity extends AppCompatActivity {

    private static final String TAG = "Periodos Activity";
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_periodos);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mRecyclerView = findViewById(R.id.periodos_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        Intent vieneDeIntent = getIntent();
        long id;
        String tipoBusqueda = "";

        if (vieneDeIntent.hasExtra(Constantes.KEY_INTENT_LIQUIDACION_PERIODO)){
            id = vieneDeIntent.getLongExtra(Constantes.KEY_INTENT_LIQUIDACION_PERIODO, 0); //busca por id de liquidacion
            tipoBusqueda = URLs.SEARCH_METHOD_BY_LIQUIDACION;

        } else {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            id = prefs.getLong(Constantes.KEY_COMERCIO_ID, 0); //buscar por id de comercio
            tipoBusqueda = URLs.SEARCH_METHOD_LAST_LIQUIDACION;
        }

        obtenerPeriodos(tipoBusqueda, id);

    }

    private void obtenerPeriodos(String tipoBusqueda, long id) {

        final String nombreLlamada = "getPeriodos";
        APIInterface mService = RetrofitClient.getClient(getApplicationContext()).create(APIInterface.class);
        Call<List<Periodo>> callGetPeriodos = mService.getPeriodos(tipoBusqueda, id);
        callGetPeriodos.enqueue(new Callback<List<Periodo>>() {
            @Override
            public void onResponse(Call<List<Periodo>> call, Response<List<Periodo>> response) {
                Log.i(TAG, getString(R.string.on_response) + nombreLlamada);

                if (response.isSuccessful()) {
                    Log.i(TAG, getString(R.string.is_successful) + nombreLlamada);
                    try {
                        List<Periodo> listaPeriodos = response.body();
                        PeriodoAdapter mAdapter = new PeriodoAdapter(listaPeriodos, PeriodosActivity.this);
                        mRecyclerView.setAdapter(mAdapter);
                        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                                DividerItemDecoration.VERTICAL);
                        mRecyclerView.addItemDecoration(dividerItemDecoration);
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                } else {

                    Log.i(TAG, getString(R.string.is_not_successful) + nombreLlamada);
                    try {
                        Log.e(TAG, response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Periodo>> call, Throwable t) {
                Log.i(TAG, getString(R.string.on_failure) + nombreLlamada);
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return(super.onOptionsItemSelected(item));
    }
}
