import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {appConfig} from "../../../app.config";
import {FacturaProveedorService} from "../../../services/factura-proveedor.service";
import {NotificationsService} from "angular2-notifications";
import {AuthService} from "../../../services/auth.service";

@Component({
  selector: 'app-facturas-gastos-pagadas',
  templateUrl: './facturas-gastos-pagadas.component.html'
})
export class FacturasGastosPagadasComponent implements OnInit {


  public loading = false;

  appConfig = appConfig;

  selOfert: any[] = [];

  //Para dataTable
  @ViewChild('tmplIndex') tmplIndex: TemplateRef<any>;
  @ViewChild('tmplTypePay') tmplTypePay: TemplateRef<any>;
  @ViewChild('tmplAmount') tmplAmount: TemplateRef<any>;
  @ViewChild('tmplDiscount') tmplDiscount: TemplateRef<any>;
  @ViewChild('tmplDate') tmplDate: TemplateRef<any>;
  columns = [];
  facturasGastos: any;

  constructor(public _facturaProveedorService: FacturaProveedorService, private _notification: NotificationsService,
              private _authService: AuthService) {
  }

  ngOnInit() {
    this.columns = [
      {name: '#', cellTemplate: this.tmplIndex, headerClass: 'table-header', width: 85},
      {prop: 'facturaProveedorNumero', name: 'No. factura', headerClass: 'table-header'},
      {prop: 'facturaProveedorFechaEmision',name: 'Fecha emision', cellTemplate: this.tmplDate, headerClass: 'table-header'},
      {prop: 'facturaProveedorRucCliente', name: 'RUC proveedor', headerClass: 'table-header'},
      {prop: 'facturaProveedorRucClienteName', name: 'Nombre proveedor', headerClass: 'table-header'},
      {prop: 'facturaProveedorTotal', name: 'Valor', cellTemplate: this.tmplAmount, headerClass: 'table-header'},
      {prop: 'facturaProveedorFechaPago', name: 'Fecha pago', cellTemplate: this.tmplDate, headerClass: 'table-header'},
      {prop: 'facturaProveedorFormaPagoDesc', name: 'Forma pago', headerClass: 'table-header'},
      {prop: 'facturaProveedorCompraDesc', name: 'Tipo compra', headerClass: 'table-header'}
    ];
    this.loadData();
  }

  loadData() {
    this.loading = true;
    this._facturaProveedorService.getFacturaProvAllByIdEstado(appConfig.idFactGastoPagadas).subscribe(data => {
      this.facturasGastos = data;
      this.loading = false;
    }, error => {
      this.loading = false;
      if(error.status == 403) this._authService.logout();
    });
  }
}
