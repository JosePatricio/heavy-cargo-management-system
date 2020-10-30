import {Component, ComponentFactoryResolver, OnInit, TemplateRef, ViewChild, ViewContainerRef} from '@angular/core';
import {DatatableComponent} from "@swimlane/ngx-datatable/src/index";
import {FacturaService} from "../../../services/factura.service";
import {NotificationsService} from "angular2-notifications";
import {appConfig} from "../../../app.config";
import {FacturasPorCobrarDetalleComponent} from "./facturas-por-cobrar-detalle/facturas-por-cobrar-detalle.component";
import {AuthService} from "../../../services/auth.service";

@Component({
  selector: 'app-facturas-por-cobrar',
  templateUrl: './facturas-por-cobrar.component.html'
})
export class FacturasPorCobrarComponent implements OnInit {

  public loading = false;
  facturas: any;
  appConfig = appConfig;

  //Para dataTable
  @ViewChild(DatatableComponent) table: DatatableComponent;
  @ViewChild('tmplDate') tmplDate: TemplateRef<any>;
  @ViewChild('tmplAmount') tmplAmount: TemplateRef<any>;
  @ViewChild('tmplFiltro') tmplFiltro: TemplateRef<any>;
  @ViewChild('linkButton') linkButton: TemplateRef<any>;
  rows = [];
  columns = [];
  temp = [];

  solCobrar = true;

  @ViewChild('detailDynamic', {read: ViewContainerRef}) detailDynamic: ViewContainerRef;

  constructor(public _facturaService: FacturaService, private _notification: NotificationsService, private _authService: AuthService,
              private viewContainerRef: ViewContainerRef, private componentFactoryResolver: ComponentFactoryResolver) {
  }

  ngOnInit() {
    this.columns = [
      {name: 'Info.', headerClass: 'table-header', cellTemplate: this.linkButton},
      {prop: 'invoiceNumber', name: 'No. factura', headerClass: 'table-header'},
      {prop: 'company', name: 'Cliente', headerClass: 'table-header'},
      {prop: 'invoiceAmount', name: 'Valor', cellTemplate: this.tmplAmount, headerClass: 'table-header'},
      {prop: 'invoiceBalanceDue', name: 'Saldo', cellTemplate: this.tmplAmount, headerClass: 'table-header'},
      {prop: 'invoiceEmisionDate', name: 'Fecha de emisión', cellTemplate: this.tmplDate, headerClass: 'table-header'},
      {prop: 'invoiceDate', name: 'Fecha de cobro', cellTemplate: this.tmplDate, headerClass: 'table-header'},
      {prop: 'diasFacturaVencida', name: 'Dias vencida la factura', headerClass: 'table-header'}
    ];
    this.loadOffer();
  }

  ngAfterViewInit() {
  }

  loadOffer() {
    this.loading = true;
    this._facturaService.getInvoiceAllListByState(appConfig.facturaPorCobrar).subscribe(data => {
      this.facturas = data;

      // cache our list
      this.temp = [...data];

      // push our inital complete list
      this.rows = data;
      this.loading = false;
    }, error => {
      if(error.status == 403) this._authService.logout();
    });
  }

  resolveContent(invoice: any) {
    this.loading = true;
    const componentFactory = this.componentFactoryResolver.resolveComponentFactory(FacturasPorCobrarDetalleComponent);
    if (this.detailDynamic) {
      this.detailDynamic.clear();
      const componentDynamic = <FacturasPorCobrarDetalleComponent>this.detailDynamic.createComponent(componentFactory).instance;
      componentDynamic.invoice = invoice;
      componentDynamic.complete.subscribe(complete => {
        this.closeDialog(complete);
      });
    }
    this.loading = false;
  }

  updateFilter(event) {
    if (event.target.value.toLowerCase().length == 0)
      this.loadOffer();

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
    this.solCobrar = false;
    this.resolveContent(invoice);
  }

  closeDialog(complete: boolean) {
    this.solCobrar = complete;
    this.loadOffer();
  }
}
