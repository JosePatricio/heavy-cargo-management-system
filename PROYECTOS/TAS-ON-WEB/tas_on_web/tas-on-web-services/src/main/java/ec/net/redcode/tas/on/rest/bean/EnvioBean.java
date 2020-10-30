package ec.net.redcode.tas.on.rest.bean;

import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.common.dto.Documents;
import ec.net.redcode.tas.on.common.dto.EnvioDTO;
import ec.net.redcode.tas.on.common.dto.FotoEnvioDTO;
import ec.net.redcode.tas.on.common.dto.IdFotosDTO;
import ec.net.redcode.tas.on.common.exception.TasOnException;
import ec.net.redcode.tas.on.common.util.TasOnUtil;
import ec.net.redcode.tas.on.persistence.entities.*;
import ec.net.redcode.tas.on.persistence.service.*;
import ec.net.redcode.tas.on.rest.util.TasOnResponse;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.apache.poi.ss.usermodel.*;

import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Log4j
public class EnvioBean extends TasOnResponse {

    @Setter protected LocalidadService localidadService;
    @Setter protected CatalogoItemService catalogoItemService;
    @Setter protected ClienteService clienteService;
    @Setter protected UsuarioService usuarioService;
    @Setter protected VehiculoService vehiculoService;
    @Setter protected ConductorService conductorService;
    @Setter protected EnvioService envioService;
    @Setter private FileService fileService;
    @Setter private EnvioFileService envioFileService;

    private static final int ESTADO_FOTO_RECOLECCION = 1;
    private static final int ESTADO_FOTO_ENTREGA = 2;

    protected List<EnvioDTO> consultarEnvios(List<Integer> estados, String razonSocial, String nroDocumento, Date fechaRecoleccionDesde, Date fechaRecoleccionHasta, Integer conductorId, Usuario usuario){
        String ruc = null;
        if(usuario.getUsuarioTipoUsuario().equals(Constant.USER_DRIVER_ENVIOS)) {
            Conductor conductor = usuario.getConductorsByUsuarioIdDocumento().stream().filter(c -> c.getConductorEstado() == 1).findFirst().orElse(null);
            if(conductor == null || conductor.getConductorId() == 0) return Collections.emptyList();
            conductorId = conductor.getConductorId();
        }
        if(usuario.getUsuarioTipoUsuario().equals(Constant.USER_CLIENTE_ADMIN_ENVIOS)) {
            ruc = usuario.getClienteByUsuarioRuc().getClienteRuc();
        }
        List<Envio> envios = envioService.getBy(estados,razonSocial, ruc, nroDocumento, fechaRecoleccionDesde, fechaRecoleccionHasta, conductorId);
        return envios.stream().map(envioToDto).collect(Collectors.toList());
    }

