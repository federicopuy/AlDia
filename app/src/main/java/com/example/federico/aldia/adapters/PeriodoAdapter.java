package com.example.federico.aldia.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    public PeriodoAdapter(List<Periodo> listaPeriodos, Context context) {
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
            holder.tvfecha.setText(Utils.obtenerSoloFechaFormateada(periodo.getHoraInicio()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (periodo.getEliminado()) {
            holder.imageEliminado.setVisibility(View.VISIBLE);
            holder.tvHoraIngresoEgreso.setTextColor(mContext.getResources().getColor(R.color.color_grey));
            holder.tvfecha.setTextColor(mContext.getResources().getColor(R.color.color_grey));
            holder.tvhorasRegularesTotales.setTextColor(mContext.getResources().getColor(R.color.color_grey));
            holder.tvhorasExtraTotales.setTextColor(mContext.getResources().getColor(R.color.color_grey));
        }

        if (!(periodo.getCategoria().getTipoCategoria().equals("FIJO"))) {

            try {
                String horaIngresoEgreso = Utils.obtenerHora(periodo.getHoraInicio())
                        + " - " + Utils.obtenerHora(periodo.getHoraFin());
                holder.tvHoraIngresoEgreso.setText(horaIngresoEgreso);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                holder.tvhorasRegularesTotales.setText(Utils.obtenerHoraYMontoRegular(periodo.getHorasReg(), periodo.getCategoria().getMonto()));
                holder.tvhorasExtraTotales.setText(Utils.obtenerHoraYMontoExtra(periodo.getHorasExt(), periodo.getCategoria().getMonto()));
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else{
            holder.tvhorasRegularesText.setText(R.string.hora_entrada);
            holder.tvhorasExtraText.setText(R.string.hora_salida);
            holder.tvhorasRegularesTotales.setText(Utils.obtenerHora(periodo.getHoraInicio()));
            holder.tvhorasExtraTotales.setText(Utils.obtenerHora(periodo.getHoraFin()));
            if (periodo.getInasistencia()) {
                holder.tvHoraIngresoEgreso.setText(R.string.inasistencia);
            }else {
                holder.tvHoraIngresoEgreso.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return listaPeriodos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvHoraIngresoEgreso;
        TextView tvfecha;
        TextView tvhorasRegularesTotales;
        TextView tvhorasRegularesText;
        TextView tvhorasExtraTotales;
        TextView tvhorasExtraText;
        ImageButton imageEliminado;

        public ViewHolder(View itemView) {
            super(itemView);
            tvHoraIngresoEgreso = itemView.findViewById(R.id.tvHoraIngresoEgreso);
            tvfecha = itemView.findViewById(R.id.tvFecha);
            tvhorasRegularesTotales = itemView.findViewById(R.id.tvHorasRegularesTotales);
            tvhorasRegularesText = itemView.findViewById(R.id.tvHorasRegularesText);
            tvhorasExtraTotales = itemView.findViewById(R.id.tvHorasExtraTotales);
            tvhorasExtraText = itemView.findViewById(R.id.tvHorasExtraText);
            imageEliminado = itemView.findViewById(R.id.imageEliminado);
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
