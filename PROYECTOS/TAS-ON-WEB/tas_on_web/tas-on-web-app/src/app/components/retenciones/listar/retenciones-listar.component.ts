import {Component, ComponentFactoryResolver, OnInit, TemplateRef, ViewChild, ViewContainerRef} from '@angular/core';
import {DatatableComponent} from "@swimlane/ngx-datatable/src/index";
import {NotificationsService} from "angular2-notifications";
import {appConfig} from "../../../app.config";
import {FacturaService} from "../../../services/factura.service";
import {AuthService} from "../../../services/auth.service";
import {DatePipe} from "@angular/common";

@Component({
  selector: 'app-retenciones-listar',
  templateUrl: './retenciones-listar.component.html'
})
export class RetencionesListarComponent implements OnInit {

  public loading = false;
  retenciones: any;
  appConfig = appConfig;

  razonSocialCliente: string;
  numeroFacturaCliente: string;
  prefactura: string;
  fechaAutorizacionDesde: any;
  fechaAutorizacionHasta: any;


  //Para dataTable
  @ViewChild(DatatableComponent) table: DatatableComponent;
  @ViewChild('tmplIndex') tmplIndex: TemplateRef<any>;
  @ViewChild('tmplDate') tmplDate: TemplateRef<any>;
  @ViewChild('tmplDateTime') tmplDateTime: TemplateRef<any>;
  @ViewChild('tmplAmount') tmplAmount: TemplateRef<any>;
  @ViewChild('tmplBreakWord') tmplBreakWord: TemplateRef<any>;
  @ViewChild('tmplDownloadFiles') tmplDownloadFiles: TemplateRef<any>;

  rows = [];
  columns = [];

  operacionCorrecta: any;
  mensajeOperacion: any;

  viewDetail = false;

  @ViewChild('detailDynamic', {read: ViewContainerRef}) detailDynamic: ViewContainerRef;

  constructor(public _facturaService: FacturaService, private _notification: NotificationsService,
              private viewContainerRef: ViewContainerRef, private componentFactoryResolver: ComponentFactoryResolver,
              private _authService: AuthService) {
  }

  ngOnInit() {
    this.columns = [
      {name: '#', cellTemplate: this.tmplIndex, headerClass: 'table-header', width: 85},
      {prop: 'cliente', name: 'Cliente/Proveedor', headerClass: 'table-header'},
      {prop: 'numeroPrefactura', name: 'Prefactura', headerClass: 'table-header'},
      {prop: 'numeroFacturaCliente', name: 'Factura cliente', headerClass: 'table-header'},
      {prop: 'estado', name: 'Estado', headerClass: 'table-header'},
      {prop: 'total', name: 'Total factura', cellTemplate: this.tmplAmount, headerClass: 'table-header'},
      {prop: 'retencion', name: 'Retención', cellTemplate: this.tmplAmount, headerClass: 'table-header'},
      {prop: 'fechaAutorizacionRetencion', name: 'Fecha autorización', cellTemplate: this.tmplDateTime, headerClass: 'table-header'},
      {prop: 'claveAccesoRetencion', name: 'Clave acceso', cellTemplate: this.tmplBreakWord, headerClass: 'table-header'},
      {prop: 'fechaAutorizacionRetencion', name: 'XML',cellTemplate: this.tmplDownloadFiles, headerClass: 'table-header'},

    ];
  }

  buscarRetenciones(){
    let query:string = "?";
    const pipe = new DatePipe('es-EC');
    if(this.razonSocialCliente) {
      if(query != "") query = query.concat("&");
      query=query.concat("razonSocialCliente="+this.razonSocialCliente);
    }
    if(this.numeroFacturaCliente){
      if(query != "") query = query.concat("&");
      query=query.concat("numeroFacturaCliente="+this.numeroFacturaCliente);
    }
    if(this.prefactura){
      if(query != "") query = query.concat("&");
      query=query.concat("prefactura="+this.prefactura);
    }
    if(this.fechaAutorizacionDesde){
      if(query != "") query = query.concat("&");
      query=query.concat("fechaAutorizacionDesde="+pipe.transform(this.fechaAutorizacionDesde, appConfig.formatoFechaURL));
    }
    if(this.fechaAutorizacionHasta){
      if(query != "") query = query.concat("&");
      query=query.concat("fechaAutorizacionHasta="+pipe.transform(this.fechaAutorizacionHasta, appConfig.formatoFechaURL));
    }
    this.loadRetencionesByQuery(query);
  }

  loadRetencionesByQuery(query: string) {
    this.loading = true;
    this._facturaService.getRetencionesFacturasBy(query).subscribe(data => {
      this.retenciones = data;
      this.loading = false;
    }, error => {
      if(error.status == 403) this._authService.logout();
    });
  }

  limpiarDatos(){
    this.razonSocialCliente = null;
    this.numeroFacturaCliente = null;
    this.prefactura = null;
    this.fechaAutorizacionDesde = null;
    this.fechaAutorizacionHasta = null;
  }

  downloadXML(invoice) {
    var popup = window.open();
    popup.document.write('Descargando archivo ...');

    this.loading = true;
    this._facturaService.getRetencionXML(invoice.claveAccesoRetencion).subscribe(data => {
      const url = window.URL.createObjectURL(data);
      var anchor = document.createElement("a");
      anchor.download = invoice.claveAccesoRetencion +".xml";
      anchor.href = url;
      anchor.target = '_blank';
      anchor.click();
      popup.location.href = url;
      this.loading = false;
      //popup.close();
    }, error => {
      this.loading = false;
    });
  }

}
