package ec.redcode.tas.on.android.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ec.redcode.tas.on.android.activities.conductor.AddCityActivity;
import ec.redcode.tas.on.android.adapters.conductor.MyZoneRecyclerViewAdapter;
import ec.redcode.tas.on.android.Constants;
import ec.redcode.tas.on.android.models.City;
import ec.redcode.tas.on.android.R;
import ec.redcode.tas.on.android.services.Servicio;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MyZonesFragment extends Fragment implements MyZoneRecyclerViewAdapter.myZoneAdapterListener{

    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private MyZoneRecyclerViewAdapter myZoneRecyclerViewAdapter;

    public MyZonesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zone_list, container, false);

        fab = view.findViewById(R.id.add_city_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddCityActivity.class);
                startActivity(intent);
            }
        });
        recyclerView = view.findViewById(R.id.list);
        return view;
    }

    private List<City> fillZones(){
        List<City> cities = new ArrayList<>();
        for (City city:Constants.cities) {
            if (city.isInteresting()){
                cities.add(city);
            }
        }
        return cities;
    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        myZoneRecyclerViewAdapter = new MyZoneRecyclerViewAdapter(fillZones(),getContext(), MyZonesFragment.this);
        recyclerView.setAdapter(myZoneRecyclerViewAdapter);
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
    public void onClickDelete(final City city) {
        Servicio servicio = new Servicio();
        Observable<Boolean> pediservicio = servicio.pediservicio(city);
        pediservicio
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        city.setInteresting(aBoolean);

                        SharedPreferences sp;
                        sp = PreferenceManager.getDefaultSharedPreferences(getContext());
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putBoolean(city.getCode(), aBoolean);
                        edit.apply();

                        myZoneRecyclerViewAdapter = new MyZoneRecyclerViewAdapter(fillZones(),getContext(), MyZonesFragment.this);
                        recyclerView.setAdapter(myZoneRecyclerViewAdapter);
                        Toast.makeText(getContext(), city.getName()+ " Borrado", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
