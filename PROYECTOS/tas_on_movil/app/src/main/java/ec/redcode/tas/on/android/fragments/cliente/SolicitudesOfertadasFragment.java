package ec.redcode.tas.on.android.fragments.cliente;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ec.redcode.tas.on.android.R;
import ec.redcode.tas.on.android.activities.base.TasFragmen;
import ec.redcode.tas.on.android.adapters.cliente.SolicitudOfertadaRowAdapter;
import ec.redcode.tas.on.android.adapters.conductor.RowEmptyFleteAdapter;
import ec.redcode.tas.on.android.models.Flete;
import ec.redcode.tas.on.android.models.FleteShort;
import ec.redcode.tas.on.android.services.FleteListService;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class SolicitudesOfertadasFragment extends TasFragmen implements SolicitudOfertadaRowAdapter.ClickListener {

    @BindView(R.id.progressbar)
    ProgressBar mProgressBar;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.list)
    RecyclerView listRecycler;

    public SolicitudesOfertadasFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_solicitudes_ofertadas, container, false);
        ButterKnife.bind(this, view);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSolicitudes();
            }
        });
        listRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        getSolicitudes();

        return view;
    }

    private List<Flete> getSolicitudes() {
        refreshLayout.setRefreshing(false);
        FleteListService fleteListService = new FleteListService();
        Observable<List<FleteShort>> observable = fleteListService.getList();
        progress();
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<FleteShort>>() {
                    @Override
                    public void onNext(List<FleteShort> fleteList) {
                        Log.d("respuestas", String.valueOf(fleteList));
                        if (fleteList.size() > 0) {
                            SolicitudOfertadaRowAdapter myOfertsAdapter = new SolicitudOfertadaRowAdapter(fleteList, SolicitudesOfertadasFragment.this);
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
    public void onClick(Object solicitud) {
        OfertadasSolicitudesFragment ofertadasSolicitudes = new OfertadasSolicitudesFragment();
        ofertadasSolicitudes.setIdSolicitud(((FleteShort) solicitud).getId());
        launchFragment(ofertadasSolicitudes);
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
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void done() {
        mProgressBar.setVisibility(View.GONE);
    }
}