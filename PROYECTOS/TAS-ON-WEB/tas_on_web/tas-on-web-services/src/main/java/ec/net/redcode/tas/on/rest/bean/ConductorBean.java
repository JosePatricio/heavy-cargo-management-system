package ec.net.redcode.tas.on.rest.bean;

import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.persistence.entities.Cliente;
import ec.net.redcode.tas.on.persistence.entities.SolicitudEnvio;
import ec.net.redcode.tas.on.persistence.service.SolicitudEnvioService;
import ec.net.redcode.tas.on.rest.util.ServicesUtil;
import ec.net.redcode.tas.on.rest.util.TasOnResponse;
import ec.net.redcode.tas.on.persistence.entities.Conductor;
import ec.net.redcode.tas.on.persistence.entities.Usuario;
import ec.net.redcode.tas.on.persistence.service.ConductorService;
import ec.net.redcode.tas.on.persistence.service.UsuarioService;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

import java.util.ArrayList;
import java.util.List;

@Log4j
public class ConductorBean extends TasOnResponse{

    @Setter private ConductorService conductorService;
    @Setter protected UsuarioService usuarioService;
    @Setter private SolicitudEnvioService solicitudEnvioService;

    public void create(Usuario usuarioPeticion, Conductor conductor){
        if(usuarioPeticion == null) return;
        if(usuarioPeticion.getUsuarioTipoUsuario() == Constant.USER_ADMIN) {
            conductorService.create(conductor);
        }else{
            conductor.setConductorUsuario(usuarioPeticion.getUsuarioIdDocumento());
            conductor.setConductorEstado(1);
            conductorService.create(conductor);
        }
    }

    public Conductor read(int idVehiculo){
        return conductorService.read(idVehiculo);
    }

    public void update(Usuario usuarioPeticion, Conductor conductorNuevosDatos){
        if(usuarioPeticion == null) return;
        Conductor conductorActualizar = conductorService.read(conductorNuevosDatos.getConductorId());
        if(conductorActualizar == null) return;

        if(usuarioPeticion.getUsuarioTipoUsuario() == Constant.USER_ADMIN){
            conductorService.update(conductorNuevosDatos);
            return;
        }
        if((usuarioPeticion.getUsuarioTipoUsuario() == Constant.USER_DRIVER_ADMIN ||
                usuarioPeticion.getUsuarioTipoUsuario() == Constant.USER_ADMIN_ENVIOS)
           && conductorActualizar.getConductorByConductorUsuario().getClienteByUsuarioRuc().getClienteRuc().equals(
                   usuarioPeticion.getClienteByUsuarioRuc().getClienteRuc()
        )){
            if(conductorActualizar.getConductorCedula() == null || conductorActualizar.getConductorCedula().isEmpty())
                conductorActualizar.setConductorCedula(conductorNuevosDatos.getConductorCedula());
            conductorActualizar.setConductorTipoLicencia(conductorNuevosDatos.getConductorTipoLicencia());
            conductorActualizar.setConductorTelefono(conductorNuevosDatos.getConductorTelefono());
            conductorActualizar.setConductorEmail(conductorNuevosDatos.getConductorEmail());
            conductorService.update(conductorActualizar);
        }
    }

    public List<Conductor> getConductoresByUser(Usuario usuario){
        List<Conductor> conductors = new ArrayList<>();
        List<Usuario> usuarios = usuarioService.getUsuarioByRuc(usuario.getUsuarioRuc());
        for (Usuario u: usuarios){
            if(u.getUsuarioEstado() == Constant.USER_ACTIVE || u.getUsuarioEstado() == Constant.USER_CREATED){
                List<Conductor> conductorList = conductorService.getConductoresByUserAndEstado(u.getUsuarioIdDocumento(),Constant.USER_ACTIVE);
                if (conductorList.size() != 0)
                    conductors.addAll(conductorList);
            }
        }
        return conductors;
    }

    public List<Conductor> getConductoresByUsuarioYSolicitud(Usuario usuario, String solicitudId){
        SolicitudEnvio solicitudEnvio = solicitudEnvioService.read(solicitudId);
        Cliente clienteCreadorSolicitud = solicitudEnvio.getUsuarioBySolicitudEnvioUsuarioId().getClienteByUsuarioRuc();
        if(ServicesUtil.puedeSoloOfertarConductoresAcreditados(clienteCreadorSolicitud)) {
            if (!ServicesUtil.tieneUsuarioCondutoresAcreditados(usuario, clienteCreadorSolicitud, conductorService))
                return new ArrayList<>();
            return conductorService.getConductoresAcreditados(usuario.getUsuarioRuc(), solicitudEnvio.getUsuarioBySolicitudEnvioUsuarioId().getUsuarioRuc());
        }
        return getConductoresByUser(usuario);
    }

}
