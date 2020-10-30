package ec.redcode.tas.on.android.activities.conductor;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import ec.redcode.tas.on.android.adapters.MyAddCityRecyclerViewAdapter;
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

public class AddCityActivity extends AppCompatActivity implements MyAddCityRecyclerViewAdapter.ClickListener {

    private Toolbar mToolbar;

    private Spinner mRegionSpinner;
    private Spinner mProvSpinner;
    private RecyclerView mCitiesRecyclerView;
    private MyAddCityRecyclerViewAdapter myAddCityRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);


        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle("Ciudades");


        mCitiesRecyclerView = findViewById(R.id.list);
        mRegionSpinner = findViewById(R.id.region_spinner);
        mProvSpinner = findViewById(R.id.prov_spinner);

        final ArrayAdapter<String> mRegionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, fillRegionSpinner());
        mRegionAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        mRegionSpinner.setAdapter(mRegionAdapter);

        mRegionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayAdapter<String> mProvAdapter = new ArrayAdapter<>(AddCityActivity.this, android.R.layout.simple_spinner_item, fillProvSpinner(mRegionSpinner.getSelectedItem().toString()));
                mProvAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
                mProvSpinner.setAdapter(mProvAdapter);

                mProvSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        mCitiesRecyclerView.setLayoutManager(new LinearLayoutManager(AddCityActivity.this));
                        List<City> cities = citiesByProv(mProvSpinner.getSelectedItem().toString());
                        myAddCityRecyclerViewAdapter = new MyAddCityRecyclerViewAdapter(AddCityActivity.this, cities, AddCityActivity.this);
                        mCitiesRecyclerView.setAdapter(myAddCityRecyclerViewAdapter);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private List<String> fillRegionSpinner() {
        List<String> regiones = new ArrayList<>();
        for (City city : Constants.cities) {
            if (!regiones.contains(city.getRegion())) {
                regiones.add(city.getRegion());
            }
        }
        return regiones;
    }

    private List<String> fillProvSpinner(String region) {
        List<String> prov = new ArrayList<>();
        for (City city : Constants.cities) {
            if (!prov.contains(city.getProvincia()) && city.getRegion().equals(region)) {
                prov.add(city.getProvincia());
            }
        }
        return prov;
    }

    private List<City> citiesByProv(String prov) {
        List<City> cities = new ArrayList<>();
        for (City city : Constants.cities) {
            if (city.getProvincia().equals(prov)) {
                cities.add(city);
            }
        }
        return cities;
    }

    @Override
    public void onClick(final City city) {
        //TODO: Send city to to server.
        if (city.isLoading()){
            return;
        }

        myAddCityRecyclerViewAdapter.setItemLoading(city.getCode(), true);
        Servicio servicio = new Servicio();
        Observable<Boolean> pediservicio = servicio.pediservicio(city);
        pediservicio
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        city.setInteresting(aBoolean);
                        myAddCityRecyclerViewAdapter.setItemLoading(city.getCode(), false);

                        SharedPreferences sp;
                        sp = PreferenceManager.getDefaultSharedPreferences(AddCityActivity.this);
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putBoolean(city.getCode(), aBoolean);
                        edit.apply();
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
