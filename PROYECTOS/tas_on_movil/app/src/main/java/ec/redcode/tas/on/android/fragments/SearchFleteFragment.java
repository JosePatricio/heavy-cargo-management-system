package ec.redcode.tas.on.android.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ec.redcode.tas.on.android.R;
import ec.redcode.tas.on.android.activities.conductor.MainActivity;
import ec.redcode.tas.on.android.adapters.conductor.RowEmptyFleteAdapter;
import ec.redcode.tas.on.android.adapters.conductor.RowWeightTimeAdapter;
import ec.redcode.tas.on.android.models.Flete;
import ec.redcode.tas.on.android.models.FleteShort;
import ec.redcode.tas.on.android.services.FleteListService;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class SearchFleteFragment extends Fragment implements RowWeightTimeAdapter.ClickListener, MainActivity.activityActions {

    @BindView(R.id.progressbar)
    ProgressBar mProgressBar;
    private Spinner mSearchSpinner;
    //private SearchView mSearchAutoComplete;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<FleteShort> newFletes;
    private RecyclerView mRecyclerView;

    public SearchFleteFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flete_list, container, false);
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
        mProgressBar.setVisibility(View.VISIBLE);
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<FleteShort>>() {
                    @Override
                    public void onNext(List<FleteShort> fleteList) {
                        Log.d("respuestas", String.valueOf(fleteList));
                        //mRecyclerView.setAdapter(new RowWeightTimeAdapter(fleteList, SearchFleteFragment.this));
                        if (fleteList.size() > 0) {
                            RowWeightTimeAdapter myOfertsAdapter = new RowWeightTimeAdapter(fleteList, SearchFleteFragment.this);
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
                        mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onComplete() {
                        mProgressBar.setVisibility(View.GONE);

                    }
                });

        return null;
    }

    private List<Flete> fillFletesListWithMockupData() {
        List<Flete> fletes = new ArrayList<>();

        String[] mTestArray;
        mTestArray = getResources().getStringArray(R.array.cities);
        for (int i = 0; i < 1; i++) {
            int n = (int) (Math.random() * 220);
            Flete flete = new Flete();
            flete.setOriginCity(mTestArray[n]);
            n = (int) (Math.random() * 220);
            flete.setDestinationCity(mTestArray[n]);
            n = (int) (Math.random() * 150);
            flete.setWeight(n);
            long date = System.currentTimeMillis() + (int) (Math.random() * 100000000);
            flete.setDeliveryDate(date);
            fletes.add(flete);
        }
        return fletes;
    }

    @Override
    public void onClick(Object flete) {
        //Toast.makeText(getContext(), "detalle de: " + flete.getOriginCity(), Toast.LENGTH_SHORT).show();
        FleteDetailFragment fleteDetailFragment = new FleteDetailFragment();
        fleteDetailFragment.setFleteShort((FleteShort) flete);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, fleteDetailFragment).addToBackStack(null).commit();
    }

    @Override
    public void onSearch(String filterType, String searchedText) {
        mRecyclerView.setAdapter(new RowWeightTimeAdapter(filter(filterType, newFletes, searchedText), this));
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
}
