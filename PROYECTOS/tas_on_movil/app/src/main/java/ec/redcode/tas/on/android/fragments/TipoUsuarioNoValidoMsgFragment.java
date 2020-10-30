package ec.redcode.tas.on.android.fragments;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import ec.redcode.tas.on.android.R;
import ec.redcode.tas.on.android.activities.LoginActivity;
import ec.redcode.tas.on.android.activities.base.TasCompatActivity;

public class TipoUsuarioNoValidoMsgFragment extends TasCompatActivity {

    private Button mSalirButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo_usuario_no_valido_msg_fragment);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);

      //  this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setTitle("Información");

        mSalirButton = findViewById(R.id.exit_button);
        mSalirButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity(LoginActivity.class);
            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
