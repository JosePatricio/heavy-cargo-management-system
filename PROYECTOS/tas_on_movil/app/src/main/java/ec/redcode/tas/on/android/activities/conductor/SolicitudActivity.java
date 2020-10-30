package ec.redcode.tas.on.android.activities.conductor;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import ec.redcode.tas.on.android.fragments.SolicitudActivityFragment;
import ec.redcode.tas.on.android.R;

public class SolicitudActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitud);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        loadInitialFragment();
    }

    private void loadInitialFragment() {
        SolicitudActivityFragment fragment = new SolicitudActivityFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content, fragment).commit();
    }

}
