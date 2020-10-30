package ec.redcode.tas.on.android.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import ec.redcode.tas.on.android.activities.LoginActivity;
import ec.redcode.tas.on.android.models.Flete;
import ec.redcode.tas.on.android.models.FleteShort;
import ec.redcode.tas.on.android.R;
import ec.redcode.tas.on.android.services.ShippingRequestDetailService;
import ec.redcode.tas.on.android.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class SolicitudDetailFragment extends Fragment {

    public SolicitudDetailFragment() {
    }

    private FleteShort fleteShort;
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
    @BindView(R.id.load_weight_type)
    TextView loadWeightType;
    @BindView(R.id.volume)
    TextView mVolume;
    @BindView(R.id.tipoVolume)
    TextView tipoVolume;
    @BindView(R.id.receiver_name)
    TextView mReceiverName;
    @BindView(R.id.receiver_address)
    TextView mRecieverAddress;
    @BindView(R.id.estiba_num)
    TextView mEstibadores;
    @BindView(R.id.fecha_envio)
    TextView mFechaEnvio;
    @BindView(R.id.fecha_entrega)
    TextView mFechaEntrega;
    @BindView(R.id.ofert_button)
    Button mOfertButton;
    @BindView(R.id.progressbar)
    ProgressBar progressBar;
    @BindView(R.id.cont_main)
    LinearLayout contMain;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_solicitud_detail, container, false);
        ButterKnife.bind(this, view);
        mOfertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getShippingRequestDeatl(fleteShort.getId());
    }

    private void getShippingRequestDeatl(String idSolicitud) {
        progress();
        ShippingRequestDetailService service = new ShippingRequestDetailService();
        Observable<Flete> observable = service.getShippingRequestDetail(idSolicitud);
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Flete>() {
                    @Override
                    public void onNext(Flete flete) {
                        mOriginCity.setText(flete.getOriginCity() == null ? "" : flete.getOriginCity());
                        mDestinationCity.setText(flete.getDestinationCity() == null ? "" : flete.getDestinationCity());
                        mDestinationAddress.setText(flete.getAddress() == null ? "" : flete.getAddress());
                        mSenderName.setText(flete.getSenderName() == null ? "" : flete.getSenderName());
                        mProductType.setText(flete.getProducType() == null ? "" : flete.getProducType());
                        mPiecesNumber.setText(String.valueOf(flete.getQuantityOfPieces()));
                        mLoadWeight.setText(String.valueOf(flete.getWeight()));
                        loadWeightType.setText(flete.getTipoPeso());
                        mVolume.setText(String.valueOf(flete.getVolume()));
                        tipoVolume.setText(flete.getTipoVolumen());
                        mReceiverName.setText(flete.getReceiverName() == null ? "" : flete.getReceiverName());
                        mRecieverAddress.setText(flete.getReceiverAddress() == null ? "" : flete.getReceiverAddress());
                        mEstibadores.setText(flete.getNumeroEstibadores() == null ? "" : flete.getNumeroEstibadores());
                        try {
                            long fechaEnvio = Long.parseLong(flete.getFechaEnvio());
                            mFechaEnvio.setText(Utils.convertDateTime(fechaEnvio));
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "Error en Fecha de Envio", Toast.LENGTH_SHORT).show();
                        }
                        try {
                            long fechaEntrega = Long.parseLong(flete.getFechaEntrega());
                            mFechaEntrega.setText(Utils.convertDateTime(fechaEntrega));
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "Error en Fecha de Entrega", Toast.LENGTH_SHORT).show();
                        }
                        done();
                    }

                    @Override
                    public void onError(Throwable e) {
                        done();
                    }

                    @Override
                    public void onComplete() {
                        done();
                    }
                });

    }

    public void setFleteShort(FleteShort fleteShort) {
        this.fleteShort = fleteShort;
    }

    private void progress() {
        progressBar.setVisibility(View.VISIBLE);
        contMain.setVisibility(View.GONE);

    }

    private void done() {
        progressBar.setVisibility(View.GONE);
        contMain.setVisibility(View.VISIBLE);

    }
}