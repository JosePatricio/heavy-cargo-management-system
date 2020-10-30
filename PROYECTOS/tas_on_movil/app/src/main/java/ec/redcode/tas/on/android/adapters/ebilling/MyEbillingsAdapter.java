package ec.redcode.tas.on.android.adapters.ebilling;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import ec.redcode.tas.on.android.R;
import ec.redcode.tas.on.android.dto.requests.MyEBillingDTO;


public class MyEbillingsAdapter extends ArrayAdapter<MyEBillingDTO> {
    public MyEbillingsAdapter(Context context, MyEBillingDTO[] myEBillingDTOS) {
        super(context, 0, myEBillingDTOS);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");

        // Get the data item for this position
        MyEBillingDTO myEBillingDTO = getItem(position);

        //Instance ViewHolder Class
        ViewHolder holder;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.my_ebilling_item, parent, false);
            holder = new ViewHolder();
            //Referenciamos el elemento a modificar y lo rellenamos;
            // Lookup view for data population
            holder.facturaNumero = convertView.findViewById(R.id.ebilling_numero);
            holder.fechaEmision = convertView.findViewById(R.id.ebilling_fecha_emision);
            holder.fechaAutorizacion = convertView.findViewById(R.id.ebilling_fecha_autorizacion);
            holder.claveAcceso = convertView.findViewById(R.id.ebilling_clave_acceso);
            holder.estado = convertView.findViewById(R.id.ebilling_estado);
            holder.total = convertView.findViewById(R.id.ebilling_total);
            holder.adquiriente = convertView.findViewById(R.id.ebilling_adquiriente);
            holder.razonSocialAdquiriente = convertView.findViewById(R.id.ebilling_razon_social_adquiriente);
            //Set older on tag view
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        String dosPuntosEspacio = ": ";
        holder.facturaNumero.setText(getContext().getString(R.string.numero_factura)+dosPuntosEspacio+myEBillingDTO.getEbillingId());
        holder.fechaEmision.setText(getContext().getString(R.string.fecha_emision)+dosPuntosEspacio+sf.format(new Date(Long.parseLong(myEBillingDTO.getEmisionDate()))));
        String fechaAutorizacion = myEBillingDTO.getAuthorizationDate() == null || myEBillingDTO.getAuthorizationDate().isEmpty() ?
                "":sf.format(new Date(Long.parseLong(myEBillingDTO.getAuthorizationDate())));
        holder.fechaAutorizacion.setText(getContext().getString(R.string.fecha_autorizacion)+dosPuntosEspacio+fechaAutorizacion);
        holder.claveAcceso.setText(getContext().getString(R.string.clave_acceso)+dosPuntosEspacio+myEBillingDTO.getClaveAcceso());
        holder.estado.setText(getContext().getString(R.string.estado)+dosPuntosEspacio+myEBillingDTO.getState());
        holder.total.setText(getContext().getString(R.string.total)+dosPuntosEspacio+myEBillingDTO.getTotal());
        holder.adquiriente.setText(getContext().getString(R.string.adquiriente)+dosPuntosEspacio+myEBillingDTO.getAdquiriente());
        holder.razonSocialAdquiriente.setText(getContext().getString(R.string.razon_social)+dosPuntosEspacio+myEBillingDTO.getRazonSocialAdquiriente());
        // Return the completed view to render on screen
        return convertView;
    }

    static class ViewHolder {
        TextView facturaNumero;
        TextView fechaEmision;
        TextView fechaAutorizacion;
        TextView claveAcceso;
        TextView estado;
        TextView total;
        TextView adquiriente;
        TextView razonSocialAdquiriente;
    }
}