    private Function<Envio, EnvioDTO> envioToDto = envio -> {
        EnvioDTO envioDTO = new EnvioDTO();
        envioDTO.setEnvioId(envio.getEnvioId());
        envioDTO.setEnvioNumeroDocumento(envio.getEnvioNumeroDocumento());
        envioDTO.setEnvioClienteRazonSocial(envio.getClienteByEnvio().getClienteRazonSocial());
        envioDTO.setEnvioTipo(envio.getEnvioTipo());
        envioDTO.setEnvioOrigen(envio.getLocalidadByEnvioOrigen().getLocalidadDescripcion());
        envioDTO.setEnvioDireccionOrigen(envio.getEnvioDireccionOrigen());
        envioDTO.setEnvioFechaRecoleccion(envio.getEnvioFechaRecoleccion());
        envioDTO.setEnvioDestino(envio.getLocalidadByEnvioDestino().getLocalidadDescripcion());
        envioDTO.setEnvioDireccionDestino(envio.getEnvioDireccionDestino());
        envioDTO.setEnvioFechaEntrega(envio.getEnvioFechaEntrega());
        envioDTO.setEnvioNumeroPiezas(envio.getEnvioNumeroPiezas());
        envioDTO.setEnvioNumeroEstibadores(envio.getEnvioNumeroEstibadores());
        Vehiculo vehiculo = envio.getVehiculoByEnvio();
        envioDTO.setEnvioVehiculo(vehiculo.getVehiculoPlaca()+" "+vehiculo.getVehiculoModelo()+" "+vehiculo.getVehiculoAnio()+"");
        Conductor conductor = envio.getConductorByEnvio();
        envioDTO.setEnvioConductor(conductor.getConductorNombre()+" "+conductor.getConductorApellido()+" "+conductor.getConductorCedula());
        envioDTO.setEnvioObservaciones(envio.getEnvioObservaciones());
        envioDTO.setEnvioEstado(envio.getEnvioEstado() == Constant.ESTADO_ENVIO_POR_RECIBIR ? "Por recibir" :
                envio.getEnvioEstado() == Constant.ESTADO_ENVIO_POR_ENTREGAR ? "Por entregar" :
                        envio.getEnvioEstado() == Constant.ESTADO_ENVIO_FINALIZADO ? "Finalizado" :
                                envio.getEnvioEstado() == Constant.ESTADO_ENVIO_CANCELADO ? "Cancelado" : "");
        envioDTO.setEnvioEstadoId(envio.getEnvioEstado());
        envioDTO.setEnvioPago(envio.getEnvioPago());
        return envioDTO;
    };

    protected void guardarEnvios(Usuario usuario, String archivoBase64){
        List<Envio> envios = getEnviosListFromFile(usuario, archivoBase64);
        envioService.create(envios);
    }

