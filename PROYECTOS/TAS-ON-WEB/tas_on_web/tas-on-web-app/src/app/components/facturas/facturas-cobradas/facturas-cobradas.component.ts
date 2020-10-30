import {Component, ComponentFactoryResolver, OnInit, TemplateRef, ViewChild, ViewContainerRef} from '@angular/core';
import {DatatableComponent} from "@swimlane/ngx-datatable/src/index";
import {NotificationsService} from "angular2-notifications";
import {appConfig} from "../../../app.config";
import {FacturaService} from "../../../services/factura.service";
import {FacturasCobradasDetalleComponent} from "./facturas-cobradas-detalle/facturas-cobradas-detalle.component";
import {AuthService} from "../../../services/auth.service";
import {DatePipe} from "@angular/common";

@Component({
  selector: 'app-facturas-cobradas',
  templateUrl: './facturas-cobradas.component.html'
})
export class FacturasCobradasComponent implements OnInit {

  public loading = false;
  facturas: any;
  appConfig = appConfig;

  clienteRazonSocial: string;
  factura: string;
  valorFacturaDesde: number;
  valorFacturaHasta: number;
  fechaCobroDesde: any;
  fechaCobroHasta: any;

  //Para dataTable
  @ViewChild(DatatableComponent) table: DatatableComponent;
  @ViewChild('tmplDate') tmplDate: TemplateRef<any>;
  @ViewChild('tmplAmount') tmplAmount: TemplateRef<any>;
  @ViewChild('tmplFiltro') tmplFiltro: TemplateRef<any>;
  @ViewChild('linkButton') linkButton: TemplateRef<any>;
  rows = [];
  columns = [];
  temp = [];

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
      {prop: 'invoiceNumber', name: 'Info.', headerClass: 'table-header', cellTemplate: this.linkButton},
      {prop: 'invoiceNumber', name: 'No. factura', headerClass: 'table-header'},
      {prop: 'company', name: 'Cliente', headerClass: 'table-header'},
      {prop: 'invoiceAmount', name: 'Valor', cellTemplate: this.tmplAmount, headerClass: 'table-header'},
      {prop: 'invoiceEmisionDate', name: 'Fecha de emisión', cellTemplate: this.tmplDate, headerClass: 'table-header'},
      {prop: 'invoiceDate', name: 'Fecha de cobro', cellTemplate: this.tmplDate, headerClass: 'table-header'},
      {prop: 'diasFacturaVencida', name: 'Dias vencida la factura', headerClass: 'table-header'}
    ];
  }

  ngAfterViewInit() {
  }

  updateFilter(event) {
    //if (event.target.value.toLowerCase().length == 0)
    //  this.loadOffer();

    const val = event.target.value.toLowerCase();

    // filter our data
    const temp = this.temp.filter(function (d) {
      let data = d.supplier != null ? d.supplier : '';
      return data.toLowerCase().indexOf(val) !== -1 || !val;
    });

    // update the rows
    this.rows = temp;
    // Whenever the filter changes, always go back to the first page
    this.table.offset = 0;
  }

  checkRow(invoice) {
    this.viewDetail = true;
    this.resolveContent(invoice.invoiceId);
  }

  closeDialog(complete: boolean) {
    this.viewDetail = complete;
    //this.loadOffer();
  }

  resolveContent(invoiceId: number) {
    this.loading = true;
    const componentFactory = this.componentFactoryResolver.resolveComponentFactory(FacturasCobradasDetalleComponent);
    if (this.detailDynamic) {
      this.detailDynamic.clear();
      const componentDynamic = <FacturasCobradasDetalleComponent>this.detailDynamic.createComponent(componentFactory).instance;
      componentDynamic.invoiceId = invoiceId;
      componentDynamic.complete.subscribe(complete => {
        this.closeDialog(complete);
      });
    }
    this.loading = false;
  }

  buscarFacturas(){
    let query = "?estado="+appConfig.facturaEstadoFinal;
    const pipe = new DatePipe('es-EC');
    if(this.clienteRazonSocial) query=query.concat("&cliente="+this.clienteRazonSocial);
    if(this.factura) query=query.concat("&facturaNro="+this.factura);
    if(this.valorFacturaDesde) query=query.concat("&valorDesde="+this.valorFacturaDesde);
    if(this.valorFacturaHasta) query=query.concat("&valorHasta="+this.valorFacturaHasta);
    if(this.fechaCobroDesde) query=query.concat("&fechaCobroDesde="+pipe.transform(this.fechaCobroDesde, appConfig.formatoFechaURL));
    if(this.fechaCobroHasta) query=query.concat("&fechaCobroHasta="+pipe.transform(this.fechaCobroHasta, appConfig.formatoFechaURL));
    this.loadFacturasByQuery(query);
  }

  loadFacturasByQuery(query: string) {
    this.loading = true;
    this._facturaService.getInvoiceAllBy(query).subscribe(data => {
      this.facturas = data;
      this.temp = [...data]; // cache our list
      this.rows = data; // push our inital complete list
      this.loading = false;
    }, error => {
      if(error.status == 403) this._authService.logout();
    });
  }


  limpiarDatos(){
    this.clienteRazonSocial = "";
    this.factura = "";
    this.valorFacturaDesde = null;
    this.valorFacturaHasta = null;
    this.fechaCobroDesde = null;
    this.fechaCobroHasta = null;
  }


}
