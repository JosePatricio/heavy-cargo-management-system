package ec.redcode.tas.on.android.activities.conductor;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import ec.redcode.tas.on.android.dto.responses.OfertResponseDTO;
import ec.redcode.tas.on.android.R;
import ec.redcode.tas.on.android.services.PublicService;
import ec.redcode.tas.on.android.Utils;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class RestoredPassword extends AppCompatActivity {

    PublicService publicService;

    ProgressBar progressbar;
    LinearLayout restoreCont;

    Button recoveryPass;
    Button cancelButton;

    EditText emailReco;
    EditText docIdReco;

    public RestoredPassword() {
        publicService = new PublicService();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restored_password);

        setElementsView();

        progress();

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        recoveryPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recoveryPassword(view);
            }
        });

        done();
    }

    private void recoveryPassword(final View view) {
        progress();

        emailReco.setError(null);
        docIdReco.setError(null);

        String emailReco_ = (String) Utils.getTextValue(emailReco);
        String docIdReco_ = (String) Utils.getTextValue(docIdReco);

        if (emailReco_.isEmpty()) {
            emailReco.setError("Debe ingresar su correo electrónico");
            return;
        }
        if (docIdReco_.isEmpty()) {
            docIdReco.setError("Debe ingresar su documento de identidad");
            return;
        }

        Observable<OfertResponseDTO> observable = publicService.restablecerPassword(emailReco_, docIdReco_);
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<OfertResponseDTO>() {
                    @Override
                    protected void onStart() {
                        progress();
                        super.onStart();
                    }

                    @Override
                    public void onNext(final OfertResponseDTO response) {
                        final String message = response.getResponseMessage();
                        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
                        snackbar.addCallback(new Snackbar.Callback() {
                            @Override
                            public void onDismissed(Snackbar transientBottomBar, int event) {
                                if (response.getResponse().equals("OK")) {
                                    done();
                                    onBackPressed();
                                }
                            }
                        });
                        snackbar.show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        done();
                        if (e.toString().toLowerCase().contains("timeout"))
                            Toast.makeText(getBaseContext(), "Lo sentimos ocurrió un error cargando la información," +
                                    " por favor intente nuevamente...", Toast.LENGTH_LONG).show();
                        else {
                            Toast.makeText(getBaseContext(), "Lo sentimos verifique los datos ingresados e intente nuevamente...", Toast.LENGTH_LONG).show();
                        }
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        done();
                    }
                });

    }

    private void setElementsView() {
        progressbar = findViewById(R.id.progressbar);
        restoreCont = findViewById(R.id.restoreCont);

        recoveryPass = findViewById(R.id.recovery_pass);
        cancelButton = findViewById(R.id.cancel_button);
        emailReco = findViewById(R.id.emailReco);
        docIdReco = findViewById(R.id.docIdReco);

    }

    private void progress() {
        progressbar.setVisibility(View.VISIBLE);
        restoreCont.setVisibility(View.GONE);
    }

    private void done() {
        progressbar.setVisibility(View.GONE);
        restoreCont.setVisibility(View.VISIBLE);
    }
}
