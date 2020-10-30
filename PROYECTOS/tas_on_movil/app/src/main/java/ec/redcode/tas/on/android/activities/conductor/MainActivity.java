package ec.redcode.tas.on.android.activities.conductor;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;

import ec.redcode.tas.on.android.Constants;
import ec.redcode.tas.on.android.R;
import ec.redcode.tas.on.android.Utils;
import ec.redcode.tas.on.android.activities.base.TasCompatActivity;
import ec.redcode.tas.on.android.fragments.ebilling.EBillingFragment;
import ec.redcode.tas.on.android.activities.main.MainDashboardActivity;
import ec.redcode.tas.on.android.dto.SolicitudDTO;
import ec.redcode.tas.on.android.fragments.FleteDetailFragment;
import ec.redcode.tas.on.android.fragments.FleteTabbedContainerFragment;
import ec.redcode.tas.on.android.fragments.MyZonesFragment;
import ec.redcode.tas.on.android.fragments.SearchFleteFragment;
import ec.redcode.tas.on.android.fragments.TermsAndCondtitionsFragment;
import ec.redcode.tas.on.android.fragments.ebilling.MyEbillingsFragment;
import ec.redcode.tas.on.android.mappers.FleteListMapper;
import ec.redcode.tas.on.android.services.FleteListService;
import ec.redcode.tas.on.android.services.SolicitudEnvioService;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends TasCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {

    private FragmentManager fragmentManager;
    private LinearLayout mContent;

    private SearchFleteFragment searchFleteFragment;
    private FleteTabbedContainerFragment fleteTabbedContainerFragment;
    private EBillingFragment eBillingFragment;
    private MyEbillingsFragment myEbillingsFragment;
    private MyZonesFragment myZonesFragment;
    private TermsAndCondtitionsFragment termsAndCondtitionsFragment;
    private activityActions mInterfaceActivityActions;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ProgressBar progressbar;
    private MenuItem menuFacturacionElectronica;

    private boolean startFlag = true;

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Utils.fillCitiesMockUpData(this);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_main);

        this.suscribeTasOnNotifi();
        this.loadFromNotification();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!Utils.isNetworkAvaliable(this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Se ha desconectado de internet...")
                    .setTitle("Error");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if (!Utils.isNetworkAvaliable(MainActivity.this))
                        finish();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.setCancelable(false);
            dialog.show();
        }
        sp = getSharedPreferences("login", Activity.MODE_PRIVATE);

        String token = sp.getString("token", "");
        checkTheToken(token);
    }

    @Override
    public void notificationManage() {
        if (getTypeNotification() != null)
            if (getTypeNotification().equals(NUEVA_NOTIFICATION)) {
                Toast.makeText(this, "Tipo: " + getTypeNotification() + " IdSolicitud: " + getIdSolicitud(), Toast.LENGTH_SHORT).show();
                SolicitudEnvioService solicitudEnvioService = new SolicitudEnvioService();
                Observable<SolicitudDTO> observable = solicitudEnvioService.getSolicitudByID(getIdSolicitud());
                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<SolicitudDTO>() {
                            @Override
                            protected void onStart() {
                                super.onStart();
                            }

                            @Override
                            public void onNext(SolicitudDTO solicitudDTO) {
                                FleteDetailFragment fleteDetailFragment = new FleteDetailFragment();
                                fleteDetailFragment.setFleteShort(FleteListMapper.responseToFleteList(solicitudDTO));
                                getSupportFragmentManager().beginTransaction().replace(R.id.content, fleteDetailFragment).addToBackStack(null).commit();
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

    @Override
    public void checkTheToken(String token) {
        FleteListService fleteListService = new FleteListService();
        Observable<Boolean> observable = fleteListService.checkLogin(token);
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            setupActivity();
                            int tipo = getTipo();
                            if (Utils.isValidUser(tipo, Utils.DRIVER)) {
                                if (startFlag) {
                                    loadInitialFragment();
                                    startFlag = false;
                                }
                                return;
                            } else if (Utils.isValidUser(tipo, Utils.CUSTOMER)) {
                                launchActivity(MainDashboardActivity.class);
                                return;
                            }

                        }
                        reloginUser();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Check Token", "No se pudo verificar", e);
                        reloginUser();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void setupActivity() {
        progressbar = findViewById(R.id.progress1);

        getConstUser();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mContent = findViewById(R.id.content);
        fragmentManager = getSupportFragmentManager();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout = navigationView.getHeaderView(0);
        TextView userNameOnHeader = headerLayout.findViewById(R.id.user_name);
        userNameOnHeader.setText(Constants.user.getNombres());
        mostrarMenuFacturacionElectronica(navigationView.getMenu());
    }

    @Override
    public void loadInitialFragment() {
        searchFleteFragment = new SearchFleteFragment();
        mInterfaceActivityActions = searchFleteFragment;
        try {
            fragmentManager.beginTransaction().replace(R.id.content, searchFleteFragment).commit();
            getSupportActionBar().setTitle("Fletes");
            Toast.makeText(this, "Bienvenido " + Constants.user.getNombres(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.w("Error al cargar : ", e);
        }
        mContent.removeView(progressbar);
    }

    private void mostrarMenuFacturacionElectronica(Menu menu){
        try{
            menuFacturacionElectronica = menu.findItem(R.id.menu_facturacion_electronica);
            if(menuFacturacionElectronica != null && getTipo()==Constants.ID_USUARIO_CONDUCTOR_CLIENTE){
                menuFacturacionElectronica.setVisible(true);
            }
        } catch (Exception e){
            Log.w("Error mostrar menu: ", e);
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            fragmentManager.popBackStack();
            //super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout, getResources().getStringArray(R.array.search_parameters));
        stringArrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_search_freight) {
            searchFleteFragment = new SearchFleteFragment();
            replaseFragment(searchFleteFragment, "Fletes");
        } else if (id == R.id.nav_my_freights) {
            fleteTabbedContainerFragment = new FleteTabbedContainerFragment();
            replaseFragment(fleteTabbedContainerFragment, "Mis Fletes");
        } else if (id == R.id.nav_e_billing) {
            eBillingFragment = new EBillingFragment();
            replaseFragment(eBillingFragment, "Facturación electrónica");
        } else if (id == R.id.nav_my_billings) {
            myEbillingsFragment = new MyEbillingsFragment();
            replaseFragment(myEbillingsFragment, "Mis facturas electrónicas");
        } else if (id == R.id.nav_terms_and_conditions) {
            termsAndCondtitionsFragment = new TermsAndCondtitionsFragment();
            replaseFragment(termsAndCondtitionsFragment, "Términos y Condiciones");
        } else if (id == R.id.nav_close_sesion) {

            closeSession();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void replaseFragment(Fragment fragment, String title) {
        fragmentManager.beginTransaction().replace(R.id.content, fragment).commit();
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    public interface activityActions {
        void onSearch(String filterType, String searchedText);
    }
}
