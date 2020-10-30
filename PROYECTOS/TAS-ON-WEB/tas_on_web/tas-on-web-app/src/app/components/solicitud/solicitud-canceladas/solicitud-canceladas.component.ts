import {Component, ComponentFactoryResolver, OnInit, TemplateRef, ViewChild, ViewContainerRef} from '@angular/core';
import {SolicitudEnvioService} from "../../../services/solicitud-envio.service";
import {AuthService} from "../../../services/auth.service";
import {appConfig} from "../../../app.config";
import {UtilsCommon} from "../../../utils/utils.common";
import {DatatableComponent} from "@swimlane/ngx-datatable/src/components/datatable.component";
import {NuevaSolicitudComponent} from "../listar-solicitud/nueva-solicitud/nueva-solicitud.component";

@Component({
  selector: 'app-solicitud-canceladas',
  templateUrl: './solicitud-canceladas.component.html'
})
export class SolicitudCanceladasComponent implements OnInit {

  public loading = false;
  solEdit: any = true;
  appConfig = appConfig;

  /**dataTable**/
  @ViewChild(DatatableComponent) table: DatatableComponent;
  @ViewChild('tmplIndex') tmplIndex: TemplateRef<any>;
  @ViewChild('tmplDate') tmplDate: TemplateRef<any>;
  @ViewChild('linkButton') linkButton: TemplateRef<any>;
  @ViewChild('tmpPeso') tmpPeso: TemplateRef<any>;
  columns = [];
  listaSolicitudes: any;

  @ViewChild('dynamicInsert', {read: ViewContainerRef}) dynamicInsert: ViewContainerRef;

  constructor(public _solicitudEnvioService: SolicitudEnvioService, private _authService: AuthService,
              private viewContainerRef: ViewContainerRef, private componentFactoryResolver: ComponentFactoryResolver) {
  }

  ngOnInit() {
    this.columns = [
      {name: '#', cellTemplate: this.tmplIndex, headerClass: 'table-header', width: 85},
      {name: 'Info.', cellTemplate: this.linkButton, headerClass: 'table-header'},
      {prop: 'idSolicitud', name: 'No. solicitud', headerClass: 'table-header'},
      {prop: 'numeroDocCliente', name: 'No. documento', headerClass: 'table-header'},
      {prop: 'origen', name: 'Origen', headerClass: 'table-header'},
      {prop: 'destino', name: 'Destino', headerClass: 'table-header'},
      {prop: 'peso', name: 'Peso', cellTemplate: this.tmpPeso, headerClass: 'table-header'},
      {prop: 'numeroPiezas', name: 'No. piezas', headerClass: 'table-header'},
      {prop: 'fechaRecepcion', name: 'F. recolección', cellTemplate: this.tmplDate, headerClass: 'table-header'},
      {prop: 'personaRecibe', name: 'Persona recibe', headerClass: 'table-header'},
      {prop: 'fechaCancelada', name: 'F. cancelada', cellTemplate: this.tmplDate, headerClass: 'table-header'},
    ];
    this.loadSolicitud();
  }

  loadSolicitud() {
    this.loading = true;
    this._solicitudEnvioService.getSolicitudEnvioAllByEstado(appConfig.estadoSolicitudCancelada).subscribe(data => {
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

  resolveContent(obtieneSecuencia: number, edit: number, idSolicitud: string) {
    this.loading = true;
    const componentFactory = this.componentFactoryResolver.resolveComponentFactory(NuevaSolicitudComponent);
    if (this.dynamicInsert) {
      this.dynamicInsert.clear();
      const dyynamicComponent = <NuevaSolicitudComponent>this.dynamicInsert.createComponent(componentFactory).instance;
      dyynamicComponent.obtieneSecuencia = obtieneSecuencia;
      dyynamicComponent.edit = edit;
      dyynamicComponent.idSolicitud = idSolicitud;
      dyynamicComponent.cancelada = 1;
      dyynamicComponent.complete.subscribe(complete => {
        this.closeDetail(complete);
      });
    }
    this.loading = false;
  }


  viewDetail(idSolicitud) {
    //this._router.navigate(['/panel/solicitudes/despachar-detail', idSolicitud]);
    this.loading = true;
    this.solEdit = false;

    this.resolveContent(0, 1, idSolicitud);

    this.loading = false;
  }

  formatNumeric(row: any) {
    if (UtilsCommon.restrictedUser(this._authService.getTypeUser()))
      return "xxxx";
    else
      return UtilsCommon.formatNumeric(row.ofertaValor, row.comision);
  }

  closeDetail(event: boolean) {
    this.solEdit = event;
    this.loadSolicitud();
  }

}
