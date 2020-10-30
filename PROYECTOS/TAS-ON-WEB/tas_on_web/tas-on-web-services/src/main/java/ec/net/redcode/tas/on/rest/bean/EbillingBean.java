package ec.net.redcode.tas.on.rest.bean;

import ec.net.redcode.tas.on.common.dto.EBillingDTO;
import ec.net.redcode.tas.on.common.dto.UsuarioEbillingDTO;
import ec.net.redcode.tas.on.persistence.entities.Adquiriente;
import ec.net.redcode.tas.on.persistence.entities.Ebilling;
import ec.net.redcode.tas.on.persistence.entities.Usuario;
import ec.net.redcode.tas.on.persistence.entities.UsuarioEbilling;
import ec.net.redcode.tas.on.persistence.service.*;
import ec.net.redcode.tas.on.rest.util.TasOnResponse;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.*;

public class EbillingBean extends TasOnResponse {

    @Setter protected AdquirienteService adquirienteService;
    @Setter protected UsuarioEbillingService usuarioEbillingService;
    @Setter protected UsuarioService usuarioService;
    @Setter protected EbillingService ebillingService;
    @Setter protected ClienteService clienteService;

    public int createAdquiriente(Adquiriente adquiriente){
        Adquiriente adq = adquirienteService.read(adquiriente.getAdquirienteIdDocumento());
        if(adq == null) {
            adquirienteService.create(adquiriente);
            return 0;
        } else {
            adquirienteService.update(adquiriente);
            return 1;
        }
    }

    public Adquiriente readAdquiriente(String numDocumento){
        return adquirienteService.read(numDocumento);
    }

    public List<Adquiriente> getAllAdquiriente(){
        return adquirienteService.getAllAdquiriente();
    }

    public UsuarioEbilling readUsuario(String numDocumento)
    {
        return usuarioEbillingService.read(numDocumento);
    }

    public UsuarioEbilling readUsuarioByUserID(String numDocumento)
    {
        return usuarioEbillingService.readByUserID(numDocumento);
    }

    public void createUsuario(UsuarioEbillingDTO usuarioEbillingDTO){
        UsuarioEbilling usuarioEbilling = usuarioEbillingService.read(usuarioEbillingDTO.getUsuarioEbillingId());
        Usuario usuario =  usuarioService.readUsuario(usuarioEbillingDTO.getUsuarioEbillingIdUsuario());
        if(usuarioEbilling == null){ //crea un usuario
            usuarioEbilling = new UsuarioEbilling();
            usuarioEbilling.setUsuarioEbillingId(usuarioEbillingDTO.getUsuarioEbillingId());
            usuarioEbilling.setUsuarioEbillingIdUsuario(usuarioEbillingDTO.getUsuarioEbillingIdUsuario());
            usuarioEbilling.setUsuarioEbillingFirma(usuarioEbillingDTO.getUsuarioEbillingFirma().getFile());
            usuarioEbilling.setUsuarioEbillingFechaFirma(new Timestamp(new Date().getTime()));
            usuarioEbilling.setUsuarioEbillingLogo(usuarioEbillingDTO.getUsuarioEbillingLogo() == null ? null : usuarioEbillingDTO.getUsuarioEbillingLogo().getFile());
            usuarioEbilling.setUsuarioEbillingEstablecimiento("001");
            usuarioEbilling.setUsuarioEbillingSecuencia(1);
            usuarioEbilling.setUsuarioEbillingPuntoEmision(usuarioEbillingDTO.getUsuarioEbillingPuntoEmision());
            usuarioEbilling.setUsuarioEBillingActividadComercial(usuarioEbillingDTO.getUsuarioEBillingActividadComercial());
            usuarioEbilling.setUsuarioEbillingEstado(1);
            usuarioEbillingService.create(usuarioEbilling);
            usuario.setUsuarioDireccion(usuarioEbillingDTO.getUsuarioEbillingDireccion());
        }else{ //actualiza
            boolean actualizarUsuarioEbilling = false;
            if(usuario.getUsuarioDireccion() == null || !usuario.getUsuarioDireccion().equals(usuarioEbillingDTO.getUsuarioEbillingDireccion())){
                usuario.setUsuarioDireccion(usuarioEbillingDTO.getUsuarioEbillingDireccion());
                usuarioService.updateUsuario(usuario);
            }
            if(!usuarioEbilling.getUsuarioEBillingActividadComercial().equals(usuarioEbillingDTO.getUsuarioEBillingActividadComercial())) {
                usuarioEbilling.setUsuarioEBillingActividadComercial(usuarioEbillingDTO.getUsuarioEBillingActividadComercial());
                actualizarUsuarioEbilling = true;
            }
            String logo = usuarioEbillingDTO.getUsuarioEbillingLogo() == null ? null : usuarioEbillingDTO.getUsuarioEbillingLogo().getFile();
            if((usuarioEbilling.getUsuarioEbillingLogo() == null || !usuarioEbilling.getUsuarioEbillingLogo().isEmpty())
                    && logo != null && !logo.isEmpty() && !logo.equals(usuarioEbilling.getUsuarioEbillingLogo())) {
                usuarioEbilling.setUsuarioEbillingLogo(logo);
                actualizarUsuarioEbilling = true;
            }
            String firma = usuarioEbillingDTO.getUsuarioEbillingFirma().getFile();
            if(firma != null && !firma.isEmpty() && !firma.equals(usuarioEbilling.getUsuarioEbillingFirma())){
                usuarioEbilling.setUsuarioEbillingFirma(usuarioEbillingDTO.getUsuarioEbillingFirma().getFile());
                usuarioEbilling.setUsuarioEbillingFechaFirma(new Timestamp(new Date().getTime()));
                actualizarUsuarioEbilling = true;
            }
            if(actualizarUsuarioEbilling) usuarioEbillingService.update(usuarioEbilling);
        }
    }

