package ec.redcode.tas.on.android.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import ec.redcode.tas.on.android.adapters.conductor.RowWeightTimeAdapter;
import ec.redcode.tas.on.android.models.Flete;
import ec.redcode.tas.on.android.models.FleteShort;
import ec.redcode.tas.on.android.R;
import ec.redcode.tas.on.android.services.ShippingRequestService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * A placeholder fragment containing a simple view.
 */
public class SolicitudActivityFragment extends Fragment implements RowWeightTimeAdapter.ClickListener {

    private RecyclerView mRecyclerView;

    @BindView(R.id.progressbar)
    ProgressBar mProgressBar;

    public SolicitudActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_solicitud, container, false);
        ButterKnife.bind(this, view);
        mRecyclerView = view.findViewById(R.id.list);
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        getShippingRequest();
        return view;
    }

    private List<Flete> getShippingRequest(){
        ShippingRequestService service = new ShippingRequestService();
        Observable<List<FleteShort>> observable = service.getShippingRequestList();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<FleteShort>>() {
                    @Override
                    public void onNext(List<FleteShort> fleteShorts) {
                        Log.d("respuestas", String.valueOf(fleteShorts));
                        mRecyclerView.setAdapter(new RowWeightTimeAdapter(fleteShorts, SolicitudActivityFragment.this));
                        mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.getStackTraceString(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return null;
    }

    @Override
    public void onClick(Object flete) {
        SolicitudDetailFragment detailFragment = new SolicitudDetailFragment();
        detailFragment.setFleteShort((FleteShort)flete);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, detailFragment).addToBackStack(null).commit();
    }
}
