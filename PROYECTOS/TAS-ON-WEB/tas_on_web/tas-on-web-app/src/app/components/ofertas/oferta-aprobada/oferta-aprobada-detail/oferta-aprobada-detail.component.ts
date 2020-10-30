import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {SolicitudEnvioService} from "../../../../services/solicitud-envio.service";
import {OfertaService} from "../../../../services/oferta.service";
import {appConfig} from "../../../../app.config";
import {AuthService} from "../../../../services/auth.service";
import Swal from "sweetalert2/dist/sweetalert2.all.min.js";

@Component({
  selector: 'app-oferta-aprobada-detail',
  templateUrl: './oferta-aprobada-detail.component.html',
  styleUrls: ['./oferta-aprobada-detail.component.css']
})
export class OfertaAprobadaDetailComponent implements OnInit {

  public loading = false;
  idSolicitud: string;
  solicitud: any;
  appConfig = appConfig;
  conductores: any;
  offer: any;
  vehiculos: any;
  operacionCorrecta: boolean = false;
  mensajeOperacion: string = '';
  bloquearBotonActualizar: boolean = false;

  constructor(private _router: Router, private _activatedRoute: ActivatedRoute, private _authService: AuthService,
              public _solicitudEnvioService: SolicitudEnvioService, public _oferta: OfertaService) {
  }

  ngOnInit() {
    this.solicitud = {};
    this.solicitud.offer = {};
    this.offer = {};
    this.loading = true;
    this._activatedRoute.params.subscribe(parametros => {
      this.idSolicitud = parametros.solicitud;
      this.loading = false;
    });
    this.loading = true;
    this._solicitudEnvioService.getSolicitudEnvioByIdSolicitud(this.idSolicitud).subscribe(
      data => {
        this.solicitud = data;
        this.loading = false;
      },
      error => {
        this.loading = false;
      }
    );

    this.loading = true;
    this._oferta.obtenerVehiculos(this.idSolicitud).subscribe(
      data => {
        this.vehiculos = data;
        this.loading = false;
      },
      error => {
        this.loading = false;
      });

    this.loading = true;
    this._oferta.obtenerConductores(this.idSolicitud).subscribe(
      data => {
        this.conductores = data;
        this.loading = false;
      },
      error => {
        this.loading = false;
      });
  }

  updateOfertas() {
    this.bloquearBotonActualizar = true;
    this.offer.idOferta = this.solicitud.offer.idOferta;
    this.offer.state = appConfig.ofertasEstadoCreada;
    this.loading = true;
    this._oferta.updateOffer(this.offer).subscribe(
      data => {
        this.loading = false;
        this.operacionCorrecta = data.response == 'OK';
        this.bloquearBotonActualizar = this.operacionCorrecta;
        Swal.fire('', data.responseMessage, (data.response == 'OK')? 'success' : 'error').then((result) => {
          this.cerrarMensaje();
        });
      },
      error => {
        this.loading = false;
        if(error.status == 403) this._authService.logout();
        this.bloquearBotonActualizar = false;
        this.operacionCorrecta = false;
        Swal.fire('', error.message, 'error');
      });
  }

  cerrarMensaje() {
    if (this.operacionCorrecta) {
      this._router.navigate(['panel/ofertas/oferta-aprobada']);
    }
  }

}
