package com.example.federico.aldia.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.federico.aldia.R;
import com.example.federico.aldia.adapters.ShiftAdapter;
import com.example.federico.aldia.network.AppController;
import com.example.federico.aldia.network.URLs;
import com.example.federico.aldia.utils.Constants;
import com.example.federico.aldia.viewmodel.ShiftsViewModel;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShiftsActivity extends AppCompatActivity {

    private static final String TAG = "Periodos Activity";
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.tvNoShiftsToDisplay)
    TextView tvNoShiftsToDisplay;
    @BindView(R.id.shifts_recycler_view)
    RecyclerView mRecyclerView;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shifts);
        ButterKnife.bind(this);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        String searchMethod = getSearchMethod();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        //https://proandroiddev.com/enter-animation-using-recyclerview-and-layoutanimation-part-1-list-75a874a5d213
        int resId = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);

        ShiftsViewModel.Factory factory = new ShiftsViewModel.Factory(AppController.get(this), searchMethod, id);
        ShiftsViewModel shiftsViewModel = ViewModelProviders.of(this, factory).get(ShiftsViewModel.class);
        shiftsViewModel.getShiftsList().observe(this, shiftsList -> {
            assert shiftsList != null;
            switch (shiftsList.status) {
                case FAILED:
                    progressBar.setVisibility(View.INVISIBLE);
                    Log.e(TAG, shiftsList.msg);
                    break;
                case SUCCESS:
                    progressBar.setVisibility(View.INVISIBLE);
                    assert shiftsList.data != null;
                    if (shiftsList.data.size() < 1) {
                        tvNoShiftsToDisplay.setVisibility(View.VISIBLE);
                    } else {
                        ShiftAdapter mAdapter = new ShiftAdapter(shiftsList.data, ShiftsActivity.this);
                        mRecyclerView.setAdapter(mAdapter);
                        mRecyclerView.setLayoutAnimation(animation);
                    }
                    break;
                case RUNNING:
                    progressBar.setVisibility(View.VISIBLE);
                    break;
            }
        });
    }

    /**
     * Determines whether it should search shifts from a past payment, or shifts pending
     * to be paid.
     */
    public String getSearchMethod() {
        Intent intent = getIntent();
        if (intent.hasExtra(Constants.KEY_INTENT_PAYMENT_INFO)) {
            id = intent.getLongExtra(Constants.KEY_INTENT_PAYMENT_INFO, 0);
            return URLs.SEARCH_METHOD_BY_PAYMENT_ID;
        } else {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            id = prefs.getLong(Constants.KEY_BUSINESS_ID, 0);
            return URLs.SEARCH_METHOD_PENDING_TO_BE_PAID;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return (super.onOptionsItemSelected(item));
    }
}
