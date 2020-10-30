package ec.net.redcode.tas.on.rest.util;

import ec.net.redcode.tas.on.persistence.entities.*;
import ec.net.redcode.tas.on.persistence.service.ConductorService;
import ec.net.redcode.tas.on.persistence.service.VehiculoService;

import java.util.List;

public class ServicesUtil {

    public static boolean puedeUsuarioOfertar(Usuario usuario, SolicitudEnvio solicitudEnvio, ConductorService conductorService, VehiculoService vehiculoService){
        Cliente clienteCreadorSolicitud = solicitudEnvio.getUsuarioBySolicitudEnvioUsuarioId().getClienteByUsuarioRuc();
        if(ServicesUtil.puedeSoloOfertarConductoresAcreditados(clienteCreadorSolicitud)){
            if(!ServicesUtil.tieneUsuarioCondutoresAcreditados(usuario, clienteCreadorSolicitud, conductorService)) return false;
        }
        if(ServicesUtil.puedeSoloOfertarVehiculosAcreditados(clienteCreadorSolicitud)){
            if(!ServicesUtil.tieneUsuarioVehiculosAcreditados(usuario, clienteCreadorSolicitud, vehiculoService)) return false;
        }
        return true;
    }

    public static boolean puedeSoloOfertarConductoresAcreditados(Cliente cliente){
        return cliente.getClienteAcreditarCondutor() != null && cliente.getClienteAcreditarCondutor() == 1;
    }

    public static boolean puedeSoloOfertarVehiculosAcreditados(Cliente cliente){
        return cliente.getClienteAcreditarVehiculo() != null && cliente.getClienteAcreditarVehiculo() == 1;
    }

    public static boolean tieneUsuarioCondutoresAcreditados(Usuario usuarioCreadorOferta, Cliente clienteCreadorSolicitud, ConductorService conductorService){
        List<Conductor> conductoresAcreditados = conductorService.getConductoresAcreditados(usuarioCreadorOferta.getUsuarioRuc(), clienteCreadorSolicitud.getClienteRuc());
        return conductoresAcreditados.size() > 0;
    }

    public static boolean tieneUsuarioVehiculosAcreditados(Usuario usuarioCreadorOferta, Cliente clienteCreadorSolicitud, VehiculoService vehiculoService){
        List<Vehiculo> vehiculosAcreditados = vehiculoService.getVehiculosAcreditados(usuarioCreadorOferta.getUsuarioRuc(), clienteCreadorSolicitud.getClienteRuc());
        return vehiculosAcreditados.size() > 0;
    }

}
