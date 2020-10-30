package ec.redcode.tas.on.android.adapters.conductor;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ec.redcode.tas.on.android.models.FleteShort;
import ec.redcode.tas.on.android.R;

import java.util.ArrayList;

/**
 * Created by Walter on 21/3/18.
 */

public class RowEmptyFleteAdapter extends RecyclerView.Adapter<RowEmptyFleteAdapter.ViewHolder> {

    private ArrayList<FleteShort> fleteShorts;

    public RowEmptyFleteAdapter(ArrayList<FleteShort> fleteShorts) {
        this.fleteShorts = fleteShorts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_empty_flete, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        FleteShort fleteShort = fleteShorts.get(position);

        // Populate the data into the template view using the data object
        holder.originCity.setText(fleteShort.getOriginCity());
    }

    @Override
    public int getItemCount() {
        return fleteShorts.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView originCity;

        public ViewHolder(View view) {
            super(view);
            originCity = view.findViewById(R.id.origin_city);
        }
    }
}
