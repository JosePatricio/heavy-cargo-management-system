package ec.redcode.tas.on.android.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import ec.redcode.tas.on.android.dto.CatalogoItemDTO;

/**
 * Created by Walter on 21/3/18.
 */
public class CatalogoItemAdapter extends ArrayAdapter<CatalogoItemDTO> {

    // Your sent context
    private Context context;
    // Your custom values for the spinner (User)
    private CatalogoItemDTO[] values;

    public CatalogoItemAdapter(@NonNull Context context, int resource, @NonNull CatalogoItemDTO[] objects) {
        super(context, resource, objects);
        this.context = context;
        this.values = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setHeight(40);
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)
        label.setText(values[position].getCatalogoItemDescripcion());

        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(values[position].getCatalogoItemDescripcion());
        return label;
    }

    @Override
    public int getCount() {
        return values.length;
    }

    @Nullable
    @Override
    public CatalogoItemDTO getItem(int position) {
        return values[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
