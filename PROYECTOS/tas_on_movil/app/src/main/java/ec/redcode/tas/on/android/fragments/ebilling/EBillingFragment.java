package ec.redcode.tas.on.android.fragments.ebilling;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import ec.redcode.tas.on.android.Constants;
import ec.redcode.tas.on.android.R;
import ec.redcode.tas.on.android.Utils;
import ec.redcode.tas.on.android.adapters.CatalogoItemAdapter;
import ec.redcode.tas.on.android.adapters.ebilling.EBillingDetailItemAdapter;
import ec.redcode.tas.on.android.dto.CatalogoItemDTO;
import ec.redcode.tas.on.android.dto.LocalidadDTO;
import ec.redcode.tas.on.android.dto.requests.Adquiriente;
import ec.redcode.tas.on.android.dto.requests.DetalleFactura;
import ec.redcode.tas.on.android.dto.requests.EBillingDTO;
import ec.redcode.tas.on.android.dto.requests.EBillingInfo;
import ec.redcode.tas.on.android.services.EBillingService;
import ec.redcode.tas.on.android.services.PublicService;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class EBillingFragment extends Fragment {

    public static final int STEP_MAX = 4;
    public static final String SELECCIONE_OPCION = "Seleccione una opción";
    public int paso = 1;
    public CatalogoItemDTO tipoDocumentoAdquiriente;
    private EBillingDTO eBillingDTO;

    private List<DetalleFactura> detalleFacturaList = new ArrayList<>();

    @BindView(R.id.txtPaso)
    TextView txtPaso;
    @BindView(R.id.btnBack)
    ImageButton btnBack;
    @BindView(R.id.btnNext)
    ImageButton btnNext;
    @BindView(R.id.barraBotones)
    LinearLayout barraBotones;
    //PASO 0
    @BindView(R.id.ebilling_step_zero)
    ScrollView stepZero;
    //PASO 1
    @BindView(R.id.ebilling_step_one)
    ScrollView stepOne;
    @BindView(R.id.loading_ebilling_info)
    LinearLayout loadingEbillingInfo;
    @BindView(R.id.cont_main_step_one)
    LinearLayout contMainStepOne;
    @BindView(R.id.txtRazonSocial)
    TextView txtRazonSocial;
    @BindView(R.id.txtRUC)
    TextView txtRUC;
    @BindView(R.id.txtDireccion)
    TextView txtDireccion;
    @BindView(R.id.txtNumeroFactura)
    TextView txtNumeroFactura;
    @BindView(R.id.txtNumeroGuia)
    EditText txtNumeroGuia;
    //PASO 2
    @BindView(R.id.ebilling_step_two)
    ScrollView stepTwo;
    @BindView(R.id.cmbTipoDocumento)
    Spinner cmbTipoDocumento;
    @BindView(R.id.txtNumDocAdquiriente)
    EditText txtNumDocAdquiriente;
    @BindView(R.id.loading_adquiriente_info)
    LinearLayout loadingAdquirienteInfo;
    @BindView(R.id.lytDatosAdquiriente)
    LinearLayout lytDatosAdquiriente;
    @BindView(R.id.txtRazonSocialAdquiriente)
    EditText txtRazonSocialAdquiriente;
    @BindView(R.id.txtDireccionAdquiriente)
    EditText txtDireccionAdquiriente;
    @BindView(R.id.txtTelefonoAdquiriente)
    EditText txtTelefonoAdquiriente;
    @BindView(R.id.txtCorreoAdquiriente)
    EditText txtCorreoAdquiriente;
    @BindView(R.id.txtPersonaContacto)
    EditText txtPersonaContacto;
    //PASO 3
    @BindView(R.id.ebilling_step_three)
    LinearLayout stepThree;
    @BindView(R.id.btnNewDetail)
    ImageButton btnNewDetail;
    @BindView(R.id.newDetail)
    LinearLayout newDetail;
    @BindView(R.id.txtNumeroPiezas)
    EditText txtNumeroPiezas;
    @BindView(R.id.cmbUnidadPiezas)
    Spinner cmbUnidadPiezas;
    @BindView(R.id.autoCompleteCiudadOrigen)
    AutoCompleteTextView autoCompleteCiudadOrigen;
    LocalidadDTO ciudadOrigen;
    @BindView(R.id.autoCompleteCiudadDestino)
    AutoCompleteTextView autoCompleteCiudadDestino;
    LocalidadDTO ciudadDestino;
    @BindView(R.id.txtDetallesAdicionales)
    EditText txtDetallesAdicionales;
    @BindView(R.id.txtValor)
    EditText txtPrecioUnitario;
    @BindView(R.id.txtDescuento)
    EditText txtDescuento;
    @BindView(R.id.btnCancelDetail)
    ImageButton btnCancelDetail;
    @BindView(R.id.btnAddDetail)
    ImageButton btnAddDetail;
    @BindView(R.id.ebillingDetailListView)
    ListView ebillingDetailListView;
    //PASO 4
    @BindView(R.id.ebilling_step_four)
    ScrollView stepFour;
    @BindView(R.id.loading_step_four)
    LinearLayout loadingStepFour;
    @BindView(R.id.content_step_four)
    LinearLayout contentStepFour;
    @BindView(R.id.subtotalNoIVA)
    TextView subtotalNoIVA;
    @BindView(R.id.subtotalSinImpuestos)
    TextView subtotalSinImpuestos;
    @BindView(R.id.totalDescuento)
    TextView totalDescuento;
    @BindView(R.id.valorPagar)
    TextView valorPagar;
    @BindView(R.id.formaPagoSpinner)
    Spinner formaPagoSpinner;
    @BindView(R.id.generarFacturaElectronicaBtn)
    Button generarFacturaElectronicaBtn;
    @BindView(R.id.txtDiasPlazo)
    EditText txtDiasPlazo;
    //PASO OK
    @BindView(R.id.ebilling_step_ok)
    ScrollView stepOk;

    EBillingDetailItemAdapter adapter;
    EBillingService eBillingService;
    PublicService publicService;
    LocalidadDTO[] ciudades = {};

    public EBillingFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_ebilling, container, false);
        ButterKnife.bind(this, view);
        eBillingService = new EBillingService();
        publicService = new PublicService();
        eBillingDTO = new EBillingDTO();
        paso = 1;
        btnBack.setVisibility(View.GONE);
        setActionListenerBtnBack();
        setActionListenerBtnNext();
        setChangeListenerIdentificacionAdquiriente();
        //steep three
        setActionListenerBtnDetailAdd();
        //steep four
        loadCities();
        txtDiasPlazo.setText("0");
        setActionLinstenerBtnGenerarFactura();
        consultarUsuarioEbilling();
        loadCatalogData();
        return view;
    }

    public void setChangeListenerIdentificacionAdquiriente(){
        cmbTipoDocumento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(Utils.getItemSpinner(cmbTipoDocumento) == null) return;
                txtNumDocAdquiriente.setText("");
                tipoDocumentoAdquiriente = ((CatalogoItemDTO) Utils.getItemSpinner(cmbTipoDocumento));
                if (tipoDocumentoAdquiriente.getCatalogoItemDescripcion().toUpperCase().contains("RUC")) {
                    txtNumDocAdquiriente.setFilters(new InputFilter[] {new InputFilter.LengthFilter(13)});
                }
                if (tipoDocumentoAdquiriente.getCatalogoItemDescripcion().toUpperCase().contains("CEDULA")) {
                    txtNumDocAdquiriente.setFilters(new InputFilter[] {new InputFilter.LengthFilter(10)});
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) { }

        });

        txtNumDocAdquiriente.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(tipoDocumentoAdquiriente == null || tipoDocumentoAdquiriente.getCatalogoItemDescripcion().equals(SELECCIONE_OPCION)){
                    lytDatosAdquiriente.setVisibility(View.GONE);
                    return;
                }

                if (tipoDocumentoAdquiriente.getCatalogoItemDescripcion().toUpperCase().contains("RUC")) {
                    if(s.length() == 13){
                        consultarAdquiriente();
                    } else{
                        lytDatosAdquiriente.setVisibility(View.GONE);
                    }
                }
                if (tipoDocumentoAdquiriente.getCatalogoItemDescripcion().toUpperCase().contains("CEDULA")) {
                    if(s.length() == 10){
                        consultarAdquiriente();
                    } else{
                        lytDatosAdquiriente.setVisibility(View.GONE);
                    }
                }

            }
        });
    }

    private void consultarUsuarioEbilling(){
        Utils.setVisibilityVisible(loadingEbillingInfo);
        Utils.setVisibilityGone(contMainStepOne, btnNext);
        final Observable<EBillingInfo> eBillingInfo = eBillingService.getEBillingInfo();
        eBillingInfo
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<EBillingInfo>() {
                    @Override
                    public void onNext(EBillingInfo info) {
                        if(getContext()== null)return;
                        mostrarDatosEbilling(info);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mostrarMensajeNoUsuarioEbilling();
                    }

                    @Override
                    public void onComplete() {
                        Utils.setVisibilityVisible(contMainStepOne, btnNext);
                        Utils.setVisibilityGone(loadingEbillingInfo);
                    }
                });
    }

    private void mostrarMensajeNoUsuarioEbilling(){
        paso = 0;
        showSteps();
    }

    private void consultarAdquiriente(){
        Utils.hideKeyboard(getActivity());
        Utils.setVisibilityVisible(loadingAdquirienteInfo);

        final Observable<Adquiriente> getAdquiriente = eBillingService.getAdquiriente((String)Utils.getTextValue(txtNumDocAdquiriente));
        getAdquiriente
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Adquiriente>() {
                    @Override
                    public void onNext(Adquiriente adquirienteResponse) {
                        if(getContext()== null)return;
                        mostrarDatosAdquiriente(adquirienteResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mostrarDatosAdquiriente(null);
                    }

                    @Override
                    public void onComplete() {
                        loadingAdquirienteInfo.setVisibility(View.GONE);
                    }
                });

    }

    private void mostrarDatosEbilling(EBillingInfo eBillingInfo){
        txtRazonSocial.setText(eBillingInfo.getRazonSocial());
        txtDireccion.setText(eBillingInfo.getDireccion());
        txtRUC.setText(eBillingInfo.getRuc());
        txtNumeroFactura.setText(eBillingInfo.getFacturaNro());
        paso=1;
        labelProcess(paso);
        showSteps();
    }

    private void mostrarDatosAdquiriente(Adquiriente adquiriente){
        loadingAdquirienteInfo.setVisibility(View.GONE);
        resetAdquirienteInputs();
        lytDatosAdquiriente.setVisibility(View.VISIBLE);
        if(adquiriente == null) return;

        txtRazonSocialAdquiriente.setText(adquiriente.getAdquirienteRazonSocial());
        txtDireccionAdquiriente.setText(adquiriente.getAdquirienteDireccion());
        txtTelefonoAdquiriente.setText(adquiriente.getAdquirienteTelefono());
        txtCorreoAdquiriente.setText(adquiriente.getAdquirienteMail());
        txtPersonaContacto.setText(adquiriente.getAdquirientePersonaContacto());
    }

    private void labelProcess(int paso) {
        txtPaso.setText(String.format(new Locale("es", "EC"), " Paso: %d de %d", paso, STEP_MAX));
    }

    public void setActionListenerBtnBack(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paso--;
                labelProcess(paso);
                Utils.hideKeyboard(getActivity());
                if(paso == 1){
                    btnBack.setVisibility(View.GONE);
                }
                if(paso < STEP_MAX){
                    btnNext.setVisibility(View.VISIBLE);
                }
                showSteps();
            }
        });
    }

    private boolean validarDatos(){
        boolean isValid = true;
        switch (paso){
            case 1:
                Utils.resetErrorView(txtNumeroGuia);
                if (Utils.getTextValue(txtNumeroGuia)!=null && !(Utils.getTextValue(txtNumeroGuia)).equals("")) {
                    if (!Utils.guiaRemisionValidator(Utils.getTextValue(txtNumeroGuia).toString())) {
                        Utils.setErrorView(txtNumeroGuia, "No cumple con el formato 001-001-123456789");
                        isValid = false;
                    }

                }
                break;
            case 2:
                if(!Utils.validateSpinnersCatalogoItemRequired(cmbTipoDocumento) ||
                        !Utils.validateTextViewsRequired(txtNumDocAdquiriente, txtRazonSocialAdquiriente,
                        txtDireccionAdquiriente, txtTelefonoAdquiriente, txtCorreoAdquiriente, txtPersonaContacto))
                    isValid = false;

                if (tipoDocumentoAdquiriente.getCatalogoItemDescripcion().toUpperCase().contains("RUC")
                        && !Utils.validateLengthRUC(txtNumDocAdquiriente)) {
                    isValid = false;
                }
                if (tipoDocumentoAdquiriente.getCatalogoItemDescripcion().toUpperCase().contains("CEDULA")
                        && !Utils.validateLengthCedula(txtNumDocAdquiriente)) {
                    isValid = false;
                }
                break;
            case 3:
                if(detalleFacturaList.isEmpty()) isValid = false;
                break;
            case 4:
                if(!Utils.validateSpinnersCatalogoItemRequired(formaPagoSpinner)){
                    isValid = false;
                }
                break;
        }
        return isValid;
    }

    private void mapearDatos(){
        switch (paso){
            case 1:
                eBillingDTO.setGuiaRemision((String) Utils.getTextValue(txtNumeroGuia));
                break;
            case 2:
                Adquiriente adquiriente = new Adquiriente();
                adquiriente.setAdquirienteTipoDocumento(((CatalogoItemDTO) Utils.getItemSpinner(cmbTipoDocumento)).getCatalogoItemId());
                adquiriente.setAdquirienteIdDocumento((String) Utils.getTextValue(txtNumDocAdquiriente));
                adquiriente.setAdquirienteRazonSocial((String) Utils.getTextValue(txtRazonSocialAdquiriente));
                adquiriente.setAdquirienteDireccion((String) Utils.getTextValue(txtDireccionAdquiriente));
                adquiriente.setAdquirienteTelefono((String) Utils.getTextValue(txtTelefonoAdquiriente));
                adquiriente.setAdquirienteMail((String) Utils.getTextValue(txtCorreoAdquiriente));
                adquiriente.setAdquirientePersonaContacto((String) Utils.getTextValue(txtPersonaContacto));
                eBillingDTO.setAdquiriente(adquiriente);
                break;
            case 3:
                eBillingDTO.setDetalles(detalleFacturaList);
                break;
            case 4:
                eBillingDTO.setFormaPago(((CatalogoItemDTO) Utils.getItemSpinner(formaPagoSpinner)).getCatalogoItemId());
                Integer diasPlazo = Utils.getIntegerFromTextView(txtDiasPlazo);
                eBillingDTO.setDiasPlazo(diasPlazo == null || diasPlazo < 0 ? 0 : diasPlazo);
                break;
        }
    }

    public void showSteps(){
        switch (paso){
            case 0:
                Utils.setVisibilityVisible(stepZero);
                Utils.setVisibilityGone(barraBotones, stepOne, stepTwo, stepThree, stepFour);
                break;
            case 1:
                Utils.setVisibilityVisible(stepOne);
                Utils.setVisibilityGone(stepTwo, stepThree, stepFour);
                break;
            case 2:
                Utils.setVisibilityVisible(stepTwo);
                Utils.setVisibilityGone(stepOne, stepThree, stepFour);
                break;
            case 3:
                Utils.setVisibilityVisible(stepThree);
                Utils.setVisibilityGone(stepOne, stepTwo, stepFour);
                break;
            case 4:
                calcularTotales();
                Utils.setVisibilityVisible(stepFour);
                Utils.setVisibilityGone(stepOne, stepTwo, stepThree);
                break;
            case 5:
                Utils.setVisibilityVisible(stepOk);
                Utils.setVisibilityGone(barraBotones, stepOne, stepTwo, stepThree, stepFour);
                txtPaso.setText("");
                break;
            default:
                Utils.setVisibilityGone(btnNext, btnBack, stepOne, stepTwo, stepThree, stepFour);
                break;

        }
    }

    public void setActionListenerBtnNext(){
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideKeyboard(getActivity());
                if(!validarDatos()) return;
                mapearDatos();
                paso++;
                labelProcess(paso);
                if(paso>1){
                    btnBack.setVisibility(View.VISIBLE);
                }
                if(paso == STEP_MAX){
                    btnNext.setVisibility(View.GONE);
                }
                showSteps();
            }
        });
    }

    public void setActionListenerBtnDetailAdd(){

        btnNewDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.setVisibilityGone(btnNewDetail, btnNext, btnBack, ebillingDetailListView);
                Utils.setVisibilityVisible(newDetail);
                Utils.hideKeyboard(getActivity());
            }
        });

        btnCancelDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetAddEbillingDetailView();
                Utils.setVisibilityGone(newDetail);
                Utils.setVisibilityVisible(btnNewDetail, btnNext, btnBack, ebillingDetailListView);
                Utils.hideKeyboard(getActivity());
            }
        });


        btnAddDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Utils.validateTextViewsRequired(txtNumeroPiezas, autoCompleteCiudadOrigen,
                        autoCompleteCiudadDestino, txtPrecioUnitario) || !Utils.validateSpinnersCatalogoItemRequired(cmbUnidadPiezas))
                    return;

                if(ciudadOrigen == null){
                    Utils.setErrorView(autoCompleteCiudadOrigen, "Elija una ciudad de las opciones mostradas");
                }
                if(ciudadDestino == null){
                    Utils.setErrorView(autoCompleteCiudadDestino, "Elija una ciudad de las opciones mostradas");
                }
                if(ciudadOrigen == null || ciudadDestino == null) return;

                DetalleFactura detalleFactura = new DetalleFactura();
                detalleFactura.setNumeroPiezas(Utils.getIntegerFromTextView(txtNumeroPiezas));
                detalleFactura.setUnidadPiezas((CatalogoItemDTO) Utils.getItemSpinner(cmbUnidadPiezas));
                detalleFactura.setCiudadOrigen(ciudadOrigen);
                detalleFactura.setCiudadDestino(ciudadDestino);
                detalleFactura.setPrecioUnitario(Utils.getBigDecimalFromTextView(txtPrecioUnitario));
                detalleFactura.setDescuento(Utils.getBigDecimalFromTextView(txtDescuento));
                detalleFactura.setDetallesAdicionales((String) Utils.getTextValue(txtDetallesAdicionales));

                detalleFacturaList.add(detalleFactura);

                adapter = new EBillingDetailItemAdapter(getContext(), (ArrayList<DetalleFactura>) detalleFacturaList);
                ebillingDetailListView.setAdapter(adapter);

                resetAddEbillingDetailView();
                Utils.setVisibilityGone(newDetail);
                Utils.setVisibilityVisible(btnNewDetail, btnNext, btnBack, ebillingDetailListView);
                Utils.hideKeyboard(getActivity());
            }
        });


        ebillingDetailListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
                adb.setTitle("Eliminar");
                adb.setMessage("¿Estás seguro que quieres eliminar este registro? ");
                final int positionToRemove = position;
                adb.setNegativeButton("Cancelar", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        detalleFacturaList.remove(positionToRemove);
                        adapter.notifyDataSetChanged();
                    }});
                adb.show();
                return false;
            }
        });

    }

    private void calcularTotales(){
        BigDecimal totalSinDescuento = BigDecimal.ZERO;
        BigDecimal totalDescuento2 = BigDecimal.ZERO;
        BigDecimal total;
        for(DetalleFactura detalle : detalleFacturaList){
            totalDescuento2 = totalDescuento2.add(detalle.getDescuento());
            totalSinDescuento = totalSinDescuento.add(detalle.getPrecioUnitario());
        }
        total = totalSinDescuento.subtract(totalDescuento2);
        subtotalNoIVA.setText(String.valueOf(total.setScale(2, BigDecimal.ROUND_HALF_UP)));
        subtotalSinImpuestos.setText(String.valueOf(total.setScale(2, BigDecimal.ROUND_HALF_UP)));
        totalDescuento.setText(String.valueOf(totalDescuento2.setScale(2, BigDecimal.ROUND_HALF_UP)));
        valorPagar.setText(String.valueOf(total.setScale(2, BigDecimal.ROUND_HALF_UP)));
    }

    private void setActionLinstenerBtnGenerarFactura(){
        generarFacturaElectronicaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Ingrese la clave de su firma electrónica");
                final EditText input = new EditText(getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        eBillingDTO.setClaveFirma(input.getText().toString());
                        createEBilling();
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
    }

    private void setAutoCompleteOptions() {
        ArrayAdapter<LocalidadDTO> adapter = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_dropdown_item_1line, ciudades);

        autoCompleteCiudadOrigen.setThreshold(1);//will start working from first character
        autoCompleteCiudadOrigen.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        autoCompleteCiudadDestino.setThreshold(1);
        autoCompleteCiudadDestino.setAdapter(adapter);

        autoCompleteCiudadOrigen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
                ciudadOrigen = ((LocalidadDTO)parent.getItemAtPosition(pos));
                Utils.hideKeyboard(getActivity());
            }
        });

        autoCompleteCiudadOrigen.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                autoCompleteCiudadOrigen.setText("");
                ciudadOrigen = null;
                return true;
            }
        });

        autoCompleteCiudadDestino.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
                ciudadDestino = ((LocalidadDTO)parent.getItemAtPosition(pos));
                Utils.hideKeyboard(getActivity());
            }
        });

        autoCompleteCiudadDestino.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                autoCompleteCiudadDestino.setText("");
                ciudadDestino = null;
                return true;
            }
        });

    }

    private void loadCatalogData() {
        publicService.getCatalogoItemByType(Constants.ID_CATALOGO_DOCUMENTO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<CatalogoItemDTO[]>() {
                    @Override
                    protected void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onNext(CatalogoItemDTO[] catalogoItemDTOS) {
                        if(getContext()== null)return;
                        List<CatalogoItemDTO> dtos = new ArrayList<>();
                        dtos.add(new CatalogoItemDTO(SELECCIONE_OPCION));
                        dtos.addAll(Arrays.asList(catalogoItemDTOS));
                        CatalogoItemDTO[] stockArr = new CatalogoItemDTO[dtos.size()];
                        catalogoItemDTOS = dtos.toArray(stockArr);

                        CatalogoItemAdapter adapter = new CatalogoItemAdapter(getContext(), android.R.layout.simple_spinner_item, catalogoItemDTOS);
                        cmbTipoDocumento.setAdapter(adapter);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.w("Error", e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        publicService.getCatalogoItemByType(Constants.ID_CATALOGO_FORMAS_PAGO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<CatalogoItemDTO[]>() {
                    @Override
                    protected void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onNext(CatalogoItemDTO[] catalogoItemDTOS) {
                        if(getContext()== null)return;
                        List<CatalogoItemDTO> dtos = new ArrayList<>();
                        dtos.add(new CatalogoItemDTO(SELECCIONE_OPCION));
                        dtos.addAll(Arrays.asList(catalogoItemDTOS));
                        CatalogoItemDTO[] stockArr = new CatalogoItemDTO[dtos.size()];
                        catalogoItemDTOS = dtos.toArray(stockArr);
                        CatalogoItemAdapter adapter = new CatalogoItemAdapter(getContext(), android.R.layout.simple_spinner_item, catalogoItemDTOS);
                        formaPagoSpinner.setAdapter(adapter);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.w("Error", e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        publicService.getCatalogoItemByType(Constants.ID_CATALOGO_UNIDAD_PIEZAS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<CatalogoItemDTO[]>() {
                    @Override
                    protected void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onNext(CatalogoItemDTO[] catalogoItemDTOS) {
                        if(getContext()== null)return;
                        List<CatalogoItemDTO> dtos = new ArrayList<>();
                        dtos.add(new CatalogoItemDTO(SELECCIONE_OPCION));
                        dtos.addAll(Arrays.asList(catalogoItemDTOS));
                        CatalogoItemDTO[] stockArr = new CatalogoItemDTO[dtos.size()];
                        catalogoItemDTOS = dtos.toArray(stockArr);
                        CatalogoItemAdapter adapter = new CatalogoItemAdapter(getContext(), android.R.layout.simple_spinner_item, catalogoItemDTOS);
                        cmbUnidadPiezas.setAdapter(adapter);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.w("Error", e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void loadCities(){
        final Observable<LocalidadDTO[]> cataProvOrg = publicService.getAllCities();
        cataProvOrg
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<LocalidadDTO[]>() {
                    @Override
                    protected void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onNext(LocalidadDTO[] localidadDTOS) {
                        if(getContext()== null)return;
                        ciudades = localidadDTOS;
                        setAutoCompleteOptions();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.w("Error", e);
                    }

                    @Override
                    public void onComplete() { }
                });
    }

    private void resetAddEbillingDetailView(){
        ciudadOrigen = null;
        ciudadDestino = null;
        EditText[] textViewsEBillingDetailArray =  {txtNumeroPiezas, autoCompleteCiudadOrigen,
                autoCompleteCiudadDestino, txtDetallesAdicionales, txtPrecioUnitario, txtDescuento};

        Utils.resetErrorView(textViewsEBillingDetailArray);
        for(EditText editText: textViewsEBillingDetailArray){
            Utils.setEmptyView(editText);
        }
    }

    private void resetAdquirienteInputs(){
        EditText[] textViewsAdquirienteArray =  {txtRazonSocialAdquiriente, txtDireccionAdquiriente,
                txtTelefonoAdquiriente, txtCorreoAdquiriente, txtPersonaContacto};

        Utils.resetErrorView(textViewsAdquirienteArray);
        for(EditText editText: textViewsAdquirienteArray){
            Utils.setEmptyView(editText);
        }
    }

    private void createEBilling(){
        mapearDatos();
        if(!validarDatos()) return;
        generarFacturaElectronicaBtn.setEnabled(false);
        loadingStepFour.setVisibility(View.VISIBLE);
        contentStepFour.setVisibility(View.GONE);
        btnBack.setVisibility(View.GONE);
        final Observable<Map<String, String>> generateEBilling = eBillingService.generateEBilling(eBillingDTO);
        generateEBilling
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Map<String, String>>() {
                    @Override
                    public void onNext(Map<String, String> response) {
                        if(getContext()== null)return;
                        if(response.get("response") !=null && response.get("response").contains("OK")){
                            paso = 5;
                            showSteps();
                        }
                        else {
                            Toast.makeText(getContext(), response.get("responseMessage"), Toast.LENGTH_SHORT).show();
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Ingrese la clave de su firma electrónica");
                            final EditText input = new EditText(getContext());
                            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            builder.setView(input);
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    eBillingDTO.setClaveFirma(input.getText().toString());
                                    createEBilling();
                                }
                            });
                            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            builder.show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getContext(), "ERROR", Toast.LENGTH_SHORT).show();
                        generarFacturaElectronicaBtn.setEnabled(true);
                        loadingStepFour.setVisibility(View.GONE);
                        contentStepFour.setVisibility(View.VISIBLE);
                        btnBack.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onComplete() {
                        generarFacturaElectronicaBtn.setEnabled(true);
                        loadingStepFour.setVisibility(View.GONE);
                        contentStepFour.setVisibility(View.VISIBLE);
                    }
                });
    }
}