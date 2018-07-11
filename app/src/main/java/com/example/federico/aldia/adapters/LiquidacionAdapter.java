package com.example.federico.aldia.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.federico.aldia.R;
import com.example.federico.aldia.model.Liquidacion;
import com.example.federico.aldia.utils.Utils;

import java.util.List;

public class LiquidacionAdapter extends RecyclerView.Adapter<LiquidacionAdapter.ViewHolder>{

    final private LiquidacionAdapter.ListItemClickListener mOnClickListener;
    private List<Liquidacion> listaLiquidaciones;
    private Context mContext;

    public LiquidacionAdapter(List<Liquidacion> listaLiquidaciones, Context context, LiquidacionAdapter.ListItemClickListener listener){
        this.listaLiquidaciones = listaLiquidaciones;
        this.mContext = context;
        mOnClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.cada_liquidacion, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Liquidacion liquidacion = listaLiquidaciones.get(position);

        String fecha = "";
        String montoTotal = "";
        String horasRegulares = "";
        String horasExtra = "";

        try{
            fecha = liquidacion.getFecha();
        } catch (Exception e){
            e.printStackTrace();
        }

        try{
            montoTotal = Utils.obtenerMontoFormateado(liquidacion.getMontoTotal());
        } catch (Exception e){
            e.printStackTrace();
        }

        try{
            horasRegulares = liquidacion.getHorasTotReg().toString() + " hs";;
            horasExtra = liquidacion.getHorasTotExt().toString() + " hs";
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.tvFechaLiquidacion.setText(Utils.obtenerFechaFormateada(fecha));
        holder.tvRecaudacionTotalLiquidacion.setText(montoTotal);
        holder.tvHorasRegularesTotales.setText(horasRegulares);
        holder.tvHorasExtraTotales.setText(horasExtra);

    }

    @Override
    public int getItemCount() {
        return listaLiquidaciones.size();
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex, Liquidacion liquidacionClickeada);
    }

      class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvRecaudacionTotalLiquidacion, tvFechaLiquidacion, tvHorasRegularesTotales, tvHorasExtraTotales;

        public ViewHolder(View itemView) {
            super(itemView);

            tvRecaudacionTotalLiquidacion = itemView.findViewById(R.id.tvRecaudacionTotalLiquidacion);
            tvFechaLiquidacion = itemView.findViewById(R.id.tvFechaLiquidacion);
            tvHorasRegularesTotales = itemView.findViewById(R.id.tvHorasRegularesTotales);
            tvHorasExtraTotales = itemView.findViewById(R.id.tvHorasExtraTotales);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            Liquidacion liquidacionClickeada = listaLiquidaciones.get(clickedPosition);
            mOnClickListener.onListItemClick(clickedPosition, liquidacionClickeada);
        }
    }

}
