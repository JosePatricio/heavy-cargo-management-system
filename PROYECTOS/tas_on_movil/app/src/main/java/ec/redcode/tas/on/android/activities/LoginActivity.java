package ec.redcode.tas.on.android.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ec.redcode.tas.on.android.Constants;
import ec.redcode.tas.on.android.R;
import ec.redcode.tas.on.android.Utils;
import ec.redcode.tas.on.android.activities.base.TasCompatActivity;
import ec.redcode.tas.on.android.activities.conductor.ActivacionUsuario;
import ec.redcode.tas.on.android.activities.conductor.ActivityEnrollment;
import ec.redcode.tas.on.android.activities.conductor.ActivityEnrollmentIndependiente;
import ec.redcode.tas.on.android.activities.conductor.ActivityEnrollmentTranspCompany;
import ec.redcode.tas.on.android.activities.conductor.MainActivity;
import ec.redcode.tas.on.android.activities.conductor.RestoredPassword;
import ec.redcode.tas.on.android.activities.conductor.SolicitudActivity;
import ec.redcode.tas.on.android.activities.main.MainDashboardActivity;
import ec.redcode.tas.on.android.dto.LoginError;
import ec.redcode.tas.on.android.dto.User;
import ec.redcode.tas.on.android.fragments.TipoUsuarioNoValidoMsgFragment;
import ec.redcode.tas.on.android.services.UtilService;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends TasCompatActivity implements LoaderCallbacks<Cursor> {

    private static final int REQUEST_READ_CONTACTS = 0;

    UtilService utilService = new UtilService();
    LoginError loginError;
    private UserLoginTask mAuthTask = null;
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Button recoviyPass;
    private Button mEmailSignInButton;
    private Button enrolledButton;
    private Button mSolicitudButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = findViewById(R.id.email);
        mEmailView.setNextFocusDownId(R.id.password);

        mPasswordView = findViewById(R.id.password);
        mPasswordView.setNextFocusDownId(R.id.email_sign_in_button);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    InputMethodManager imm = (InputMethodManager) textView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);

                    attemptLogin();
                    return true;
                }
                return false;
            }
        });


        recoviyPass = findViewById(R.id.recoviy_pass);
        recoviyPass.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                restoredPass();
            }
        });

        mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        enrolledButton = findViewById(R.id.enrolled_button);
        enrolledButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                enrolledUser();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        mSolicitudButton = findViewById(R.id.solicitudes_button);
        mSolicitudButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SolicitudActivity.class);
                startActivity(intent);
            }
        });
    }

    private void enrolledUser() {
        //startActivity(new Intent(LoginActivity.this, ActivityEnrollment.class));

        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(LoginActivity.this, enrolledButton);
        //Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(R.menu.menu_enrol, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.enrolIndependiente)
                    startActivity(new Intent(LoginActivity.this, ActivityEnrollmentIndependiente.class));
                else if (item.getItemId() == R.id.enrolConductorComp)
                    startActivity(new Intent(LoginActivity.this, ActivityEnrollment.class));
                else if (item.getItemId() == R.id.enrolCompTrans)
                    startActivity(new Intent(LoginActivity.this, ActivityEnrollmentTranspCompany.class));

                return true;
            }
        });

        popup.show(); //showing popup menu
    }

    private void restoredPass() {
        startActivity(new Intent(LoginActivity.this, RestoredPassword.class));
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (!Utils.isNetworkAvaliable(this)) {
            Toast.makeText(this, "No estas conectado a internet", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        Utils.resetErrorView(mEmailView);
        Utils.resetErrorView(mPasswordView);

        if (Utils.getTextValue(mEmailView).toString().isEmpty()) {
            mEmailView.requestFocus();
            Toast.makeText(this, "Ingrese su nombre de usuario", Toast.LENGTH_SHORT).show();
            return;
        } else if (Utils.getTextValue(mPasswordView).toString().isEmpty()) {
            mPasswordView.requestFocus();
            Toast.makeText(this, "Ingrese su contraseña", Toast.LENGTH_SHORT).show();
            return;
        } else {
            mEmailView.clearFocus();
            mPasswordView.clearFocus();
            mEmailSignInButton.requestFocus();
        }

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isUserNamelValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_user_name));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    String post(String url, String plainText) throws IOException {
        RequestBody body = RequestBody.create(utilService.TEXT_PLAIN, plainText);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "x-www-form-url-encoded")
                .post(body)
                .build();
        Response response = utilService.getBuilder().newCall(request).execute();
        return response.body().string();
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean response = false;
            try {
                String fcmToken = FirebaseInstanceId.getInstance().getToken();
                String plainText = "username=" +
                        mEmail +
                        "&password=" +
                        mPassword +
                        "&fcmToken=" + fcmToken;
                String respuesta = post(Constants.urlLogin, plainText);
                Gson gson = new Gson();

                loginError = gson.fromJson(respuesta, LoginError.class);
                if (loginError.getStatus() != null) {
                    return false;
                }

                Constants.user = gson.fromJson(respuesta, User.class);

                if (!Utils.isValidUser(Constants.user.getTipoUsuario(), Utils.DRIVER)) {
                    return false;
                }

                if (Constants.user.getToken() != null) {
                    if (Constants.user.getEstadoUsuario() != 5 && Utils.isValidUser(Constants.user.getTipoUsuario(), Utils.ALL)) {

                        setSharedPref(Constants.user);

                        if (Utils.isValidUser(Constants.user.getTipoUsuario(), Utils.DRIVER)) {
                            launchActivity(MainActivity.class);
                        } else if (Utils.isValidUser(Constants.user.getTipoUsuario(), Utils.CUSTOMER)) {
                            launchActivity(MainDashboardActivity.class);
                        }
                        response = true;
                    } else if (Constants.user.getEstadoUsuario() == 5) {
                        Intent intent = new Intent(getApplicationContext(), ActivacionUsuario.class);
                        intent.putExtra(ActivacionUsuario.USERNAME, mEmail);
                        startActivity(intent);
                        response = true;
                    }
                }
            } catch (Exception e) {
                Log.w("Error al conectar WS: ", e);
            }

            return response;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                recoviyPass.setVisibility(View.GONE);
                finish();
            } else {

                if (loginError != null && Utils.isValidObject(Constants.user) &&!Utils.isValidUser(Constants.user.getTipoUsuario(), Utils.DRIVER)) {
                    Constants.user = new User();
                    mEmailView.setText("");
                    mPasswordView.setText("");
                    launchActivity(TipoUsuarioNoValidoMsgFragment.class);
                }

                else if (loginError == null && Utils.isValidObject(Constants.user) && !Utils.isValidUser(Constants.user.getTipoUsuario(), Utils.ALL)) {
                    final String message = "Lo sentimos: \nEste tipo de usuario no esta habilitado para usar la aplicación.";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    Utils.setErrorView(mEmailView, message);
                    Constants.user = new User();
                } else if (loginError != null) {
                    if (loginError.getResponse() != null && loginError.getResponse().equals("ERROR")) {
                        Utils.setErrorView(mPasswordView, loginError.getResponseMessage());
                        Toast.makeText(getApplicationContext(), loginError.getResponseMessage(), Toast.LENGTH_LONG).show();
                    }
                    loginError = null;
                    recoviyPass.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getApplicationContext(), "Algo ha salido mal. \nRevisa tu conexion a internet.", Toast.LENGTH_LONG).show();
                }

            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

