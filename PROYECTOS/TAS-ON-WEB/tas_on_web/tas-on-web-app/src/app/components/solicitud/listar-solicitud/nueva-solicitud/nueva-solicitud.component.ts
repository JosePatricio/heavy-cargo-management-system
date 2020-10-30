import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {appConfig} from '../../../../app.config';
import {ActivatedRoute, Router} from '@angular/router';
import {SolicitudEnvioService} from '../../../../services/solicitud-envio.service';
import {UtilService} from '../../../../services/util.service';
import {UtilsCommon} from "../../../../utils/utils.common";
import {LocalidadService} from "../../../../services/localidad.service";
import {AuthService} from "../../../../services/auth.service";
import {SolicitudEnvio} from "../../../../interfaces/solicitudEnvio";
import Swal from "sweetalert2/dist/sweetalert2.all.min.js";

declare var $: any;

@Component({
  selector: 'app-nueva-solicitud',
  templateUrl: './nueva-solicitud.component.html'
})
export class NuevaSolicitudComponent implements OnInit {

  entregan: string[] = [];
  reciben: string[] = [];
  direccionesOrigen: string[] = [];
  direccionesDestino: string[] = [];

  horasCaducidad: number = this._authService.getHorasCaducidadSolicitud();

  appConfig = appConfig;
  fechRecol: any;
  fechEntreg: any;
  camposValidos: boolean = true;
  deshabilitarBtnGuardar: boolean = false;
  deshabilitarBtnEditar: boolean = false;
  deshabilitarBtnCancelar: boolean = false;
  tiposSubasta: any;

  public loading = false;
  tiposDocumento: any;
  tiposPeso: any;
  tiposVolumen: any;
  provincias: any;
  provinciaSeleccionada: string = '';
  provinciaDestino: string = '';
  provinciaOrigen: string = '';
  cantonesOrigen: any;
  cantonesDestino: any;
  solicitudEnvio: any;
  solicitudEnvioPeso: any;
  //Mensaje de Operacion exitosa o erronea
  operacionCorrecta: boolean = false;
  mensajeOperacion: string = '';
  emitirNotaCredito: boolean = this._authService.getClienteNotaCredito() == "true";

  @Input('idSolicitud') idSolicitud;
  @Input('obtieneSecuencia') obtieneSecuencia;
  @Input('edit') edit;
  @Input('cancelada') cancelada;
  @Output() complete: EventEmitter<boolean> = new EventEmitter<boolean>();

  constructor(private _router: Router, private _activatedRoute: ActivatedRoute, public _solicitudEnvioService: SolicitudEnvioService,
              private _utilService: UtilService, private _localidad: LocalidadService, private _authService: AuthService) {
  }

  ngOnInit() {
    this.cleanUpSolicitudEnvio();
    this.loadTiposSubasta();
  }

  loadTiposSubasta(){
    this.loading = true;
    this._utilService.obtenerCatalogoItem(appConfig.idCatalogoTiposSubasta).subscribe(
      data => {
        this.tiposSubasta = data;
        if (this.obtieneSecuencia == "1") {
          if(this.emitirNotaCredito){
            this.solicitudEnvio.tipoSubasta = data.filter((tipoSubasta => tipoSubasta.catalogoItemId == appConfig.idSubastaInversaValorObjetivo))[0];
          } else {
            this.solicitudEnvio.tipoSubasta = data.filter((tipoSubasta => tipoSubasta.catalogoItemId == appConfig.idSubastaInversaAbierta))[0];
          }
        }
        this.loading = false;
        this.loadCatalogoPesos();
      },
      error => {
        this.loading = false;
      }
    );
  }

  loadCatalogoPesos(){
    this.loading = true;
    this._utilService.obtenerCatalogoItem(appConfig.idCatalogoPesos).subscribe(
      data => {
        this.tiposPeso = data;
        this.loading = false;
        this.loadCatalogoVolumen();
      },
      error => {
        this.loading = false;
      }
    );
  }

  loadCatalogoVolumen(){
    this.loading = true;
    this._utilService.obtenerCatalogoItem(appConfig.idCatalogoVolumen).subscribe(
      data => {
        this.tiposVolumen = data;
        this.loading = false;
        this.loadProvincias();
      },
      error => {
        this.loading = false;
      }
    );
  }

  loadProvincias(){
    this.loading = true;
    this._utilService.obtenerLocalidad(appConfig.idLocalidadPadreEcuador).subscribe(
      data => {
        this.provincias = data;
        this.loading = false;
        this.loadData();
      },
      error => {
        this.loading = false;
      }
    );
  }

