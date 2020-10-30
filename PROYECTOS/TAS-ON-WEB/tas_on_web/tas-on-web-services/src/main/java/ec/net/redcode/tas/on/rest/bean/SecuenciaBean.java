package ec.net.redcode.tas.on.rest.bean;

import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.common.util.TasOnUtil;
import ec.net.redcode.tas.on.persistence.entities.Secuencia;
import ec.net.redcode.tas.on.persistence.service.SecuenciaService;
import ec.net.redcode.tas.on.rest.util.TasOnResponse;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class SecuenciaBean extends TasOnResponse {

    @Setter private SecuenciaService secuenciaService;

    public SecuenciaBean(SecuenciaService secuenciaService){
        this.secuenciaService = secuenciaService;
    }

    public String getSecuenciaByCliente(String idCliente){
        Secuencia secuencia = secuenciaService.getSecuenciaByCliente(idCliente);
        int nextValue = secuencia.getSecuencia() + secuencia.getSecuenciaIncremental();
        String sequence = secuencia.getSecuenciaClienteNemonico().concat(TasOnUtil.divideAndConquer(nextValue)).concat(String.valueOf(nextValue));
        secuencia.setSecuencia(nextValue);
        secuenciaService.update(secuencia);
        return sequence;
    }

    public String getSecuenciaTASON(){
        Secuencia secuencia = secuenciaService.getSecuenciaByNemonico(Constant.NEMONICO_TASON);
        int nextValue = secuencia.getSecuencia() + secuencia.getSecuenciaIncremental();
        String sequence = secuencia.getSecuenciaClienteNemonico().concat(TasOnUtil.divideAndConquer(nextValue)).concat(String.valueOf(nextValue));
        secuencia.setSecuencia(nextValue);
        secuenciaService.update(secuencia);
        return sequence;
    }

}