    public List<EBillingDTO> getEbillings(String id, boolean all){
        List<Ebilling> ebillings;
        if(all) ebillings = ebillingService.readByUserEbillingId(id);
        else ebillings = ebillingService.readByUserId(id);
        List<EBillingDTO> eBillingDTOList = new ArrayList<>();
        Map<String, String> adquirientesMap = new HashMap<>();
        for(Ebilling ebilling : ebillings){
            EBillingDTO eBillingDTO = new EBillingDTO();
            eBillingDTO.setEbillingId(ebilling.getEbillingNumero());
            eBillingDTO.setEmisionDate(ebilling.getEbillingFechaEmision());
            eBillingDTO.setAuthorizationDate(ebilling.getEbillingFechaAutorizacion());
            eBillingDTO.setClaveAcceso(ebilling.getEbillingClaveAcceso());
            eBillingDTO.setState(ebilling.getEbillingEstado());
            eBillingDTO.setTotal(ebilling.getEbillingTotal());
            eBillingDTO.setAdquiriente(ebilling.getEbillingAdquiriente());
            eBillingDTO.setUsuarioCreador(getNombresUsuario(ebilling.getEbillingUsuarioId()));
            eBillingDTO.setRazonSocialAdquiriente(adquirientesMap.computeIfAbsent(ebilling.getEbillingAdquiriente(),
                    k -> adquirienteService.read(ebilling.getEbillingAdquiriente()).getAdquirienteRazonSocial()));
            eBillingDTOList.add(eBillingDTO);
        }
        return eBillingDTOList;
    }

    public String getNombresUsuario(String usuarioId){
        if(usuarioId == null || usuarioId.isEmpty()) return "";
        Usuario usuario = usuarioService.readUsuario(usuarioId);
        if(usuario==null) return "";
        return usuario.getUsuarioNombres() + " " + usuario.getUsuarioApellidos();
    }

    public String getNombresUsuario(Usuario usuario){
        if(usuario==null) return "";
        return usuario.getUsuarioNombres() + " " + usuario.getUsuarioApellidos();
    }

    public byte[] getRIDE(String claveAcceso){
        Ebilling ebilling = ebillingService.readByClaveAcceso(claveAcceso);
        if(ebilling == null) return null;
        if(ebilling.getEbillingRide()!=null && !ebilling.getEbillingRide().isEmpty()) return Base64.getDecoder().decode(ebilling.getEbillingRide());
        return null;
    }

    public byte[] getXML(String claveAcceso){
        Ebilling ebilling = ebillingService.readByClaveAcceso(claveAcceso);
        if(ebilling == null) return null;
        if(ebilling.getEbillingXml()!=null && !ebilling.getEbillingXml().isEmpty()) return ebilling.getEbillingXml().getBytes();
        return null;
    }



}
