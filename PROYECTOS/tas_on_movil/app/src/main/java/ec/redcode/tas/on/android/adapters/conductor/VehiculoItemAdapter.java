package ec.redcode.tas.on.android.adapters.conductor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import ec.redcode.tas.on.android.dto.VehiculoDTO;
import ec.redcode.tas.on.android.R;

import java.util.ArrayList;

/**
 * Created by Walter on 21/3/18.
 */

public class VehiculoItemAdapter extends ArrayAdapter<VehiculoDTO> {
    public VehiculoItemAdapter(Context context, ArrayList<VehiculoDTO> vehiculo) {
        super(context, 0, vehiculo);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        VehiculoDTO vehiculo = getItem(position);

        //Instance ViewHolder Class
        ViewHolder holder;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_vehiculo, parent, false);
            holder = new ViewHolder();
            //Referenciamos el elemento a modificar y lo rellenamos;
            // Lookup view for data population
            holder.vehicModelo = convertView.findViewById(R.id.vehicModelo);
            holder.vehicPlaca = convertView.findViewById(R.id.vehicPlaca);
            holder.vehicCapacidad = convertView.findViewById(R.id.vehicCapacidad);
            //Set older on tag view
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        holder.vehicModelo.setText(vehiculo.getVehiculoModelo());
        holder.vehicPlaca.setText(vehiculo.getVehiculoPlaca());
        holder.vehicCapacidad.setText(String.valueOf(vehiculo.getVehiculoCapacidad()));
        // Return the completed view to render on screen
        return convertView;
    }

    static class ViewHolder {
        TextView vehicModelo;
        TextView vehicPlaca;
        TextView vehicCapacidad;
    }
}
