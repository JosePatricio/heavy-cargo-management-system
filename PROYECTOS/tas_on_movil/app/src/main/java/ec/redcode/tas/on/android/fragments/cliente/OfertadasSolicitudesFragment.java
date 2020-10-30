package ec.redcode.tas.on.android.fragments.cliente;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ec.redcode.tas.on.android.R;
import ec.redcode.tas.on.android.activities.base.TasFragmen;
import ec.redcode.tas.on.android.adapters.cliente.OfertadaSolicitudRowAdapter;
import ec.redcode.tas.on.android.adapters.conductor.RowEmptyFleteAdapter;
import ec.redcode.tas.on.android.dto.FleteDTO;
import ec.redcode.tas.on.android.dto.OffersDTO;
import ec.redcode.tas.on.android.dto.requests.OfertUpdateRequestDTO;
import ec.redcode.tas.on.android.fragments.misfletes.MisOfertasDetailFragment;
import ec.redcode.tas.on.android.models.Flete;
import ec.redcode.tas.on.android.models.FleteShort;
import ec.redcode.tas.on.android.services.OffertsService;
import ec.redcode.tas.on.android.services.SolicitudEnvioService;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class OfertadasSolicitudesFragment extends TasFragmen implements OfertadaSolicitudRowAdapter.ClickListener {

    @BindView(R.id.progressbar)
    ProgressBar mProgressBar;
    @BindView(R.id.contentLayout)
    LinearLayout contentLayout;
    @BindView(R.id.oferIdSolic)
    TextView oferIdSolic;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.list)
    RecyclerView listRecycler;

    private String idSolicitud;

    public OfertadasSolicitudesFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ofertadas_solicitud, container, false);
        ButterKnife.bind(this, view);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getOfertasSolicitud();
            }
        });
        listRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        oferIdSolic.setText(idSolicitud);

        getOfertasSolicitud();

        return view;
    }

    private List<Flete> getOfertasSolicitud() {
        refreshLayout.setRefreshing(false);
        OffertsService offertsService = new OffertsService();
        Observable<List<OffersDTO>> observable = offertsService.getSolicitudOfertada(idSolicitud);
        progress();
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<OffersDTO>>() {
                    @Override
                    public void onNext(List<OffersDTO> offersList) {
                        Log.d("respuestas", String.valueOf(offersList));
                        if (offersList.size() > 0) {
                            OfertadaSolicitudRowAdapter myOfertsAdapter = new OfertadaSolicitudRowAdapter(offersList, OfertadasSolicitudesFragment.this);
                            listRecycler.setAdapter(myOfertsAdapter);
                        } else {
                            ArrayList<FleteShort> emptyList = new ArrayList<>();
                            emptyList.add(new FleteShort("0", "Busqueda no generó resultados...", ""));
                            RowEmptyFleteAdapter rowEmptyFleteAdapter = new RowEmptyFleteAdapter(emptyList);
                            listRecycler.setAdapter(rowEmptyFleteAdapter);
                        }
                        //mProgressBar.setVisibility(View.GONE);
                        refreshLayout.setRefreshing(false);
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

        return null;
    }

    @Override
    public void onClick(final Object solicitud, int typeAction) {
        refreshLayout.setRefreshing(false);
        progress();
        OffersDTO offersDTO = (OffersDTO) solicitud;
        if (typeAction == 1) {
            final MisOfertasDetailFragment offertaDetail = new MisOfertasDetailFragment();

            OffertsService offertsService = new OffertsService();
            Observable<FleteDTO> fleteShortObservable = offertsService.getdOfertaById(offersDTO.getIdOferta());
            fleteShortObservable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableObserver<FleteDTO>() {
                        @Override
                        public void onNext(FleteDTO fleteDTO) {

                            offertaDetail.setFlete(fleteDTO);
                            offertaDetail.setBandera(1);
                            done();
                            launchFragment(offertaDetail);
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
        } else if (typeAction == 2) {
            SolicitudEnvioService solicitudEnvioService = new SolicitudEnvioService();
            OfertUpdateRequestDTO envioDTO = new OfertUpdateRequestDTO();
            envioDTO.setIdSolicitud(offersDTO.getIdSolicitud());
            envioDTO.setIdOferta(String.valueOf(offersDTO.getIdOferta()));
            Observable<String> observable = solicitudEnvioService.aceptaSolicitudOferta(envioDTO);
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableObserver<String>() {
                        @Override
                        public void onNext(String responce) {
                            Snackbar snackbar = Snackbar.make(getView(), "Registro exitoso: " + responce, Snackbar.LENGTH_LONG);
                            snackbar.addCallback(new Snackbar.Callback() {
                                @Override
                                public void onDismissed(Snackbar transientBottomBar, int event) {
                                    done();
                                    regresar();
                                }
                            });
                            snackbar.show();
                        }

                        @Override
                        public void onError(Throwable e) {
                            done();
                            if (e.toString().toLowerCase().contains("timeout"))
                                showLongMessage("Lo sentimos ocurrió un error cargando la información," +
                                        " por favor intente nuevamente...");
                            else
                                e.printStackTrace();
                        }

                        @Override
                        public void onComplete() {
                            done();
                        }
                    });
        }
    }

    private void regresar() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    protected void progress() {
        contentLayout.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void done() {
        contentLayout.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    public String getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(String idSolicitud) {
        this.idSolicitud = idSolicitud;
    }
}