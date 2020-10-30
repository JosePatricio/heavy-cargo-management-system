package ec.redcode.tas.on.android.adapters.conductor;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import ec.redcode.tas.on.android.models.FleteShort;
import ec.redcode.tas.on.android.R;

import java.util.List;


public class RowWeightTimeAdapter extends RecyclerView.Adapter<RowWeightTimeAdapter.ViewHolder> {

    private List<FleteShort> mValues;
    private ClickListener clickListener;

    public RowWeightTimeAdapter(List<FleteShort> items, ClickListener clickListener) {
        mValues = items;
        this.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_flete_weight_time, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String s = mValues.get(position).getOriginCity();
        //holder.mOriginCity.setText(s.length() > 8 ? mValues.get(position).getOriginCity().substring(0,7) + "." : s);
        holder.mOriginCity.setText(mValues.get(position).getOriginCity());
        s = mValues.get(position).getDestinationCity();
        //holder.mDestinationCity.setText(s.length() > 8 ? mValues.get(position).getDestinationCity().substring(0,7) + "." : s);
        holder.mDestinationCity.setText(mValues.get(position).getDestinationCity());
        holder.mWeight.setText(String.valueOf(mValues.get(position).getNumeroPiezas()));
        holder.mWeightType.setText("No.Piezas");
        //long timeLeft = mValues.get(position).getDeliveryDate() - System.currentTimeMillis();
        holder.mTimeLimit.setText(mValues.get(position).getDays() + "");
        holder.mDaysUni.setText(((mValues.get(position).getDays() != 1) ? "días" : "día"));
        if (mValues.get(position).getDayPay() != null)
            holder.diasPagos.setText(mValues.get(position).getDayPay() + "");
        else
            holder.diasPagosLy.setVisibility(View.GONE);
        holder.mFleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClick(mValues.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout mFleteLayout;
        public final LinearLayout diasPagosLy;
        public final TextView mOriginCity;
        public final TextView mDestinationCity;
        public final TextView mWeight;
        public final TextView mTimeLimit;
        public final TextView mWeightType;
        public final TextView mDaysUni;
        public final TextView diasPagos;


        public ViewHolder(View view) {
            super(view);
            mFleteLayout = view.findViewById(R.id.flete_layout);
            mOriginCity = view.findViewById(R.id.origin_city);
            mDestinationCity = view.findViewById(R.id.destination_city);
            mWeight = view.findViewById(R.id.load_weight);
            mTimeLimit = view.findViewById(R.id.load_time_limit);
            mWeightType = view.findViewById(R.id.load_weight_type);
            mDaysUni = view.findViewById(R.id.days_uni);
            diasPagos = view.findViewById(R.id.dias_pagos);
            diasPagosLy = view.findViewById(R.id.dias_pagos_ly);
        }
    }

    public void updateList(List<FleteShort> list) {
        mValues = list;
        notifyDataSetChanged();
    }


    public interface ClickListener {
        void onClick(Object flete);
    }
}

