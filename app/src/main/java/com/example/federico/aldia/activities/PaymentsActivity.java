package com.example.federico.aldia.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.example.federico.aldia.R;
import com.example.federico.aldia.adapters.PaymentAdapter;
import com.example.federico.aldia.model.Liquidacion;
import com.example.federico.aldia.network.AppController;
import com.example.federico.aldia.utils.Constants;
import com.example.federico.aldia.viewmodel.PaymentsActivityViewModel;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaymentsActivity extends AppCompatActivity implements PaymentAdapter.ListItemClickListener {

    private static final String TAG = "Payments Activity";
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.payments_recycler_view)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);
        ButterKnife.bind(this);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        PaymentAdapter mAdapter = new PaymentAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        long comercioId = prefs.getLong(Constants.KEY_BUSINESS_ID, 0);

        PaymentsActivityViewModel.Factory factory = new PaymentsActivityViewModel.Factory(AppController.get(this), comercioId);
        PaymentsActivityViewModel paymentsActivityViewModel = ViewModelProviders.of(this, factory).get(PaymentsActivityViewModel.class);
        paymentsActivityViewModel.getPaymentsLiveData().observe(this, payments -> {
            mAdapter.submitList(payments);
            mRecyclerView.setAdapter(mAdapter);

        });
    }

    /*-------------------------------------- OnListItemClick --------------------------------------------***/

    @Override
    public void onListItemClick(View view) {
        Intent intentToShiftsList = new Intent(PaymentsActivity.this, ShiftsActivity.class);
        Liquidacion clickedPayment = (Liquidacion) view.getTag();
        intentToShiftsList.putExtra(Constants.KEY_INTENT_PAYMENT_INFO, clickedPayment.getId());
        startActivity(intentToShiftsList);
    }

}
