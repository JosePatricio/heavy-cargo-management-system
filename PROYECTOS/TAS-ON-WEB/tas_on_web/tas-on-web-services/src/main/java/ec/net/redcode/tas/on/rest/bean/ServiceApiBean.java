package ec.net.redcode.tas.on.rest.bean;

import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.common.dto.ResponseAPI;
import ec.net.redcode.tas.on.common.exception.TasOnException;
import ec.net.redcode.tas.on.common.util.TasOnUtil;
import ec.net.redcode.tas.on.persistence.entities.*;
import ec.net.redcode.tas.on.persistence.service.*;
import ec.net.redcode.tas.on.rest.util.TasOnResponse;
import org.apache.cxf.rs.security.oauth2.provider.OAuthDataProvider;

import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.util.Date;

public class ServiceApiBean extends TasOnResponse {

    private OAuthDataProvider oAuthDataProvider;
    private SolicitudEnvioService solicitudEnvioService;
    private UsuarioService usuarioService;
    private SecuenciaService secuenciaService;
    private OfertaService ofertaService;
    private ConductorService conductorService;
    private VehiculoService vehiculoService;

    public String getToken(String username) throws TasOnException {
        Usuario usuario = usuarioService.getUsuariosByUserName(username);
        String token;
        if (usuario != null && usuario.getUsuarioTipoUsuario() == Constant.USER_CLIENT_ADMIN) {
            token = generateToken(usuario, Constant.SUBJECT_TOKEN, oAuthDataProvider);
        } else
            throw new TasOnException(Response.Status.FORBIDDEN.getStatusCode(), "Not Allowed", "Usuario no tiene permisos para generar token");
        return token;
    }

    public String createSolicitudEnvio(SolicitudEnvio solicitudEnvio, String username) throws TasOnException {
        try {
            Usuario usuario = usuarioService.getUsuariosByUserName(username);
            Secuencia secuencia = secuenciaService.getSecuenciaByCliente(usuario.getUsuarioRuc());
            int nextValue = secuencia.getSecuencia() + secuencia.getSecuenciaIncremental();
            String sequence = secuencia.getSecuenciaClienteNemonico().concat(TasOnUtil.divideAndConquer(nextValue)).concat(String.valueOf(nextValue));
            secuencia.setSecuencia(nextValue);
            secuenciaService.update(secuencia);
            solicitudEnvio.setSolicitudEnvioId(sequence);
            solicitudEnvio.setSolicitudEnvioUsuarioId(usuario.getUsuarioIdDocumento());
            solicitudEnvio.setSolicitudEnvioFechaCreacion(new Timestamp(new Date().getTime()));
            solicitudEnvioService.create(solicitudEnvio);
            return sequence;
        } catch (Exception e) {
            throw new TasOnException(Response.Status.BAD_REQUEST.getStatusCode(), "Error en creacion de solicitud", e.getMessage());
        }
    }

    public ResponseAPI getSolicitudEnvio(String idSolicitud) throws TasOnException {
        SolicitudEnvio solicitudEnvio = solicitudEnvioService.read(idSolicitud);
        ResponseAPI response = new ResponseAPI();
        if (solicitudEnvio != null){
            if (solicitudEnvio.getSolicitudEnvioOfertaId() != null) {
                Oferta oferta = ofertaService.read(solicitudEnvio.getSolicitudEnvioOfertaId());
                response.setValor(oferta.getOfertaValor());
                Usuario usuario = usuarioService.readUsuario(oferta.getOfertaIdUsuario());
                response.setRucProveedor(usuario.getUsuarioRuc());
                Vehiculo vehiculo = vehiculoService.read(oferta.getOfertaIdVehiculo());
                response.setPlaca(vehiculo.getVehiculoPlaca());
                Conductor conductor = conductorService.read(oferta.getOfertaIdConductor());
                response.setCedulaConductor(conductor.getConductorUsuario());
            } else {
                throw new TasOnException(Response.Status.NOT_FOUND.getStatusCode(), Response.Status.NOT_FOUND.getReasonPhrase(), "La Solicitud " + idSolicitud + " no tiene asignado ninguna oferta");
            }
        } else {
            throw new TasOnException(Response.Status.NOT_FOUND.getStatusCode(), Response.Status.NOT_FOUND.getReasonPhrase(), "Solicitud " + idSolicitud + " no existe en TAS-ON");
        }
        return response;
    }

    public void setoAuthDataProvider(OAuthDataProvider oAuthDataProvider) {
        this.oAuthDataProvider = oAuthDataProvider;
    }

    public void setSolicitudEnvioService(SolicitudEnvioService solicitudEnvioService) {
        this.solicitudEnvioService = solicitudEnvioService;
    }

    public void setUsuarioService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public SecuenciaService getSecuenciaService() {
        return secuenciaService;
    }

    public void setSecuenciaService(SecuenciaService secuenciaService) {
        this.secuenciaService = secuenciaService;
    }

    public void setOfertaService(OfertaService ofertaService) {
        this.ofertaService = ofertaService;
    }

    public void setConductorService(ConductorService conductorService) {
        this.conductorService = conductorService;
    }

    public void setVehiculoService(VehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

}
