package ec.redcode.tas.on.android.adapters.conductor;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;

import butterknife.ButterKnife;
import ec.redcode.tas.on.android.R;
import ec.redcode.tas.on.android.models.FleteShort;


public class RowGeneraFacturaFlete extends RecyclerView.Adapter<RowGeneraFacturaFlete.ViewHolder> {

    private ClickListener clickListener;
    public final static String GEN_FACT = "#genFact";
    public final static String FACT_NORMAL = "normal";
    public final static String FACT_RAPIDA = "rapida";

    public RowGeneraFacturaFlete(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_genera_factura_flete, parent, false);
        // Inflate the layout for this fragment
        ButterKnife.bind(this, view);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.btngenfact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                /*clickListener.onClick(new FleteShort(null, GEN_FACT, FACT_NORMAL));*/
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(view.getContext(), view);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.menu_facturacion, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.factNormal)
                            clickListener.onClick(new FleteShort(null, GEN_FACT, FACT_NORMAL));
                        else if (item.getItemId() == R.id.factRapid)
                            clickListener.onClick(new FleteShort(null, GEN_FACT, FACT_RAPIDA));

                        return true;
                    }
                });

                popup.show(); //showing popup menu
            }
        });

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final Button btngenfact;

        public ViewHolder(View view) {
            super(view);
            btngenfact = view.findViewById(R.id.btngenfact);
        }
    }

    public interface ClickListener {
        void onClick(Object flete);
    }
}

