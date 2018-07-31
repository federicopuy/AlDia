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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LiquidacionAdapter extends RecyclerView.Adapter<LiquidacionAdapter.ViewHolder> {

    final private LiquidacionAdapter.ListItemClickListener mOnClickListener;
    private List<Liquidacion> listaLiquidaciones;
    private Context mContext;

    public LiquidacionAdapter(Context context, LiquidacionAdapter.ListItemClickListener listener) {
        this.mContext = context;
        mOnClickListener = listener;
        listaLiquidaciones = new ArrayList<>();
    }

    public void addItems(List<Liquidacion> listaLiquidaciones) {
        for (Liquidacion l : listaLiquidaciones) {
            addItem(l);
        }
    }

    void addItem(Liquidacion l) {
        listaLiquidaciones.add(l);
        notifyItemInserted(listaLiquidaciones.size() - 1);
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

        if (!(liquidacion.getCategoria().getTipoCategoria().equals("FIJO"))) {
            holder.tvHorasRegularesText.setVisibility(View.VISIBLE);
            holder.tvHorasExtraText.setVisibility(View.VISIBLE);
            holder.tvHorasExtraTotales.setVisibility(View.VISIBLE);
            holder.tvHorasRegularesTotales.setVisibility(View.VISIBLE);

            String horasRegulares = "";
            String horasExtra = "";
            try {
                horasRegulares = liquidacion.getHorasTotReg().toString() + " hs";
                horasExtra = liquidacion.getHorasTotExt().toString() + " hs";
            } catch (Exception e) {
                e.printStackTrace();
            }
            holder.tvHorasRegularesTotales.setText(horasRegulares);
            holder.tvHorasExtraTotales.setText(horasExtra);
        }

        try {
            holder.tvFechaLiquidacion.setText(Utils.obtenerFechaFormateada(liquidacion.getFecha()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            holder.tvRecaudacionTotalLiquidacion.setText(Utils.obtenerMontoFormateado(liquidacion.getMontoTotal()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return listaLiquidaciones.size();
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex, Liquidacion liquidacionClickeada);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tvRecaudacionTotalLiquidacion)
        TextView tvRecaudacionTotalLiquidacion;
        @BindView(R.id.tvFechaLiquidacion)
        TextView tvFechaLiquidacion;
        @BindView(R.id.tvHorasRegularesTotales)
        TextView tvHorasRegularesTotales;
        @BindView(R.id.tvHorasExtraTotales)
        TextView tvHorasExtraTotales;
        @BindView(R.id.tvHorasRegularesText)
        TextView tvHorasRegularesText;
        @BindView(R.id.tvHorasExtraText)
        TextView tvHorasExtraText;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
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
