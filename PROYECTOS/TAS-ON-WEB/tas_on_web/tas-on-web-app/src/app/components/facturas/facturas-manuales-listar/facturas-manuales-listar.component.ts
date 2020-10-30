import {Component, ComponentFactoryResolver, OnInit, TemplateRef, ViewChild, ViewContainerRef} from '@angular/core';
import {DatatableComponent} from "@swimlane/ngx-datatable/src/index";
import {NotificationsService} from "angular2-notifications";
import {appConfig} from "../../../app.config";
import {FacturaService} from "../../../services/factura.service";
import {AuthService} from "../../../services/auth.service";
import {DatePipe} from "@angular/common";

@Component({
  selector: 'app-facturas-manuales-listar',
  templateUrl: './facturas-manuales-listar.component.html'
})
export class FacturasManualesListarComponent implements OnInit {

  public loading = false;
  facturas: any;
  appConfig = appConfig;

  secuencial: string;
  razonSocial: string;
  ruc: string;
  fechaEmisionDesde: any;
  fechaEmisionHasta: any;

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
      {prop: 'secuencial', name: 'Secuencial', headerClass: 'table-header'},
      {prop: 'fechaEmision', name: 'Fecha de emisión', cellTemplate: this.tmplDateTime, headerClass: 'table-header'},
      {prop: 'fechaAutorizacion', name: 'Fecha autorizacion', cellTemplate: this.tmplDate, headerClass: 'table-header'},
      {prop: 'estado', name: 'Estado', headerClass: 'table-header'},
      {prop: 'total', name: 'Total', cellTemplate: this.tmplAmount, headerClass: 'table-header'},
      {prop: 'razonSocialComprador', name: 'Razon social', headerClass: 'table-header'},
      {prop: 'comprador', name: 'RUC', headerClass: 'table-header'},
      {prop: 'usuario', name: 'Usuario', headerClass: 'table-header'},
      {prop: 'claveAcceso', name: 'Clave acceso', cellTemplate: this.tmplBreakWord, headerClass: 'table-header'},
      {prop: 'fechaAutorizacion', name: 'XML',cellTemplate: this.tmplDownloadFiles, headerClass: 'table-header'},
    ];
  }

  buscarFacturas(){
    let query:string = "?";
    const pipe = new DatePipe('es-EC');
    if(this.secuencial) {
      if(query != "") query = query.concat("&");
      query=query.concat("secuencial="+this.secuencial);
    }
    if(this.razonSocial){
      if(query != "") query = query.concat("&");
      query=query.concat("razonSocial="+this.razonSocial);
    }
    if(this.ruc){
      if(query != "") query = query.concat("&");
      query=query.concat("ruc="+this.ruc);
    }
    if(this.fechaEmisionDesde){
      if(query != "") query = query.concat("&");
      query=query.concat("fechaEmisionDesde="+pipe.transform(this.fechaEmisionDesde, appConfig.formatoFechaURL));
    }
    if(this.fechaEmisionHasta){
      if(query != "") query = query.concat("&");
      query=query.concat("fechaEmisionHasta="+pipe.transform(this.fechaEmisionHasta, appConfig.formatoFechaURL));
    }
    this.loadFacturasByQuery(query);
  }

  loadFacturasByQuery(query: string) {
    this.loading = true;
    this._facturaService.getFacturasManualesBy(query).subscribe(data => {
      this.facturas = data;
      this.loading = false;
    }, error => {
      if(error.status == 403) this._authService.logout();
    });
  }

  limpiarDatos(){
    this.secuencial = null;
    this.razonSocial = null;
    this.ruc = null;
    this.fechaEmisionDesde = null;
    this.fechaEmisionHasta = null;
  }

  downloadXML(invoice) {
    var popup = window.open();
    popup.document.write('Descargando archivo ...');

    this.loading = true;
    this._facturaService.getFacturaManualXML(invoice.claveAcceso).subscribe(data => {
      const url = window.URL.createObjectURL(data);
      var anchor = document.createElement("a");
      anchor.download = invoice.claveAcceso +".xml";
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
