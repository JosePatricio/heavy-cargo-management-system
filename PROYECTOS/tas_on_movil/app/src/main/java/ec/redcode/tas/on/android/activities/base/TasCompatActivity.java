package ec.redcode.tas.on.android.activities.base;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import ec.redcode.tas.on.android.Constants;
import ec.redcode.tas.on.android.R;
import ec.redcode.tas.on.android.activities.LoginActivity;
import ec.redcode.tas.on.android.dto.User;
import ec.redcode.tas.on.android.fragments.SearchFleteFragment;
import ec.redcode.tas.on.android.services.FleteListService;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public abstract class TasCompatActivity extends AppCompatActivity {

    public static final String NUEVA_NOTIFICATION = "nueva";
    public static final String APROBADA_NOTIFICATION = "aprobada";

    public static final String TIPO = "tipo";
    public static final String ID = "solicitud";

    private static final String TAG = "TasOnCompat";
    public boolean startFlag = true;

    public SharedPreferences sp;
    public ProgressBar progressbar;

    public Toolbar toolbar;
    public LinearLayout mContent;
    public DrawerLayout drawer;

    public FragmentManager fragmentManager;

    public NavigationView navigationView;

    private String typeNotification;
    private String idSolicitud;

    public void suscribeTasOnNotifi() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }

        Log.d(TAG, "Subscribing to news topic");
        // [START subscribe_topics]
        FirebaseMessaging.getInstance().subscribeToTopic("news")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Subscripción a notificaciónes fallida.");
                        } else {
                            Log.i(TAG, "Subscripción completa.");
                        }
                    }
                });
        // [END subscribe_topics]
    }

    public void launchActivity(Class<?> cls) {
        Intent intent = new Intent(getApplicationContext(), cls);
        startActivity(intent);
        finish();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
    }

    public boolean isUserNamelValid(String userName) {
        return userName.length() > 0;
    }

    public boolean isPasswordValid(String password) {
        return password.length() > 1;
    }

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
                            if (startFlag) {
                                loadInitialFragment();
                                startFlag = false;
                            }
                        } else {
                            reloginUser();
                        }
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

    public int getTipo() {
        int tipo = 0;
        if (Constants.user != null && Constants.user.getTipoUsuario() != null) {
            tipo = Constants.user.getTipoUsuario();
        }
        return tipo;
    }

    public void reloginUser() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void closeSession() {
        Constants.user = new User();

        sp = getSharedPreferences("login", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("token", "");
        editor.putString("email", "");
        editor.putString("nombres", "");
        //editor.putString("apellidos", "");
        editor.putString("estadoUser", "");
        editor.putString("tipoUsuario", "");
        editor.putString("tipoUsuarioDesc", "");
        editor.apply();

        reloginUser();
    }

    public void setSharedPref(User user) {
        sp = getSharedPreferences("login", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("token", Constants.user.getToken());
        editor.putString("email", Constants.user.getEmail());
        editor.putString("nombres", Constants.user.getNombres());
        //editor.putString("apellidos", Constants.user.getApellidos());
        editor.putString("estadoUser", Constants.user.getEstadoUsuario().toString());
        editor.putString("tipoUsuario", Constants.user.getTipoUsuario().toString());
        editor.putString("tipoUsuarioDesc", Constants.user.getTipoUsuarioDesc());
        editor.apply();
    }

    public void getConstUser() {
        sp = getSharedPreferences("login", Activity.MODE_PRIVATE);
        Constants.user = new User();
        Constants.user.setToken(sp.getString("token", ""));
        Constants.user.setEmail(sp.getString("email", ""));
        Constants.user.setNombres(sp.getString("nombres", ""));
        //Constants.user.setApellidos(sp.getString("apellidos", ""));
        Constants.user.setTipoUsuario(Integer.parseInt(sp.getString("tipoUsuario", "")));
        Constants.user.setTipoUsuarioDesc(sp.getString("tipoUsuarioDesc", ""));
    }

    public void checkStateSupport(int containerViewId, Fragment fragment) {
        if (!getSupportFragmentManager().isStateSaved())
            getSupportFragmentManager().beginTransaction().replace(containerViewId, fragment).commit();
    }

    protected void loadFromNotification() {
        Intent intent = getIntent();
        setTypeNotification(intent.getStringExtra(TIPO));
        setIdSolicitud(intent.getStringExtra(ID));
        notificationManage();
    }

    public String getTypeNotification() {
        return typeNotification;
    }

    public void setTypeNotification(String typeNotification) {
        this.typeNotification = typeNotification;
    }

    public String getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(String idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    /**
     * Overide methods
     **/
    protected void setupActivity() {
        throw new IllegalStateException("Fragment " + this + " not attached to Activity");
    }

    protected void loadInitialFragment() {
        throw new IllegalStateException("Fragment " + this + " not attached to Activity");
    }

    public void notificationManage() {
        throw new IllegalStateException("Fragment " + this + " not attached to Activity");
    }

}
