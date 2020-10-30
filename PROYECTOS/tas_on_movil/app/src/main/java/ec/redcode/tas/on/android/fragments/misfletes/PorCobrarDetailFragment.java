package ec.redcode.tas.on.android.fragments.misfletes;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ec.redcode.tas.on.android.Constants;
import ec.redcode.tas.on.android.adapters.conductor.ImagentemAdapterRV;
import ec.redcode.tas.on.android.dto.FleteDTO;
import ec.redcode.tas.on.android.fragments.AceptedFleteDetailFragment;
import ec.redcode.tas.on.android.R;
import ec.redcode.tas.on.android.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PorCobrarDetailFragment extends AceptedFleteDetailFragment {

    @BindView(R.id.origin_city)
    TextView mOriginCity;
    @BindView(R.id.destination_city)
    TextView mDestinationCity;
    @BindView(R.id.destination_address)
    TextView mDestinationAddress;
    @BindView(R.id.sender_name)
    TextView mSenderName;
    @BindView(R.id.product_type)
    TextView mProductType;
    @BindView(R.id.pieces_number)
    TextView mPiecesNumber;
    @BindView(R.id.load_weight)
    TextView mLoadWeight;
    @BindView(R.id.volume)
    TextView mVolume;
    @BindView(R.id.receiver_name)
    TextView mReceiverName;
    @BindView(R.id.receiver_address)
    TextView mRecieverAddress;

    @BindView(R.id.volume_unidad)
    TextView mVolumenUnidad;
    @BindView(R.id.estiba_num)
    TextView mEstibadores;
    @BindView(R.id.fecha_envio)
    TextView mFechaEnvio;
    @BindView(R.id.fecha_entrega)
    TextView mFechaEntrega;
    @BindView(R.id.load_weight_type)
    TextView mTipoPeso;

    @BindView(R.id.recycler_view_img_recoleccion)
    RecyclerView recyclerViewRecoleccion;
    @BindView(R.id.recycler_view_img_entrega)
    RecyclerView recyclerViewEntrega;

    @BindView(R.id.loading_photo_recoleccion)
    LinearLayout loadingPhotoRecoleccion;
    @BindView(R.id.loading_photo_entrega)
    LinearLayout loadingPhotoEntrega;

    public PorCobrarDetailFragment() { }

    public void setFlete(FleteDTO flete) {
        this.flete = flete;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_por_cobrar_detail, container, false);
        // Inflate the layout for this fragment
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<Bitmap> fotosPorRecibir = new ArrayList<>();
        ImagentemAdapterRV adapaterRecoleccion = new ImagentemAdapterRV(getContext(), fotosPorRecibir, false);
        askForPicture(Constants.ESTADO_FOTO_POR_RECIBIR, fotosPorRecibir, adapaterRecoleccion, loadingPhotoRecoleccion);
        asignarReciclerViewHorizontal(recyclerViewRecoleccion, adapaterRecoleccion);

        List<Bitmap> fotosPorEntregar = new ArrayList<>();
        ImagentemAdapterRV adapaterEntrega = new ImagentemAdapterRV(getContext(), fotosPorEntregar, false);
        askForPicture(Constants.ESTADO_FOTO_POR_ENTREGAR, fotosPorEntregar, adapaterEntrega, loadingPhotoEntrega);
        asignarReciclerViewHorizontal(recyclerViewEntrega, adapaterEntrega);

        fillData();

    }

    private void fillData() {
        mOriginCity.setText(flete.getOrigen() == null ? "" : flete.getOrigen());
        mDestinationCity.setText(flete.getDestino() == null ? "" : flete.getDestino());
//        Long fechaEnvio = (Long) (flete.getFechaEnvio() == "null" ? 0:flete.getFechaEnvio());
//        mDepartureDate.setText(  fechaEnvio == null? "" : Utils.convertTimeHour(fechaEnvio));
        mDestinationAddress.setText(flete.getDireccion() == null ? "" : flete.getDireccion());
        mSenderName.setText(flete.getPersonaEntrega() == null ? "" : flete.getPersonaEntrega());
        mProductType.setText(flete.getTipo() == null ? "" : flete.getTipo());
        mPiecesNumber.setText(String.valueOf(flete.getNumeroPiezas()));
        mLoadWeight.setText(String.valueOf(flete.getPeso()));
        mVolume.setText(String.valueOf(flete.getVolumen()));
        mReceiverName.setText(flete.getPersonaRecibe() == null ? "" : flete.getPersonaRecibe());
        mRecieverAddress.setText(flete.getDireccionEntrega() == null ? "" : flete.getDireccionEntrega());

        mVolumenUnidad.setText(flete.getTipoVolumen() == null ? "" : flete.getTipoVolumen());
        mEstibadores.setText(flete.getNumeroEstibadores() == null ? "" : flete.getNumeroEstibadores());
        mTipoPeso.setText(flete.getTipoPeso() == null ? "" : flete.getTipoPeso());
        try {
            long fechaEnvio = Long.parseLong(flete.getFechaEnvio());
            mFechaEnvio.setText(Utils.addNewLineBetweenDateTime(Utils.convertDateTime(fechaEnvio)));
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error en la fecha de envio", Toast.LENGTH_SHORT).show();
        }
        try {
            long fechaEntrega = Long.parseLong(flete.getFechaEntrega());
            mFechaEntrega.setText(Utils.addNewLineBetweenDateTime(Utils.convertDateTime(fechaEntrega)));
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error en la fecha de entrega", Toast.LENGTH_SHORT).show();
        }
    }
}