    protected List<Envio> getEnviosListFromFile(Usuario usuario, String archivoBase64){
        List<Envio> envios = new ArrayList<>();
        if(TasOnUtil.isStringNullOrEmpty(archivoBase64)) return  envios;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(archivoBase64);
            Workbook wb = WorkbookFactory.create(new ByteArrayInputStream(decodedBytes));
            Sheet sheet = wb.getSheetAt(0);
            Row row;
            int rows = sheet.getPhysicalNumberOfRows();
            int cols = 0;
            int tmp;
            // This trick ensures that we get the data properly even if it doesn't start from first few rows
            for(int i = 0; i < 10 || i < rows; i++) {
                row = sheet.getRow(i);
                if(row != null) {
                    tmp = sheet.getRow(i).getPhysicalNumberOfCells();
                    if(tmp > cols) cols = tmp;
                }
            }
            for(int r = 1; r < rows; r++) {
                row = sheet.getRow(r);
                if(row != null) {
                    Envio envio = new Envio();
                    int i=0;
                    envio.setEnvioTipo(row.getCell(i++).getStringCellValue().trim().toUpperCase());
                    if(envio.getEnvioTipo().length() != 1) continue;
                    envio.setEnvioOrigen(((Double)row.getCell(i++).getNumericCellValue()).intValue());
                    envio.setLocalidadByEnvioOrigen(localidadService.readLocalidad(envio.getEnvioOrigen()));
                    envio.setEnvioDireccionOrigen(row.getCell(i++).getStringCellValue());
                    String fechaRecoleccion = row.getCell(i++).getStringCellValue();
                    envio.setEnvioFechaRecoleccion(Timestamp.valueOf(LocalDateTime.parse(fechaRecoleccion, formatter)));
                    envio.setEnvioDestino(((Double)row.getCell(i++).getNumericCellValue()).intValue());
                    envio.setLocalidadByEnvioDestino(localidadService.readLocalidad(envio.getEnvioDestino()));
                    envio.setEnvioDireccionDestino(row.getCell(i++).getStringCellValue());
                    String fechaEntrega = row.getCell(i++).getStringCellValue();
                    envio.setEnvioFechaEntrega(Timestamp.valueOf(LocalDateTime.parse(fechaEntrega, formatter)));
                    envio.setEnvioNumeroDocumento(row.getCell(i) != null ? row.getCell(i++).getStringCellValue() : null);
                    envio.setEnvioNumeroPiezas(((Double)row.getCell(i++).getNumericCellValue()).intValue());
                    envio.setEnvioNumeroEstibadores(((Double)row.getCell(i++).getNumericCellValue()).intValue());
                    envio.setEnvioConductor(((Double)row.getCell(i++).getNumericCellValue()).intValue());
                    envio.setConductorByEnvio(conductorService.read(envio.getEnvioConductor()));
                    envio.setEnvioVehiculo(((Double)row.getCell(i++).getNumericCellValue()).intValue());
                    envio.setVehiculoByEnvio(vehiculoService.read(envio.getEnvioVehiculo()));
                    row.getCell(i).setCellType(CellType.STRING);
                    envio.setEnvioCliente(row.getCell(i++).getStringCellValue());
                    envio.setClienteByEnvio(clienteService.readCliente(envio.getEnvioCliente()));
                    envio.setEnvioObservaciones(row.getCell(i) != null ? row.getCell(i++).getStringCellValue() : null);
                    envio.setEnvioEstado(Constant.ESTADO_ENVIO_POR_RECIBIR);
                    envio.setEnvioUsuarioCrea(usuario.getUsuarioIdDocumento());
                    envio.setEnvioFechaCreacion(new Timestamp(new Date().getTime()));
                    if(envio.getLocalidadByEnvioOrigen() == null || envio.getLocalidadByEnvioDestino() == null ||
                            envio.getClienteByEnvio() == null || envio.getVehiculoByEnvio() == null ||
                            envio.getConductorByEnvio() == null) continue;
                    envios.add(envio);
                }
            }
        } catch(Exception e) {
            log.error("Error al leer archivo para cargar solicitudes");
            e.printStackTrace();
        }
        return envios;
    }

    protected void guardarFotos(FotoEnvioDTO fotoEnvioDTO, Usuario usuario) throws TasOnException{
        Envio envio = envioService.read(fotoEnvioDTO.getEnvioId());
        if(envio != null && (envio.getConductorByEnvio().getConductorByConductorUsuario().getUsuarioIdDocumento().equals(usuario.getUsuarioIdDocumento())
            || envio.getUsuarioByEnvioUsuarioCrea().getUsuarioIdDocumento().equals(usuario.getUsuarioIdDocumento()))){
            int estadoFoto = envio.getEnvioEstado() == Constant.ESTADO_ENVIO_POR_RECIBIR? ESTADO_FOTO_RECOLECCION : envio.getEnvioEstado() == Constant.ESTADO_ENVIO_POR_ENTREGAR? ESTADO_FOTO_ENTREGA : 0;
            if(estadoFoto == 0) throw new TasOnException(Response.Status.FORBIDDEN.getStatusCode(), Constant.RESPONSE_ERROR, "No se puede actualizar la oferta");
            for(Documents foto : fotoEnvioDTO.getFotos()){
                File fileTasOn = new File();
                fileTasOn.setFileContent(foto.getFile());
                fileTasOn.setFileName(foto.getFileName());
                fileTasOn.setFileSize(foto.getFileSize());
                fileTasOn.setFileType(foto.getFileType());
                fileTasOn.setFileUploadDate(new Timestamp(new Date().getTime()));
                fileService.create(fileTasOn);

                EnvioFile envioFile = new EnvioFile();
                envioFile.setEnvioFileEnvioId(envio.getEnvioId());
                envioFile.setEnvioFileFileId(fileTasOn.getFileId());
                envioFile.setEnvioFileTipo(estadoFoto);
                envioFileService.create(envioFile);
            }
        } else {
            throw new TasOnException(Response.Status.FORBIDDEN.getStatusCode(), Constant.RESPONSE_ERROR, "No se puede actualizar la oferta");
        }
    }

    protected void cambiarEstado(FotoEnvioDTO fotoEnvioDTO, Usuario usuario) throws TasOnException{
        Envio envio = envioService.read(fotoEnvioDTO.getEnvioId());
        if(envio != null && (envio.getConductorByEnvio().getConductorByConductorUsuario().getUsuarioIdDocumento().equals(usuario.getUsuarioIdDocumento())
                || envio.getUsuarioByEnvioUsuarioCrea().getUsuarioIdDocumento().equals(usuario.getUsuarioIdDocumento()))){
            if(envio.getEnvioEstado() == Constant.ESTADO_ENVIO_POR_RECIBIR && envio.getFotos().stream().anyMatch(f -> f.getEnvioFileTipo() == ESTADO_FOTO_RECOLECCION)){
                envio.setEnvioEstado(Constant.ESTADO_ENVIO_POR_ENTREGAR);
                envio.setEnvioFechaRecoleccionCompletada(new Timestamp(new Date().getTime()));
            }
            else if(envio.getEnvioEstado() == Constant.ESTADO_ENVIO_POR_ENTREGAR && envio.getFotos().stream().anyMatch(f -> f.getEnvioFileTipo() == ESTADO_FOTO_ENTREGA)){
                envio.setEnvioEstado(Constant.ESTADO_ENVIO_FINALIZADO);
                envio.setEnvioPago(TasOnUtil.roundDouble(fotoEnvioDTO.getPago(), 2));
                envio.setEnvioFechaEntregaCompletada(new Timestamp(new Date().getTime()));
            }
            else throw new TasOnException(Response.Status.FORBIDDEN.getStatusCode(), Constant.RESPONSE_ERROR, "No se puede actualizar la oferta");
            envioService.update(envio);
        } else {
            throw new TasOnException(Response.Status.FORBIDDEN.getStatusCode(), Constant.RESPONSE_ERROR, "No se puede actualizar la oferta");
        }
    }

    protected IdFotosDTO consultarFotosEnvio(int envioId, Usuario usuario){
        Envio envio = envioService.read(envioId);
        if(envio != null && (envio.getConductorByEnvio().getConductorByConductorUsuario().getUsuarioIdDocumento().equals(usuario.getUsuarioIdDocumento()) ||
                envio.getUsuarioByEnvioUsuarioCrea().getUsuarioIdDocumento().equals(usuario.getUsuarioIdDocumento()) ||
                envio.getClienteByEnvio().getClienteRuc().equals(usuario.getUsuarioRuc())
        )){
            IdFotosDTO idFotosDTO = new IdFotosDTO();
            idFotosDTO.setRecoleccion(envio.getFotos().stream().filter(f -> f.getEnvioFileTipo() == ESTADO_FOTO_RECOLECCION).map(EnvioFile::getEnvioFileFileId).collect(Collectors.toList()));
            idFotosDTO.setEntrega(envio.getFotos().stream().filter(f -> f.getEnvioFileTipo() == ESTADO_FOTO_ENTREGA).map(EnvioFile::getEnvioFileFileId).collect(Collectors.toList()));
            return idFotosDTO;
        }else{
            return new IdFotosDTO();
        }
    }

    protected File getFotoEnvio(int fotoId, Usuario usuario){
        File foto = fileService.read(fotoId);
        if(foto == null) return new File();
        EnvioFile envioFile = envioFileService.readByFileId(fotoId);
        if(envioFile == null) return new File();
        Envio envio = envioFile.getEnvioByFoto();
        if(envio != null && (envio.getConductorByEnvio().getConductorByConductorUsuario().getUsuarioIdDocumento().equals(usuario.getUsuarioIdDocumento()) ||
                envio.getUsuarioByEnvioUsuarioCrea().getUsuarioIdDocumento().equals(usuario.getUsuarioIdDocumento()) ||
                envio.getClienteByEnvio().getClienteRuc().equals(usuario.getUsuarioRuc())

        )){
            return foto;
        }
        return new File();
    }

}
