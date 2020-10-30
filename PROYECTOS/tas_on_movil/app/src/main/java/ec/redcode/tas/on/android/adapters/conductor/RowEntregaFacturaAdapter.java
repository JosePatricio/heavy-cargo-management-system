package ec.redcode.tas.on.android.adapters.conductor;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import ec.redcode.tas.on.android.R;
import ec.redcode.tas.on.android.Utils;
import ec.redcode.tas.on.android.dto.InvoicesDTO;

public class RowEntregaFacturaAdapter extends RecyclerView.Adapter<RowEntregaFacturaAdapter.ViewHolder> {

    private List<InvoicesDTO> mInvoices;
    private ClickListener clickListener;

    public RowEntregaFacturaAdapter(List<InvoicesDTO> items, ClickListener clickListener) {
        mInvoices = items;
        this.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_entrega_factura_flete, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.idPrefact.setText(getItem(position).getInvoiceId());
        holder.numeroFact.setText(getItem(position).getInvoiceNumber());
        holder.fechaPrefact.setText(Utils.convertDateYear(getItem(position).getInvoiceDatePrefactura()));
        holder.montoFact.setText(String.format(new Locale("es", "EC"), "%.2f", getItem(position).getInvoiceAmount() - getItem(position).getInvoicesDiscount()));
        holder.facturaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClick(getItem(holder.getAdapterPosition()));
            }
        });
    }

    private InvoicesDTO getItem(int position) {
        return mInvoices.get(position);
    }

    @Override
    public int getItemCount() {
        return mInvoices.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout facturaLayout;
        public final TextView idPrefact;
        public final TextView numeroFact;
        public final TextView fechaPrefact;
        public final TextView montoFact;


        public ViewHolder(View view) {
            super(view);
            facturaLayout = view.findViewById(R.id.factura_layout);
            idPrefact = view.findViewById(R.id.idPrefact);
            numeroFact = view.findViewById(R.id.numeroFact);
            fechaPrefact = view.findViewById(R.id.fechaPrefact);
            montoFact = view.findViewById(R.id.montoFact);
        }
    }

    public void updateList(List<InvoicesDTO> list) {
        mInvoices = list;
        notifyDataSetChanged();
    }

    public interface ClickListener {
        void onClick(Object invoicesDTO);
    }
}

