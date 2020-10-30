package ec.net.redcode.tas.on.persistence.service.impl;

import ec.net.redcode.tas.on.persistence.dao.VehiculoDAO;
import ec.net.redcode.tas.on.persistence.entities.Vehiculo;
import ec.net.redcode.tas.on.persistence.service.VehiculoService;
import lombok.Setter;

import java.util.List;

public class VehiculoServiceImpl implements VehiculoService {

    @Setter private VehiculoDAO vehiculoDAO;

    @Override
    public void create(Vehiculo vehiculo) {
        vehiculoDAO.create(vehiculo);
    }

    @Override
    public Vehiculo read(int idVehiculo) {
        return vehiculoDAO.read(idVehiculo);
    }

    @Override
    public void update(Vehiculo vehiculo) {
        vehiculoDAO.update(vehiculo);
    }

    @Override
    public void delete(Vehiculo vehiculo) {
        vehiculoDAO.delete(vehiculo);
    }

    @Override
    public List<Vehiculo> getVehiculosByUser(String user) {
        return vehiculoDAO.getVehiculoByUser(user);
    }

    @Override
    public List<Vehiculo> getVehiculosByUserAndEstado(String user, int estado) {
        return vehiculoDAO.getVehiculoByUserAndEstado(user, estado);
    }

    @Override
    public Vehiculo getVehiculoyPlaca(String placa) {
        return vehiculoDAO.getVehiculoByPlaca(placa);
    }

    @Override
    public List<Vehiculo> getVehiculosAcreditados(String rucClienteOferta, String rucClienteSolicitud){
        return vehiculoDAO.getVehiculosAcreditados(rucClienteOferta, rucClienteSolicitud);
    }

    @Override
    public Vehiculo readVehiculoAcreditado(int idVehiculo, String rucClienteSolicitud){
        return vehiculoDAO.readVehiculoAcreditado(idVehiculo, rucClienteSolicitud);
    }
}
