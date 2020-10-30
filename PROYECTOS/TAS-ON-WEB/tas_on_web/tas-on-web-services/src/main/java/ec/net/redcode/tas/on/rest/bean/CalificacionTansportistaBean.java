package ec.net.redcode.tas.on.rest.bean;

import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.common.dto.CalificacionTransportistaDTO;
import ec.net.redcode.tas.on.persistence.entities.CalificacionTransportista;
import ec.net.redcode.tas.on.persistence.entities.Catalogo;
import ec.net.redcode.tas.on.persistence.entities.Usuario;
import ec.net.redcode.tas.on.persistence.service.CalificacionTransportistaService;
import ec.net.redcode.tas.on.persistence.service.CatalogoService;
import ec.net.redcode.tas.on.persistence.service.UsuarioService;
import ec.net.redcode.tas.on.rest.util.TasOnResponse;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class CalificacionTansportistaBean extends TasOnResponse {

    @Setter private CalificacionTransportistaService calificacionTransportistaService;
    @Setter private UsuarioService usuarioService;

    public List<CalificacionTransportistaDTO> getCalificacionTransportistasByUser(String nombreUsuario){
        return calificacionTransportistaService.getCalificacionTransportistasByUser(nombreUsuario);
    }

    public boolean puedeCalificarTransportista(CalificacionTransportistaDTO calificacionTransportistaDTO, String nombreUsuario){
        Usuario usuario = usuarioService.getUsuariosByUserName(nombreUsuario);
        if(usuario == null || usuario.getUsuarioTipoUsuario() != Constant.USER_CLIENT_ADMIN) return  false;
        return calificacionTransportistaService.puedeCalificarTransportista(calificacionTransportistaDTO.getId(), nombreUsuario);
    }

    public void actualizarCalificacion(CalificacionTransportistaDTO calificacionTransportistaDTO, String nombreUsuario){
        CalificacionTransportista calificacionTransportista = calificacionTransportistaService.read(calificacionTransportistaDTO.getId());
        if(calificacionTransportista==null) return;
        calificacionTransportista.setCalificacionTransportistaValor(calificacionTransportistaDTO.getCalificacion());
        calificacionTransportista.setCalificacionTransportistaComentario(calificacionTransportistaDTO.getComentario());
        calificacionTransportista.setCalificacionTransportistaFechaCalificacion(new Timestamp(new Date().getTime()));
        calificacionTransportista.setCalificacionTransportistaUsuario(usuarioService.getUsuariosByUserName(nombreUsuario).getUsuarioIdDocumento());
    }

}
