package ec.net.redcode.tas.on.persistence.dao;

import ec.net.redcode.tas.on.persistence.entities.Vehiculo;

import java.util.List;

public interface VehiculoDAO extends GenericDAO<Vehiculo, Integer> {

    List<Vehiculo> getVehiculoByUser(String user);
    List<Vehiculo> getVehiculoByUserAndEstado(String user, int estado);
    Vehiculo getVehiculoByPlaca(String placa);
    List<Vehiculo> getVehiculosAcreditados(String rucClienteOferta, String rucClienteSolicitud);
    Vehiculo readVehiculoAcreditado(int idVehiculo, String rucClienteSolicitud);

}
