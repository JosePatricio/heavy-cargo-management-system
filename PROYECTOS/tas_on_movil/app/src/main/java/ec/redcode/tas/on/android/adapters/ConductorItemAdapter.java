package ec.redcode.tas.on.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import ec.redcode.tas.on.android.dto.ConductorListaDTO;
import ec.redcode.tas.on.android.R;

import java.util.ArrayList;

/**
 * Created by Walter on 21/3/18.
 */

public class ConductorItemAdapter extends ArrayAdapter<ConductorListaDTO> {
    public ConductorItemAdapter(Context context, ArrayList<ConductorListaDTO> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ConductorListaDTO user = getItem(position);

        //Instance ViewHolder Class
        ViewHolder holder;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_conductor, parent, false);
            holder = new ViewHolder();
            //Referenciamos el elemento a modificar y lo rellenamos;
            // Lookup view for data population
            holder.condNomb = convertView.findViewById(R.id.condNomb);
            holder.condApell = convertView.findViewById(R.id.condApell);
            holder.condTipLic = convertView.findViewById(R.id.condTipLic);
            //Set older on tag view
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Populate the data into the template view using the data object
        holder.condNomb.setText(user.getConductorNombre());
        holder.condApell.setText(user.getConductorApellido());
        holder.condTipLic.setText(user.getTipoLicencia());
        // Return the completed view to render on screen
        return convertView;
    }

    static class ViewHolder {
        TextView condNomb;
        TextView condApell;
        TextView condTipLic;
    }
}
