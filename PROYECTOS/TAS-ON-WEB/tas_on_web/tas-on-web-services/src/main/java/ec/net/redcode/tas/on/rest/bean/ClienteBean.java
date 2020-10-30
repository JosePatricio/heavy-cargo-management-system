package ec.net.redcode.tas.on.rest.bean;

import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.common.dto.Company;
import ec.net.redcode.tas.on.common.dto.InfoBancariaDTO;
import ec.net.redcode.tas.on.common.exception.TasOnException;
import ec.net.redcode.tas.on.persistence.entities.*;
import ec.net.redcode.tas.on.persistence.service.*;
import lombok.Setter;
import org.apache.cxf.jaxrs.utils.ExceptionUtils;

import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ClienteBean extends CommonBean {

    @Setter private SecuenciaService secuenciaService;

    protected void createCliente(Cliente cliente){
        clienteService.createCliente(cliente);
        if (cliente.getClienteTipoCliente() == 60) {
            Secuencia secuencia = new Secuencia();
            secuencia.setSecuenciaClienteId(cliente.getClienteRuc());
            String nemonico = cliente.getClienteRazonSocial().substring(0, 5).toUpperCase().trim();
            if(secuenciaService.getSecuenciaByNemonico(nemonico) == null){
                secuencia.setSecuenciaClienteNemonico(nemonico);
            } else{
                secuencia.setSecuenciaClienteNemonico(generateRandomSecuencial(cliente.getClienteRazonSocial()));
            }
            secuencia.setSecuenciaIncremental(1);
            secuencia.setSecuenciaValorInicial(0);
            secuencia.setSecuenciaValorFinal(999999);
            secuenciaService.create(secuencia);
        }
    }

    protected void updateCliente(Cliente cliente){
        clienteService.updateCliente(cliente);
    }

    protected void deleteCliente(Cliente cliente) throws TasOnException{
        List<Usuario> usuarios = usuarioService.getUsuarioByRuc(cliente.getClienteRuc());
        if (usuarios.size() == 0){
            clienteService.deleteCliente(cliente);
        } else {
            throw new TasOnException(Response.Status.NOT_FOUND.getStatusCode(), "Error deleting", "Cliente no puede ser eliminado, contiene data");
        }
    }

    protected Cliente readClientByUsername(String username){
        Usuario usuario = usuarioService.getUsuariosByUserName(username);
        return usuario.getClienteByUsuarioRuc();
    }

    protected List<Company> getAllCliente(){
        List<Cliente> clientes = clienteService.getAllCiente();
        return clientes.stream().map(clientFunction).filter(Objects::nonNull).collect(Collectors.toList());
    }

    protected void actualizarInformacionBancaria (InfoBancariaDTO infoBancariaDTO, Usuario usuarioSolicita) throws TasOnException{
        Cliente cliente = usuarioSolicita.getClienteByUsuarioRuc();
        Timestamp solicitaActualizarInfor = cliente.getClienteBancoSolicActualizar();
        int intentoActualizar = cliente.getClienteBancoIntentoActualizar() == null ? 0 : cliente.getClienteBancoIntentoActualizar();
        if(solicitaActualizarInfor == null || usuarioSolicita.getUsuarioTipoUsuario() != Constant.USER_DRIVER_ADMIN){
            cliente.setClienteBancoIntentoActualizar(++intentoActualizar);
            clienteService.updateCliente(cliente);
            validarBloqueaUsuario(usuarioSolicita, intentoActualizar);
            throw new TasOnException(Response.Status.NOT_ACCEPTABLE.getStatusCode(), Constant.RESPONSE_ERROR, "No tiene aprobado actualizar la información. Intento "+intentoActualizar+" de 3");
        }
        LocalDateTime horaSolicitaActualizar = solicitaActualizarInfor.toLocalDateTime();
        if(!LocalDateTime.now().isBefore(horaSolicitaActualizar.plusMinutes(15))){
            cliente.setClienteBancoIntentoActualizar(++intentoActualizar);
            clienteService.updateCliente(cliente);
            validarBloqueaUsuario(usuarioSolicita, intentoActualizar);
            throw new TasOnException(Response.Status.NOT_ACCEPTABLE.getStatusCode(), Constant.RESPONSE_ERROR, "Han pasado 15 minutos desde su aprobación para actualizar. Intente nuevamente. Intento "+intentoActualizar+" de 3");
        }
        cliente.setClienteBanco(infoBancariaDTO.getBanco());
        cliente.setClienteTipoCuenta(infoBancariaDTO.getTipoCuenta());
        cliente.setClienteCuenta(infoBancariaDTO.getNumeroCuenta());
        cliente.setClienteBancoTipoIdentificacion(infoBancariaDTO.getTipoIdentificacion());
        cliente.setClienteBancoIdentificacion(infoBancariaDTO.getIdentificacion());
        cliente.setClienteBancoNombres(infoBancariaDTO.getNombres());
        cliente.setClienteBancoUltimaActualizacion(new Timestamp(new Date().getTime()));
        cliente.setClienteBancoIntentoActualizar(0);
        clienteService.updateCliente(cliente);
    }

    private void validarBloqueaUsuario(Usuario usuario, int intento){
        if(intento == 3){
            usuario.setUsuarioEstado(Constant.USER_BLOCKED);
            usuarioService.updateUsuario(usuario);
            throw ExceptionUtils.toForbiddenException(null, null);
        }
    }

    protected InfoBancariaDTO getInfoBancaria(Usuario usuario){
        InfoBancariaDTO infoBancariaDTO = new InfoBancariaDTO();
        if (usuario.getUsuarioTipoUsuario() == Constant.USER_DRIVER_ADMIN){
            Cliente cliente = usuario.getClienteByUsuarioRuc();
            if(cliente.getClienteBanco() != 0){
                infoBancariaDTO.setBanco(cliente.getClienteBanco());
                infoBancariaDTO.setBancoDesc(cliente.getCatalogoItemByClienteBanco().getCatalogoItemDescripcion());
            }
            if(cliente.getClienteTipoCuenta() != 0){
                infoBancariaDTO.setTipoCuenta(cliente.getClienteTipoCuenta());
                infoBancariaDTO.setTipoCuentaDesc(cliente.getCatalogoItemByClienteTipoCuenta().getCatalogoItemDescripcion());
                infoBancariaDTO.setNumeroCuenta(cliente.getClienteCuenta());
            }
            if(cliente.getClienteBancoTipoIdentificacion() != null ){
                infoBancariaDTO.setTipoIdentificacion(cliente.getClienteBancoTipoIdentificacion());
                infoBancariaDTO.setTipoIdentificacionDesc(cliente.getCatalogoItemByClienteBancoTipoIdentificacion().getCatalogoItemDescripcion());
                infoBancariaDTO.setIdentificacion(cliente.getClienteBancoIdentificacion());
            }
            infoBancariaDTO.setNombres(cliente.getClienteBancoNombres());
            infoBancariaDTO.setUltimaActualizacion(cliente.getClienteBancoUltimaActualizacion());
        }
        return infoBancariaDTO;
    }

}