  loadData() {
    this.loading = true;
    if (this.obtieneSecuencia == "1") {
      this._solicitudEnvioService.getDefaultValues().subscribe(
        data => {
          this.solicitudEnvio.idSolicitud = data.idSolicitud;
          this.provinciaOrigen = data.provinciaOrigen;
          this.cargarCantones(1);
          this.solicitudEnvio.idOrigen = data.ciudadOrigen;
          this.consultarDatosOrigen();
          if(data.provinciaDestino){//Este dato existe si esque ya ha creado una solicitud antes
            this.provinciaDestino = data.provinciaDestino;
            this.cargarCantones(2);
            this.solicitudEnvio.idDestino = data.ciudadDestino;
            this.consultarDatosDestino();
            this.solicitudEnvio.tipoPeso = data.unidadPeso;
            this.solicitudEnvio.tipoVolumen = data.unidadVolumen;
          }
        },
        error => {
          this.loading = false;
          if(error.status == 403) this._authService.logout();
        }
      );
    } else {
      this._solicitudEnvioService.getSolicitudEnvioByIdSolicitud(this.idSolicitud).subscribe(
        data => {
          this.solicitudEnvio = data;
          this.loadDefaultData();
          this.loading = false;
        },
        error => {
          this.loading = false;
          if(error.status == 403) this._authService.logout();
        }
      );
    }
  }

  loadDefaultData() {
    //Date for form
    this.fechRecol = this.solicitudEnvio.fechaEnvio;
    this.fechEntreg = this.solicitudEnvio.fechaEntrega;
    this.solicitudEnvio.tipoSubasta = this.tiposSubasta.filter( tipoSubasta => tipoSubasta.catalogoItemId == this.solicitudEnvio.tipoSubasta)[0];
    this.solicitudEnvio.solicitudEnvioValorObjetivo = this.solicitudEnvio.valorObjetivo;
    //Default for selects
    //Data catalogs
    var pesoval: any = UtilsCommon.filterData(this.tiposPeso, this.solicitudEnvio.tipoPeso, 'catalogoItemDescripcion');
    this.solicitudEnvio.tipoPeso = pesoval.catalogoItemId;
    var volumenval: any = UtilsCommon.filterData(this.tiposVolumen, this.solicitudEnvio.tipoVolumen, 'catalogoItemDescripcion');
    //Data locale
    this.solicitudEnvio.tipoVolumen = volumenval.catalogoItemId;
    this._utilService.obtenerLocalidadById(this.solicitudEnvio.idOrigen).subscribe(data => {
      this.provinciaOrigen = data.localidadLocalidadPadre;
      this.cargarCantones(1);
    });
    this._utilService.obtenerLocalidadById(this.solicitudEnvio.idDestino).subscribe(data => {
      this.provinciaDestino = data.localidadLocalidadPadre;
      this.cargarCantones(2);
    });

    if (this.cancelada === 1) {
      this.fechRecol = null;
      this.fechEntreg = null;
    }

  }

  cargarCantones(parametroCombo) {

    if (parametroCombo == 1) {
      this.provinciaSeleccionada = this.provinciaOrigen;
    } else {
      this.provinciaSeleccionada = this.provinciaDestino;
    }
    if (this.provinciaSeleccionada) {
      this.loading = true;
      this._utilService.obtenerLocalidad(this.provinciaSeleccionada).subscribe(
        data => {
          if (parametroCombo == 1) {
            this.cantonesOrigen = data;
          } else {
            this.cantonesDestino = data;
          }
          this.loading = false;
        },
        error => {
          this.loading = false;
        }
      );
    }

  }

  setSolicitud(solicitud: any) {
    let solicitud_ = {} as SolicitudEnvio;
    solicitud_.solicitudEnvioId = solicitud.idSolicitud;
    solicitud_.solicitudEnvioPeso = solicitud.peso;
    solicitud_.solicitudEnvioUnidadPeso = solicitud.tipoPeso;
    solicitud_.solicitudEnvioVolumen = solicitud.volumen;
    solicitud_.solicitudEnvioUnidadVolumen = solicitud.tipoVolumen;
    solicitud_.solicitudEnvioNumeroPiesas = solicitud.numeroPiezas;
    solicitud_.solicitudEnvioDireccionOrigen = solicitud.direccion;
    solicitud_.solicitudEnvioLocalidadOrigen = solicitud.idOrigen;
    solicitud_.solicitudEnvioDireccionDestino = solicitud.direccionEntrega;
    solicitud_.solicitudEnvioLocalidadDestino = solicitud.idDestino;
    solicitud_.solicitudEnvioNumeroEstibadores = solicitud.numeroEstibadores;
    solicitud_.solicitudEnvioPersonaEntrega = solicitud.personaEntrega;
    solicitud_.solicitudEnvioPersonaRecibe = solicitud.personaRecibe;
    solicitud_.solicitudEnvioFechaRecoleccion = UtilsCommon.dataForDatePiker(this.fechRecol).formatted;
    solicitud_.solicitudEnvioFechaEntrega = UtilsCommon.dataForDatePiker(this.fechEntreg).formatted;
    solicitud_.solicitudEnvioNumeroDocCliente = solicitud.numeroDocCliente;
    solicitud_.solicitudEnvioValorObjetivo = solicitud.valorObjetivo;
    solicitud_.solicitudEnvioTipoSubasta = solicitud.tipoSubasta.catalogoItemId;
    solicitud_.solicitudEnvioEstado = solicitud.estado;
    solicitud_.solicitudEnvioComentario = solicitud.comments;
    solicitud_.solicitudEnvioObservaciones = solicitud.observaciones;
    return solicitud_;
  }

