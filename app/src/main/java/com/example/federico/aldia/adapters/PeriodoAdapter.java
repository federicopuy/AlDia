package com.example.federico.aldia.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.federico.aldia.R;
import com.example.federico.aldia.model.Periodo;
import com.example.federico.aldia.utils.Utils;

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
        try {
            String horaIngresoEgreso = Utils.obtenerHora(periodo.getHoraInicio())
                    + " - " + Utils.obtenerHora(periodo.getHoraFin());
            holder.tvHoraIngresoEgreso.setText(horaIngresoEgreso);
            holder.tvfecha.setText(Utils.obtenerSoloFechaFormateada(periodo.getHoraInicio()));
            holder.tvhorasRegularesTotales.setText(Utils.obtenerHoraYMontoRegular(periodo.getHorasReg(), periodo.getCategoria().getMonto()));
            holder.tvhorasExtraTotales.setText(Utils.obtenerHoraYMontoExtra(periodo.getHorasExt(), periodo.getCategoria().getMonto()));

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return listaPeriodos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvHoraIngresoEgreso;
        TextView tvfecha;
        TextView tvhorasRegularesTotales;
        TextView tvhorasExtraTotales;

        public ViewHolder(View itemView) {
            super(itemView);
            tvHoraIngresoEgreso = itemView.findViewById(R.id.tvHoraIngresoEgreso);
            tvfecha = itemView.findViewById(R.id.tvFecha);
            tvhorasRegularesTotales = itemView.findViewById(R.id.tvHorasRegularesTotales);
            tvhorasExtraTotales = itemView.findViewById(R.id.tvHorasExtraTotales);
        }

    }


}
