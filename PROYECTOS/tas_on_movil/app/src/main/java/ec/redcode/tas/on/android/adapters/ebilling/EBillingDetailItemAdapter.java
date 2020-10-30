package ec.redcode.tas.on.android.adapters.ebilling;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;

import ec.redcode.tas.on.android.R;
import ec.redcode.tas.on.android.dto.requests.DetalleFactura;


public class EBillingDetailItemAdapter extends ArrayAdapter<DetalleFactura> {
    public EBillingDetailItemAdapter(Context context, ArrayList<DetalleFactura> detalleFacturaArrayList) {
        super(context, 0, detalleFacturaArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        DetalleFactura detalleFactura = getItem(position);

        //Instance ViewHolder Class
        ViewHolder holder;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.ebilling_item_detail, parent, false);
            holder = new ViewHolder();
            //Referenciamos el elemento a modificar y lo rellenamos;
            // Lookup view for data population
            holder.numeroPiezas = convertView.findViewById(R.id.numeroPiezas);
            holder.ciudadOrigen = convertView.findViewById(R.id.ciudadOrigen);
            holder.ciudadDestino = convertView.findViewById(R.id.ciudadDestino);
            holder.detallesAdicionales = convertView.findViewById(R.id.detallesAdicionales);
            holder.precioUnitario = convertView.findViewById(R.id.precioUnitario);
            holder.descuento = convertView.findViewById(R.id.descuento);
            holder.total = convertView.findViewById(R.id.total);
            //Set older on tag view
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        String dosPuntosEspacio = ": ";
        holder.numeroPiezas.setText(getContext().getString(R.string.transportado)+dosPuntosEspacio+detalleFactura.getNumeroPiezas()+" "+detalleFactura.getUnidadPiezas());
        holder.ciudadOrigen.setText(getContext().getString(R.string.ciudad_origen)+dosPuntosEspacio+detalleFactura.getCiudadOrigen());
        holder.ciudadDestino.setText(getContext().getString(R.string.ciudad_destino)+dosPuntosEspacio+detalleFactura.getCiudadDestino());
        holder.detallesAdicionales.setText(getContext().getString(R.string.detalles_adicionales)+dosPuntosEspacio+detalleFactura.getDetallesAdicionales());
        holder.precioUnitario.setText(getContext().getString(R.string.precio_unitario)+dosPuntosEspacio+detalleFactura.getPrecioUnitario() );
        holder.descuento.setText(getContext().getString(R.string.descuento)+dosPuntosEspacio+detalleFactura.getDescuento() );
        BigDecimal precioUnitario = detalleFactura.getPrecioUnitario() != null ? detalleFactura.getPrecioUnitario() : BigDecimal.ZERO;
        BigDecimal descuento = detalleFactura.getDescuento() != null ? detalleFactura.getDescuento() : BigDecimal.ZERO;
        BigDecimal total = (precioUnitario.subtract(descuento)).setScale(2, BigDecimal.ROUND_HALF_UP);
        holder.total.setText(getContext().getString(R.string.total)+dosPuntosEspacio+total);
        // Return the completed view to render on screen
        return convertView;
    }

    static class ViewHolder {
        TextView numeroPiezas;
        TextView ciudadOrigen;
        TextView ciudadDestino;
        TextView detallesAdicionales;
        TextView precioUnitario;
        TextView descuento;
        TextView total;
    }
}