  agregarSolicitudEnvio(forma: any) {
    this.deshabilitarBtnGuardar = true;
    if(!forma.valid){
      this.camposValidos = false;
      this.deshabilitarBtnGuardar = false;
      return;
    }

    this.loading = true;
    this.solicitudEnvio.estado = appConfig.estadoSolicitudEnvioActiva;
    this._solicitudEnvioService.addSolicitudEnvio(this.setSolicitud(this.solicitudEnvio)).subscribe(data => {
      this.loading = false;
      this.operacionCorrecta = data.response == 'OK';
      Swal.fire('', data.responseMessage, (data.response == 'OK')? 'success' : 'error').then((result) => {
        this.deshabilitarBtnGuardar = data.response == 'OK';
        this.cerrarMensaje();
      });
      },
      error => {
        this.loading = false;
        if(error.status == 403) this._authService.logout();
        this.deshabilitarBtnGuardar = false;
        Swal.fire('', 'Lo sentimos la solicitud no pude ser registrada correctamente, por favor intente de nuevo', 'error');
      }
    );
  }

  editarSolicitudEnvio(forma: any) {
    this.deshabilitarBtnEditar = true;
    if(!forma.valid){
      this.camposValidos = false;
      this.deshabilitarBtnEditar = false;
      return;
    }

    this.loading = true;
    if (this.cancelada === 1)
      this.solicitudEnvio.estado = appConfig.estadoSolicitudEnvioActiva;
    this._solicitudEnvioService.editSolicitudEnvio(this.setSolicitud(this.solicitudEnvio)).subscribe(data => {
      this.loading = false;
      this.operacionCorrecta = data.response == 'OK';
      this.deshabilitarBtnEditar = this.operacionCorrecta;
      Swal.fire('', data.responseMessage, (data.response == 'OK')? 'success' : 'error').then((result) => {
        this.deshabilitarBtnEditar = data.response == 'OK';
        this.cerrarMensaje();
      });
    }, error => {
      this.loading = false;
      if(error.status == 403) this._authService.logout();
      this.operacionCorrecta = false;
      this.deshabilitarBtnEditar = this.operacionCorrecta;
      Swal.fire('', 'La solicitud no pudo ser actualizada', 'error');
    });
  }

  cancelarSolicitudEnvio(forma: any) {
    this.deshabilitarBtnCancelar = true;
    if(!forma.valid){
      this.camposValidos = false;
      this.deshabilitarBtnCancelar = false;
      return;
    }

    this.loading = true;
    this.solicitudEnvio.solicitudEnvioId = appConfig.estadoSolicitudCancelada;

    this._solicitudEnvioService.cancelarSolicitudEnvio(this.setSolicitud(this.solicitudEnvio)).subscribe(data => {
      this.loading = false;
      this.operacionCorrecta = data.response == 'OK';
      this.deshabilitarBtnCancelar = this.operacionCorrecta;
      Swal.fire('', data.responseMessage, (data.response == 'OK')? 'success' : 'error').then((result) => {
        this.deshabilitarBtnCancelar = data.response == 'OK';
        this.cerrarMensaje();
      });
    }, error => {
      this.loading = false;
      if(error.status == 403) this._authService.logout();
      this.operacionCorrecta = false;
      this.deshabilitarBtnCancelar = this.operacionCorrecta;
      Swal.fire('', 'La solicitud no pudo ser actualizada', 'error');
    });
  }

  private cleanUpSolicitudEnvio() {
    this.solicitudEnvio = {} as SolicitudEnvio;
  }

  cerrarMensaje() {
    if (this.operacionCorrecta) this.regresar();
  }

  regresar() {
    this.complete.emit(true);
    this.cleanUpSolicitudEnvio();
  }

  consultarDatosOrigen(){
    this.loading = true;
    this._solicitudEnvioService.getUltimosDatosOrigen(this.solicitudEnvio.idOrigen).subscribe(
      data => {
        this.entregan = [];
        this.direccionesOrigen = [];
        if(data && data[0]){
          this.solicitudEnvio.direccion = data[0].direccion;
          this.solicitudEnvio.personaEntrega = data[0].persona;
          data.forEach(x=> {
            this.entregan.push(x.persona);
            this.direccionesOrigen.push(x.direccion);
          });
        }
        this.loading = false;
      },
      error => {
        this.loading = false;
      }
    );
  }

  consultarDatosDestino(){
    this.loading = true;
    this._solicitudEnvioService.getUltimosDatosDestino(this.solicitudEnvio.idDestino).subscribe(
      data => {
        this.reciben = [];
        this.direccionesDestino = [];
        if(data && data[0]){
          this.solicitudEnvio.direccionEntrega = data[0].direccion;
          this.solicitudEnvio.personaRecibe = data[0].persona;
          data.forEach(x=> {
            this.reciben.push(x.persona);
            this.direccionesDestino.push(x.direccion);
          });
        }
        this.loading = false;
      },
      error => {
        this.loading = false;
      }
    );
  }

}
