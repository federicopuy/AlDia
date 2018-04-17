package com.example.federico.aldia.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.federico.aldia.Adapters.LiquidacionAdapter;
import com.example.federico.aldia.R;
import com.example.federico.aldia.data.Liquidacion;

import java.util.ArrayList;
import java.util.List;

public class LiquidacionesActivity extends AppCompatActivity implements LiquidacionAdapter.ListItemClickListener {


    private static final String Tag = "Liquidaciones Activity";
    private static LiquidacionAdapter mAdapter;
    RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liquidaciones);

        mRecyclerView = (RecyclerView) findViewById(R.id.liquidaciones_recycler_view);

        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);

        List<Liquidacion> listaLiquidaciones = obtenerListaLiquidacionesDummy();

        mAdapter = new LiquidacionAdapter(listaLiquidaciones,this, this);

        mRecyclerView.setAdapter(mAdapter);

    }

    private List<Liquidacion> obtenerListaLiquidacionesDummy() {

        List<Liquidacion> listaLiquidacionesDummy = new ArrayList<Liquidacion>();

        Liquidacion l1 = new Liquidacion("05/02/2018", "$12350,08");
        listaLiquidacionesDummy.add(l1);

        Liquidacion l2 = new Liquidacion("03/01/2018", "$1380,10");
        listaLiquidacionesDummy.add(l2);

        Liquidacion l3 = new Liquidacion("06/12/2017", "$11250,04");
        listaLiquidacionesDummy.add(l3);

        Liquidacion l4 = new Liquidacion("05/11/2017", "$12640,76");
        listaLiquidacionesDummy.add(l4);

        Liquidacion l5 = new Liquidacion("02/10/2017", "$152570,18");
        listaLiquidacionesDummy.add(l5);

        Liquidacion l6 = new Liquidacion("05/09/2017", "$14252,45");
        listaLiquidacionesDummy.add(l6);

        return listaLiquidacionesDummy;


    }

    @Override
    public void onListItemClick(int clickedItemIndex, Liquidacion liquidacionClickeada) {

        Log.e("Liquidaciones Activity " , "Click");


        Intent pasarAListaPeriodos = new Intent(LiquidacionesActivity.this, HistorialActivity.class);

        startActivity(pasarAListaPeriodos);

    }
}
