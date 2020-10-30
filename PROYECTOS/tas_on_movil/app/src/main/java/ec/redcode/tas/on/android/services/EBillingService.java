package ec.redcode.tas.on.android.services;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import ec.redcode.tas.on.android.Constants;
import ec.redcode.tas.on.android.dto.requests.Adquiriente;
import ec.redcode.tas.on.android.dto.requests.EBillingDTO;
import ec.redcode.tas.on.android.dto.requests.EBillingInfo;
import ec.redcode.tas.on.android.dto.requests.MyEBillingDTO;
import io.reactivex.Observable;
import io.reactivex.internal.operators.observable.ObservableBuffer;

public class EBillingService extends UtilService {

    public Observable<Map<String, String>> generateEBilling(final EBillingDTO eBillingDTO) {
        return Observable.fromCallable(new Callable<Map<String, String>>() {
            @Override
            public Map<String, String> call() throws Exception {
                String json = new Gson().toJson(eBillingDTO);
                String respuesta = post(Constants.urlGenerateEBilling, json); ;
                Map<String, String> responseMap =  new Gson().fromJson(respuesta, Map.class);
                if(responseMap == null){
                    responseMap = new HashMap<>();
                    responseMap.put("responseMessage", "Ha ocurrido un error");
                }
                return responseMap;
            }
        });
    }

    public Observable<Adquiriente> getAdquiriente(final String adquirienteId) {
        return ObservableBuffer.fromCallable(new Callable<Adquiriente>() {
            @Override
            public Adquiriente call() throws Exception {
                String respuesta = get(Constants.urlGetAdquiriente.replace("#adquirienteId", adquirienteId));
                return new Gson().fromJson(respuesta, Adquiriente.class);
            }
        });
    }

    public Observable<EBillingInfo> getEBillingInfo() {
        return ObservableBuffer.fromCallable(new Callable<EBillingInfo>() {
            @Override
            public EBillingInfo call() throws Exception {
                String respuesta = get(Constants.urlGetEBillingInfo);
                return new Gson().fromJson(respuesta, EBillingInfo.class);
            }
        });
    }

    public Observable<MyEBillingDTO[]> getMyEBillings() {
        return ObservableBuffer.fromCallable(new Callable<MyEBillingDTO[]>() {
            @Override
            public MyEBillingDTO[] call() throws Exception {
                String respuesta = get(Constants.urlGetMyEBillings);
                return new Gson().fromJson(respuesta, MyEBillingDTO[].class);
            }
        });
    }

    public Observable<Map<String, String>> sendMail(final String claveAcceso, final String correo) {
        return Observable.fromCallable(new Callable<Map<String, String>>() {
            @Override
            public Map<String, String> call() throws Exception {
                String respuesta = get(Constants.urlSendMailEbilling.replace("#claveAcceso", claveAcceso).replace("#correo", correo));
                return new Gson().fromJson(respuesta, Map.class);
            }
        });
    }

}
