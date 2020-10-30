import {Component, OnInit} from '@angular/core';
import {appConfig} from '../../../app.config';
import {ActivatedRoute, Router} from '@angular/router';
import {OfertaService} from '../../../services/oferta.service';
import {SolicitudEnvioService} from '../../../services/solicitud-envio.service';
import {DatePipe} from "@angular/common";
import Swal from "sweetalert2/dist/sweetalert2.all.min.js";
import {AuthService} from "../../../services/auth.service";

@Component({
  selector: 'app-nueva-oferta',
  templateUrl: './nueva-oferta.component.html',
  styleUrls: ['./nueva-oferta.component.css']
})

export class NuevaOfertaComponent implements OnInit {
  public loading = false;
  appConfig = appConfig;
  oferta: any;
  solicitudesOfertas: any;
  camposValidos: boolean = true;
  deshabilitarBtnOfertar: boolean = false;
  operacionCorrecta: boolean = false;
  public indexEdit: any;

  constructor(private _router: Router, private _activatedRoute: ActivatedRoute, public _ofertaService: OfertaService,
              public _solicitudEnvioService: SolicitudEnvioService, private _authService: AuthService) {
    this.cleanUpOferta();
    this.solicitudesOfertas = {};
    this._activatedRoute.params.subscribe(
      parametros => {
        this.loading = true;
        this._ofertaService.obtenerSolicitud(parametros.idSolicitud).subscribe(data => {
          this.solicitudesOfertas = data;
          let datePipe = new DatePipe("en-US");
          this.oferta.idSolicitud = this.solicitudesOfertas.idSolicitud;
          this.loading = false;
        });
        if (null != parametros.index) {
          this.indexEdit = parametros.index;
        }
      }
    );
  }

  ngOnInit() {}

  ofertar(forma: any) {
    this.deshabilitarBtnOfertar = true;
    if(!forma.valid){
      this.camposValidos = false;
      this.deshabilitarBtnOfertar = false;
      return;
    }
    this.loading = true;
    this._ofertaService.ofertar(this.oferta).subscribe(data => {
      this.loading = false;
      this.operacionCorrecta = data.response == 'OK';
      this.deshabilitarBtnOfertar = this.operacionCorrecta;
      Swal.fire('', data.responseMessage, (data.response == 'OK')? 'success' : 'error').then((result) => {
        this.cerrarMensaje();
      });
    }, error => {
      this.loading = false;
      if(error.status == 403) this._authService.logout();
      this.operacionCorrecta = false;
      this.deshabilitarBtnOfertar = this.operacionCorrecta;
      Swal.fire('', 'La oferta no pudo ser enviada', 'error');
    });
  };

  cerrarMensaje() {
    if (this.operacionCorrecta) {
      this._router.navigate(['panel/ofertas/puja']);
    }
  }

  private cleanUpOferta() {
    this.oferta = {
      "idSolicitud": "",
      "amount": "",
      "comments": "",
    };
  }

}
