import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {SolicitudEnvioService} from "../../../services/solicitud-envio.service";
import {Router} from "@angular/router";
import {appConfig} from "../../../app.config";
import {UtilsCommon} from "../../../utils/utils.common";
import {AuthService} from "../../../services/auth.service";

@Component({
  selector: 'app-solicitud-despachar',
  templateUrl: './solicitud-despachar.component.html'
})
export class SolicitudDespacharComponent implements OnInit {

  public loading = false;

  /**Table**/
  @ViewChild('tmplIndex') tmplIndex: TemplateRef<any>;
  @ViewChild('tmpPeso') tmpPeso: TemplateRef<any>;
  @ViewChild('linkButton') linkButton: TemplateRef<any>;
  @ViewChild('tmplAmount') tmplAmount: TemplateRef<any>;
  columns = [];
  listaSolicitudes: any;
  isRestrictedUser: boolean;

  constructor(private _router: Router, public _solicitudEnvioService: SolicitudEnvioService, private _authService: AuthService) {
  }

  ngOnInit() {
    this.isRestrictedUser = UtilsCommon.restrictedUser(this._authService.getTypeUser());
    this.columns = [
      {name: '#', cellTemplate: this.tmplIndex, headerClass: 'table-header', width: 85},
      {name: 'Info.', cellTemplate: this.linkButton, headerClass: 'table-header', width: 85},
      {prop: 'idSolicitud', name: 'No. solicitud', headerClass: 'table-header'},
      {prop: 'numeroDocCliente', name: 'No. documento', headerClass: 'table-header'},
      {prop: 'cliente', name: 'Empresa', headerClass: 'table-header'},
      {prop: 'transportista', name: 'Transportista', headerClass: 'table-header'},
      {prop: 'origen', name: 'Origen', headerClass: 'table-header'},
      {prop: 'destino', name: 'Destino', headerClass: 'table-header'},
      {prop: 'peso', name: 'Peso', cellTemplate: this.tmpPeso, headerClass: 'table-header'},
    ];

    if(!this.isRestrictedUser) {
      this.columns.push(
        {prop: 'ofertaValor', name: 'Valor oferta', cellTemplate: this.tmplAmount, headerClass: 'table-header'},
      )
    }

    this.loadSolicitud();
  }

  loadSolicitud() {
    this.loading = true;
    this._solicitudEnvioService.getSolicitudEnvioAllByEstado(appConfig.estadoSolicitudEnvioPorDespachar).subscribe(data => {
      this.listaSolicitudes = data;
      this.loading = false;
    }, error => {
      this.loading = false;
      if(error.status == 403) this._authService.logout();
    });
  }

  refreshData() {
    this.loadSolicitud();
  }

  viewDetail(idSolicitud) {
    this._router.navigate(['/panel/solicitudes/solicitud-detail/', idSolicitud]);
  }

}
