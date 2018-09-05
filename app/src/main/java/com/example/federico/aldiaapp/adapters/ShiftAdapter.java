package com.example.federico.aldiaapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.federico.aldiaapp.R;
import com.example.federico.aldiaapp.model.Shift;
import com.example.federico.aldiaapp.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Federico on 16/04/2018.
 */

public class ShiftAdapter extends RecyclerView.Adapter<ShiftAdapter.ViewHolder> {

    private List<Shift> shiftsList;
    private Context mContext;

    public ShiftAdapter(List<Shift> shiftsList, Context context) {
        this.shiftsList = shiftsList;
        this.mContext = context;
    }

    @Override
    public ShiftAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.each_shift, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ShiftAdapter.ViewHolder holder, int position) {

        Shift shift = shiftsList.get(position);

        try {
            holder.tvDate.setText(Utils.getDate(shift.getHoraInicio()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // If for some reason the employer deletes the shift via the web app,
        // shift is shown to the employee in grey color.
        if (shift.getEliminado()) {
            holder.deletedImage.setVisibility(View.VISIBLE);
            holder.tvEntryExitHour.setTextColor(mContext.getResources().getColor(R.color.color_grey));
            holder.tvDate.setTextColor(mContext.getResources().getColor(R.color.color_grey));
            holder.tvRegularHoursValue.setTextColor(mContext.getResources().getColor(R.color.color_grey));
            holder.tvExtraHoursValue.setTextColor(mContext.getResources().getColor(R.color.color_grey));
        }
        String entryHour;
        String exitHour;
        try {
            entryHour = Utils.getHour(shift.getHoraInicio());
        } catch (Exception e) {
            e.printStackTrace();
            entryHour = "";
        }
        try {
            exitHour = Utils.getHour(shift.getHoraFin());
        } catch (Exception e) {
            e.printStackTrace();
            exitHour = "";
        }

        String horaIngresoEgreso = entryHour + " - " + exitHour;
        holder.tvEntryExitHour.setText(horaIngresoEgreso);

        try {
            holder.tvRegularHoursValue.setText(Utils.getTimeAndMoneyRegular(shift.getHorasReg(), shift.getPosition().getMonto()));
            holder.tvExtraHoursValue.setText(Utils.getTimeAndMoneyExtra(shift.getHorasExt(), shift.getPosition().getMonto()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return shiftsList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tvEntryExitHour)
        TextView tvEntryExitHour;
        @BindView(R.id.tvDate)
        TextView tvDate;
        @BindView(R.id.tvRegularHoursValue)
        TextView tvRegularHoursValue;
        @BindView(R.id.tvExtraHoursValue)
        TextView tvExtraHoursValue;
        @BindView(R.id.deletedImage)
        ImageButton deletedImage;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            deletedImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == deletedImage.getId()) {
                Toast.makeText(view.getContext(), R.string.periodo_eliminado, Toast.LENGTH_LONG).show();
            }
        }
    }

}
