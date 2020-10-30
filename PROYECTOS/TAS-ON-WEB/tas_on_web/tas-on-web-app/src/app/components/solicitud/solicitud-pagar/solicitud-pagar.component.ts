import {Component, ComponentFactoryResolver, OnInit, TemplateRef, ViewChild, ViewContainerRef} from '@angular/core';
import {appConfig} from "../../../app.config";
import {SolicitudEnvioService} from "../../../services/solicitud-envio.service";
import {DatatableComponent} from "@swimlane/ngx-datatable/src/index";
import {SolicitudPagarDetalleComponent} from "./solicitud-pagar-detalle/solicitud-pagar-detalle.component";
import {UtilsCommon} from "../../../utils/utils.common";
import {AuthService} from "../../../services/auth.service";

@Component({
  selector: 'app-solicitud-pagar',
  templateUrl: './solicitud-pagar.component.html'
})
export class SolicitudPagarComponent implements OnInit {

  public loading = false;
  solFact: any = true;
  appConfig = appConfig;
  //Para dataTable
  @ViewChild(SolicitudPagarComponent) table: DatatableComponent;
  @ViewChild('tmplDate') tmplDate: TemplateRef<any>;
  @ViewChild('tmplAmount') tmplAmount: TemplateRef<any>;
  @ViewChild('tmplFiltro') tmplFiltro: TemplateRef<any>;
  @ViewChild('linkButton') linkButton: TemplateRef<any>;
  @ViewChild('tmplValorComic') tmplValorComic: TemplateRef<any>;
  solicitudes = [];
  columns = [];

  @ViewChild('dynamicInsert', {read: ViewContainerRef}) dynamicInsert: ViewContainerRef;

  constructor(public _solicitudEnvioService: SolicitudEnvioService, private viewContainerRef: ViewContainerRef,
              private componentFactoryResolver: ComponentFactoryResolver, private _authService: AuthService) {
  }

  ngOnInit() {
    this.columns = [
      {prop: 'facturaId', name: 'Info.', cellTemplate: this.linkButton, headerClass: 'table-header'},
      {prop: 'facturaId', name: 'No. factura', headerClass: 'table-header'},
      {prop: 'cliente', name: 'Transportista', headerClass: 'table-header'},
      {prop: 'idSolicitud', name: 'No. solicitud', headerClass: 'table-header'},
      {prop: 'numeroDocCliente', name: 'No. documento', headerClass: 'table-header'},
      {prop: 'origen', name: 'Origen', headerClass: 'table-header'},
      {prop: 'destino', name: 'Destino', headerClass: 'table-header'},
      {prop: 'fechaEntrega', name: 'Fecha entrega mercadería', cellTemplate: this.tmplDate, headerClass: 'table-header'},
      {prop: 'fechaFacturacion', name: 'Fecha prevista pago', cellTemplate: this.tmplDate, headerClass: 'table-header'},
      {name: 'Valor', cellTemplate: this.tmplValorComic, headerClass: 'table-header'},
      {prop: 'descuento', name: 'Descuento', cellTemplate: this.tmplAmount, headerClass: 'table-header'},
      {prop: 'dias', name: 'Dias vencida factura', headerClass: 'table-header'}
    ];
    this.loadOffer();
  }

  resolveContent(idFactura: number) {
    this.loading = true;
    const componentFactory = this.componentFactoryResolver.resolveComponentFactory(SolicitudPagarDetalleComponent);
    if (this.dynamicInsert) {
      this.dynamicInsert.clear();
      const dyynamicComponent = <SolicitudPagarDetalleComponent>this.dynamicInsert.createComponent(componentFactory).instance;
      dyynamicComponent.idFactura = idFactura;
      dyynamicComponent.complete.subscribe(complete => {
        this.closeDetail(complete);
      });
    }
    this.loading = false;
  }

  loadOffer() {
    this.loading = true;
    this._solicitudEnvioService.getSolicitudEnvioAllByEstado(appConfig.estadoSolicitudPorPagar).subscribe(data => {

      // push our inital complete list
      this.solicitudes = data;
      this.loading = false;
    }, error => {
      if(error.status == 403) this._authService.logout();
    });
  }

  showDetail(solicitud: any) {
    this.solFact = false;
    this.resolveContent(solicitud.facturaId);
  }

  closeDetail(event: boolean) {
    this.solFact = event;
    this.loadOffer();
  }

}