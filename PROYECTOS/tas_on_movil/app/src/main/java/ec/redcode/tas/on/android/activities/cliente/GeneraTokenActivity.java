package ec.redcode.tas.on.android.activities.cliente;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import ec.redcode.tas.on.android.R;

public class GeneraTokenActivity extends AppCompatActivity {

    Button tokenGenera;
    Button tokenMen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genera_token);

        tokenGenera=findViewById(R.id.tokenGenera);


        tokenMen=findViewById(R.id.tokenMen);

    }
}
