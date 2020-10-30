package ec.redcode.tas.on.android.adapters.conductor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import java.util.List;

import ec.redcode.tas.on.android.PhotoFullPopupWindow;
import ec.redcode.tas.on.android.R;

public class ImagentemAdapterRV extends RecyclerView.Adapter<ImagentemAdapterRV.ViewHolder>  {

    private List<Bitmap> imagenes;
    private Context conntextRV;
    private boolean eliminarImagenes;
    protected AlertDialog dialog;
    private int imagenSeleccionada;

    public ImagentemAdapterRV(Context conntextRV, List<Bitmap> imagenes, boolean eliminarImagenes) {
        this.imagenes = imagenes;
        this.conntextRV = conntextRV;
        this.eliminarImagenes = eliminarImagenes;
        crearMenu();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_imagen, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //Glide.with(conntextRV).asBitmap().load(imagenes.get(position)).into(holder.imageView);
        holder.imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        holder.imageView.setImageBitmap(imagenes.get(position));

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PhotoFullPopupWindow(conntextRV, R.layout.popup_photo_full, holder.imageView, null, (imagenes.get(holder.getAdapterPosition())));

            }
        });

        holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(!eliminarImagenes) return false;
                imagenSeleccionada = holder.getAdapterPosition();
                dialog.show();
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return imagenes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;

        public ViewHolder(View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.imagen);
        }
    }

    private void crearMenu(){
        if(!this.eliminarImagenes) return;
        // Array con las opciones para el diálogo que se abrirá al pulsar el botón "PIC"
        final String[] items = new String[]{"Eliminar", "Cancelar"};
        // Creamos el diálogo
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(conntextRV, android.R.layout.select_dialog_item, items);
        AlertDialog.Builder builder = new AlertDialog.Builder(conntextRV);
        builder.setTitle("¿Desea eliminar la imagen?");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    // Opción eliminar
                    eliminarImagen();
                    dialog.cancel();
                } else {
                    // Opción cancelar
                    dialog.cancel();

                }
            }
        });

        dialog = builder.create();
    }

    private void eliminarImagen(){
        imagenes.remove(imagenSeleccionada);
        notifyDataSetChanged();
    }

}
