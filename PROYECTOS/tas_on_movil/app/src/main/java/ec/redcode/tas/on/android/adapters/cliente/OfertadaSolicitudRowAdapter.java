package ec.redcode.tas.on.android.adapters.cliente;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ec.redcode.tas.on.android.R;
import ec.redcode.tas.on.android.dto.OffersDTO;


public class OfertadaSolicitudRowAdapter extends RecyclerView.Adapter<OfertadaSolicitudRowAdapter.ViewHolder> {

    private List<OffersDTO> mValues;
    private ClickListener clickListener;

    public OfertadaSolicitudRowAdapter(List<OffersDTO> items, ClickListener clickListener) {
        mValues = items;
        this.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_oferta_solicitud, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.ofertaTransportista.setText(mValues.get(position).getSupplier());
        holder.ofertaOrigin.setText(mValues.get(position).getOrigen());
        holder.ofertaDestino.setText(mValues.get(position).getDestino());
        holder.ofertaPeso.setText(String.valueOf(mValues.get(position).getPeso()));
        holder.ofertaMonto.setText("$ " + String.valueOf(mValues.get(position).getAmount()));
        holder.ofertaFecha.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date(mValues.get(position).getDate())));
        holder.viewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClick(mValues.get(holder.getAdapterPosition()), 1);
            }
        });
        holder.aceptOffert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClick(mValues.get(holder.getAdapterPosition()), 2);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void updateList(List<OffersDTO> list) {
        mValues = list;
        notifyDataSetChanged();
    }

    public interface ClickListener {
        void onClick(Object flete, int typeAction);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout ofertaLayout;
        public final TextView ofertaTransportista;
        public final TextView ofertaOrigin;
        public final TextView ofertaDestino;
        public final TextView ofertaPeso;
        public final TextView ofertaMonto;
        public final TextView ofertaFecha;
        public final ImageButton viewMore;
        public final ImageButton aceptOffert;

        public ViewHolder(View view) {
            super(view);
            ofertaLayout = view.findViewById(R.id.ofertaLayout);
            ofertaTransportista = view.findViewById(R.id.ofertaTransportista);
            ofertaOrigin = view.findViewById(R.id.ofertaOrigin);
            ofertaDestino = view.findViewById(R.id.ofertaDestino);
            ofertaPeso = view.findViewById(R.id.ofertaPeso);
            ofertaMonto = view.findViewById(R.id.ofertaMonto);
            ofertaFecha = view.findViewById(R.id.ofertaFecha);
            viewMore = view.findViewById(R.id.viewMore);
            aceptOffert = view.findViewById(R.id.aceptOffert);
        }
    }
}

