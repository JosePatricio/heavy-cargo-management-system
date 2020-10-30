package ec.redcode.tas.on.android.activities.main;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;

import ec.redcode.tas.on.android.Constants;
import ec.redcode.tas.on.android.R;
import ec.redcode.tas.on.android.Utils;
import ec.redcode.tas.on.android.activities.base.TasCompatActivity;
import ec.redcode.tas.on.android.fragments.TermsAndCondtitionsFragment;
import ec.redcode.tas.on.android.fragments.cliente.SolicitudIngresadaFragment;
import ec.redcode.tas.on.android.fragments.cliente.SolicitudesOfertadasFragment;
import ec.redcode.tas.on.android.fragments.main.HomeDashFragment;

public class MainDashboardActivity extends TasCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment contentFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Utils.fillCitiesMockUpData(this);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_main_dashboard);
        drawer = findViewById(R.id.main_dash);

        this.suscribeTasOnNotifi();
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
                    if (!Utils.isNetworkAvaliable(MainDashboardActivity.this))
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
    public void loadInitialFragment() {
        LoadNavMenu(R.menu.main_dash_client);
        contentFragment = new HomeDashFragment();
        try {
            fragmentManager.beginTransaction().replace(R.id.content, contentFragment).commit();
            getSupportActionBar().setTitle("TAS-ON");
            Toast.makeText(this, "Bienvenido " + Constants.user.getNombres(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.w("Error al cargar : ", e);
        }
        mContent.removeView(progressbar);
    }

    @Override
    public void setupActivity() {
        progressbar = findViewById(R.id.progress1);

        getConstUser();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mContent = findViewById(R.id.content);
        fragmentManager = getSupportFragmentManager();

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
    }

    @Override
    public void onBackPressed() {
        /*DrawerLayout drawer = findViewById(R.id.drawer_layout);*/
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            fragmentManager.popBackStack();
            //super.onBackPressed();
        }
    }

    /**
     * @Override public boolean onCreateOptionsMenu(Menu menu) {
     * // Inflate the menu; this adds items to the action bar if it is present.
     * MenuInflater inflater = getMenuInflater();
     * inflater.inflate(R.menu.main_dash_client, menu);
     * return true;
     * }
     * @Override public boolean onOptionsItemSelected(MenuItem item) {
     * switch (item.getItemId()) {
     * case R.id.nav_search_freight:
     * // save it here
     * return true;
     * default:
     * return super.onOptionsItemSelected(item);
     * }
     * }
     * @SuppressWarnings("StatementWithEmptyBody")
     * @Override public boolean onNavigationItemSelected(MenuItem item) {
     * int id = item.getItemId();
     * <p>
     * if (id == R.id.nav_search_freight) {
     * //mSearchSpinner.setVisibility(View.VISIBLE);
     * //itemSearch.setVisible(true);
     * homeDashFragment = new HomeDashFragment();
     * replaseFragment(homeDashFragment, "User Dashboard");
     * } else if (id == R.id.nav_my_freights) {
     * //mSearchSpinner.setVisibility(View.VISIBLE);
     * //itemSearch.setVisible(true);
     * fleteTabbedContainerFragment = new FleteTabbedContainerFragment();
     * replaseFragment(fleteTabbedContainerFragment, "Mis Fletes");
     * } else if (id == R.id.nav_terms_and_conditions) {
     * //itemSearch.setVisible(false);
     * //mSearchSpinner.setVisibility(View.GONE);
     * termsAndCondtitionsFragment = new TermsAndCondtitionsFragment();
     * replaseFragment(termsAndCondtitionsFragment, "Terminos y Condiciones");
     * } else if (id == R.id.nav_close_sesion) {
     * <p>
     * closeSession();
     * }
     * <p>
     * System.out.println(item.getTitle());
     * <p>
     * drawer.closeDrawer(GravityCompat.START);
     * return true;
     * }
     **/

    public void LoadNavMenu(int iMenu) {
        navigationView.getMenu().clear();
        navigationView.inflateMenu(iMenu);

        NavigationMenuView navMenuView = (NavigationMenuView) navigationView.getChildAt(0);
        navMenuView.addItemDecoration(new DividerItemDecoration(MainDashboardActivity.this, DividerItemDecoration.VERTICAL));

        closeGroups();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int idItem = item.getItemId();

        closeGroups();

        groupOpen(item);

        switch (idItem) {
            case R.id.solicidNew:
                contentFragment = new SolicitudIngresadaFragment();
                replaseFragment(contentFragment, "Solicitudes");
                closeMenu();
                break;
            case R.id.solicidOffer:
                contentFragment = new SolicitudesOfertadasFragment();
                replaseFragment(contentFragment, "Solicitudes");
                closeMenu();
                break;
            /*case R.id.tokenGen:
                Intent intent = new Intent(getApplicationContext(), GeneraTokenActivity.class);
                startActivity(intent);
                closeMenu();
                break;*/
            case R.id.termsConditions:
                contentFragment = new TermsAndCondtitionsFragment();
                replaseFragment(contentFragment, "Terminos y Condiciones");
                closeMenu();
                break;
            case R.id.nav_close_sesion:
                closeSession();
                break;
        }
        return true;
    }

    private void closeMenu() {
        DrawerLayout drawer = findViewById(R.id.main_dash);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void groupOpen(@NonNull MenuItem item) {
        int idgrp = item.getGroupId() > 0 ? item.getGroupId() : item.getItemId();
        switch (idgrp) {
            case R.id.solicitudGroup:
            case R.id.solicitudItem:
                navigationView.getMenu().setGroupVisible(R.id.solicitudGroup, true);
                /*navigationView.getMenu().setGroupVisible(R.id.utilidadesGroup, false);*/
                break;
            /*case R.id.utilidadesGroup:
            case R.id.utilidadesItem:
                navigationView.getMenu().setGroupVisible(R.id.solicitudGroup, false);
                navigationView.getMenu().setGroupVisible(R.id.utilidadesGroup, true);
                break;*/
            default:
                break;
        }
    }

    private void closeGroups() {
        navigationView.getMenu().setGroupVisible(R.id.solicitudGroup, false);
        /*navigationView.getMenu().setGroupVisible(R.id.utilidadesGroup, false);*/
    }

    public void replaseFragment(Fragment fragment, String title) {
        fragmentManager.beginTransaction().replace(R.id.content, fragment).commit();
        getSupportActionBar().setTitle(title);
    }

    public interface activityActions {
        void onSearch(String filterType, String searchedText);
    }
}
