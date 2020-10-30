package ec.redcode.tas.on.android.activities.conductor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import ec.redcode.tas.on.android.R;
import ec.redcode.tas.on.android.activities.base.TasCompatActivity;
import ec.redcode.tas.on.android.fragments.GeneraFacturaFlete;

public class GeneraFacturaFleteActivity extends TasCompatActivity {
    private Toolbar mToolbar;
    public static final String ID_PREFACT = "idPrefact";
    public static final String PREFACT = "tipoPrefact";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genera_factura_flete);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle(getTitle());

        GeneraFacturaFlete generaFacturaFlete = new GeneraFacturaFlete();
        Intent intent = getIntent();
        String paramCod = intent.getStringExtra(ID_PREFACT);
        if (paramCod != null) {
            generaFacturaFlete.setCodeFact(paramCod);
            getSupportActionBar().setTitle("Prefact. No. " + paramCod);
        }
        paramCod = intent.getStringExtra(PREFACT);
        if (paramCod != null) {
            generaFacturaFlete.setTypeFact(paramCod);
            getSupportActionBar().setTitle(getText(R.string.genera_factura) + " pago inmediato");
        }

        checkStateSupport(R.id.content, generaFacturaFlete);
    }

}
