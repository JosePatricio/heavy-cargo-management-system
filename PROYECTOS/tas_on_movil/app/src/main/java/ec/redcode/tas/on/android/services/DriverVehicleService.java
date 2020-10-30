package ec.redcode.tas.on.android.services;

import android.util.Log;

import ec.redcode.tas.on.android.Constants;
import ec.redcode.tas.on.android.dto.DriverDTO;
import ec.redcode.tas.on.android.dto.VehicleDTO;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.internal.operators.observable.ObservableBuffer;

/**
 * Created by hotak on 15/1/2018.
 */

public class DriverVehicleService extends UtilService {

    public Observable<List<DriverDTO>> getDrivers(final String idSolicitud) {
        return ObservableBuffer.fromCallable(new Callable<List<DriverDTO>>() {
            @Override
            public List<DriverDTO> call() throws Exception {
                String respuesta = get(Constants.urlMyDrivers.replace("#idSolicitud", idSolicitud));
                Gson gson = new Gson();
                Log.d("respuesta", respuesta);

                DriverDTO[] response = gson.fromJson(respuesta, DriverDTO[].class);

                List<DriverDTO> drivers = new ArrayList<>();

                for (DriverDTO driver : response) {
                    drivers.add(driver);
                }

                return drivers;
            }
        });
    }

    public Observable<List<VehicleDTO>> getVehicles(final String idSolicitud) {
        return ObservableBuffer.fromCallable(new Callable<List<VehicleDTO>>() {
            @Override
            public List<VehicleDTO> call() throws Exception {
                String respuesta = get(Constants.urlVehicle.replace("#idSolicitud", idSolicitud));
                Gson gson = new Gson();
                Log.d("respuesta", respuesta);

                VehicleDTO[] response = gson.fromJson(respuesta, VehicleDTO[].class);

                List<VehicleDTO> vehicleDTOS = new ArrayList<>();

                for (VehicleDTO vehicleDTO : response) {
                    vehicleDTOS.add(vehicleDTO);
                }

                return vehicleDTOS;
            }
        });
    }

}
