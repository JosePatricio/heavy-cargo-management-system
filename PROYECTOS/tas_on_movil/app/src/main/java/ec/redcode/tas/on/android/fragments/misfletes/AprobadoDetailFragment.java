package ec.redcode.tas.on.android.fragments.misfletes;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ec.redcode.tas.on.android.R;
import ec.redcode.tas.on.android.Utils;
import ec.redcode.tas.on.android.dto.DriverDTO;
import ec.redcode.tas.on.android.dto.FleteDTO;
import ec.redcode.tas.on.android.dto.VehicleDTO;
import ec.redcode.tas.on.android.dto.requests.OfertUpdateRequestDTO;
import ec.redcode.tas.on.android.dto.responses.OfertResponseDTO;
import ec.redcode.tas.on.android.fragments.AceptedFleteDetailFragment;
import ec.redcode.tas.on.android.services.DriverVehicleService;
import ec.redcode.tas.on.android.services.MyOfertListService;
import ec.redcode.tas.on.android.services.OffertsService;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public class AprobadoDetailFragment extends AceptedFleteDetailFragment {

    @BindView(R.id.spinner_chofer)
    Spinner mChoferSpinner;
    @BindView(R.id.spinner_vehiculo)
    Spinner mVehiculoSpinner;
    @BindView(R.id.origin_city)
    TextView mOriginCity;
    @BindView(R.id.destination_city)
    TextView mDestinationCity;
    @BindView(R.id.destination_address)
    TextView mDestinationAddress;
    @BindView(R.id.sender_name)
    TextView mSenderName;
    @BindView(R.id.sender_contact_phone)
    TextView mSenderContactPhone;
    @BindView(R.id.sender_contact_email)
    TextView mSenderContactEmail;
    @BindView(R.id.product_type)
    TextView mProductType;
    @BindView(R.id.pieces_number)
    TextView mPiecesNumber;
    @BindView(R.id.load_weight)
    TextView mLoadWeight;
    @BindView(R.id.volume)
    TextView mVolume;
    @BindView(R.id.receiver_name)
    TextView mReceiverName;
    @BindView(R.id.receiver_address)
    TextView mRecieverAddress;
    @BindView(R.id.reofert_button)
    Button mOfertButton;
    @BindView(R.id.volume_unidad)
    TextView mVolumenUnidad;
    @BindView(R.id.estiba_num)
    TextView mEstibadores;
    @BindView(R.id.fecha_envio)
    TextView mFechaEnvio;
    @BindView(R.id.fecha_entrega)
    TextView mFechaEntrega;
    @BindView(R.id.load_weight_type)
    TextView mTipoPeso;
    @BindView(R.id.progressbar)
    ProgressBar progressbar;
    @BindView(R.id.cont_main)
    LinearLayout contMain;
    @BindView(R.id.viewTerms)
    Button viewTerms;
    @BindView(R.id.termsCheck)
    CheckBox termsCheck;
    @BindView(R.id.observacion)
    TextView observacion;
    private List<DriverDTO> drivers;
    private List<VehicleDTO> vehicles;

    public AprobadoDetailFragment() {
    }

    public void setFlete(FleteDTO flete) {
        this.flete = flete;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_aprobado_detail, container, false);
        // Inflate the layout for this fragment
        ButterKnife.bind(this, view);
        //View loader
        progress();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fillData();

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

        mOfertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress(mOfertButton);
                if (mChoferSpinner.getSelectedItemPosition() == 0) {
                    Toast.makeText(getContext(), "Debe seleccionar un conductor", Toast.LENGTH_SHORT).show();
                    done(mOfertButton);
                    return;
                }

                if (mVehiculoSpinner.getSelectedItemPosition() == 0) {
                    Toast.makeText(getContext(), "Debe seleccionar un vehiculo", Toast.LENGTH_SHORT).show();
                    done(mOfertButton);
                    return;
                }
                if (!termsCheck.isChecked()) {
                    Toast.makeText(getContext(), "Debe aceptar los terminos y condiciones", Toast.LENGTH_SHORT).show();
                    done(mOfertButton);
                    return;
                }

                String driverId = "";
                for (DriverDTO driver : drivers) {
                    if (mChoferSpinner.getSelectedItem().toString().equals(driver.getConductorNombre())) {
                        driverId = driver.getConductorId();
                    }
                }

                String vehicleId = "";
                for (VehicleDTO vehicle : vehicles) {
                    if (mVehiculoSpinner.getSelectedItem().toString().equals(vehicle.getVehiculoPlaca())) {
                        vehicleId = vehicle.getVehiculoId();
                    }
                }

                OfertUpdateRequestDTO request = new OfertUpdateRequestDTO();
                request.setIdConductor(driverId);
                request.setIdVehiculo(vehicleId);
                request.setIdOferta(flete.getOffer().getIdOferta());
                request.setState(MyOfertListService.MY_OFERTS_TO_RECIEVE);

                OffertsService service = new OffertsService();
                Observable<OfertResponseDTO> observable = service.updateMyOfert(request);

                observable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<OfertResponseDTO>() {
                            @Override
                            public void onNext(OfertResponseDTO response) {
                                String messaje = "";
                                if (response.getResponseMessage() != null && !response.getResponseMessage().isEmpty()) {
                                    messaje = response.getResponseMessage();
                                } else {
                                    messaje = response.getResponse();
                                }
                                Toast.makeText(getContext(), messaje, Toast.LENGTH_LONG).show();
                                done(mOfertButton);
                                finish();
                            }

                            @Override
                            public void onError(Throwable e) {
                                done(mOfertButton);
                            }

                            @Override
                            public void onComplete() {
                                done(mOfertButton);
                            }
                        });
            }
        });

        mSenderContactPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PHONE_NUMBER = (String) mSenderContactPhone.getText();
                String[] PERMISSIONS = {Manifest.permission.CALL_PHONE};
                if (!hasPermissions(getContext(), PERMISSIONS)) {
                    ActivityCompat.requestPermissions((Activity) getContext(), PERMISSIONS, CALL_PERMISSION_REQUEST);
                } else {
                    makePhoneCall(PHONE_NUMBER);
                }
            }
        });
    }


    private void progress(Button btnDisable) {
        btnDisable.setClickable(false);
        btnDisable.setEnabled(false);
        progress();
    }

    private void progress() {
        progressbar.setVisibility(View.VISIBLE);
        contMain.setVisibility(View.GONE);
    }

    private void done(Button btnEnable) {
        btnEnable.setClickable(true);
        btnEnable.setEnabled(true);
        done();
    }

    private void done() {
        progressbar.setVisibility(View.GONE);
        contMain.setVisibility(View.VISIBLE);

    }

    private void fillData() {

        mOriginCity.setText(flete.getOrigen() == null ? "" : flete.getOrigen());
        mDestinationCity.setText(flete.getDestino() == null ? "" : flete.getDestino());
        mDestinationAddress.setText(flete.getDireccion() == null ? "" : flete.getDireccion());
        mSenderName.setText(flete.getPersonaEntrega() == null ? "" : flete.getPersonaEntrega());
        mSenderContactEmail.setText(flete.getCorreoContacto() == null ? "" : flete.getCorreoContacto());
        mSenderContactPhone.setText(flete.getTelefonoContacto() == null ? "" : flete.getTelefonoContacto());
        mProductType.setText(flete.getTipo() == null ? "" : flete.getTipo());
        mPiecesNumber.setText(String.valueOf(flete.getNumeroPiezas()));
        mLoadWeight.setText(String.valueOf(flete.getPeso()));
        mVolume.setText(String.valueOf(flete.getVolumen()));
        mReceiverName.setText(flete.getPersonaRecibe() == null ? "" : flete.getPersonaRecibe());
        mRecieverAddress.setText(flete.getDireccionEntrega() == null ? "" : flete.getDireccionEntrega());
        mVolumenUnidad.setText(flete.getTipoVolumen() == null ? "" : flete.getTipoVolumen());
        mEstibadores.setText(flete.getNumeroEstibadores() == null ? "" : flete.getNumeroEstibadores());
        mTipoPeso.setText(flete.getTipoPeso() == null ? "" : flete.getTipoPeso());
        observacion.setText(flete.getObservaciones() == null ? "" : flete.getObservaciones());
        try {
            long fechaEnvio = Long.parseLong(flete.getFechaEnvio());
            mFechaEnvio.setText(Utils.addNewLineBetweenDateTime(Utils.convertDateTime(fechaEnvio)));
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error en Fecha de Envio", Toast.LENGTH_SHORT).show();
        }
        try {
            long fechaEntrega = Long.parseLong(flete.getFechaEntrega());
            mFechaEntrega.setText(Utils.addNewLineBetweenDateTime(Utils.convertDateTime(fechaEntrega)));
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error en Fecha de Entrega", Toast.LENGTH_SHORT).show();
        }
        progress();
        DriverVehicleService service = new DriverVehicleService();
        final Observable<List<DriverDTO>> drivers = service.getDrivers(flete.getIdSolicitud());
        drivers
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<DriverDTO>>() {
                    @Override
                    public void onNext(List<DriverDTO> driverDTOS) {
                        AprobadoDetailFragment.this.drivers = driverDTOS;
                        List nombres = new ArrayList<>();
                        nombres.add("Seleccione un Conductor");
                        for (DriverDTO driver : driverDTOS) {
                            nombres.add(driver.getConductorNombre());
                        }
                        mChoferSpinner.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, nombres));
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

        progress();
        Observable<List<VehicleDTO>> vehicles = service.getVehicles(flete.getIdSolicitud());
        vehicles
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<VehicleDTO>>() {
                    @Override
                    public void onNext(List<VehicleDTO> vehicleDTOS) {
                        AprobadoDetailFragment.this.vehicles = vehicleDTOS;
                        List nombres = new ArrayList<>();
                        nombres.add("Seleccione un Vehículo");
                        for (VehicleDTO driver : vehicleDTOS) {
                            nombres.add(driver.getVehiculoPlaca());
                        }
                        mVehiculoSpinner.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, nombres));
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
}
