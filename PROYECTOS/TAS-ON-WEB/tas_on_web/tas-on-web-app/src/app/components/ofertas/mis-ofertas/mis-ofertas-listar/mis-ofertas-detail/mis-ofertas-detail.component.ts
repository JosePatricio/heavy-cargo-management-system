import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {SolicitudEnvioService} from "../../../../../services/solicitud-envio.service";
import {OfertaService} from "../../../../../services/oferta.service";
import {appConfig} from "../../../../../app.config";
import Swal from "sweetalert2/dist/sweetalert2.all.min.js";
import {AuthService} from "../../../../../services/auth.service";

@Component({
  selector: 'app-mis-ofertas-detail',
  templateUrl: './mis-ofertas-detail.component.html',
  styleUrls: ['./mis-ofertas-detail.component.css']
})
export class MisOfertasDetailComponent implements OnInit {

  public loading = false;
  solicitud: any;
  amount: any = "";
  comments: any;
  oferta: any;
  offer: any;
  appConfig = appConfig;
  operacionCorrecta: boolean = false;
  mensajeOperacion: string = '';

  camposValidos: boolean = true;
  deshabilitarBtnActualizar: boolean = false;

  @Input('idSolicitud') idSolicitud;
  @Input('indexAction') indexAction;
  @Input('idOferta') idOferta;
  @Output() complete: EventEmitter<boolean> = new EventEmitter<boolean>();

  constructor(public _solicitudEnvioService: SolicitudEnvioService, public _ofertaService: OfertaService,
              private _authService: AuthService) {
  }

  ngOnInit() {
    this.solicitud = {
      "offer": {
        "amount" : 0,
        "comision": 0
      }
    };
    this.oferta = {};
    this.offer = {};

    if (this.indexAction === 3) {
      this.loading = true;
      this._ofertaService.obtenerOfertaByOfertaId(this.idOferta).subscribe(
        data => {
          this.solicitud = data;
          this.amount = data.offer.amount;
          this.comments = data.offer.comments;
          this.loading = false;
        },
        error => {
          this.loading = false;
          if(error.status == 403) this._authService.logout();
        }
      );
    } else {
      this.loading = true;
      this._solicitudEnvioService.getSolicitudEnvioByIdSolicitud(this.idSolicitud).subscribe(
        data => {
          this.loading = false;
          this.solicitud = data;
          this.amount = this.solicitud.offer.amount;
          this.comments = this.solicitud.offer.comments;
        },
        error => {
          this.loading = false;
          if(error.status == 403) this._authService.logout();
        }
      );
    }
  }

  updateOfertas(forma: any) {
    this.deshabilitarBtnActualizar = true;
    if(!forma.valid){
      this.camposValidos = false;
      this.deshabilitarBtnActualizar = false;
      return;
    }
    this.loading = true;
    if (this.amount != this.solicitud.offer.amount
      || this.comments != this.solicitud.offer.comments) {
      this.oferta.idSolicitud = this.solicitud.offer.idSolicitud;
      this.oferta.amount = this.solicitud.offer.amount;
      this.oferta.comments = this.solicitud.offer.comments;
      this.oferta.idOferta = this.solicitud.offer.idOferta;
      this._ofertaService.updateOffer(this.oferta).subscribe(data => {
        this.loading = false;
        this.operacionCorrecta = data.response == 'OK';
        this.deshabilitarBtnActualizar = this.operacionCorrecta;
        Swal.fire('', data.responseMessage, (data.response == 'OK')? 'success' : 'error').then((result) => {
          this.cerrarMensaje();
        });
      }, error => {
        this.loading = false;
        this.operacionCorrecta = false;
        this.deshabilitarBtnActualizar = false;
        Swal.fire('', 'La oferta no pudo ser enviada', 'error');
      });
    } else {
      this.loading = false;
      this.operacionCorrecta = false;
      this.deshabilitarBtnActualizar = false;
      Swal.fire('', 'La oferta no ha tenido cambios', 'warning');
    }
  }

  cancelOfertas() {
    this.loading = true;
    this.offer.idOferta = this.solicitud.offer.idOferta;
    this.offer.state = appConfig.ofertaEstadoCancelada;
    this._ofertaService.updateOffer(this.offer).subscribe(data => {
      this.loading = false;
      this.operacionCorrecta = data.response == 'OK';
      this.deshabilitarBtnActualizar = this.operacionCorrecta;
      Swal.fire('', data.responseMessage, (data.response == 'OK')? 'success' : 'error').then((result) => {
        this.cerrarMensaje();
      });
    }, error => {
      this.loading = false;
      if(error.status == 403) this._authService.logout();
      this.operacionCorrecta = false;
      Swal.fire('', 'La oferta no pudo ser enviada', 'error');
    });
  }

  cerrarMensaje() {
    if (this.operacionCorrecta) {
      this.cerrarDetalle();
    }
  }

  cerrarDetalle() {
    this.complete.emit(true);
  }
}
