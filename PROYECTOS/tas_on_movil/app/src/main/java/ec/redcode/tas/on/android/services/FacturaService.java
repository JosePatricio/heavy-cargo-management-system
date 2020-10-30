package ec.redcode.tas.on.android.services;

import android.util.Log;

import ec.redcode.tas.on.android.Constants;
import ec.redcode.tas.on.android.dto.CodigoFacturaDTO;
import ec.redcode.tas.on.android.dto.InvoiceDTO;
import ec.redcode.tas.on.android.dto.InvoiceDetailDTO;
import ec.redcode.tas.on.android.dto.InvoicesDTO;
import ec.redcode.tas.on.android.dto.responses.OfertResponseDTO;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.internal.operators.observable.ObservableBuffer;

public class FacturaService extends UtilService {

    public Observable<CodigoFacturaDTO> getCodeFactura() {
        return ObservableBuffer.fromCallable(new Callable<CodigoFacturaDTO>() {
            @Override
            public CodigoFacturaDTO call() throws Exception {
                String respuesta = get(Constants.urlGetCodeFactura);
                Gson gson = new Gson();
                Log.d("respuesta", respuesta);

                CodigoFacturaDTO response = gson.fromJson(respuesta, CodigoFacturaDTO.class);

                return response;
            }
        });
    }

    public Observable<String> createInvoice(final InvoiceDTO invoiceDTO) {
        return ObservableBuffer.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {

                Gson gson = new Gson();
                String invoiceJson = gson.toJson(invoiceDTO);

                String respuesta = post(Constants.urlCreateInvoice, invoiceJson);

                Log.d("respuesta", respuesta);

                OfertResponseDTO response = gson.fromJson(respuesta, OfertResponseDTO.class);

                if (response.getStatus() != null && !response.getStatus().isEmpty()) {
                    //error en la respuesta
                    return response.getMessage();
                } else {
                    //error en la correcta
                    return response.getResponseMessage();
                }
            }
        });
    }

    public Observable<List<InvoicesDTO>> getInvoiceListByState(final String estado) {
        return ObservableBuffer.fromCallable(new Callable<List<InvoicesDTO>>() {
            @Override
            public List<InvoicesDTO> call() throws Exception {
                String respuesta = get(Constants.urlInvoiceListByState.replace("#estado", estado));
                Gson gson = new Gson();
                Log.d("respuesta", respuesta);

                InvoicesDTO[] response = gson.fromJson(respuesta, InvoicesDTO[].class);

                return Arrays.asList(response);
            }
        });
    }

    public Observable<InvoiceDetailDTO> getInvoiceDetailByNumber(final String numbPreInv) {
        return ObservableBuffer.fromCallable(new Callable<InvoiceDetailDTO>() {
            @Override
            public InvoiceDetailDTO call() throws Exception {
                String respuesta = get(Constants.urlInvoiceDetailByNumber.replace("#numbPreInv", numbPreInv));
                Gson gson = new Gson();
                Log.d("respuesta", respuesta);

                InvoiceDetailDTO response = gson.fromJson(respuesta, InvoiceDetailDTO.class);

                return response;
            }
        });
    }
}
