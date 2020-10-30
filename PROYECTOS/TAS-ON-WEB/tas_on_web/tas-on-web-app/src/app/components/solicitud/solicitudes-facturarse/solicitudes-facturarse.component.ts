import {Component, ComponentFactoryResolver, OnInit, TemplateRef, ViewChild, ViewContainerRef} from '@angular/core';
import {DatatableComponent} from "@swimlane/ngx-datatable/src/index";
import {appConfig} from "../../../app.config";
import {NotificationsService} from "angular2-notifications";
import {SolicitudEnvioService} from "../../../services/solicitud-envio.service";
import {SolicitudesFacturarseDetalleComponent} from "./solicitudes-facturarse-detalle/solicitudes-facturarse-detalle.component";
import {AuthService} from "../../../services/auth.service";

declare var $: any;

@Component({
  selector: 'app-solicitudes-facturarse',
  templateUrl: './solicitudes-facturarse.component.html'
})
export class SolicitudesFacturarseComponent implements OnInit {
  public loading = false;
  solicitudes: any;
  solicitudesDetalle: any;
  appConfig = appConfig;

  solFact = true;

  //Principal data table
  @ViewChild(DatatableComponent) table: DatatableComponent;
  @ViewChild('tmplDate') tmplDate: TemplateRef<any>;
  @ViewChild('tmplAmount') tmplAmount: TemplateRef<any>;
  @ViewChild('linkButton') linkButton: TemplateRef<any>;
  rows = [];
  columns = [];
  temp = [];

  @ViewChild('dynamicInsert', {read: ViewContainerRef}) dynamicInsert: ViewContainerRef;

  constructor(public _solicitudEnvio: SolicitudEnvioService, private _notification: NotificationsService,
              private viewContainerRef: ViewContainerRef, private componentFactoryResolver: ComponentFactoryResolver,
              private _authService: AuthService) {
  }

  ngAfterViewInit() {
    //this.resolveContent(0);
  }

  resolveContent(idSolicitud: number) {
    this.loading = true;
    const componentFactory = this.componentFactoryResolver.resolveComponentFactory(SolicitudesFacturarseDetalleComponent);
    if (this.dynamicInsert) {
      this.dynamicInsert.clear();
      const dyynamicComponent = <SolicitudesFacturarseDetalleComponent>this.dynamicInsert.createComponent(componentFactory).instance;
      dyynamicComponent.idSolicitud = idSolicitud;
      dyynamicComponent.complete.subscribe(complete => {
        this.closeDialog(complete);
      });
    }
    this.loading = false;
  }

  ngOnInit() {

    this.solicitudesDetalle = {};
    this.solicitudesDetalle.offer = {};

    this.columns = [
      {prop: 'idSolicitud', name: 'Info.', headerClass: 'table-header', cellTemplate: this.linkButton},
      {prop: 'idSolicitud', name: 'No. solicitud', headerClass: 'table-header'},
      {prop: 'cliente', name: 'Cliente', headerClass: 'table-header'},
      {prop: 'origen', name: 'Origen', headerClass: 'table-header'},
      {prop: 'destino', name: 'Destino', headerClass: 'table-header'},
      {prop: 'ofertaValor', name: 'Valor', cellTemplate: this.tmplAmount, headerClass: 'table-header'},
      {
        prop: 'fechaEntrega',
        name: 'Fecha entrega mercadería',
        cellTemplate: this.tmplDate,
        headerClass: 'table-header'
      },
      {
        prop: 'fechaFacturacion',
        name: 'Fecha facturación',
        cellTemplate: this.tmplDate,
        headerClass: 'table-header'
      }
    ];

    this.loadOffer();
  }

  loadOffer() {
    this.loading = true;
    this._solicitudEnvio.getSolicitudEnvioAllByEstado(appConfig.estadoSolicitudEnvioEntregada).subscribe(data => {
      this.solicitudes = data;

      // cache our list
      this.temp = [...data];

      // push our inital complete list
      this.rows = data;
      this.loading = false;
    }, error => {
      if(error.status == 403) this._authService.logout();
    });
  }

  showDetail(solicitud) {
    this.solFact = false;

    this.resolveContent(solicitud.idSolicitud);
  }

  closeDialog(event: boolean) {
    this.solFact = event;
    this.loadOffer();
  }
}
