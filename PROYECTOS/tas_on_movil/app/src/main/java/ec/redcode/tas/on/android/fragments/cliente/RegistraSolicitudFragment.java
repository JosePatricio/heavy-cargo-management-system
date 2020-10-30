package ec.redcode.tas.on.android.fragments.cliente;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import ec.redcode.tas.on.android.Constants;
import ec.redcode.tas.on.android.R;
import ec.redcode.tas.on.android.Utils;
import ec.redcode.tas.on.android.activities.base.TasFragmen;
import ec.redcode.tas.on.android.activities.custom.CustomDateTimeDialog;
import ec.redcode.tas.on.android.adapters.CatalogoItemAdapter;
import ec.redcode.tas.on.android.adapters.LocalidadAdapter;
import ec.redcode.tas.on.android.dto.CatalogoItemDTO;
import ec.redcode.tas.on.android.dto.EmpresaDTO;
import ec.redcode.tas.on.android.dto.LocalidadDTO;
import ec.redcode.tas.on.android.dto.SecuenciaDTO;
import ec.redcode.tas.on.android.dto.SolicitudDTO;
import ec.redcode.tas.on.android.dto.SolicitudEnvioDTO;
import ec.redcode.tas.on.android.services.EmpresaService;
import ec.redcode.tas.on.android.services.PublicService;
import ec.redcode.tas.on.android.services.SecuenciaService;
import ec.redcode.tas.on.android.services.SolicitudEnvioService;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegistraSolicitudFragment extends TasFragmen {

    @BindView(R.id.progressbar)
    ProgressBar progressbar;
    @BindView(R.id.cont_load)
    RelativeLayout contLoad;
    @BindView(R.id.termsLayout)
    LinearLayout termsLayout;
    @BindView(R.id.lySolData)
    ScrollView lySolData;

    @BindView(R.id.crearSolic)
    Button crearSolic;
    @BindView(R.id.regresaSolic)
    Button regresaSolic;
    @BindView(R.id.cancelSolic)
    Button cancelSolic;

    @BindView(R.id.solicId)
    EditText solicId;
    @BindView(R.id.solicNumPiesas)
    EditText solicNumPiesas;
    @BindView(R.id.solicPesoTot)
    EditText solicPesoTot;
    @BindView(R.id.solicSpnUnidPeso)
    Spinner solicSpnUnidPeso;
    @BindView(R.id.solicVolTotBruto)
    EditText solicVolTotBruto;
    @BindView(R.id.solicSpnUnidVolum)
    Spinner solicSpnUnidVolum;
    @BindView(R.id.solicFechaCaduca)
    Button solicFechaCaduca;

    @BindView(R.id.solicSpProvOrig)
    Spinner solicSpProvOrig;
    @BindView(R.id.solicSpCantOrig)
    Spinner solicSpCantOrig;
    @BindView(R.id.solicPersonOrig)
    EditText solicPersonOrig;
    @BindView(R.id.solicDirecOrig)
    EditText solicDirecOrig;

    @BindView(R.id.solicSpProvDest)
    Spinner solicSpProvDest;
    @BindView(R.id.solicSpCantDest)
    Spinner solicSpCantDest;
    @BindView(R.id.solicPersonDes)
    EditText solicPersonDes;
    @BindView(R.id.solicDirecDes)
    EditText solicDirecDes;

    @BindView(R.id.solicNumEstiba)
    EditText solicNumEstiba;
    @BindView(R.id.solicDiasVal)
    EditText solicDiasVal;
    @BindView(R.id.solicObserva)
    EditText solicObserva;
    @BindView(R.id.comentarioLayout)
    LinearLayout comentarioLayout;
    @BindView(R.id.solicComentario)
    EditText solicComentario;
    @BindView(R.id.termsCheck)
    CheckBox termsCheck;
    @BindView(R.id.viewTerms)
    Button viewTerms;

    @BindView(R.id.solicFechaRecolecta)
    Button solicFechaRecolecta;
    @BindView(R.id.solicFechaEntrega)
    Button solicFechaEntrega;

    SecuenciaService secuenciaService;
    EmpresaService empresaService;
    SolicitudEnvioService solicitudEnvioService;

    Date fechaCaduca;
    Date fechaRecolecta_;
    Date fechaEntrega_;
    String idSolicitud;
    private SimpleDateFormat dateFormat;

    public RegistraSolicitudFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_regrista_solicitud, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        secuenciaService = new SecuenciaService();
        empresaService = new EmpresaService();
        solicitudEnvioService = new SolicitudEnvioService();

        this.loadData();

        this.controls();

        if (idSolicitud == null)
            this.obtieneSecuencia();

        if (this.idSolicitud != null)
            this.loadDefaultData();

    }

    private void loadData() {
        PublicService publicService = new PublicService();
        this.dateFormat = new SimpleDateFormat(getContext().getString(R.string.defaultDateTime), new Locale("es", "EC"));

        final Observable<CatalogoItemDTO[]> peso = publicService.getCatalogoItemByType(Constants.ID_CATALOGO_PESO);
        peso
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
                        if (checkContext(getContext())) {
                            List<CatalogoItemDTO> dtos = new ArrayList<>();
                            dtos.add(new CatalogoItemDTO(SEL_PESO));
                            dtos.addAll(Arrays.asList(catalogoItemDTOS));
                            CatalogoItemDTO[] stockArr = new CatalogoItemDTO[dtos.size()];
                            catalogoItemDTOS = dtos.toArray(stockArr);

                            CatalogoItemAdapter adapter = new CatalogoItemAdapter(getContext(), android.R.layout.simple_spinner_item, catalogoItemDTOS);
                            solicSpnUnidPeso.setAdapter(adapter);
                        }
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

        final Observable<CatalogoItemDTO[]> volumen = publicService.getCatalogoItemByType(Constants.ID_CATALOGO_VOLUMEN);
        volumen
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
                        if (checkContext(getContext())) {
                            List<CatalogoItemDTO> dtos = new ArrayList<>();
                            dtos.add(new CatalogoItemDTO(SEL_VOLUMEN));
                            dtos.addAll(Arrays.asList(catalogoItemDTOS));
                            CatalogoItemDTO[] stockArr = new CatalogoItemDTO[dtos.size()];
                            catalogoItemDTOS = dtos.toArray(stockArr);

                            CatalogoItemAdapter adapter = new CatalogoItemAdapter(getContext(), android.R.layout.simple_spinner_item, catalogoItemDTOS);
                            solicSpnUnidVolum.setAdapter(adapter);
                        }
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

        final Observable<LocalidadDTO[]> cataProvOrg = publicService.getLocalidadByPadreAndState(Constants.ID_LOCAL_PADRE_ECUADOR, Constants.ID_ESTADO_ACTIVO);
        cataProvOrg
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
                        if (checkContext(getContext())) {
                            List<LocalidadDTO> dtos = new ArrayList<>();
                            dtos.add(new LocalidadDTO(SEL_PROVINCIA));
                            dtos.addAll(Arrays.asList(localidadDTOS));
                            LocalidadDTO[] stockArr = new LocalidadDTO[dtos.size()];
                            localidadDTOS = dtos.toArray(stockArr);

                            LocalidadAdapter adapter = new LocalidadAdapter(getContext(), android.R.layout.simple_spinner_item, localidadDTOS);
                            solicSpProvOrig.setAdapter(adapter);
                            solicSpProvDest.setAdapter(adapter);

                            adapter = new LocalidadAdapter(getContext(), android.R.layout.simple_spinner_item, new LocalidadDTO[]{new LocalidadDTO(SEL_CANTON)});
                            solicSpCantOrig.setAdapter(adapter);
                            solicSpCantDest.setAdapter(adapter);
                        }
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

    private void controls() {
        crearSolic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearSolicitud();
            }
        });

        regresaSolic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regresar();
            }
        });

        cancelSolic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelar();
            }
        });

        viewTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(getContext()); // Context, this, etc.

                View terms = getLayoutInflater().inflate(R.layout.fragment_terms_and_condtitions, null);
                TextView termsText = terms.findViewById(R.id.terms_text);
                Spanned htmlAsSpanned = Html.fromHtml(getString(R.string.Terms_and_conditions_cont));
                termsText.setText(htmlAsSpanned);
                dialog.setContentView(terms);
                dialog.setTitle(R.string.Terms_and_conditions);
                dialog.show();
            }
        });

        solicFechaCaduca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog alertDialog = new CustomDateTimeDialog(view.getContext(), new View.OnClickListener() {
                    @Override
                    public void onClick(View data) {
                        solicFechaCaduca.setText(((TextView) data).getText());
                        try {
                            fechaCaduca = dateFormat.parse(((TextView) data).getText().toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        //mProgressBar.setVisibility(View.GONE);
                        Observable<SecuenciaDTO> observable = secuenciaService.diasHabiles(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(fechaCaduca));
                        observable
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeWith(new DisposableObserver<SecuenciaDTO>() {
                                    @Override
                                    protected void onStart() {
                                        progress();
                                        super.onStart();
                                    }

                                    @Override
                                    public void onNext(SecuenciaDTO responce) {
                                        solicDiasVal.setText(responce.getDias());
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        done();
                                        if (e.toString().toLowerCase().contains("timeout"))
                                            showLongMessage("Lo sentimos ocurrió un error cargando la información," +
                                                    " por favor intente nuevamente...");
                                        else
                                            e.printStackTrace();
                                    }

                                    @Override
                                    public void onComplete() {
                                        done();
                                    }
                                });
                    }
                }, fechaCaduca);
                alertDialog.show();
            }
        });

        solicSpProvOrig.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                LocalidadDTO catalogoItem = (LocalidadDTO) parentView.getSelectedItem();
                if (catalogoItem.getLocalidadId() > 0) {
                    loadCantones(catalogoItem.getLocalidadId(), solicSpCantOrig);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        solicSpProvDest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                LocalidadDTO catalogoItem = (LocalidadDTO) parentView.getSelectedItem();
                if (catalogoItem.getLocalidadId() > 0) {
                    loadCantones(catalogoItem.getLocalidadId(), solicSpCantDest);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        solicFechaRecolecta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog alertDialog = new CustomDateTimeDialog(view.getContext(), new View.OnClickListener() {
                    @Override
                    public void onClick(View data) {
                        solicFechaRecolecta.setText(((TextView) data).getText());
                        try {
                            fechaRecolecta_ = dateFormat.parse(((TextView) data).getText().toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if (fechaRecolecta_ != null && fechaCaduca.after(fechaRecolecta_)) {
                            fechaRecolecta_ = null;
                            solicFechaRecolecta.setText(" - ");

                            showShortMessage("La fecha recoleccion no puede ser menor a: " + solicFechaCaduca.getText().toString());

                        }
                    }
                }, fechaRecolecta_);
                alertDialog.show();
            }
        });

        solicFechaEntrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog alertDialog = new CustomDateTimeDialog(view.getContext(), new View.OnClickListener() {
                    @Override
                    public void onClick(View data) {
                        solicFechaEntrega.setText(((TextView) data).getText());
                        try {
                            fechaEntrega_ = dateFormat.parse(((TextView) data).getText().toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if (fechaEntrega_ != null && fechaRecolecta_.after(fechaEntrega_)) {
                            fechaEntrega_ = null;
                            solicFechaEntrega.setText(" - ");

                            showShortMessage("La fecha recoleccion no puede ser menor a: " + solicFechaEntrega.getText().toString());

                        }
                    }
                }, fechaEntrega_);
                alertDialog.show();
            }
        });
    }

    private void loadDefaultData() {
        crearSolic.setText(getString(R.string.actualizar));
        cancelSolic.setVisibility(View.VISIBLE);
        termsLayout.setVisibility(View.GONE);
        comentarioLayout.setVisibility(View.VISIBLE);
        Observable<SolicitudDTO> observable = solicitudEnvioService.getSolicitudByID(this.idSolicitud);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<SolicitudDTO>() {
                    @Override
                    protected void onStart() {
                        super.onStart();
                        progress();
                    }

                    @Override
                    public void onNext(SolicitudDTO solicitudDTO) {
                        solicId.setText(solicitudDTO.getIdSolicitud());
                        solicNumPiesas.setText(String.valueOf(solicitudDTO.getNumeroPiezas()));
                        solicPesoTot.setText(String.valueOf(solicitudDTO.getPeso()));
                        setDefValSpinCatalogItem(solicSpnUnidPeso, solicitudDTO.getTipoPeso());
                        solicVolTotBruto.setText(String.valueOf(solicitudDTO.getVolumen()));
                        setDefValSpinCatalogItem(solicSpnUnidVolum, solicitudDTO.getTipoVolumen());

                        fechaCaduca = new Date(solicitudDTO.getFechaCaducidad());
                        solicFechaCaduca.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(solicitudDTO.getFechaCaducidad())));

                        spinProvCant(solicSpProvOrig, solicSpCantOrig, solicitudDTO.getIdOrigen());

                        solicPersonOrig.setText(solicitudDTO.getPersonaEntrega());
                        solicDirecOrig.setText(solicitudDTO.getDireccion());

                        spinProvCant(solicSpProvDest, solicSpCantDest, solicitudDTO.getIdDestino());
                        solicPersonDes.setText(solicitudDTO.getPersonaRecibe());
                        solicDirecDes.setText(solicitudDTO.getDireccionEntrega());

                        solicNumEstiba.setText(String.valueOf(solicitudDTO.getNumeroEstibadores()));
                        solicDiasVal.setText(String.valueOf(solicitudDTO.getDiasValidez()));

                        fechaRecolecta_ = new Date(solicitudDTO.getFechaEnvio());
                        solicFechaRecolecta.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(solicitudDTO.getFechaEnvio())));
                        fechaEntrega_ = new Date(solicitudDTO.getFechaEntrega());
                        solicFechaEntrega.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(solicitudDTO.getFechaEntrega())));

                        solicObserva.setText(solicitudDTO.getObservaciones());
                        solicComentario.setText(solicitudDTO.getComments());
                        progress();
                        done();
                    }

                    @Override
                    public void onError(Throwable e) {
                        done();
                    }

                    @Override
                    public void onComplete() {
                        done();
                    }
                });
    }

    private void obtieneSecuencia() {
        Observable<EmpresaDTO> observable = empresaService.getClienteByToken();
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<EmpresaDTO>() {
                    @Override
                    protected void onStart() {
                        progress();
                        super.onStart();
                    }

                    @Override
                    public void onNext(final EmpresaDTO clienteDTO) {
                        solicPersonOrig.setText(Constants.user.getNombres());
                        solicDirecOrig.setText(clienteDTO.getClienteDireccion());

                        Observable<SecuenciaDTO> observable = secuenciaService.obtenerSecuencial(clienteDTO.getClienteRuc());
                        observable
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeWith(new DisposableObserver<SecuenciaDTO>() {
                                    @Override
                                    protected void onStart() {
                                        progress();
                                        super.onStart();
                                    }

                                    @Override
                                    public void onNext(SecuenciaDTO responce) {
                                        solicId.setText(responce.getCodigo());
                                        try {
                                            String date = responce.getFechaCaducidad() + " 00:00:00";
                                            fechaCaduca = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
                                            solicFechaCaduca.setText(date);
                                            solicDiasVal.setText("2");
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        done();
                                        if (e.toString().toLowerCase().contains("timeout"))
                                            showLongMessage("Lo sentimos ocurrió un error cargando la información," +
                                                    " por favor intente nuevamente...");
                                        else
                                            e.printStackTrace();
                                    }

                                    @Override
                                    public void onComplete() {
                                        done();
                                    }
                                });

                        spinProvCant(solicSpProvOrig, solicSpCantOrig, clienteDTO.getClienteLocalidadId());
                        progress();
                        done();
                    }

                    @Override
                    public void onError(Throwable e) {
                        done();
                        if (e.toString().toLowerCase().contains("timeout"))
                            showLongMessage("Lo sentimos ocurrió un error cargando la información," +
                                    " por favor intente nuevamente...");
                        else
                            e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        done();
                    }
                });

    }

    private SolicitudEnvioDTO setSolEnv() {
        SolicitudEnvioDTO solicitudEnvio = new SolicitudEnvioDTO();
        solicitudEnvio.setSolicitudEnvioId(solicId.getText().toString());
        solicitudEnvio.setSolicitudEnvioNumeroPiesas(Integer.parseInt(solicNumPiesas.getText().toString()));
        solicitudEnvio.setSolicitudEnvioPeso(Double.parseDouble(solicPesoTot.getText().toString()));
        solicitudEnvio.setSolicitudEnvioUnidadPeso(((CatalogoItemDTO) Utils.getItemSpinner(solicSpnUnidPeso)).getCatalogoItemId());
        solicitudEnvio.setSolicitudEnvioVolumen(Double.parseDouble(solicVolTotBruto.getText().toString()));
        solicitudEnvio.setSolicitudEnvioUnidadVolumen(((CatalogoItemDTO) Utils.getItemSpinner(solicSpnUnidVolum)).getCatalogoItemId());
        solicitudEnvio.setSolicitudEnvioFechaCaducidad(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(fechaCaduca));

        solicitudEnvio.setSolicitudEnvioLocalidadOrigen(((LocalidadDTO) Utils.getItemSpinner(solicSpCantOrig)).getLocalidadId());
        solicitudEnvio.setSolicitudEnvioPersonaEntrega(solicPersonOrig.getText().toString());
        solicitudEnvio.setSolicitudEnvioDireccionOrigen(solicDirecOrig.getText().toString());

        solicitudEnvio.setSolicitudEnvioLocalidadDestino(((LocalidadDTO) Utils.getItemSpinner(solicSpCantDest)).getLocalidadId());
        solicitudEnvio.setSolicitudEnvioPersonaRecibe(solicPersonDes.getText().toString());
        solicitudEnvio.setSolicitudEnvioDireccionDestino(solicDirecDes.getText().toString());

        solicitudEnvio.setSolicitudEnvioNumeroEstibadores(Integer.parseInt(solicNumEstiba.getText().toString()));
        solicitudEnvio.setSolicitudEnvioDiasValides(Integer.parseInt(solicDiasVal.getText().toString()));
        solicitudEnvio.setSolicitudEnvioFechaRecoleccion(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(fechaRecolecta_));
        solicitudEnvio.setSolicitudEnvioFechaEntrega(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(fechaEntrega_));
        solicitudEnvio.setSolicitudEnvioObservaciones(solicObserva.getText().toString());
        solicitudEnvio.setSolicitudEnvioObservaciones(solicObserva.getText().toString());
        solicitudEnvio.setSolicitudEnvioObservaciones(solicComentario.getText().toString());

        return solicitudEnvio;
    }

    private void crearSolicitud() {

        if (!isFilledRequiredFields()) {
            showShortMessage("Debe registrar todos los campos.");
            done(crearSolic);
            return;
        }

        progress(crearSolic);
        if (termsLayout.getVisibility() == View.VISIBLE) {
            if (!termsCheck.isChecked()) {
                showShortMessage("Debe aceptar los terminos y condiciones");
                done(crearSolic);
                return;
            }

            done(crearSolic);

            SolicitudEnvioDTO envioDTO = setSolEnv();
            envioDTO.setSolicitudEnvioEstado(Constants.ID_SOLICITUD_ENVIO_ACTIVA);

            Observable<String> observable = solicitudEnvioService.creaSolicitudEnvio(envioDTO);
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
                        public void onNext(String responce) {
                            Snackbar snackbar = Snackbar.make(getView(), "Registro exitoso: " + responce, Snackbar.LENGTH_LONG);
                            snackbar.addCallback(new Snackbar.Callback() {
                                @Override
                                public void onDismissed(Snackbar transientBottomBar, int event) {
                                    done(crearSolic);
                                    regresar();
                                }
                            });
                            snackbar.show();
                        }

                        @Override
                        public void onError(Throwable e) {
                            done(crearSolic);
                            if (e.toString().toLowerCase().contains("timeout"))
                                showLongMessage("Lo sentimos ocurrió un error cargando la información," +
                                        " por favor intente nuevamente...");
                            else
                                e.printStackTrace();
                        }

                        @Override
                        public void onComplete() {
                            done(crearSolic);
                        }
                    });
        } else if (termsLayout.getVisibility() == View.GONE) {
            SolicitudEnvioDTO envioDTO = setSolEnv();
            Observable<String> observable = solicitudEnvioService.actualizarSolicitudEnvio(envioDTO);
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
                        public void onNext(String responce) {
                            Snackbar snackbar = Snackbar.make(getView(), "Registro exitoso: " + responce, Snackbar.LENGTH_LONG);
                            snackbar.addCallback(new Snackbar.Callback() {
                                @Override
                                public void onDismissed(Snackbar transientBottomBar, int event) {
                                    done(crearSolic);
                                    regresar();
                                }
                            });
                            snackbar.show();
                        }

                        @Override
                        public void onError(Throwable e) {
                            done(crearSolic);
                            if (e.toString().toLowerCase().contains("timeout"))
                                showLongMessage("Lo sentimos ocurrió un error cargando la información," +
                                        " por favor intente nuevamente...");
                            else
                                e.printStackTrace();
                        }

                        @Override
                        public void onComplete() {
                            done(crearSolic);
                        }
                    });
        }
    }

    private boolean isFilledRequiredFields() {
        boolean ban = true;

        if (Utils.getTextValue(solicId).equals("")) {
            Utils.setErrorView(solicId);
            ban = false;
        } else {
            Utils.resetErrorView(solicId);
            if (solicId.getText().length() == 0) {
                Utils.setErrorView(solicId, "Porfavor vuelva a ingresar solicitud");
                ban = false;
            }
        }

        if (Utils.getTextValue(solicNumPiesas).equals("")) {
            Utils.setErrorView(solicNumPiesas);
            ban = false;
        } else {
            Utils.resetErrorView(solicNumPiesas);
        }

        if (Utils.getTextValue(solicPesoTot).equals("")) {
            Utils.setErrorView(solicPesoTot);
            ban = false;
        } else {
            Utils.resetErrorView(solicPesoTot);
        }

        if (((CatalogoItemDTO) Utils.getItemSpinner(solicSpnUnidPeso)).getCatalogoItemDescripcion().equals(SEL_PESO)) {
            ban = false;
            Utils.setErrorView(solicSpnUnidPeso.getSelectedView());
        } else Utils.resetErrorView(solicSpnUnidPeso.getSelectedView());

        if (Utils.getTextValue(solicVolTotBruto).equals("")) {
            Utils.setErrorView(solicVolTotBruto);
            ban = false;
        } else {
            Utils.resetErrorView(solicVolTotBruto);
        }

        if (((CatalogoItemDTO) Utils.getItemSpinner(solicSpnUnidVolum)).getCatalogoItemDescripcion().equals(SEL_VOLUMEN)) {
            ban = false;
            Utils.setErrorView(solicSpnUnidVolum.getSelectedView());
        } else Utils.resetErrorView(solicSpnUnidVolum.getSelectedView());

        if (Utils.getTextValue(solicFechaCaduca).equals(" - ")) {
            Utils.setErrorView(solicFechaCaduca, "Por favor seleccione una fecha");
            ban = false;
        } else {
            Utils.resetErrorView(solicFechaCaduca);
        }

        if (((LocalidadDTO) Utils.getItemSpinner(solicSpCantOrig)).getLocalidadDescripcion().equals(SEL_CANTON)) {
            ban = false;
            Utils.setErrorView(solicSpCantOrig.getSelectedView());
        } else Utils.resetErrorView(solicSpCantOrig.getSelectedView());

        if (Utils.getTextValue(solicPersonOrig).equals("")) {
            Utils.setErrorView(solicPersonOrig);
            ban = false;
        } else {
            Utils.resetErrorView(solicPersonOrig);
        }

        if (Utils.getTextValue(solicDirecOrig).equals("")) {
            Utils.setErrorView(solicDirecOrig);
            ban = false;
        } else {
            Utils.resetErrorView(solicDirecOrig);
        }

        if (((LocalidadDTO) Utils.getItemSpinner(solicSpCantDest)).getLocalidadDescripcion().equals(SEL_CANTON)) {
            ban = false;
            Utils.setErrorView(solicSpCantDest.getSelectedView());
        } else Utils.resetErrorView(solicSpCantDest.getSelectedView());

        if (Utils.getTextValue(solicPersonDes).equals("")) {
            Utils.setErrorView(solicPersonDes);
            ban = false;
        } else {
            Utils.resetErrorView(solicPersonDes);
        }

        if (Utils.getTextValue(solicDirecDes).equals("")) {
            Utils.setErrorView(solicDirecDes);
            ban = false;
        } else {
            Utils.resetErrorView(solicDirecDes);
        }

        if (Utils.getTextValue(solicNumEstiba).equals("")) {
            Utils.setErrorView(solicNumEstiba);
            ban = false;
        } else {
            Utils.resetErrorView(solicNumEstiba);
        }

        if (Utils.getTextValue(solicDiasVal).equals("")) {
            Utils.setErrorView(solicDiasVal);
            ban = false;
        } else {
            Utils.resetErrorView(solicDiasVal);
        }

        if (Utils.getTextValue(solicFechaRecolecta).equals(" - ")) {
            Utils.setErrorView(solicFechaRecolecta, "Por favor seleccione una fecha");
            ban = false;
        } else {
            Utils.resetErrorView(solicFechaRecolecta);
        }

        if (Utils.getTextValue(solicFechaEntrega).equals(" - ")) {
            Utils.setErrorView(solicFechaEntrega, "Por favor seleccione una fecha");
            ban = false;
        } else {
            Utils.resetErrorView(solicFechaEntrega);
        }

        if (Utils.getTextValue(solicObserva).equals("")) {
            Utils.setErrorView(solicObserva);
            ban = false;
        } else {
            Utils.resetErrorView(solicObserva);
        }

        return ban;
    }

    private void regresar() {
        /*if (this.idSolicitud != null)*/
        /*if (getFragmentManager().isStateSaved())*/
        getFragmentManager().popBackStack();
        /*else
            showShortMessage("Cargando información por favor espere....");*/
        /*else {
            HomeDashFragment dashFragment = new HomeDashFragment();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, dashFragment).addToBackStack(null).commit();
        }*/
    }

    private void cancelar() {
        progress(crearSolic);
        if (!isFilledRequiredFields()) {
            showShortMessage("Debe registrar todos los campos.");
            done(crearSolic);
            return;
        }

        boolean ban = true;
        if (Utils.getTextValue(solicComentario).equals("")) {
            Utils.setErrorView(solicComentario);
            ban = false;
        } else {
            Utils.resetErrorView(solicComentario);
            if (solicComentario.getText().length() == 0) {
                Utils.setErrorView(solicComentario, "Por favor ingrese un comentario");
                ban = false;
            }
        }
        if (!ban) {
            showShortMessage("Debe registrar todos los campos.");
            done(crearSolic);
            return;
        }

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getContext());
        }
        builder.setTitle("Cancelar la solicitud")
                .setMessage("Esta seguro de cancelar la solicitud?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        progress(crearSolic);

                        SolicitudEnvioDTO envioDTO = setSolEnv();
                        envioDTO.setSolicitudEnvioEstado(Constants.ID_SOLICITUD_ENVIO_CANCELADA);

                        Observable<String> observable = solicitudEnvioService.cancelarSolicitudEnvio(envioDTO);
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
                                    public void onNext(String responce) {
                                        Snackbar snackbar = Snackbar.make(getView(), "Registro exitoso: " + responce, Snackbar.LENGTH_LONG);
                                        snackbar.addCallback(new Snackbar.Callback() {
                                            @Override
                                            public void onDismissed(Snackbar transientBottomBar, int event) {
                                                done(crearSolic);
                                                regresar();
                                            }
                                        });
                                        snackbar.show();
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        done(crearSolic);
                                        if (e.toString().toLowerCase().contains("timeout"))
                                            showLongMessage("Lo sentimos ocurrió un error cargando la información," +
                                                    " por favor intente nuevamente...");
                                        else
                                            e.printStackTrace();
                                    }

                                    @Override
                                    public void onComplete() {
                                        done(crearSolic);
                                    }
                                });
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        done(crearSolic);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    protected void progress() {
        progressbar.setVisibility(View.VISIBLE);
        contLoad.setVisibility(View.GONE);
    }

    @Override
    protected void done() {
        progressbar.setVisibility(View.GONE);
        contLoad.setVisibility(View.VISIBLE);
    }

    public void setIdSolicitud(String idSolicitud) {
        this.idSolicitud = idSolicitud;
    }
}
