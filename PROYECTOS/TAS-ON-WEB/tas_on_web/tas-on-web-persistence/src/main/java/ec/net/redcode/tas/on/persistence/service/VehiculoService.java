package ec.net.redcode.tas.on.persistence.service;

import ec.net.redcode.tas.on.persistence.entities.Vehiculo;

import java.util.List;

public interface VehiculoService {

    void create(Vehiculo vehiculo);
    Vehiculo read(int idVehiculo);
    void update(Vehiculo vehiculo);
    void delete(Vehiculo vehiculo);
    List<Vehiculo> getVehiculosByUser(String user);
    List<Vehiculo> getVehiculosByUserAndEstado(String user, int estado);
    Vehiculo getVehiculoyPlaca(String placa);
    List<Vehiculo> getVehiculosAcreditados(String rucClienteOferta, String rucClienteSolicitud);
    Vehiculo readVehiculoAcreditado(int idVehiculo, String rucClienteSolicitud);
}
