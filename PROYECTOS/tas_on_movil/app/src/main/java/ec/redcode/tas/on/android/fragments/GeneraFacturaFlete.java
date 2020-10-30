package ec.redcode.tas.on.android.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import ec.redcode.tas.on.android.Constants;
import ec.redcode.tas.on.android.R;
import ec.redcode.tas.on.android.adapters.conductor.RowGeneraFacturaFlete;
import ec.redcode.tas.on.android.dto.ClienteDTO;
import ec.redcode.tas.on.android.dto.CodigoFacturaDTO;
import ec.redcode.tas.on.android.dto.EmpresaDTO;
import ec.redcode.tas.on.android.dto.InvoiceDTO;
import ec.redcode.tas.on.android.dto.InvoiceDetailDTO;
import ec.redcode.tas.on.android.dto.OffersDTO;
import ec.redcode.tas.on.android.mappers.FleteListMapper;
import ec.redcode.tas.on.android.models.FleteShort;
import ec.redcode.tas.on.android.services.EmpresaService;
import ec.redcode.tas.on.android.services.FacturaService;
import ec.redcode.tas.on.android.services.MyOfertListService;
import ec.redcode.tas.on.android.services.PublicService;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public class GeneraFacturaFlete extends Fragment {

    //Services
    private MyOfertListService myOfertListService;
    private EmpresaService empresaService;
    private FacturaService facturaService;
    private PublicService publicService;
    private final String detalleFactura = "Código de documento: #numDoc," +
            System.getProperty("line.separator") + " transporte de mercadería pesada.";

    //Elements
    private List<FleteShort> mValues;
    @BindView(R.id.btnAddSol)
    Button btnAddSol;
    @BindView(R.id.cont_fact_gen)
    LinearLayout contFactGen;
    @BindView(R.id.progressbar)
    ProgressBar progressbar;
    @BindView(R.id.cont_load)
    LinearLayout contLoad;

    //Form elements
    @BindView(R.id.txtNombreCompania)
    TextView txtNombreCompania;
    @BindView(R.id.txtRucCompania)
    TextView txtRucCompania;
    @BindView(R.id.txtDirCompania)
    TextView txtDirCompania;
    @BindView(R.id.txtNombreTasOn)
    TextView txtNombreTasOn;
    @BindView(R.id.txtRucTasOn)
    TextView txtRucTasOn;
    @BindView(R.id.txtDirTasOn)
    TextView txtDirTasOn;
    @BindView(R.id.btnCancelFact)
    Button btnCancelFact;
    @BindView(R.id.btnGuardaFact)
    Button btnGuardaFact;

    //Factura detail
    @BindView(R.id.descripFact)
    TextView descripFact;
    @BindView(R.id.precioUniFact)
    TextView precioUniFact;
    @BindView(R.id.subto0)
    TextView subto0;
    @BindView(R.id.subto)
    TextView subto;
    @BindView(R.id.total)
    TextView total;

    //Factura detail
    @BindView(R.id.descripFactRap)
    TextView descripFactRap;
    @BindView(R.id.precioUniFactRap)
    TextView precioUniFactRap;
    @BindView(R.id.preFactInfo)
    TextView preFactInfo;

    private double COMISION;
    double totalFact;
    private String codeFact;
    private String typeFact;

    public GeneraFacturaFlete() {
        mValues = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gernera_factura, container, false);
        // Inflate the layout for this fragment
        ButterKnife.bind(this, view);
        //View loader
        progress();
        return view;
    }

    private void getComision() {
        if (typeFact != null && typeFact.equals(RowGeneraFacturaFlete.FACT_RAPIDA)) {
            preFactInfo.setText("Facturación pago inmediato tiene un descuento.");

        } else {
            preFactInfo.setText("Seleccione los fletes a facturar.");
            precioUniFactRap.setText("  -  ");
        }
        /*final Observable<CatalogoItemDTO> tipoDocumento = publicService.getCatalogoItemById(Constants.ID_FACTURA_INMEDIATA);
         tipoDocumento
         .subscribeOn(Schedulers.io())
         .observeOn(AndroidSchedulers.mainThread())
         .subscribeWith(new DisposableObserver<CatalogoItemDTO>() {
        @Override protected void onStart() {
        super.onStart();
        progress();
        }

        @Override public void onNext(CatalogoItemDTO catalogoItemDTOS) {
        COMISION = Float.parseFloat(catalogoItemDTOS.getCatalogoItemValor());
        done();
        }

        @Override public void onError(Throwable e) {
        Log.w("Error", e);
        done();
        }

        @Override public void onComplete() {
        if (typeFact != null && typeFact.equals(RowGeneraFacturaFlete.FACT_RAPIDA)) {
        preFactInfo.setText("Facturación pago inmediato tiene un descuento del #COMISION%, en la generación de la prefactura.".replace("#COMISION", String.valueOf(COMISION)));

        } else {
        preFactInfo.setText("Seleccione los fletes a facturar.");
        precioUniFactRap.setText("  -  ");
        }
        }
        });*/
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Services instances
        myOfertListService = new MyOfertListService();
        empresaService = new EmpresaService();
        facturaService = new FacturaService();
        publicService = new PublicService();

        //Events
        btnAddSol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                progress();
                Observable<List<FleteShort>> observable = myOfertListService.getList(MyOfertListService.MY_OFERTS_GENERATE_INVOICE);
                observable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<List<FleteShort>>() {
                            @Override
                            protected void onStart() {
                                progress();
                                super.onStart();
                            }

                            @Override
                            public void onNext(List<FleteShort> fleteList) {
                                try {
                                    getGenButton(fleteList);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                done();
                            }

                            @Override
                            public void onError(Throwable e) {
                                done();
                                if (e.toString().toLowerCase().contains("timeout"))
                                    Toast.makeText(view.getContext(), "Lo sentimos ocurrió un error cargando la información," +
                                            " por favor intente nuevamente...", Toast.LENGTH_LONG).show();
                                else
                                    e.printStackTrace();
                            }

                            @Override
                            public void onComplete() {
                                done();
                            }
                        });
            }
        });

        getComision();

        Log.d("onViewCreated ... ","codeFact "+codeFact);
        if (codeFact != null) {
            progress();
            btnGuardaFact.setVisibility(View.GONE);
            btnCancelFact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().onBackPressed();
                }
            });
            enableForm();
            loadDefaultData();
        } else {
            formLogic();
            done();
        }
    }

    private void addSelectedValues(String idOffer, List<FleteShort> fleteShorts) {
        for (FleteShort aShort : fleteShorts) {
            if (String.valueOf(aShort.getIdSolicitud()).equals(idOffer))
                mValues.add(aShort);
        }
    }

    private void removeSelectedValues(String idOffer) {
        for (Iterator<FleteShort> iter = mValues.listIterator(); iter.hasNext(); ) {
            FleteShort aShort = iter.next();
            if (String.valueOf(aShort.getId()).equals(idOffer)) {
                iter.remove();
            }
        }
    }

    private String getValueSplit(String data) {
        return getValueSplit(data, 0);
    }

    private String getValueSplit(String data, int idxVal) {
        String value = "";
        if (data != null) {
            value = data.split("\\|")[idxVal].trim();
        }
        return value;
    }

    private void getGenButton(final List<FleteShort> fleteShorts) throws Exception {
        mValues.clear();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        //set the title for alert dialog
        builder.setTitle("Seleccione solicitudes: ");
        final ArrayList<String> data = new ArrayList<>();

        for (FleteShort aShort : fleteShorts) {
            data.add(aShort.getIdSolicitud() + " |\nOrigen: " + aShort.getOriginCity() + " | Destino: " + aShort.getDestinationCity() + " | Valor: " + aShort.getAmount()+"\n");
        }

        final boolean[] checkedItems = new boolean[data.size()];
        final ArrayList<Integer> mUserItems = new ArrayList<>();

        //set items to alert dialog. i.e. our array , which will be shown as list view in alert dialog
        builder.setMultiChoiceItems(data.toArray(new String[data.size()]), checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                if (isChecked) {
                    mUserItems.add(position);
                    addSelectedValues(getValueSplit(data.get(position)), fleteShorts);
                } else {
                    mUserItems.remove((Integer.valueOf(position)));
                    removeSelectedValues(getValueSplit(data.get(position)));
                }
            }
        });

        builder.setCancelable(false);

        //builder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                showElements();
            }
        });

        //builder.setNegativeButton(R.string.dismiss_label, new DialogInterface.OnClickListener() {
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                showElements();
            }
        });

        //builder.setNeutralButton(R.string.clear_all_label, new DialogInterface.OnClickListener() {
        builder.setNeutralButton("Limpiar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                for (int i = 0; i < checkedItems.length; i++) {
                    checkedItems[i] = false;
                    mUserItems.clear();
                    mValues.clear();
                }
            }
        });

        AlertDialog mDialog = builder.create();
        mDialog.show();
    }

    private void showElements() {
        if (mValues.size() > 0) {
            enableForm();
            loadData();
        } else {
            disableForm();
        }
    }

    private void loadData() {
        Log.d("CARGAR DATO  "," TIPO  "+typeFact );
        progress();
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
                    public void onNext(EmpresaDTO clienteDTO) {
                        //mProgressBar.setVisibility(View.GONE);
                        setDatosCompania(clienteDTO);
                        done();
                    }

                    @Override
                    public void onError(Throwable e) {
                        done();
                        if (e.toString().toLowerCase().contains("timeout"))
                            Toast.makeText(getContext(), "Lo sentimos ocurrió un error cargando la información," +
                                    " por favor intente nuevamente...", Toast.LENGTH_LONG).show();
                        else
                            e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        done();
                    }
                });

        observable = empresaService.readEmpresa(Constants.RUC_TASON);
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
                    public void onNext(EmpresaDTO clienteDTO) {
                        //mProgressBar.setVisibility(View.GONE);
                        setDatosTasOn(clienteDTO);
                        done();
                    }

                    @Override
                    public void onError(Throwable e) {
                        done();
                        if (e.toString().toLowerCase().contains("timeout"))
                            Toast.makeText(getContext(), "Lo sentimos ocurrió un error cargando la información," +
                                    " por favor intente nuevamente...", Toast.LENGTH_LONG).show();
                        else
                            e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        done();
                    }
                });

        Observable<CodigoFacturaDTO> codeFactura = facturaService.getCodeFactura();
        codeFactura
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<CodigoFacturaDTO>() {
                    @Override
                    protected void onStart() {
                        progress();
                        super.onStart();
                    }

                    @Override
                    public void onNext(CodigoFacturaDTO code) {
                        codeFact = code.getCode();
                        setGridValues(detalleFactura.replace("#numDoc", codeFact));
                        done();
                    }

                    @Override
                    public void onError(Throwable e) {
                        done();
                        if (e.toString().toLowerCase().contains("timeout"))
                            Toast.makeText(getContext(), "Lo sentimos ocurrió un error cargando la información," +
                                    " por favor intente nuevamente...", Toast.LENGTH_LONG).show();
                        else
                            e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        done();
                    }
                });
    }

    private void loadDefaultData() {
        Log.d("loadDefaultData  ",""+codeFact);
        Observable<InvoiceDetailDTO> detInvoice = facturaService.getInvoiceDetailByNumber(codeFact);
        detInvoice
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<InvoiceDetailDTO>() {
                    @Override
                    protected void onStart() {
                        progress();
                        super.onStart();
                    }

                    @Override
                    public void onNext(InvoiceDetailDTO detail) {
                        setDatosCompania(detail.getClient());
                        setDatosTasOn(detail.getTason());
                        float discIndiv = 0;
                        if (detail.getInvoiceDiscount() > 0)
                            discIndiv = ((Double) detail.getInvoiceDiscount()).floatValue() / detail.getOffers().size();
                        for (OffersDTO fleteInList : detail.getOffers()) {
                            FleteShort flete = FleteListMapper.responseToFleteList(fleteInList);
                            flete.setDescuento(discIndiv);
                            mValues.add(flete);
                        }

                        if (detail.getInvoiceTypePay() == Constants.ID_FACTURA_INMEDIATA)
                            typeFact = RowGeneraFacturaFlete.FACT_RAPIDA;

                        setGridValues(detalleFactura.replace("#numDoc", codeFact));
                        done();
                    }

                    @Override
                    public void onError(Throwable e) {
                        done();
                        if (e.toString().toLowerCase().contains("timeout"))
                            Toast.makeText(getContext(), "Lo sentimos ocurrió un error cargando la información," +
                                    " por favor intente nuevamente...", Toast.LENGTH_LONG).show();
                        else
                            e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        done();
                    }
                });

    }

    private void setDatosCompania(EmpresaDTO datosCompania) {
        txtNombreCompania.setText(datosCompania.getClienteRazonSocial());
        txtRucCompania.setText(datosCompania.getClienteRuc());
        txtDirCompania.setText(datosCompania.getClienteDireccion());
    }

    private void setDatosCompania(ClienteDTO datosCompania) {
        txtNombreCompania.setText(datosCompania.getRazonSocial());
        txtRucCompania.setText(datosCompania.getRuc());
        txtDirCompania.setText(datosCompania.getDireccion());
    }

    private void setDatosTasOn(EmpresaDTO datosCompania) {
        txtNombreTasOn.setText(datosCompania.getClienteRazonSocial());
        txtRucTasOn.setText(datosCompania.getClienteRuc());
        txtDirTasOn.setText(datosCompania.getClienteDireccion());
    }

    private void setDatosTasOn(ClienteDTO datosCompania) {
        txtNombreTasOn.setText(datosCompania.getRazonSocial());
        txtRucTasOn.setText(datosCompania.getRuc());
        txtDirTasOn.setText(datosCompania.getDireccion());
    }

    private void setGridValues(String detalle) {

        Log.d("setGrid_Values",""+detalle);
        totalFact = 0.0;
        COMISION = 0.0;
        for (FleteShort aShort : mValues) {
            totalFact = totalFact + aShort.getAmount();
            COMISION = COMISION + aShort.getDescuento();
        }

        Log.d("SELECCIONADO ",""+mValues.toString());

        String val = String.format(new Locale("es", "EC"), "%.2f", totalFact);
        descripFact.setText(detalle);
        precioUniFact.setText(val);
        subto0.setText(val);
        if (typeFact != null && typeFact.equals(RowGeneraFacturaFlete.FACT_RAPIDA)) {
            descripFactRap.setText("DESCUENTO");
            String valComi = String.format(new Locale("es", "EC"), "%.2f", COMISION);
            precioUniFactRap.setText(valComi);
            val = String.format(new Locale("es", "EC"), "%.2f", totalFact - COMISION);
        } else descripFactRap.setText("DESCUENTO");
        subto.setText(val);
        total.setText(val);
    }

    private void formLogic() {

        Log.d("form_Logic ",codeFact+"  ENTRO form_Logic "+totalFact );
        btnCancelFact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableForm();
            }
        });

        btnGuardaFact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress();

                InvoiceDTO invoice = new InvoiceDTO();
                invoice.setNumberInvoice(codeFact);
                invoice.setRucSupplier(Constants.RUC_TASON);
                invoice.setAmount(totalFact);

                if (typeFact != null && typeFact.equals(RowGeneraFacturaFlete.FACT_RAPIDA)) {
                    invoice.setInvoiceTypePay(Constants.ID_FACTURA_INMEDIATA);
                    //invoice.setInvoiceDiscount(totalFact * (COMISION / 100));
                } else {
                    invoice.setInvoiceTypePay(Constants.ID_FACTURA_NORMAL);
                    //invoice.setInvoiceDiscount(0);
                }
