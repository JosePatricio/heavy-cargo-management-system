package ec.redcode.tas.on.android.activities.conductor;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import ec.redcode.tas.on.android.dto.PasswordDTO;
import ec.redcode.tas.on.android.R;
import ec.redcode.tas.on.android.services.AuthService;
import ec.redcode.tas.on.android.Utils;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class ActivacionUsuario extends AppCompatActivity {

    public static final String USERNAME = "userActiva";

    AuthService authService;

    ProgressBar progressbar;
    LinearLayout activateCont;

    Button activateAccount;
    Button cancelButton;

    TextView user_name;

    EditText password;
    EditText newPass;
    EditText confirmPass;

    public ActivacionUsuario() {
        authService = new AuthService();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activacion_usuario);
        //Set elements in view
        setElementsView();
        //set form logic
        progress();
        formLogic();
        done();
    }

    private void formLogic() {
        activateAccount.setClickable(false);
        Intent intent = getIntent();
        String extUserName = intent.getStringExtra(USERNAME);
        user_name.setText(extUserName);

        newPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    checkPasswords();
                }
            }
        });
        confirmPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    checkPasswords();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        activateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUserActivate(view);
            }
        });

        confirmPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b)
                    activateAccount.setClickable(true);
            }
        });
        activateAccount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                view.setClickable(true);
            }
        });
    }

    private void checkPasswords() {
        Utils.resetErrorView(password);
        Utils.resetErrorView(newPass);
        Utils.resetErrorView(confirmPass);

        String pass = password.getText().toString();
        String newPass_ = newPass.getText().toString();
        String confirmPass_ = confirmPass.getText().toString();
        activateAccount.setClickable(false);
        if (pass.equals(newPass_) || pass.equals(confirmPass_)) {
            Utils.setErrorView(newPass, "La contraseÃ±a debe ser diferente a la proporcionada.");
            return;
        } else if (newPass_.isEmpty() || confirmPass_.isEmpty()) {
            Utils.setErrorView(confirmPass, "Debe ingresar una constraseña.");
            return;
        } else if (!newPass_.equals(confirmPass_)) {
            Utils.setErrorView(confirmPass, "Las contraseña ingresadas no son iguales.");
            return;
        }
        activateAccount.setClickable(true);
    }

    private void checkUserActivate(final View view) {
        checkPasswords();
        progress();
        PasswordDTO passwordDTO = new PasswordDTO(user_name.getText().toString(), password.getText().toString(),
                newPass.getText().toString(), confirmPass.getText().toString());
        Observable<String> observable = authService.updatePassword(passwordDTO);
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    protected void onStart() {
                        progress();
                        super.onStart();
                    }

                    @Override
                    public void onNext(final String responce) {
                        Snackbar snackbar = Snackbar.make(view, responce, Snackbar.LENGTH_LONG);
                        snackbar.addCallback(new Snackbar.Callback() {
                            @Override
                            public void onDismissed(Snackbar transientBottomBar, int event) {
                                if (responce.toLowerCase().contains("correctamente")) {
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
        activateCont = findViewById(R.id.activateCont);

        activateAccount = findViewById(R.id.activateAccount);
        cancelButton = findViewById(R.id.cancelButton);

        user_name = findViewById(R.id.user_name);

        password = findViewById(R.id.password);
        newPass = findViewById(R.id.newPass);
        confirmPass = findViewById(R.id.confirmPass);

    }

    private void progress() {
        progressbar.setVisibility(View.VISIBLE);
        activateCont.setVisibility(View.GONE);
    }

    private void done() {
        progressbar.setVisibility(View.GONE);
        activateCont.setVisibility(View.VISIBLE);
    }
}
