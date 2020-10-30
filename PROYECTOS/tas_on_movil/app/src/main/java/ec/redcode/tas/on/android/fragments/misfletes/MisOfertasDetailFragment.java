package ec.redcode.tas.on.android.fragments.misfletes;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import ec.redcode.tas.on.android.R;
import ec.redcode.tas.on.android.Utils;
import ec.redcode.tas.on.android.dto.FleteDTO;
import ec.redcode.tas.on.android.dto.requests.OfertUpdateRequestDTO;
import ec.redcode.tas.on.android.dto.responses.OfertResponseDTO;
import ec.redcode.tas.on.android.fragments.AceptedFleteDetailFragment;
import ec.redcode.tas.on.android.services.MyOfertListService;
import ec.redcode.tas.on.android.services.OffertsService;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public class MisOfertasDetailFragment extends AceptedFleteDetailFragment {

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
    @BindView(R.id.best_ofert)
    TextView mBestOfert;
    @BindView(R.id.everage_ofert)
    TextView mAverageOfert;
    @BindView(R.id.total_oferts)
    TextView mTotalOfets;
    @BindView(R.id.receiver_address)
    TextView mRecieverAddress;
    @BindView(R.id.my_ofert_edtittext)
    EditText mOfertEditText;
    @BindView(R.id.my_ofert_layout)
    TextInputLayout mOfertLayout;
    @BindView(R.id.comments_edittext)
    EditText mCommentsEditText;
    @BindView(R.id.reofert_button)
    Button mOfertButton;
    @BindView(R.id.cancel_button)
    Button mCancelButton;

    @BindView(R.id.tipoVolume)
    TextView tipoVolume;
    @BindView(R.id.estiba_num)
    TextView mEstibadores;
    @BindView(R.id.fecha_envio)
    TextView mFechaEnvio;
    @BindView(R.id.fecha_entrega)
    TextView mFechaEntrega;
    @BindView(R.id.load_weight_type)
    TextView mTipoPeso;

    ProgressBar mProgressBar;

    @BindView(R.id.writeLayout)
    LinearLayout writeLayout;
    @BindView(R.id.readLayout)
    LinearLayout readLayout;
    @BindView(R.id.regresaButton)
    Button regresaButton;
    @BindView(R.id.observacion)
    TextView observacion;

    private int read = 0;

    public MisOfertasDetailFragment() {
        // Required empty public constructor
    }

    public void setFlete(FleteDTO flete) {
        this.flete = flete;
    }

    public void setBandera(int read) {
        this.read = read;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mis_ofertas_detail, container, false);
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

        fillData();

        mOfertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOfertLayout.setErrorEnabled(false);
                progress(mOfertButton);
                if (mOfertEditText.getText().toString().equals(flete.getOffer().getAmount())
                        && mCommentsEditText.getText().toString().equals(flete.getOffer().getComments())) {
                    mOfertLayout.setError("No ha hecho cambios");
                    done(mOfertButton);
                    return;
                }
                OfertUpdateRequestDTO request = new OfertUpdateRequestDTO();
                request.setIdSolicitud(flete.getIdSolicitud());
                request.setAmount(mOfertEditText.getText().toString());
                request.setComments(mCommentsEditText.getText().toString());
                request.setIdOferta(flete.getOffer().getIdOferta());

                OffertsService service = new OffertsService();
                Observable<OfertResponseDTO> observable = service.updateMyOfert(request);
                observable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<OfertResponseDTO>() {
                            @Override
                            public void onNext(OfertResponseDTO response) {
                                mostrarMensajePopUp(response.getResponseMessage());
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

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress(mCancelButton);
                OfertUpdateRequestDTO request = new OfertUpdateRequestDTO();
                request.setIdOferta(flete.getOffer().getIdOferta());
                request.setState(MyOfertListService.MY_OFERTS_CANCELED);

                OffertsService service = new OffertsService();
                Observable<OfertResponseDTO> observable = service.updateMyOfert(request);
                observable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<OfertResponseDTO>() {
                            @Override
                            public void onNext(OfertResponseDTO response) {
                                String messaje = response.getResponse();
                                if (response.getResponseMessage() != null) {
                                    messaje = response.getResponseMessage();
                                }
                                mostrarMensajePopUp(messaje);
                            }

                            @Override
                            public void onError(Throwable e) {
                                done(mCancelButton);
                            }

                            @Override
                            public void onComplete() {
                                done(mCancelButton);
                            }
                        });
            }
        });


        if (read == 1) {
            writeLayout.setVisibility(View.GONE);
            readLayout.setVisibility(View.VISIBLE);
            regresaButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getFragmentManager().popBackStack();
                }
            });
        }
    }

    private void mostrarMensajePopUp(String mensaje){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(mensaje);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                done(mCancelButton);
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
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
        mProductType.setText(flete.getTipo() == null ? "" : flete.getTipo());
        mPiecesNumber.setText(String.valueOf(flete.getNumeroPiezas()));
        mLoadWeight.setText(String.valueOf(flete.getPeso()));

        mReceiverName.setText(flete.getPersonaRecibe() == null ? "" : flete.getPersonaRecibe());
        mRecieverAddress.setText(flete.getDireccionEntrega() == null ? "" : flete.getDireccionEntrega());
        mBestOfert.setText(flete.getBestOffer() == null ? "" : flete.getBestOffer());
        mAverageOfert.setText(flete.getAverageOffer() == null ? "" : flete.getAverageOffer());
        mTotalOfets.setText(flete.getTotalOffer() == null ? "" : flete.getTotalOffer());
        mOfertEditText.setText(flete.getOffer().getAmount() == null ? "" : flete.getOffer().getAmount());
        mCommentsEditText.setText(flete.getOffer().getComments() == null ? "" : flete.getOffer().getComments());

        mVolume.setText(String.valueOf(flete.getVolumen()));
        tipoVolume.setText(flete.getTipoVolumen() == null ? "" : flete.getTipoVolumen());
        mEstibadores.setText(flete.getNumeroEstibadores() == null ? "" : flete.getNumeroEstibadores());
        mTipoPeso.setText(flete.getTipoPeso() == null ? "" : flete.getTipoPeso());
        observacion.setText(flete.getObservaciones() == null ? "" : flete.getObservaciones());
        try {
            long fechaEnvio = Long.parseLong(flete.getFechaEnvio());
            mFechaEnvio.setText(Utils.addNewLineBetweenDateTime(Utils.convertDateTime(fechaEnvio)));
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error en Fecha de Envio", Toast.LENGTH_SHORT).show();
        }
        try {
            long fechaEntrega = Long.parseLong(flete.getFechaEntrega());
            mFechaEntrega.setText(Utils.addNewLineBetweenDateTime(Utils.convertDateTime(fechaEntrega)));
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error en Fecha de Entrega", Toast.LENGTH_SHORT).show();
        }
        mProgressBar.setVisibility(View.GONE);
    }
}
