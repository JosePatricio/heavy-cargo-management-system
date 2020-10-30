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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ec.redcode.tas.on.android.R;
import ec.redcode.tas.on.android.activities.base.TasFragmen;
import ec.redcode.tas.on.android.adapters.cliente.SolicitudIngresadaRowAdapter;
import ec.redcode.tas.on.android.adapters.conductor.RowEmptyFleteAdapter;
import ec.redcode.tas.on.android.models.Flete;
import ec.redcode.tas.on.android.models.FleteShort;
import ec.redcode.tas.on.android.services.FleteListService;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class SolicitudIngresadaFragment extends TasFragmen implements SolicitudIngresadaRowAdapter.ClickListener {

    @BindView(R.id.progressbar)
    ProgressBar mProgressBar;
    @BindView(R.id.mainLayout)
    LinearLayout mainLayout;
    @BindView(R.id.nuevaSolidictud)
    Button nuevaSolidictud;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    public SolicitudIngresadaFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_solicitud_ingresada, container, false);
        ButterKnife.bind(this, view);
        mSwipeRefreshLayout = view.findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFletes();
            }
        });
        mRecyclerView = view.findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        getFletes();

        nuevaSolidictud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegistraSolicitudFragment registraSolicitud = new RegistraSolicitudFragment();
                launchFragment(registraSolicitud);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private List<Flete> getFletes() {
        mSwipeRefreshLayout.setRefreshing(false);
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
                        //mRecyclerView.setAdapter(new RowWeightTimeAdapter(fleteList, SearchFleteFragment.this));
                        if (fleteList.size() > 0) {
                            SolicitudIngresadaRowAdapter myOfertsAdapter = new SolicitudIngresadaRowAdapter(fleteList, SolicitudIngresadaFragment.this);
                            mRecyclerView.setAdapter(myOfertsAdapter);
                        } else {
                            ArrayList<FleteShort> emptyList = new ArrayList<>();
                            emptyList.add(new FleteShort("0", "Busqueda no generó resultados...", ""));
                            RowEmptyFleteAdapter rowEmptyFleteAdapter = new RowEmptyFleteAdapter(emptyList);
                            mRecyclerView.setAdapter(rowEmptyFleteAdapter);
                        }
                        //mProgressBar.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(false);
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
        RegistraSolicitudFragment registraSolicitud = new RegistraSolicitudFragment();
        registraSolicitud.setIdSolicitud(((FleteShort) solicitud).getId());
        launchFragment(registraSolicitud);
    }

    private List<FleteShort> filter(String filterType, List<FleteShort> mList, String query) {

        query = query.toLowerCase();
        final List<FleteShort> filteredList = new ArrayList<>();

        if (filterType.equals("Destino")) {
            for (FleteShort flete : mList) {
                if (flete.getDestinationCity().toLowerCase().contains(query)) {
                    filteredList.add(flete);
                }
            }
        } else {
            for (FleteShort flete : mList) {
                if (flete.getOriginCity().toLowerCase().contains(query)) {
                    filteredList.add(flete);
                }
            }
        }

        return filteredList;
    }

    @Override
    protected void progress() {
        mProgressBar.setVisibility(View.VISIBLE);
        mainLayout.setVisibility(View.GONE);
    }

    @Override
    protected void done() {
        mProgressBar.setVisibility(View.GONE);
        mainLayout.setVisibility(View.VISIBLE);
    }
}
