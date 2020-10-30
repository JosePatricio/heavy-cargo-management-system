package ec.redcode.tas.on.android.fragments.misfletes;


import android.Manifest;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ec.redcode.tas.on.android.R;
import ec.redcode.tas.on.android.Utils;
import ec.redcode.tas.on.android.dto.FleteDTO;
import ec.redcode.tas.on.android.dto.requests.DocumentRequestDTO;
import ec.redcode.tas.on.android.dto.requests.OfertUpdateRequestDTO;
import ec.redcode.tas.on.android.dto.responses.OfertResponseDTO;
import ec.redcode.tas.on.android.fragments.AceptedFleteDetailFragment;
import ec.redcode.tas.on.android.services.OffertsService;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public class PorRecibirDetailFragment extends AceptedFleteDetailFragment {


    @BindView(R.id.origin_city)
    TextView mOriginCity;
    @BindView(R.id.destination_city)
    TextView mDestinationCity;
    @BindView(R.id.destination_address)
    TextView mDestinationAddress;
    @BindView(R.id.sender_name)
    TextView mSenderName;
    @BindView(R.id.sender_contact_phone)
    TextView mSenderContactPhone;
    @BindView(R.id.sender_contact_email)
    TextView mSenderContactEmail;
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
    @BindView(R.id.reofert_button)
    Button mOfertButton;
    @BindView(R.id.spinner_chofer)
    TextView mChoferSpinner;
    @BindView(R.id.spinner_vehiculo)
    TextView mVehiculoSpinner;
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
    ProgressBar mProgressBar;

    @BindView(R.id.origin_pic)
    ImageView mOriginPic;
    @BindView(R.id.observacion)
    TextView observacion;
    @BindView(R.id.recycler_view_img_recoleccion)
    RecyclerView recyclerView;

    private FleteDTO flete;

    public PorRecibirDetailFragment() { }

    public void setFlete(FleteDTO flete) {
        this.flete = flete;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_por_recibir_detail, container, false);
        // Inflate the layout for this fragment
        ButterKnife.bind(this, view);
        //View loader
        mProgressBar = view.findViewById(R.id.progressbar);
        mProgressBar.setVisibility(View.VISIBLE);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setEventsPicture(recyclerView, mOriginPic);

        fillData();

        mOfertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress(mOfertButton);
                if (nuevasFotos == null || nuevasFotos.isEmpty()) {
                    Toast.makeText(getContext(), "Debe tomar al menos una foto de la recolección", Toast.LENGTH_LONG).show();
                    done(mOfertButton);
                    return;
                }

                List<DocumentRequestDTO> imagenesList = new ArrayList<>();
                for(Bitmap bitmap : nuevasFotos){
                    DocumentRequestDTO imagen = new DocumentRequestDTO();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm = Bitmap
                    byte[] byteArrayImage = baos.toByteArray();
                    String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
                    imagen.setFile(encodedImage);
                    imagen.setFileName(System.currentTimeMillis() + "");
                    imagen.setFileSize(byteArrayImage.length);
                    imagen.setFileType("image/jpeg");
                    imagenesList.add(imagen);
                }

                OfertUpdateRequestDTO request = new OfertUpdateRequestDTO();
                request.setFotos(imagenesList);
                request.setIdOferta(flete.getOffer().getIdOferta());



                OffertsService service = new OffertsService();
                Observable<OfertResponseDTO> observable = service.updateMyOfert(request);

                observable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<OfertResponseDTO>() {
                            @Override
                            public void onNext(OfertResponseDTO response) {
                                String messaje = "";
                                if (response.getResponseMessage() != null && !response.getResponseMessage().isEmpty()) {
                                    messaje = response.getResponseMessage();
                                } else {
                                    messaje = response.getResponse();
                                }
                                Toast.makeText(getContext(), messaje, Toast.LENGTH_LONG).show();
                                done(mOfertButton);
                                finish();
                            }

                            @Override
                            public void onError(Throwable e) {
                                done(mOfertButton);
                            }

                            @Override
                            public void onComplete() {
                                done(mOfertButton);
                            }
                        });
            }
        });

        mSenderContactPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PHONE_NUMBER = (String) mSenderContactPhone.getText();
                String[] PERMISSIONS = {Manifest.permission.CALL_PHONE};
                if (!hasPermissions(getContext(), PERMISSIONS)) {
                    ActivityCompat.requestPermissions((Activity) getContext(), PERMISSIONS, CALL_PERMISSION_REQUEST);
                } else {
                    makePhoneCall(PHONE_NUMBER);
                }
            }
        });
    }

    private void progress(Button btnDisable) {
        btnDisable.setClickable(false);
        btnDisable.setEnabled(false);
        getView().findViewById(R.id.cont_main).setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void done(Button btnEnable) {
        btnEnable.setClickable(true);
        btnEnable.setEnabled(true);
        getView().findViewById(R.id.cont_main).setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);

    }

    private void fillData() {
        mOriginCity.setText(flete.getOrigen() == null ? "" : flete.getOrigen());
        mDestinationCity.setText(flete.getDestino() == null ? "" : flete.getDestino());
        mDestinationAddress.setText(flete.getDireccion() == null ? "" : flete.getDireccion());
        mSenderName.setText(flete.getPersonaEntrega() == null ? "" : flete.getPersonaEntrega());
        mSenderContactEmail.setText(flete.getCorreoContacto() == null ? "" : flete.getCorreoContacto());
        mSenderContactPhone.setText(flete.getTelefonoContacto() == null ? "" : flete.getTelefonoContacto());
        mProductType.setText(flete.getTipo() == null ? "" : flete.getTipo());
        mPiecesNumber.setText(String.valueOf(flete.getNumeroPiezas()));
        mLoadWeight.setText(String.valueOf(flete.getPeso()));
        mVolume.setText(String.valueOf(flete.getVolumen()));
        mReceiverName.setText(flete.getPersonaRecibe() == null ? "" : flete.getPersonaRecibe());
        mRecieverAddress.setText(flete.getDireccionEntrega() == null ? "" : flete.getDireccionEntrega());
        mChoferSpinner.setText(flete.getOffer().getConductor() == null ? "" : flete.getOffer().getConductor());
        mVehiculoSpinner.setText(flete.getOffer().getVehiculo() == null ? "" : flete.getOffer().getVehiculo());
        mVolumenUnidad.setText(flete.getTipoVolumen() == null ? "" : flete.getTipoVolumen());
        mEstibadores.setText(flete.getNumeroEstibadores() == null ? "" : flete.getNumeroEstibadores());
        mTipoPeso.setText(flete.getTipoPeso() == null ? "" : flete.getTipoPeso());
        observacion.setText(flete.getObservaciones() == null ? "" : flete.getObservaciones());
        try {
            long fechaEnvio = Long.parseLong(flete.getFechaEnvio());
            mFechaEnvio.setText(Utils.addNewLineBetweenDateTime(Utils.convertDateTime(fechaEnvio)));
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error en fecha de envio", Toast.LENGTH_SHORT).show();
        }
        try {
            long fechaEntrega = Long.parseLong(flete.getFechaEntrega());
            mFechaEntrega.setText(Utils.addNewLineBetweenDateTime(Utils.convertDateTime(fechaEntrega)));
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error en fecha de entrega", Toast.LENGTH_SHORT).show();
        }

        mProgressBar.setVisibility(View.GONE);
    }
}
