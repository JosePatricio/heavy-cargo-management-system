import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {appConfig} from "../../../app.config";
import {SolicitudEnvioService} from "../../../services/solicitud-envio.service";
import {DatatableComponent} from "@swimlane/ngx-datatable/src/index";
import {UtilsCommon} from "../../../utils/utils.common";
import {AuthService} from "../../../services/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-solicitud-proceso-entrega',
  templateUrl: './solicitud-proceso-entrega.component.html'
})
export class SolicitudProcesoEntregaComponent implements OnInit {

  public loading = false;
  isRestrictedUser: boolean;
  appConfig = appConfig;

  //Para dataTable
  @ViewChild(SolicitudProcesoEntregaComponent) table: DatatableComponent;
  @ViewChild('tmplDate') tmplDate: TemplateRef<any>;
  @ViewChild('tmplAmount') tmplAmount: TemplateRef<any>;
  @ViewChild('linkButton') linkButton: TemplateRef<any>;
  solicitudes = [];
  columns = [];

  constructor(private _router: Router, public _solicitudEnvioService: SolicitudEnvioService,
              private _authService: AuthService) {
  }

  ngOnInit() {
    this.isRestrictedUser = UtilsCommon.restrictedUser(this._authService.getTypeUser());
    this.columns = [
      {prop: 'idSolicitud', name: 'Info.', cellTemplate: this.linkButton, headerClass: 'table-header'},
      {prop: 'idSolicitud', name: 'No. solicitud', headerClass: 'table-header'},
      {prop: 'numeroDocCliente', name: 'No. documento', headerClass: 'table-header'},
      {prop: 'cliente', name: 'Empresa', headerClass: 'table-header'},
      {prop: 'transportista', name: 'Transportista', headerClass: 'table-header'},
      {prop: 'origen', name: 'Origen', headerClass: 'table-header'},
      {prop: 'fechaRecepcion', name: 'Fecha recepción mercadería', cellTemplate: this.tmplDate, headerClass: 'table-header'},
      {prop: 'destino', name: 'Destino', headerClass: 'table-header'},
      {prop: 'fechaEntrega', name: 'Fecha requerida entrega', cellTemplate: this.tmplDate, headerClass: 'table-header'},
    ];

    if(!this.isRestrictedUser){
      this.columns.push(
        {prop: 'ofertaValor', name: 'Valor', cellTemplate: this.tmplAmount, headerClass: 'table-header'}
      )
    }
    this.loadOffer();
  }

  loadOffer() {
    this.loading = true;
    this._solicitudEnvioService.getSolicitudEnvioAllByEstado(appConfig.estadoSolicitudEnvioProcesoEntrega).subscribe(data => {
      this.solicitudes = data;
      this.loading = false;
    }, error => {
      if(error.status == 403) this._authService.logout();
    });
  }

  viewDetail(idSolicitud: any) {
    this._router.navigate(['/panel/solicitudes/solicitud-detail/', idSolicitud]);
  }

}
