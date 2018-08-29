package com.example.federico.aldia.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.federico.aldia.R;
import com.example.federico.aldia.model.Periodo;
import com.example.federico.aldia.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Federico on 16/04/2018.
 */

public class ShiftAdapter extends RecyclerView.Adapter<ShiftAdapter.ViewHolder> {

    private List<Periodo> shiftsList;
    private Context mContext;

    public ShiftAdapter(List<Periodo> shiftsList, Context context) {
        this.shiftsList = shiftsList;
        this.mContext = context;
    }

    @Override
    public ShiftAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.cada_periodo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ShiftAdapter.ViewHolder holder, int position) {

        Periodo shift = shiftsList.get(position);

        try {
            holder.tvfecha.setText(Utils.getDate(shift.getHoraInicio()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // If for some reason the employer deletes the shift via the web app,
        // shift is shown to the employee in grey color.
        if (shift.getEliminado()) {
            holder.imageEliminado.setVisibility(View.VISIBLE);
            holder.tvHoraIngresoEgreso.setTextColor(mContext.getResources().getColor(R.color.color_grey));
            holder.tvfecha.setTextColor(mContext.getResources().getColor(R.color.color_grey));
            holder.tvhorasRegularesTotales.setTextColor(mContext.getResources().getColor(R.color.color_grey));
            holder.tvhorasExtraTotales.setTextColor(mContext.getResources().getColor(R.color.color_grey));
        } else {
            try {
                String horaIngresoEgreso = Utils.getHour(shift.getHoraInicio())
                        + " - " + Utils.getHour(shift.getHoraFin());
                holder.tvHoraIngresoEgreso.setText(horaIngresoEgreso);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                holder.tvhorasRegularesTotales.setText(Utils.getTimeAndMoneyRegular(shift.getHorasReg(), shift.getCategoria().getMonto()));
                holder.tvhorasExtraTotales.setText(Utils.getTimeAndMoneyExtra(shift.getHorasExt(), shift.getCategoria().getMonto()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return shiftsList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tvHoraIngresoEgreso)
        TextView tvHoraIngresoEgreso;
        @BindView(R.id.tvFecha)
        TextView tvfecha;
        @BindView(R.id.tvHorasRegularesTotales)
        TextView tvhorasRegularesTotales;
        @BindView(R.id.tvHorasRegularesText)
        TextView tvhorasRegularesText;
        @BindView(R.id.tvHorasExtraTotales)
        TextView tvhorasExtraTotales;
        @BindView(R.id.tvHorasExtraText)
        TextView tvhorasExtraText;
        @BindView(R.id.imageEliminado)
        ImageButton imageEliminado;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            imageEliminado.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == imageEliminado.getId()) {
                Toast.makeText(view.getContext(), R.string.periodo_eliminado, Toast.LENGTH_LONG).show();
            }
        }
    }

}
