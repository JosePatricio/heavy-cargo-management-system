package ec.redcode.tas.on.android.adapters.cliente;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ec.redcode.tas.on.android.R;
import ec.redcode.tas.on.android.models.FleteShort;


public class SolicitudOfertadaRowAdapter extends RecyclerView.Adapter<SolicitudOfertadaRowAdapter.ViewHolder> {

    private List<FleteShort> mValues;
    private ClickListener clickListener;

    public SolicitudOfertadaRowAdapter(List<FleteShort> items, ClickListener clickListener) {
        mValues = items;
        this.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_solicitud_ofertada, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.idSolicitud.setText(mValues.get(position).getId());
        holder.solicCiudOrigin.setText(mValues.get(position).getOriginCity());
        holder.solicCiudDestino.setText(mValues.get(position).getDestinationCity());
        holder.solicDiasValidez.setText(mValues.get(position).getDays() + "");
        holder.solicPeso.setText(mValues.get(position).getWeight() + " " + mValues.get(position).getWeightType());
        holder.viewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Button) v).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_chevron_down_white, 0, 0, 0);
                clickListener.onClick(mValues.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void updateList(List<FleteShort> list) {
        mValues = list;
        notifyDataSetChanged();
    }

    public interface ClickListener {
        void onClick(Object flete);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout solicitudLayout;
        public final TextView idSolicitud;
        public final TextView solicCiudOrigin;
        public final TextView solicCiudDestino;
        public final TextView solicDiasValidez;
        public final TextView solicPeso;
        public final Button viewMore;


        public ViewHolder(View view) {
            super(view);
            solicitudLayout = view.findViewById(R.id.solicitudLayout);
            idSolicitud = view.findViewById(R.id.idSolicitud);
            solicCiudOrigin = view.findViewById(R.id.solicCiudOrigin);
            solicCiudDestino = view.findViewById(R.id.solicCiudDestino);
            solicDiasValidez = view.findViewById(R.id.solicDiasValidez);
            solicPeso = view.findViewById(R.id.solicPeso);
            viewMore = view.findViewById(R.id.viewMore);
        }
    }
}

