package ec.redcode.tas.on.android.activities.conductor;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ec.redcode.tas.on.android.Constants;
import ec.redcode.tas.on.android.R;
import ec.redcode.tas.on.android.Utils;
import ec.redcode.tas.on.android.adapters.CatalogoItemAdapter;
import ec.redcode.tas.on.android.adapters.LocalidadAdapter;
import ec.redcode.tas.on.android.dto.CatalogoItemDTO;
import ec.redcode.tas.on.android.dto.ConductorDTO;
import ec.redcode.tas.on.android.dto.EmpresaDTO;
import ec.redcode.tas.on.android.dto.LocalidadDTO;
import ec.redcode.tas.on.android.dto.UsuarioDTO;
import ec.redcode.tas.on.android.dto.VehiculoDTO;
import ec.redcode.tas.on.android.dto.responses.OfertResponseDTO;
import ec.redcode.tas.on.android.services.PublicService;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class ActivityEnrollment extends AppCompatActivity {
    public static final String SEL_TIPO_DOC = "Seleccione tipo documento";
    public static final String SEL_PROVINCIA = "Seleccione provincia";
    public static final String SEL_CANTON = "Seleccione canton";
    public static final String SEL_LICENCIA = "Seleccione licencia";

    public static final int STEP_ZERO = 0;
    public static final int STEP_ONE = 1;
    public static final int STEP_TWO = 2;
    public static final int STEP_MAX = 4;
    public static int STEP_END = STEP_TWO;

    private final UsuarioDTO usuarioDTO = new UsuarioDTO();
    private final EmpresaDTO empresaDTO = new EmpresaDTO();

    private TextView txtPaso;
    private Toolbar toolbar;
    private ImageButton btnBack;
    private ImageButton btnNext;
    private ImageButton btnEnd;

    private LinearLayout mainCont;
    private ProgressBar progressbar;

    private LinearLayout lyStepZero;
    private LinearLayout lyStepOne;
    private LinearLayout lyStepTwo;
    private LinearLayout lyStepThree;

    private Spinner cmbContacProv;

    private Spinner cmbTipoLicencia;

    private EditText txtEmpRucPertenece;
    private TextView txtRazonSocial;
    private EditText txtUserName;
    private EditText txtEmail;
    private EditText txtNumDoc;

    private Spinner cmbTipoDoc;
    private EditText txtNombres;
    private EditText txtApellidos;

    private Spinner cmbContacCanton;
    private EditText txtContacConven;
    private EditText txtContacCelular;

    private GridLayout gridBackNextButtons;

    private LinearLayout lytDatosEmpresaConductor;
    private LinearLayout lytRazonSocEmpresaConductor;
    private LinearLayout lytDatosEnrolamientoConductor;

    final int[] paso = {1};
    final int[] pasoT = {2};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrollment);

        getViews();

        setSupportActionBar(toolbar);
        loadCatalogData();

        labelProcess(paso[0], pasoT[0]);

        gridBackNextButtons.setVisibility(View.GONE);
        lytRazonSocEmpresaConductor.setVisibility(View.GONE);
        lytDatosEnrolamientoConductor.setVisibility(View.GONE);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paso[0] = paso[0] - 1;
                labelProcess(paso[0], pasoT[0]);

                if (paso[0] == (pasoT[0] == STEP_MAX ? STEP_ZERO : STEP_ONE)) {
                    view.setVisibility(View.INVISIBLE);
                }
                if (paso[0] == STEP_END && btnEnd.getVisibility() == View.GONE)
                    btnEnd.setVisibility(View.VISIBLE);
                else {
                    btnEnd.setVisibility(View.GONE);
                    btnNext.setVisibility(View.VISIBLE);
                }

                checkLayout(paso[0]);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFilledRequiredFields(paso[0])) {
                    setUserData(paso[0]);
                    paso[0] = paso[0] + 1;
                    checkLayout(paso[0]);
                    labelProcess(paso[0], pasoT[0]);
                } else {
                    Snackbar.make(view, "Por favor verifique los datos ingresados", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

                if (paso[0] == STEP_END) {
                    view.setVisibility(View.GONE);
                    btnEnd.setVisibility(View.VISIBLE);
                }

                if (paso[0] > (pasoT[0] == STEP_MAX ? STEP_ZERO : STEP_ONE) && btnBack.getVisibility() != View.VISIBLE) {
                    btnBack.setVisibility(View.VISIBLE);
                }
            }
        });

        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Spanned htmlAsSpanned = Html.fromHtml(getString(R.string.Terms_and_conditions_cont));
                new AlertDialog.Builder(view.getContext())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(R.string.acep_terms_y_cond)
                        .setMessage(htmlAsSpanned)
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                progress();
                                btnEnd.setEnabled(false);
                                btnEnd.setClickable(false);
                                if (isFilledRequiredFields(paso[0])) {
                                    setUserData(paso[0]);
                                    PublicService publicService = new PublicService();
                                    if (pasoT[0] == STEP_END) {
                                        usuarioDTO.setUsuarioTipoUsuario(Constants.ID_USUARIO_CONDUCTOR);
                                        usuarioDTO.setUsuarioEstado(Constants.ID_ESTADO_USUARIO_PENDIENTE);
                                        final Observable<String> creaUser = publicService.createUsuario(usuarioDTO);
                                        creaUser
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribeWith(new DisposableObserver<String>() {
                                                    @Override
                                                    public void onNext(String response) {
                                                        Gson gsonResponse = new Gson();


                                                        OfertResponseDTO response_ = gsonResponse.fromJson(response, OfertResponseDTO.class);

                                                        if (response_.getResponse() != null && response_.getResponse().equals("OK")) {
                                                            //Snackbar snackbar = Snackbar.make(view, response_.getResponseMessage(), Snackbar.LENGTH_LONG);
                                                            Snackbar snackbar = Snackbar.make(view, "Registro exitoso, dentro de unos minutos llegará a su correo electrónico sus datos de usuario...", Snackbar.LENGTH_LONG);
                                                            snackbar.addCallback(new Snackbar.Callback() {
                                                                @Override
                                                                public void onDismissed(Snackbar transientBottomBar, int event) {
                                                                    finish();
                                                                }
                                                            });
                                                            snackbar.show();
                                                        } else {
                                                            Snackbar.make(view, "Error: " + response_.getDeveloperMessage(), Snackbar.LENGTH_LONG)
                                                                    .setAction("Action", null).show();
                                                        }
                                                        done();
                                                    }

                                                    @Override
                                                    public void onError(Throwable e) {
                                                        Log.w("Error al crear: ", e);
                                                        btnEnd.setEnabled(true);
                                                        btnEnd.setClickable(true);
                                                        done();
                                                    }

                                                    @Override
                                                    public void onComplete() {
                                                        btnEnd.setEnabled(true);
                                                        btnEnd.setClickable(true);
                                                        done();
                                                    }
                                                });
                                    } else if (pasoT[0] == STEP_MAX) {
                                        Map<String, Object> data = new HashMap<>();
                                        data.put("empresa", empresaDTO);
                                        usuarioDTO.setUsuarioRuc(empresaDTO.getClienteRuc());
                                        usuarioDTO.setUsuarioPrincipal(true);
                                        usuarioDTO.setUsuarioTipoUsuario(Constants.ID_USUARIO_CONDUCTOR_CLIENTE);
                                        usuarioDTO.setUsuarioEstado(Constants.ID_ESTADO_USUARIO_CREADO);
                                        data.put("cliente", usuarioDTO);
                                        final Observable<String> creaUser = publicService.createEmpresa(data);
                                        creaUser
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribeWith(new DisposableObserver<String>() {
                                                    @Override
                                                    public void onNext(String response) {
                                                        Gson gsonResponse = new Gson();
                                                        Log.d("respuesta", response);

                                                        OfertResponseDTO response_ = gsonResponse.fromJson(response, OfertResponseDTO.class);

                                                        if (response_.getResponse() != null && response_.getResponse().equals("OK")) {
                                                            Snackbar snackbar = Snackbar.make(view, response_.getResponseMessage(), Snackbar.LENGTH_LONG);
                                                            snackbar.addCallback(new Snackbar.Callback() {
                                                                @Override
                                                                public void onDismissed(Snackbar transientBottomBar, int event) {
                                                                    if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                                                                        finish();
                                                                    }
                                                                }
                                                            });
                                                            snackbar.show();
                                                        } else {
                                                            Snackbar.make(view, "Error: " + response_.getDeveloperMessage(), Snackbar.LENGTH_LONG)
                                                                    .setAction("Action", null).show();
                                                        }
                                                        done();
                                                    }

                                                    @Override
                                                    public void onError(Throwable e) {
                                                        Log.w("Error al crear: ", e);
                                                        btnEnd.setEnabled(true);
                                                        btnEnd.setClickable(true);
                                                        done();
                                                    }

                                                    @Override
                                                    public void onComplete() {
                                                        btnEnd.setEnabled(true);
                                                        btnEnd.setClickable(true);
                                                        done();
                                                    }
                                                });
                                    }
                                } else {
                                    Snackbar.make(view, "Por favor verifique los datos ingresados", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                    btnEnd.setEnabled(true);
                                    btnEnd.setClickable(true);
                                    done();
                                }
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        cmbContacProv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                LocalidadDTO catalogoItem = (LocalidadDTO) parentView.getSelectedItem();
                if (catalogoItem.getLocalidadId() > 0) {
                    loadCantones(catalogoItem.getLocalidadId(), cmbContacCanton);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        txtEmpRucPertenece.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() == 13){
                    checkEmpresaTransporteExist(txtEmpRucPertenece.getText().toString(), findViewById(R.id.txtEmpRucPertenece));
                    Toast.makeText(getApplicationContext(),"Buscando compañía de transporte", Toast.LENGTH_SHORT).show();
                } else{
                    lytDatosEnrolamientoConductor.setVisibility(View.GONE);
                }
            }
        });

        txtUserName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b)
                    checkUserByUsername(txtUserName.getText().toString(), view);
            }
        });

        txtEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b)
                    checkUserByEmail(txtEmail.getText().toString(), view);
            }
        });

        txtNumDoc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b)
                    checkUserByEmail(txtNumDoc.getText().toString(), view);
            }
        });
    }

    private void checkEmpresaTransporteExist(String ruc, final View view) {
        PublicService publicService = new PublicService();
        publicService.readEmpresaTransporte(ruc).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableObserver<EmpresaDTO>() {
            @Override
            public void onNext(EmpresaDTO empresaDTO) {
                Utils.resetErrorView(view);
                if (empresaDTO.getClienteRuc() == null || empresaDTO.getClienteRuc().isEmpty()){
                    Utils.setErrorView(view, "No se encuentra registrado una empresa con el RUC ingresado");
                    lytDatosEnrolamientoConductor.setVisibility(View.GONE);

                } else {
                    gridBackNextButtons.setVisibility(View.VISIBLE);
                    lytRazonSocEmpresaConductor.setVisibility(View.VISIBLE);
                    lytDatosEnrolamientoConductor.setVisibility(View.VISIBLE);
                    lytDatosEmpresaConductor.setVisibility(View.GONE);
                    txtRazonSocial.setText(empresaDTO.getClienteRazonSocial());
                }
            }

            @Override
            public void onError(Throwable e) {
                Utils.setErrorView(view, "Por favor intente nuevamente...");
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void checkUserByUsername(String ruc, final View view) {
        PublicService publicService = new PublicService();
        publicService.readUserByUsername(ruc).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableObserver<UsuarioDTO>() {
            @Override
            public void onNext(UsuarioDTO empresaDTO) {
                Utils.resetErrorView(view);
                if (empresaDTO.getUsuarioIdDocumento() != null && !empresaDTO.getUsuarioIdDocumento().isEmpty())
                    Utils.setErrorView(view, "Los datos ingresados ya se encuentran registrados...");
            }

            @Override
            public void onError(Throwable e) {
                Utils.setErrorView(view, "Por favor intente nuevamente...");
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void checkUserByEmail(String ruc, final View view) {
        PublicService publicService = new PublicService();
        publicService.readUserByEmail(ruc).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableObserver<UsuarioDTO>() {
            @Override
            public void onNext(UsuarioDTO empresaDTO) {
                Utils.resetErrorView(view);
                if (empresaDTO.getUsuarioIdDocumento() != null && !empresaDTO.getUsuarioIdDocumento().isEmpty())
                    Utils.setErrorView(view, "Los datos ingresados ya se encuentran registrados...");
            }

            @Override
            public void onError(Throwable e) {
                Utils.setErrorView(view, "Por favor intente nuevamente...");
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private boolean setUserData(int paso) {
        boolean ban = true;
        String nombres = (String) Utils.getTextValue(txtNombres);
        String apellidos = (String) Utils.getTextValue(txtApellidos);
        switch (paso) {
            case STEP_ONE:
                usuarioDTO.setUsuarioRuc((String) Utils.getTextValue(txtEmpRucPertenece));
                usuarioDTO.setUsuarioTipoDocumento(((CatalogoItemDTO) Utils.getItemSpinner(cmbTipoDoc)).getCatalogoItemId());
                usuarioDTO.setUsuarioIdDocumento((String) Utils.getTextValue(txtNumDoc));
                usuarioDTO.setUsuarioNombres(nombres.trim());
                usuarioDTO.setUsuarioApellidos(apellidos.trim());
                usuarioDTO.setUsuarioNombreUsuario((String) Utils.getTextValue(txtUserName));
                usuarioDTO.setUsuarioMail((String) Utils.getTextValue(txtEmail));
                break;

            case STEP_TWO:
                usuarioDTO.setUsuarioLocalidad(((LocalidadDTO) Utils.getItemSpinner(cmbContacCanton)).getLocalidadId());
                usuarioDTO.setUsuarioConvecional((String) Utils.getTextValue(txtContacConven));
                usuarioDTO.setUsuarioCelular((String) Utils.getTextValue(txtContacCelular));

                List<ConductorDTO> conductor = new ArrayList<>();
                if (pasoT[0] != STEP_MAX) {
                    try {
                        ConductorDTO conductor_ = new ConductorDTO();
                        conductor_.setConductorNombre(usuarioDTO.getUsuarioNombres());
                        conductor_.setConductorApellido(usuarioDTO.getUsuarioApellidos());
                        conductor_.setConductorUsuario(usuarioDTO.getUsuarioIdDocumento());
                        conductor_.setConductorEmail(usuarioDTO.getUsuarioMail());
                        conductor_.setConductorTelefono(usuarioDTO.getUsuarioCelular());
                        conductor_.setConductorTipoLicencia(((CatalogoItemDTO) Utils.getItemSpinner(cmbTipoLicencia)).getCatalogoItemId());

                        conductor.add(conductor_);
                    } catch (Exception e) {
                        Log.w("Error parsear objeto:", e);
                    }
                }

                usuarioDTO.setConductorsByUsuarioIdDocumento(conductor);
                usuarioDTO.setVehiculosByUsuarioIdDocumento(new ArrayList<VehiculoDTO>());
                break;
        }
        return ban;
    }

    private boolean isFilledRequiredFields(int paso) {
        boolean ban = true;
        switch (paso) {
            case STEP_ONE:
                if (Utils.getTextValue(txtEmpRucPertenece).equals("")) {
                    Utils.setErrorView(txtEmpRucPertenece);
                    ban = false;
                } else Utils.resetErrorView(txtEmpRucPertenece);

                if (((CatalogoItemDTO) Utils.getItemSpinner(cmbTipoDoc)).getCatalogoItemDescripcion().equals(SEL_TIPO_DOC)) {
                    ban = false;
                    Utils.setErrorView(cmbTipoDoc.getSelectedView());
                } else Utils.resetErrorView(cmbTipoDoc.getSelectedView());

                if (Utils.getTextValue(txtNumDoc).equals("")) {
                    Utils.setErrorView(txtNumDoc);
                    ban = false;
                } else {
                    Utils.resetErrorView(txtNumDoc);
                    if (Utils.getItemSpinner(cmbTipoDoc).equals("RUC") && txtNumDoc.getText().length() != 13) {
                        Utils.setErrorView(txtNumDoc, "Ingrese los 13 digitos del RUC.");
                        ban = false;
                    }
                    if (Utils.getItemSpinner(cmbTipoDoc).equals("CEDULA") && txtNumDoc.getText().length() != 10) {
                        Utils.setErrorView(txtNumDoc, "Ingrese su CÉDULA completa.");
                        ban = false;
                    }
                }

                if (Utils.getTextValue(txtNombres).equals("")) {
                    Utils.setErrorView(txtNombres);
                    ban = false;
                } else Utils.resetErrorView(txtNombres);

                if (Utils.getTextValue(txtApellidos).equals("")) {
                    Utils.setErrorView(txtApellidos);
                    ban = false;
                } else Utils.resetErrorView(txtApellidos);

                if (Utils.getTextValue(txtUserName).equals("")) {
                    Utils.setErrorView(txtUserName);
                    ban = false;
                } else {
                    Utils.resetErrorView(txtUserName);

                    if (!Utils.regexCheck(Utils.RGX_ALPHANUMERIC, Utils.getTextValue(txtUserName).toString())) {
                        Utils.setErrorView(txtUserName, "El nombre de usuario puede contener letras y números únicamente.");
                        ban = false;
                    }
                }
                if (Utils.getTextValue(txtEmail).equals("")) {
                    Utils.setErrorView(txtEmail);
                    ban = false;
                } else {
                    Utils.resetErrorView(txtEmail);
                    if (!Utils.emailCheck(Utils.getTextValue(txtEmail).toString())) {
                        Utils.setErrorView(txtEmail, "Ingrese su correo electrónico correctamente.");
                        ban = false;
                    }
                }
                if (pasoT[0] != STEP_MAX)
                    if (((CatalogoItemDTO) Utils.getItemSpinner(cmbTipoLicencia)).getCatalogoItemDescripcion().equals(SEL_LICENCIA)) {
                        ban = false;
                        Utils.setErrorView(cmbTipoLicencia.getSelectedView());
                    } else Utils.resetErrorView(cmbTipoLicencia.getSelectedView());

                if (ban)
                    ban = !Utils.isErrorView(txtNumDoc, txtNombres, txtApellidos, txtUserName,
                            txtEmail);
                break;

            case STEP_TWO:
                if (((LocalidadDTO) Utils.getItemSpinner(cmbContacProv)).getLocalidadDescripcion().equals(SEL_PROVINCIA)) {
                    ban = false;
                    Utils.setErrorView(cmbContacProv.getSelectedView());
                } else Utils.resetErrorView(cmbContacProv.getSelectedView());

                if (((LocalidadDTO) Utils.getItemSpinner(cmbContacCanton)).getLocalidadDescripcion().equals(SEL_CANTON)) {
                    ban = false;
                    Utils.setErrorView(cmbContacCanton.getSelectedView());
                } else Utils.resetErrorView(cmbContacCanton.getSelectedView());

                if (Utils.getTextValue(txtContacCelular).equals("")) {
                    Utils.setErrorView(txtContacCelular);
                    ban = false;
                } else {
                    Utils.resetErrorView(txtContacCelular);
                    if (txtContacCelular.getText().length() != 10) {
                        Utils.setErrorView(txtContacCelular, "Ingrese los 10 dígitos de su número.");
                        ban = false;
                    }
                }

                if (txtContacConven.getText().length() > 0 && txtContacConven.getText().length() != 9) {
                    Utils.setErrorView(txtContacConven, "Ingrese los 9 dígitos de su número.");
                    ban = false;
                } else Utils.resetErrorView(txtContacConven);

                if (ban)
                    ban = !Utils.isErrorView(txtContacConven, txtContacCelular);
                break;
        }
        return ban;
    }

    public void checkLayout(int paso) {
        if (paso == STEP_ONE) {
            lyStepZero.setVisibility(View.GONE);
            lyStepOne.setVisibility(View.VISIBLE);
            lyStepTwo.setVisibility(View.GONE);
            lyStepThree.setVisibility(View.GONE);
        } else if (paso == STEP_TWO) {
            lyStepZero.setVisibility(View.GONE);
            lyStepOne.setVisibility(View.GONE);
            lyStepTwo.setVisibility(View.VISIBLE);
            lyStepThree.setVisibility(View.GONE);
        }
    }

    private void loadCatalogData() {
        PublicService publicService = new PublicService();

        final Observable<CatalogoItemDTO[]> tipoDocumento = publicService.getCatalogoItemByType(Constants.ID_CATALOGO_DOCUMENTO);
        tipoDocumento
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<CatalogoItemDTO[]>() {
                    @Override
                    protected void onStart() {
                        super.onStart();
                        progress();
                    }

                    @Override
                    public void onNext(CatalogoItemDTO[] catalogoItemDTOS) {
                        List<CatalogoItemDTO> dtos = new ArrayList<>();
                        dtos.add(new CatalogoItemDTO(SEL_TIPO_DOC));
                        dtos.addAll(Arrays.asList(catalogoItemDTOS));
                        CatalogoItemDTO[] stockArr = new CatalogoItemDTO[dtos.size()];
                        catalogoItemDTOS = dtos.toArray(stockArr);

                        CatalogoItemAdapter adapter = new CatalogoItemAdapter(ActivityEnrollment.this, android.R.layout.simple_spinner_item, catalogoItemDTOS);
                        cmbTipoDoc.setAdapter(adapter);
                        done();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.w("Error", e);
                        done();
                    }

                    @Override
                    public void onComplete() {
                        done();
                    }
                });

        final Observable<LocalidadDTO[]> catalogoProv = publicService.getLocalidadByPadreAndState(Constants.ID_LOCAL_PADRE_ECUADOR, Constants.ID_ESTADO_ACTIVO);
        catalogoProv
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<LocalidadDTO[]>() {
                    @Override
                    protected void onStart() {
                        super.onStart();
                        progress();
                    }

                    @Override
                    public void onNext(LocalidadDTO[] localidadDTOS) {
                        List<LocalidadDTO> dtos = new ArrayList<>();
                        dtos.add(new LocalidadDTO(SEL_PROVINCIA));
                        dtos.addAll(Arrays.asList(localidadDTOS));
                        LocalidadDTO[] stockArr = new LocalidadDTO[dtos.size()];
                        localidadDTOS = dtos.toArray(stockArr);

                        LocalidadAdapter adapter = new LocalidadAdapter(ActivityEnrollment.this, android.R.layout.simple_spinner_item, localidadDTOS);
                        cmbContacProv.setAdapter(adapter);

                        adapter = new LocalidadAdapter(ActivityEnrollment.this, android.R.layout.simple_spinner_item, new LocalidadDTO[]{new LocalidadDTO(SEL_CANTON)});
                        cmbContacCanton.setAdapter(adapter);
                        done();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.w("Error", e);
                        done();
                    }

                    @Override
                    public void onComplete() {
                        done();
                    }
                });

        final Observable<CatalogoItemDTO[]> obsTipLicen = publicService.getCatalogoItemByType(Constants.ID_CATALOGO_LICENCIA);
        obsTipLicen
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<CatalogoItemDTO[]>() {
                    @Override
                    protected void onStart() {
                        super.onStart();
                        progress();
                    }

                    @Override
                    public void onNext(CatalogoItemDTO[] catalogoItemDTOS) {
                        List<CatalogoItemDTO> dtos = new ArrayList<>();
                        dtos.add(new CatalogoItemDTO(SEL_LICENCIA));
                        dtos.addAll(Arrays.asList(catalogoItemDTOS));
                        CatalogoItemDTO[] stockArr = new CatalogoItemDTO[dtos.size()];
                        catalogoItemDTOS = dtos.toArray(stockArr);

                        CatalogoItemAdapter adapter = new CatalogoItemAdapter(ActivityEnrollment.this, android.R.layout.simple_spinner_item, catalogoItemDTOS);
                        cmbTipoLicencia.setAdapter(adapter);
                        done();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.w("Error", e);
                        done();
                    }

                    @Override
                    public void onComplete() {
                        done();
                    }
                });
    }

    private void loadCantones(Integer idLocalidad, final Spinner spinner) {
        PublicService publicService = new PublicService();
        final Observable<LocalidadDTO[]> catalogoProv = publicService.getLocalidadByPadreAndState(idLocalidad, Constants.ID_ESTADO_ACTIVO);
        catalogoProv
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<LocalidadDTO[]>() {
                    @Override
                    protected void onStart() {
                        super.onStart();
                        progress();
                    }

                    @Override
                    public void onNext(LocalidadDTO[] localidadDTOS) {
                        List<LocalidadDTO> dtos = new ArrayList<>();
                        dtos.add(new LocalidadDTO(SEL_CANTON));
                        dtos.addAll(Arrays.asList(localidadDTOS));
                        LocalidadDTO[] stockArr = new LocalidadDTO[dtos.size()];
                        localidadDTOS = dtos.toArray(stockArr);

                        LocalidadAdapter adapter = new LocalidadAdapter(ActivityEnrollment.this, android.R.layout.simple_spinner_item, localidadDTOS);
                        spinner.setAdapter(adapter);
                        done();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.w("Error", e);
                        done();
                    }

                    @Override
                    public void onComplete() {
                        done();
                    }
                });
    }

    private void labelProcess(int paso, int pasoT) {
        paso = (pasoT == STEP_MAX ? (paso + 1) : paso);
        txtPaso.setText(String.format(new Locale("es", "EC"), " Paso: %d de %d", paso, pasoT));
    }

    private void getViews() {
        toolbar = findViewById(R.id.toolbar);
        txtPaso = findViewById(R.id.txtPaso);

        btnBack = findViewById(R.id.btnBack);
        btnNext = findViewById(R.id.btnNext);
        btnEnd = findViewById(R.id.btnEnd);

        cmbContacProv = findViewById(R.id.cmbContacProv);
        cmbTipoLicencia = findViewById(R.id.cmbTipoLicencia);

        txtEmpRucPertenece = findViewById(R.id.txtEmpRucPertenece);
        txtRazonSocial = findViewById(R.id.txtRazonSocial);
        txtUserName = findViewById(R.id.txtUserName);
        txtEmail = findViewById(R.id.txtEmail);
        txtNumDoc = findViewById(R.id.txtNumDoc);

        cmbTipoDoc = findViewById(R.id.cmbTipoDocumento);
        txtNombres = findViewById(R.id.txtNombres);
        txtApellidos = findViewById(R.id.txtApellidos);

        cmbContacCanton = findViewById(R.id.cmbContacCanton);
        txtContacConven = findViewById(R.id.txtContacConven);
        txtContacCelular = findViewById(R.id.txtContacCelular);

        lyStepZero = findViewById(R.id.lyStepZero);
        lyStepOne = findViewById(R.id.lyStepOne);
        lyStepTwo = findViewById(R.id.lyStepTwo);
        lyStepThree = findViewById(R.id.lyStepThree);

        mainCont = findViewById(R.id.mainCont);
        progressbar = findViewById(R.id.progressbar);

        gridBackNextButtons = findViewById(R.id.gridBackNextButtons);

        lytDatosEmpresaConductor = findViewById(R.id.lytDatosEmpresaConductor);
        lytRazonSocEmpresaConductor = findViewById(R.id.lytRazonSocEmpresaConductor);
        lytDatosEnrolamientoConductor = findViewById(R.id.lytDatosEnrolamientoConductor);
    }

    private void progress() {
        progressbar.setVisibility(View.VISIBLE);
        mainCont.setVisibility(View.GONE);
    }

    private void done() {
        progressbar.setVisibility(View.GONE);
        mainCont.setVisibility(View.VISIBLE);
    }
}