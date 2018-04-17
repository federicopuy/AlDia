package com.example.federico.aldia.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.federico.aldia.Activities.HistorialActivity;
import com.example.federico.aldia.R;
import com.example.federico.aldia.data.Liquidacion;

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

        holder.tvFechaLiquidacion.setText(liquidacion.getFecha());

        holder.tvRecaudacionTotalLiquidacion.setText(liquidacion.getMontoTotal());

    }

    @Override
    public int getItemCount() {
        return listaLiquidaciones.size();
    }


    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex, Liquidacion liquidacionClickeada);
    }


      class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvFechaLiquidacion;
        TextView tvRecaudacionTotalLiquidacion;


        private AdapterView.OnItemClickListener listener;

        public ViewHolder(View itemView) {
            super(itemView);


            tvFechaLiquidacion = itemView.findViewById(R.id.tvFechaLiquidacion);
            tvRecaudacionTotalLiquidacion = itemView.findViewById(R.id.tvRecaudacionTotalLiquidacion);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            int clickedPosition = getAdapterPosition();

            Liquidacion liquidacionClickeada = listaLiquidaciones.get(clickedPosition);

            mOnClickListener.onListItemClick(clickedPosition,liquidacionClickeada);


        }
    }

}
