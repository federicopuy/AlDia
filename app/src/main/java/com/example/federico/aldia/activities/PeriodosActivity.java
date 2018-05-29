package com.example.federico.aldia.activities;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.federico.aldia.adapters.PeriodoAdapter;
import com.example.federico.aldia.R;
import com.example.federico.aldia.model.Constantes;
import com.example.federico.aldia.model.Periodo;
import com.example.federico.aldia.network.APIInterface;
import com.example.federico.aldia.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PeriodosActivity extends AppCompatActivity {

    private static final String TAG = "Historial Activity";
    private static PeriodoAdapter mAdapter;
    RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    SharedPreferences prefs;
    private APIInterface mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

          mRecyclerView = (RecyclerView) findViewById(R.id.periodos_recycler_view);

       mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);

        obtenerPeriodos();





    }

    private void obtenerPeriodos() {

        int comercioId = prefs.getInt(Constantes.KEY_COMERCIO_ID, 0);

        mService = RetrofitClient.getClient(getApplicationContext()).create(APIInterface.class);

        Call<Periodo> callGetPeriodos = mService.getPeriodos(comercioId);

        callGetPeriodos.enqueue(new Callback<Periodo>() {
            @Override
            public void onResponse(Call<Periodo> call, Response<Periodo> response) {
                Log.i(TAG, "on Response callGetPeriodos");

                if (response.isSuccessful()) {

                    Log.i(TAG, "is Succesful callGetPeriodos");

                    //   List<Periodo> listaPeriodos = obtenerListaPeriodosDummy();

                    // mAdapter = new PeriodoAdapter(listaPeriodos,PeriodosActivity.this);

                    // mRecyclerView.setAdapter(mAdapter);

                } else {

                    Log.i(TAG, "is NOT Succesful callGetPeriodos");


                }
            }

            @Override
            public void onFailure(Call<Periodo> call, Throwable t) {

                Log.i(TAG, "onFailure callGetPeriodos");


            }
        });






    }

    // private List<Periodo> obtenerListaPeriodosDummy() {

//        List<Periodo> listaPeriodos = new ArrayList<Periodo>();
//
//        Periodo p1 = new Periodo("1", "10:00", "13:00", "13/3/2018");
//        p1.setHorasRegularesTotales("2:00");
//        p1.setRecaudadoHorasRegulares("$60,00");
//        p1.setHorasExtraTotales("00:00");
//        p1.setRecaudadoHorasExtra("$00,00");
//        listaPeriodos.add(p1);
//
//        Periodo p2 = new Periodo("2", "10:10", "13:25", "14/3/2018");
//        p2.setHorasRegularesTotales("2:15");
//        p2.setRecaudadoHorasRegulares("$60,00");
//        p2.setHorasExtraTotales("01:20");
//        p2.setRecaudadoHorasExtra("$80,20");
//        listaPeriodos.add(p2);
//
//
//        Periodo p3 = new Periodo("3", "22:05", "02:15", "15/3/2018");
//        p3.setHorasRegularesTotales("04:10");
//        p3.setRecaudadoHorasRegulares("$60,00");
//        p3.setHorasExtraTotales("01:10");
//        p3.setRecaudadoHorasExtra("$56,60");
//        listaPeriodos.add(p3);
//
//
//        Periodo p4 = new Periodo("4", "10:25", "13:27", "16/3/2018");
//        p4.setHorasRegularesTotales("02:02");
//        p4.setRecaudadoHorasRegulares("$60,00");
//        p4.setHorasExtraTotales("00:00");
//        p4.setRecaudadoHorasExtra("$00,00");
//        listaPeriodos.add(p4);
//
//
//        Periodo p5 = new Periodo("5", "11:00", "13:00", "18/3/2018");
//        p5.setHorasRegularesTotales("02:00");
//        p5.setRecaudadoHorasRegulares("$60,00");
//        p5.setHorasExtraTotales("00:00");
//        p5.setRecaudadoHorasExtra("$00,00");
//        listaPeriodos.add(p5);
//
//        return listaPeriodos;

  //  }
}
