package com.example.federico.aldia.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.federico.aldia.adapters.LiquidacionAdapter;
import com.example.federico.aldia.R;
import com.example.federico.aldia.model.AllLiquidaciones;
import com.example.federico.aldia.utils.Constantes;
import com.example.federico.aldia.model.Liquidacion;
import com.example.federico.aldia.network.APIInterface;
import com.example.federico.aldia.network.RetrofitClient;
import com.example.federico.aldia.utils.PaginationScrollListener;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LiquidacionesActivity extends AppCompatActivity implements LiquidacionAdapter.ListItemClickListener {


    private static final String TAG = "Liquidaciones Activity";
    private LiquidacionAdapter mAdapter;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.tvSinLiquidaciones)
    TextView tvSinLiquidaciones;
    int size = 15;

    int pageNumber = 0;
    boolean isLastPage = false;
    private boolean isLoading = false;

    RecyclerView.LayoutManager mLayoutManager;
    APIInterface mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liquidaciones);
        ButterKnife.bind(this);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        RecyclerView mRecyclerView = findViewById(R.id.liquidaciones_recycler_view);

        mAdapter = new LiquidacionAdapter(LiquidacionesActivity.this, this);
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addOnScrollListener(new PaginationScrollListener((LinearLayoutManager) mLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                pageNumber += 1;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage();
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return 0;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        mService = RetrofitClient.getClient(getApplicationContext()).create(APIInterface.class);
        loadFirstPage();
    }

    /*-------------------------------------- Llamada para Obtener Liquidaciones --------------------------------------------***/

    private void loadFirstPage() {
        final String nombreLlamada = "callGetLiquidaciones";
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        long comercioId = prefs.getLong(Constantes.KEY_COMERCIO_ID, 0);
        progressBar.setVisibility(View.VISIBLE);

        Call<AllLiquidaciones> callGetLiquidaciones = mService.getAllLiquidaciones(comercioId, pageNumber, size);

        callGetLiquidaciones.enqueue(new Callback<AllLiquidaciones>() {

            @Override
            public void onResponse(Call<AllLiquidaciones> call, Response<AllLiquidaciones> response) {
                progressBar.setVisibility(View.INVISIBLE);

                Log.i(TAG, getString(R.string.on_response) + nombreLlamada);
                if (response.isSuccessful()) {
                    Log.i(TAG, getString(R.string.is_successful) + nombreLlamada);

                    AllLiquidaciones allLiquidaciones = response.body();
                    List<Liquidacion> listaLiquidaciones = allLiquidaciones.getLiquidacion();
                    mAdapter.addItems(listaLiquidaciones);

                    if (!(pageNumber <= allLiquidaciones.getTotalPages())) {
                        isLastPage = true;
                    }

                } else {
                    Log.i(TAG, getString(R.string.is_not_successful) + nombreLlamada);
                }
            }

            @Override
            public void onFailure(Call<AllLiquidaciones> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Log.i(TAG, getString(R.string.on_failure) + nombreLlamada);
            }
        });
    }

    private void loadNextPage() {

        final String nombreLlamada = "callGetLiquidaciones";
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        long comercioId = prefs.getLong(Constantes.KEY_COMERCIO_ID, 0);
        progressBar.setVisibility(View.VISIBLE);

        Call<AllLiquidaciones> callGetLiquidaciones = mService.getAllLiquidaciones(comercioId, pageNumber, size);
        callGetLiquidaciones.enqueue(new Callback<AllLiquidaciones>() {
            @Override
            public void onResponse(Call<AllLiquidaciones> call, Response<AllLiquidaciones> response) {
                progressBar.setVisibility(View.INVISIBLE);
                if (response.isSuccessful()) {

                    Log.i(TAG, getString(R.string.on_response) + nombreLlamada);
                    isLoading = false;

                    AllLiquidaciones allLiquidaciones = response.body();
                    List<Liquidacion> listaLiquidaciones = allLiquidaciones.getLiquidacion();
                    mAdapter.addItems(listaLiquidaciones);

                    if (pageNumber == allLiquidaciones.getTotalPages()) {
                        isLastPage = true;
                    }
                } else {
                    Log.i(TAG, getString(R.string.is_not_successful) + nombreLlamada);
                }
            }

            @Override
            public void onFailure(Call<AllLiquidaciones> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
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
