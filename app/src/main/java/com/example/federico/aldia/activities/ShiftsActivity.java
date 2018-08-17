package com.example.federico.aldia.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.federico.aldia.adapters.ShiftAdapter;
import com.example.federico.aldia.R;
import com.example.federico.aldia.network.AppController;
import com.example.federico.aldia.utils.Constants;
import com.example.federico.aldia.model.Periodo;
import com.example.federico.aldia.network.URLs;
import com.example.federico.aldia.viewmodel.ShiftsViewModel;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShiftsActivity extends AppCompatActivity {

    private static final String TAG = "Periodos Activity";
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.tvSinPeriodos)
    TextView tvSinPeriodos;
    @BindView(R.id.periodos_recycler_view)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_periodos);
        ButterKnife.bind(this);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);




        Intent comesFromIntent = getIntent();
        long id;
        String tipoBusqueda = "";

        if (comesFromIntent.hasExtra(Constants.KEY_INTENT_LIQUIDACION_PERIODO)){
            id = comesFromIntent.getLongExtra(Constants.KEY_INTENT_LIQUIDACION_PERIODO, 0); //busca por id de liquidacion
            tipoBusqueda = URLs.SEARCH_METHOD_BY_PAYMENT_ID;

        } else {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            id = prefs.getLong(Constants.KEY_COMERCIO_ID, 0); //buscar por id de comercio
            tipoBusqueda = URLs.SEARCH_METHOD_LAST_PAYMENT;
        }

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        //https://proandroiddev.com/enter-animation-using-recyclerview-and-layoutanimation-part-1-list-75a874a5d213
        int resId = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);

        //Observer
        ShiftsViewModel.Factory factory = new ShiftsViewModel.Factory(AppController.get(this), tipoBusqueda, id);
        ShiftsViewModel shiftsViewModel = ViewModelProviders.of(this, factory).get(ShiftsViewModel.class);
        shiftsViewModel.getShiftsList().observe(this, new Observer<List<Periodo>>() {
            @Override
            public void onChanged(@Nullable List<Periodo> periodos) {
                if (periodos.size()<1){
                    tvSinPeriodos.setVisibility(View.VISIBLE);
                } else {
                    ShiftAdapter mAdapter = new ShiftAdapter(periodos, ShiftsActivity.this);
                    mRecyclerView.setAdapter(mAdapter);
                    mRecyclerView.setLayoutAnimation(animation);
                }
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
