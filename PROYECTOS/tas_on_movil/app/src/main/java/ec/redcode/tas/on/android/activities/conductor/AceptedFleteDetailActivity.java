package ec.redcode.tas.on.android.activities.conductor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import ec.redcode.tas.on.android.activities.base.TasCompatActivity;
import ec.redcode.tas.on.android.dto.FleteDTO;
import ec.redcode.tas.on.android.fragments.misfletes.AprobadoDetailFragment;
import ec.redcode.tas.on.android.fragments.misfletes.MisOfertasDetailFragment;
import ec.redcode.tas.on.android.fragments.misfletes.PorCobrarDetailFragment;
import ec.redcode.tas.on.android.fragments.misfletes.PorEntregarDetailFragment;
import ec.redcode.tas.on.android.fragments.misfletes.PorRecibirDetailFragment;
import ec.redcode.tas.on.android.fragments.misfletes.TerminadorDetailFragment;
import ec.redcode.tas.on.android.R;
import ec.redcode.tas.on.android.services.MyOfertListService;
import ec.redcode.tas.on.android.services.MyOfertService;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class AceptedFleteDetailActivity extends TasCompatActivity {

    public static final String MY_FLETE = "my_flete";
    private LinearLayout mContent;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acepted_flete_detail);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mContent = findViewById(R.id.content);

        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle(getTitle());

        Intent intent = getIntent();
        String stringExtra = intent.getStringExtra(MY_FLETE);
        MyOfertService service = new MyOfertService();
        Observable<FleteDTO> observable = service.getDetail(stringExtra);
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<FleteDTO>() {
                    @Override
                    public void onNext(FleteDTO fleteDTO) {
                        mToolbar.setTitle("Solicitud No.: " + fleteDTO.getIdSolicitud());
                        findViewById(R.id.progressbar).setVisibility(View.GONE);
                        switch (fleteDTO.getOffer().getState()) {
                            case MyOfertListService.MY_OFERTS:
                                MisOfertasDetailFragment misOfertasDetailFragment = new MisOfertasDetailFragment();
                                misOfertasDetailFragment.setFlete(fleteDTO);
                                checkStateSupport(R.id.content, misOfertasDetailFragment);
                                break;
                            case MyOfertListService.MY_OFERTS_APPROVED:
                                AprobadoDetailFragment aprobadoFragment = new AprobadoDetailFragment();
                                aprobadoFragment.setFlete(fleteDTO);
                                checkStateSupport(R.id.content, aprobadoFragment);
                                break;
                            case MyOfertListService.MY_OFERTS_TO_RECIEVE:
                                PorRecibirDetailFragment porRecibirFragment = new PorRecibirDetailFragment();
                                porRecibirFragment.setFlete(fleteDTO);
                                checkStateSupport(R.id.content, porRecibirFragment);
                                break;
                            case MyOfertListService.MY_OFERTS_TO_DELIVER:
                                PorEntregarDetailFragment porEntregarFragment = new PorEntregarDetailFragment();
                                porEntregarFragment.setFlete(fleteDTO);
                                checkStateSupport(R.id.content, porEntregarFragment);
                                break;
                            case MyOfertListService.MY_OFERTS_GENERATE_INVOICE:
                                porEntregarFragment = new PorEntregarDetailFragment();
                                porEntregarFragment.setFlete(fleteDTO);
                                checkStateSupport(R.id.content, porEntregarFragment);
                                break;
                            case MyOfertListService.MY_OFERTS_COLLECT:
                                PorCobrarDetailFragment porCobrarFragment = new PorCobrarDetailFragment();
                                porCobrarFragment.setFlete(fleteDTO);
                                checkStateSupport(R.id.content, porCobrarFragment);
                                break;
                            case MyOfertListService.MY_OFERTS_FINISHED:
                                TerminadorDetailFragment terminadoFragment = new TerminadorDetailFragment();
                                terminadoFragment.setFlete(fleteDTO);
                                getSupportFragmentManager().beginTransaction().replace(R.id.content, terminadoFragment).commit();
                                break;
                            default:
                                Toast.makeText(getApplicationContext(), "Lo sentimos ocurrió un error cargando este Flete...", Toast.LENGTH_LONG).show();
                                finish();
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        finish();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}