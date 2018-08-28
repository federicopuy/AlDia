package com.example.federico.aldia.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.federico.aldia.R;
import com.example.federico.aldia.adapters.PaymentAdapter;
import com.example.federico.aldia.model.AllPayments;
import com.example.federico.aldia.model.Liquidacion;
import com.example.federico.aldia.network.RetrofitClient;
import com.example.federico.aldia.utils.Constants;
import com.example.federico.aldia.utils.PaginationScrollListener;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentsActivity extends AppCompatActivity implements PaymentAdapter.ListItemClickListener {


    private static final String TAG = "Payments Activity";
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.tvSinLiquidaciones)
    TextView tvSinLiquidaciones;
    @BindView(R.id.liquidaciones_recycler_view)
    RecyclerView mRecyclerView;
    int size = 15;
    int pageNumber = 0;
    boolean isLastPage = false;
    RecyclerView.LayoutManager mLayoutManager;
    LayoutAnimationController animation;
    private PaymentAdapter mAdapter;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liquidaciones);
        ButterKnife.bind(this);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mAdapter = new PaymentAdapter(PaymentsActivity.this, this);
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //https://proandroiddev.com/enter-animation-using-recyclerview-and-layoutanimation-part-1-list-75a874a5d213
        int resId = R.anim.layout_animation_fall_down;
        animation = AnimationUtils.loadLayoutAnimation(this, resId);

        mRecyclerView.addOnScrollListener(new PaginationScrollListener((LinearLayoutManager) mLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                pageNumber += 1;
                new Handler().postDelayed(() -> loadNextPage(), 1000);
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

        loadFirstPage();
    }

    /*-------------------------------------- Calls to get Payments --------------------------------------------***/

    private void loadFirstPage() {
        final String nombreLlamada = "callGetLiquidaciones";
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        long comercioId = prefs.getLong(Constants.KEY_BUSINESS_ID, 0);
        progressBar.setVisibility(View.VISIBLE);

        RetrofitClient.getClient().getAllPayments(comercioId, pageNumber, size)
                .enqueue(new Callback<AllPayments>() {

                    @Override
                    public void onResponse(Call<AllPayments> call, Response<AllPayments> response) {
                        progressBar.setVisibility(View.INVISIBLE);

                        Log.i(TAG, getString(R.string.on_response) + nombreLlamada);
                        if (response.isSuccessful()) {
                            Log.i(TAG, getString(R.string.is_successful) + nombreLlamada);

                            AllPayments allPayments = response.body();
                            List<Liquidacion> listaLiquidaciones = allPayments.getLiquidacion();
                            mAdapter.addItems(listaLiquidaciones);
                            mRecyclerView.setLayoutAnimation(animation);

                            if (!(pageNumber <= allPayments.getTotalPages())) {
                                isLastPage = true;
                            }

                        } else {
                            Log.i(TAG, getString(R.string.is_not_successful) + nombreLlamada);
                        }
                    }

                    @Override
                    public void onFailure(Call<AllPayments> call, Throwable t) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.i(TAG, getString(R.string.on_failure) + nombreLlamada);
                    }
                });
    }

    private void loadNextPage() {

        final String nombreLlamada = "callGetLiquidaciones";
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        long comercioId = prefs.getLong(Constants.KEY_BUSINESS_ID, 0);
        progressBar.setVisibility(View.VISIBLE);

        RetrofitClient.getClient().getAllPayments(comercioId, pageNumber, size)
                .enqueue(new Callback<AllPayments>() {
                    @Override
                    public void onResponse(Call<AllPayments> call, Response<AllPayments> response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        if (response.isSuccessful()) {

                            Log.i(TAG, getString(R.string.on_response) + nombreLlamada);
                            isLoading = false;

                            AllPayments allPayments = response.body();
                            List<Liquidacion> listaLiquidaciones = allPayments.getLiquidacion();
                            mAdapter.addItems(listaLiquidaciones);

                            if (pageNumber == allPayments.getTotalPages()) {
                                isLastPage = true;
                            }
                        } else {
                            Log.i(TAG, getString(R.string.is_not_successful) + nombreLlamada);
                        }
                    }

                    @Override
                    public void onFailure(Call<AllPayments> call, Throwable t) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.i(TAG, getString(R.string.on_failure) + nombreLlamada);
                    }
                });
    }

    /*-------------------------------------- OnListItemClick --------------------------------------------***/

    @Override
    public void onListItemClick(int clickedItemIndex, Liquidacion clickedPayment) {
        Intent intentToShiftsList = new Intent(PaymentsActivity.this, ShiftsActivity.class);
        intentToShiftsList.putExtra(Constants.KEY_INTENT_PAYMENT_INFO, clickedPayment.getId());
        startActivity(intentToShiftsList);
    }

}
