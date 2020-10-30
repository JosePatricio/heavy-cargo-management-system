package ec.net.redcode.tas.on.rest.bean;

import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.persistence.entities.Cliente;
import ec.net.redcode.tas.on.persistence.entities.SolicitudEnvio;
import ec.net.redcode.tas.on.persistence.entities.Usuario;
import ec.net.redcode.tas.on.persistence.entities.Vehiculo;
import ec.net.redcode.tas.on.persistence.service.SolicitudEnvioService;
import ec.net.redcode.tas.on.persistence.service.UsuarioService;
import ec.net.redcode.tas.on.rest.util.ServicesUtil;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

import java.util.ArrayList;
import java.util.List;

@Log4j
public class VehiculoBean extends CommonBean {

    @Setter protected UsuarioService usuarioService;
    @Setter private SolicitudEnvioService solicitudEnvioService;

    public void create(Usuario usuarioPeticion, Vehiculo vehiculo){
        if(usuarioPeticion == null) return;
        if(usuarioPeticion.getUsuarioTipoUsuario() == Constant.USER_ADMIN) {
            vehiculoService.create(vehiculo);
        }else{
            vehiculo.setVehiculoUsuario(usuarioPeticion.getUsuarioIdDocumento());
            vehiculo.setVehiculoEstado(1);
            vehiculoService.create(vehiculo);
        }
    }

    public Vehiculo read(int idVehiculo){
        return vehiculoService.read(idVehiculo);
    }

    public void update(Usuario usuarioPeticion, Vehiculo vehiculoNuevosDatos){
        if(usuarioPeticion == null) return;
        Vehiculo vehiculoActualizar = vehiculoService.read(vehiculoNuevosDatos.getVehiculoId());
        if(vehiculoActualizar == null) return;

        if(usuarioPeticion.getUsuarioTipoUsuario() == Constant.USER_ADMIN){
            vehiculoService.update(vehiculoNuevosDatos);
            return;
        }

        if((usuarioPeticion.getUsuarioTipoUsuario() == Constant.USER_DRIVER_ADMIN ||
                usuarioPeticion.getUsuarioTipoUsuario() == Constant.USER_ADMIN_ENVIOS)
                && vehiculoActualizar.getUsuarioByVehiculoUsuario().getClienteByUsuarioRuc().getClienteRuc().equals(
                usuarioPeticion.getClienteByUsuarioRuc().getClienteRuc()
        )){
            vehiculoActualizar.setVehiculoModelo(vehiculoNuevosDatos.getVehiculoModelo());
            vehiculoActualizar.setVehiculoAnio(vehiculoNuevosDatos.getVehiculoAnio());
            vehiculoActualizar.setVehiculoTipoCarga(vehiculoNuevosDatos.getVehiculoTipoCarga());
            vehiculoActualizar.setVehiculoTipoCamion(vehiculoNuevosDatos.getVehiculoTipoCamion());
            vehiculoActualizar.setVehiculoTipoCapacidad(vehiculoNuevosDatos.getVehiculoTipoCapacidad());
            vehiculoActualizar.setVehiculoCapacidad(vehiculoNuevosDatos.getVehiculoCapacidad());
            vehiculoActualizar.setVehiculoArcsaAlimentos(vehiculoNuevosDatos.getVehiculoArcsaAlimentos());
            vehiculoActualizar.setVehiculoArcsaCosmeticos(vehiculoNuevosDatos.getVehiculoArcsaCosmeticos());
            vehiculoActualizar.setVehiculoArcsaMedicamentos(vehiculoNuevosDatos.getVehiculoArcsaMedicamentos());
            vehiculoActualizar.setVehiculoBasc(vehiculoNuevosDatos.getVehiculoBasc());
            vehiculoActualizar.setVehiculoPaseInternacional(vehiculoNuevosDatos.getVehiculoPaseInternacional());
            vehiculoService.update(vehiculoActualizar);
        }
    }

    protected List<Vehiculo> getVehiculosByUser(Usuario usuario){
        List<Vehiculo> vehiculos = new ArrayList<>();
        List<Usuario> usuarios = usuarioService.getUsuarioByRuc(usuario.getUsuarioRuc());
        for (Usuario u: usuarios){
            List<Vehiculo> vehiculoList = vehiculoService.getVehiculosByUserAndEstado(u.getUsuarioIdDocumento(), 1);
            if (vehiculoList.size() != 0)
                vehiculos.addAll(vehiculoList);
        }

        return vehiculos;
    }

    protected List<Vehiculo> getVehiculosByUsuarioYSolicitud(Usuario usuarioOferta, String solicitudId){
        SolicitudEnvio solicitudEnvio = solicitudEnvioService.read(solicitudId);
        Cliente clienteCreadorSolicitud = solicitudEnvio.getUsuarioBySolicitudEnvioUsuarioId().getClienteByUsuarioRuc();
        if(ServicesUtil.puedeSoloOfertarVehiculosAcreditados(clienteCreadorSolicitud)) {
            if (!ServicesUtil.tieneUsuarioVehiculosAcreditados(usuarioOferta, clienteCreadorSolicitud, vehiculoService))
                return new ArrayList<>();
            return vehiculoService.getVehiculosAcreditados(usuarioOferta.getUsuarioRuc(), solicitudEnvio.getUsuarioBySolicitudEnvioUsuarioId().getUsuarioRuc());
        }
        return getVehiculosByUser(usuarioOferta);
    }

}
