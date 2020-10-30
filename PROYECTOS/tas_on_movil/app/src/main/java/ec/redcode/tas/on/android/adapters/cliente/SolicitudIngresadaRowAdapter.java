package ec.redcode.tas.on.android.adapters.cliente;


import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ec.redcode.tas.on.android.R;
import ec.redcode.tas.on.android.models.FleteShort;


public class SolicitudIngresadaRowAdapter extends RecyclerView.Adapter<SolicitudIngresadaRowAdapter.ViewHolder> {

    private List<FleteShort> mValues;
    private ClickListener clickListener;

    public SolicitudIngresadaRowAdapter(List<FleteShort> items, ClickListener clickListener) {
        mValues = items;
        this.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_solicitud_ingresada, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.idSolicitud.setText(mValues.get(position).getId());
        holder.solicCiudOrigin.setText(mValues.get(position).getOriginCity());
        holder.solicCiudDestino.setText(mValues.get(position).getDestinationCity());
        holder.solicNumPiesas.setText(Html.fromHtml("<b>" + String.valueOf(mValues.get(position).getNumeroPiezas()) + " "
                + ((mValues.get(position).getNumeroPiezas() != 1) ? "piezas" : "pieza") + "</b>"));
        holder.solicDiasValidez.setText(mValues.get(position).getDays() + "");
        holder.solicPeso.setText(mValues.get(position).getWeight() + " " + mValues.get(position).getWeightType());
        holder.solicFechaRecol.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date(mValues.get(position).getFechaEntrega())));
        holder.solicPersonRecive.setText(mValues.get(position).getPersonaRecibe());
        /*holder.mDaysUni.setText(((mValues.get(position).getDays() != 1) ? "días" : "día"));
        if (mValues.get(position).getDayPay() != null)
            holder.diasPagos.setText(mValues.get(position).getDayPay() + "");
        else
            holder.diasPagosLy.setVisibility(View.GONE);*/
        /*holder.solicitudLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClick(mValues.get(holder.getAdapterPosition()));
            }
        });*/
        holder.viewMore.setOnClickListener(new View.OnClickListener() {
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
        public final TextView solicNumPiesas;
        public final TextView solicCiudOrigin;
        public final TextView solicCiudDestino;
        public final TextView solicDiasValidez;
        public final TextView solicPeso;
        public final TextView solicFechaRecol;
        public final TextView solicPersonRecive;
        public final Button viewMore;


        public ViewHolder(View view) {
            super(view);
            solicitudLayout = view.findViewById(R.id.solicitudLayout);
            idSolicitud = view.findViewById(R.id.idSolicitud);
            solicNumPiesas = view.findViewById(R.id.solicNumPiesas);
            solicCiudOrigin = view.findViewById(R.id.solicCiudOrigin);
            solicCiudDestino = view.findViewById(R.id.solicCiudDestino);
            solicDiasValidez = view.findViewById(R.id.solicDiasValidez);
            solicPeso = view.findViewById(R.id.solicPeso);
            solicFechaRecol = view.findViewById(R.id.solicFechaRecol);
            solicPersonRecive = view.findViewById(R.id.solicPersonRecive);
            viewMore = view.findViewById(R.id.viewMore);
        }
    }
}

