package com.example.federico.aldia.adapters;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.federico.aldia.R;
import com.example.federico.aldia.model.Liquidacion;
import com.example.federico.aldia.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaymentAdapter extends PagedListAdapter<Liquidacion, RecyclerView.ViewHolder> {

    final private PaymentAdapter.ListItemClickListener mOnClickListener;

    public PaymentAdapter(PaymentAdapter.ListItemClickListener listener) {
        super(Liquidacion.DIFF_CALLBACK);
        mOnClickListener = listener;
    }
    //todo selectable item

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.each_payment, viewGroup, false);
        return new PaymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        PaymentViewHolder viewHolder = (PaymentViewHolder) holder;
        Liquidacion liquidacion = getItem(position);


        String regularHours = "";
        String extraHours = "";
        try {
            regularHours = liquidacion.getHorasTotReg().toString() + " hs";
            extraHours = liquidacion.getHorasTotExt().toString() + " hs";
        } catch (Exception e) {
            e.printStackTrace();
        }
        viewHolder.tvRegularHoursValue.setText(regularHours);
        viewHolder.tvExtraHoursValue.setText(extraHours);

        try {
            viewHolder.tvPaymentDate.setText(Utils.getDateAndHour(liquidacion.getFecha()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            viewHolder.tvTotalMoney.setText(Utils.getFormattedAmount(liquidacion.getMontoTotal()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface ListItemClickListener {
        void onListItemClick(View view);
    }

    class PaymentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tvTotalMoney)
        TextView tvTotalMoney;
        @BindView(R.id.tvPaymentDate)
        TextView tvPaymentDate;
        @BindView(R.id.tvRegularHoursValue)
        TextView tvRegularHoursValue;
        @BindView(R.id.tvExtraHoursValue)
        TextView tvExtraHoursValue;


        public PaymentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            view.setTag(getCurrentList().get(clickedPosition));
            mOnClickListener.onListItemClick(view);
        }
    }

}