Log.d("ONCLICK  ",""+invoice);
                List<Integer> offersId = new ArrayList<>();
                for (FleteShort aShort : mValues)
                    offersId.add(aShort.getIdOferta());
                invoice.setOffersId(offersId);


                Gson gson = new Gson();
                String invoiceJson = gson.toJson(invoice);

                Log.d("OBJEOTOO",""+invoiceJson);

                Observable<String> observable = facturaService.createInvoice(invoice);
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
                            public void onNext(String response) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setTitle("Registro exitoso");
                                builder.setMessage(getString(R.string.facturacion_corta_msg));
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                                AlertDialog alertDialog = builder.create();
                                alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        done();
                                        disableForm();
                                    }
                                });
                                alertDialog.show();
                            }

                            @Override
                            public void onError(Throwable e) {
                                done();
                                if (e.toString().toLowerCase().contains("timeout"))
                                    Toast.makeText(getContext(), "Lo sentimos ocurrió un error cargando la información," +
                                            " por favor intente nuevamente...", Toast.LENGTH_LONG).show();
                                else
                                    e.printStackTrace();
                            }

                            @Override
                            public void onComplete() {
                                done();
                            }
                        });
            }
        });
    }

    private void enableForm() {
        btnAddSol.setVisibility(View.GONE);
        preFactInfo.setVisibility(View.GONE);
        contFactGen.setVisibility(View.VISIBLE);
    }

    private void disableForm() {
        btnAddSol.setVisibility(View.VISIBLE);
        preFactInfo.setVisibility(View.VISIBLE);
        contFactGen.setVisibility(View.GONE);
    }

    private void progress() {
        progressbar.setVisibility(View.VISIBLE);
        contLoad.setVisibility(View.GONE);
    }

    private void done() {
        progressbar.setVisibility(View.GONE);
        contLoad.setVisibility(View.VISIBLE);
    }

    public void setCodeFact(String codeFact) {
        this.codeFact = codeFact;
    }

    public void setTypeFact(String typeFact) {
        this.typeFact = typeFact;
    }
}

