import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {appConfig} from "../../../app.config";
import {UtilsCommon} from "../../../utils/utils.common";
import {AuthService} from "../../../services/auth.service";
import {SolicitudEnvioService} from "../../../services/solicitud-envio.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-solicitud-ofertas-aprobadas',
  templateUrl: './solicitud-ofertas-aprobadas.component.html'
})
export class SolicitudOfertasAprobadasComponent implements OnInit {

  public loading = false;

  /**Table**/
  @ViewChild('tmplIndex') tmplIndex: TemplateRef<any>;
  @ViewChild('tmpPeso') tmpPeso: TemplateRef<any>;
  @ViewChild('linkButton') linkButton: TemplateRef<any>;
  @ViewChild('tmplAmount') tmplAmount: TemplateRef<any>;
  columns = [];
  listaSolicitudes: any;

  constructor(private _router: Router, public _solicitudEnvioService: SolicitudEnvioService, private _authService: AuthService) {
  }

  ngOnInit() {
    this.columns = [
      {name: '#', cellTemplate: this.tmplIndex, headerClass: 'table-header', width: 85},
      {name: 'Info.', cellTemplate: this.linkButton, headerClass: 'table-header', width: 85},
      {prop: 'idSolicitud', name: 'No. solicitud', headerClass: 'table-header'},
      {prop: 'numeroDocCliente', name: 'No. documento', headerClass: 'table-header'},
      {prop: 'cliente', name: 'Transportista', headerClass: 'table-header'},
      {prop: 'origen', name: 'Origen', headerClass: 'table-header'},
      {prop: 'destino', name: 'Destino', headerClass: 'table-header'},
      {prop: 'peso', name: 'Peso', cellTemplate: this.tmpPeso, headerClass: 'table-header'},
      {prop: 'ofertaValor', name: 'Valor oferta', cellTemplate: this.tmplAmount, headerClass: 'table-header'},
    ];
    this.loadSolicitud();
  }

  loadSolicitud() {
    this.loading = true;
    this._solicitudEnvioService.getSolicitudEnvioAllByEstado(appConfig.estadoSolicitudEnvioAsignada).subscribe(data => {
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

  formatNumeric(row: any) {
    if (UtilsCommon.restrictedUser(this._authService.getTypeUser()))
      return "xxxx";
    else
      return UtilsCommon.formatNumeric(row.ofertaValor, row.comision);
  }

}
