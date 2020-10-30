package ec.redcode.tas.on.android.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import ec.redcode.tas.on.android.R;
import ec.redcode.tas.on.android.Utils;
import ec.redcode.tas.on.android.models.Flete;
import ec.redcode.tas.on.android.models.FleteShort;
import ec.redcode.tas.on.android.services.FleteDetailService;
import ec.redcode.tas.on.android.services.OffertsService;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class FleteDetailFragment extends Fragment {

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
    @BindView(R.id.dias_pagos)
    TextView mDiasPagos;

    @BindView(R.id.receiver_address)
    TextView mRecieverAddress;
    @BindView(R.id.my_ofert_edtittext)
    EditText mOfertEditText;
    @BindView(R.id.my_ofert_layout)
    TextInputLayout mOfertLayout;
    @BindView(R.id.comments_edittext)
    EditText mCommentsEditText;
    @BindView(R.id.ofert_button)
    Button mOfertButton;

    @BindView(R.id.tipoVolume)
    TextView tipoVolume;
    @BindView(R.id.estiba_num)
    TextView mEstibadores;
    @BindView(R.id.fecha_envio)
    TextView mFechaEnvio;
    @BindView(R.id.fecha_entrega)
    TextView mFechaEntrega;
    @BindView(R.id.load_weight_type)
    TextView mPesoTipo;

    @BindView(R.id.progressbar)
    ProgressBar mProgressBar;
    @BindView(R.id.cont_main)
    LinearLayout contMain;

    @BindView(R.id.observacion)
    TextView observacion;

    private FleteShort fleteShort;
    private Flete fleteDetail;

    public FleteDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flete_detail, container, false);
        ButterKnife.bind(this, view);

        mOfertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cleanFilds();
                Utils.hideKeyboard(getActivity());
                progress(mOfertButton);
                if (checkFilds()) {
                    sendMyOfert(Float.valueOf(mOfertEditText.getText().toString()), mCommentsEditText.getText().toString());
                } else {
                    done(mOfertButton);
                }
            }
        });

        //View loader
        progress();
        return view;
    }

    private void cleanFilds() {
        mOfertLayout.setErrorEnabled(false);
    }

    private boolean checkFilds() {
        boolean response = false;
        if (mOfertEditText.getText().toString().length() > 0) {

            try {
                if (Float.parseFloat(mOfertEditText.getText().toString()) > 0) {
                    response = true;
                } else {
                    mOfertLayout.setError("Campo Obligatorio");
                }
            } catch (Exception e) {
                mOfertLayout.setError("Campo Obligatorio");
            }
        } else {
            mOfertLayout.setError("Campo Obligatorio");
        }
        return response;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getFleteDetail(fleteShort.getId());
    }

    private void sendMyOfert(float myOfert, String comments) {
        progress();
        OffertsService service = new OffertsService();
        Observable<Map<String, String>> observable = service.sendMyOfert(fleteShort.getId(), myOfert, comments);

        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Map<String, String>>() {
                    @Override
                    public void onNext(Map<String, String> s) {
                        if(getContext()== null)return;
                        Toast.makeText(getContext(), s.get("responseMessage"), Toast.LENGTH_LONG).show();
                        done(mOfertButton);
                        getFragmentManager().popBackStack();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        done(mOfertButton);
                    }

                    @Override
                    public void onComplete() {
                        done(mOfertButton);
                    }
                });
    }

    private void progress(Button btnDisable) {
        btnDisable.setClickable(false);
        btnDisable.setEnabled(false);
        progress();
    }

    private void progress() {
        mProgressBar.setVisibility(View.VISIBLE);
        contMain.setVisibility(View.GONE);
    }

    private void done(Button btnEnable) {
        btnEnable.setClickable(true);
        btnEnable.setEnabled(true);
        done();

    }

    private void done() {
        mProgressBar.setVisibility(View.GONE);
        contMain.setVisibility(View.VISIBLE);
    }

    private void getFleteDetail(String id) {
        progress();
        FleteDetailService service = new FleteDetailService();
        Observable<Flete> observable = service.getFlete(id);

        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Flete>() {
                    @Override
                    public void onNext(Flete flete) {
                        fleteDetail = flete;
                        mOriginCity.setText(flete.getOriginCity() == null ? "" : flete.getOriginCity());
                        mDestinationCity.setText(flete.getDestinationCity() == null ? "" : flete.getDestinationCity());
                        mDestinationAddress.setText(flete.getAddress() == null ? "" : flete.getAddress());
                        mSenderName.setText(flete.getSenderName() == null ? "" : flete.getSenderName());
                        mProductType.setText(flete.getProducType() == null ? "" : flete.getProducType());
                        mPiecesNumber.setText(String.valueOf(flete.getQuantityOfPieces()));
                        mLoadWeight.setText(String.valueOf(flete.getWeight()));
                        mReceiverName.setText(flete.getReceiverName() == null ? "" : flete.getReceiverName());
                        mRecieverAddress.setText(flete.getReceiverAddress() == null ? "" : flete.getReceiverAddress());
                        mBestOfert.setText(flete.getBestOffer() == null ? "" : flete.getBestOffer());
                        mAverageOfert.setText(flete.getAverageOffer() == null ? "" : flete.getAverageOffer());
                        mTotalOfets.setText(flete.getTotalOffer() == null ? "" : flete.getTotalOffer());
                        mDiasPagos.setText("* " + String.valueOf(flete.getDiasPago() == null ? 0 : flete.getDiasPago()));
                        mVolume.setText(String.valueOf(flete.getVolume()));
                        tipoVolume.setText(flete.getTipoVolumen() == null ? "" : flete.getTipoVolumen());
                        mEstibadores.setText(flete.getNumeroEstibadores() == null ? "" : flete.getNumeroEstibadores());
                        mPesoTipo.setText(flete.getTipoPeso() == null ? "" : flete.getTipoPeso());
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
}
