package com.example.federico.aldia.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.federico.aldia.Activities.data.Periodo;
import com.example.federico.aldia.R;

import java.util.List;

/**
 * Created by Federico on 16/04/2018.
 */

public class PeriodoAdapter extends RecyclerView.Adapter<PeriodoAdapter.ViewHolder> {

    private List<Periodo> listaPeriodos;
    private Context mContext;

    public PeriodoAdapter(List<Periodo> listaPeriodos, Context context){

        this.listaPeriodos = listaPeriodos;
        this.mContext = context;

    }

    @Override
    public PeriodoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.cada_periodo, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(PeriodoAdapter.ViewHolder holder, int position) {

        Periodo periodo = listaPeriodos.get(position);

        holder.tvHoraIngresoEgreso.setText(periodo.getHoraIngreso() + " - " + periodo.getHoraEgreso());

        holder.tvfecha.setText(periodo.getFecha());

        holder.tvhorasRegularesTotales.setText(periodo.getHorasRegularesTotales() + " - " + periodo.getRecaudadoHorasRegulares());

        holder.tvhorasExtraTotales.setText(periodo.getHorasExtraTotales() + " - " + periodo.getRecaudadoHorasExtra());

    }

    @Override
    public int getItemCount() {
        return listaPeriodos.size();
    }





    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvHoraIngresoEgreso;
        TextView tvfecha;
        TextView tvhorasRegularesTotales;
        TextView tvhorasExtraTotales;

        private AdapterView.OnItemClickListener listener;

        public ViewHolder(View itemView) {
            super(itemView);


            tvHoraIngresoEgreso = itemView.findViewById(R.id.tvHoraIngresoEgreso);
            tvfecha = itemView.findViewById(R.id.tvfecha);
            tvhorasRegularesTotales = itemView.findViewById(R.id.tvhorasRegularesTotales);
            tvhorasExtraTotales = itemView.findViewById(R.id.tvhorasExtraTotales);


        }

        @Override
        public void onClick(View view) {

        }
    }


}
