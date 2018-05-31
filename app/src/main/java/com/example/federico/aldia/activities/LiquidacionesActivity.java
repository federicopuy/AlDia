package com.example.federico.aldia.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.federico.aldia.adapters.LiquidacionAdapter;
import com.example.federico.aldia.R;
import com.example.federico.aldia.model.AllLiquidaciones;
import com.example.federico.aldia.utils.Constantes;
import com.example.federico.aldia.model.Liquidacion;
import com.example.federico.aldia.network.APIInterface;
import com.example.federico.aldia.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LiquidacionesActivity extends AppCompatActivity implements LiquidacionAdapter.ListItemClickListener {


    private static final String TAG = "Liquidaciones Activity";
    private LiquidacionAdapter mAdapter;
    RecyclerView mRecyclerView;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liquidaciones);

        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        mRecyclerView = findViewById(R.id.liquidaciones_recycler_view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);

        obtenerLiquidaciones();

    }

    /*-------------------------------------- Llamada para Obtener Liquidaciones --------------------------------------------***/

    private void obtenerLiquidaciones() {

        final String nombreLlamada = "callGetLiquidaciones";

        long comercioId = prefs.getLong(Constantes.KEY_COMERCIO_ID, 0);

        APIInterface mService = RetrofitClient.getClient(getApplicationContext()).create(APIInterface.class);

        Call<AllLiquidaciones> callGetLiquidaciones = mService.getAllLiquidaciones(comercioId);

        callGetLiquidaciones.enqueue(new Callback<AllLiquidaciones>() {
            @Override
            public void onResponse(Call<AllLiquidaciones> call, Response<AllLiquidaciones> response) {

                Log.i(TAG, getString(R.string.on_response) + nombreLlamada);

                if (response.isSuccessful()) {

                    Log.i(TAG, getString(R.string.is_successful) + nombreLlamada);

                    try {

                        AllLiquidaciones allLiquidaciones = response.body();

                        List<Liquidacion> listaLiquidaciones = allLiquidaciones.getLiquidacion();

                        mAdapter = new LiquidacionAdapter(listaLiquidaciones, LiquidacionesActivity.this, LiquidacionesActivity.this);

                        mRecyclerView.setAdapter(mAdapter);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {

                    Log.i(TAG, getString(R.string.is_not_successful) + nombreLlamada);

                }

            }

            @Override
            public void onFailure(Call<AllLiquidaciones> call, Throwable t) {

                Log.i(TAG, getString(R.string.on_failure) + nombreLlamada);

            }
        });

    }

    /*-------------------------------------- OnListItemClick --------------------------------------------***/

    @Override
    public void onListItemClick(int clickedItemIndex, Liquidacion liquidacionClickeada) {

        Intent pasarAListaPeriodos = new Intent(LiquidacionesActivity.this, PeriodosActivity.class);

        pasarAListaPeriodos.putExtra(Constantes.KEY_INTENT_LIQUIDACION_PERIODO, liquidacionClickeada.getId());

        startActivity(pasarAListaPeriodos);

    }
}
