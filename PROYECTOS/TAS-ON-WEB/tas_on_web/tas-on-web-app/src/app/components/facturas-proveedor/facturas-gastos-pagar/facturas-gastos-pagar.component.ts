import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {appConfig} from "../../../app.config";
import {NotificationsService} from "angular2-notifications";
import {FacturaProveedorService} from "../../../services/factura-proveedor.service";
import {AuthService} from "../../../services/auth.service";

@Component({
  selector: 'app-facturas-gastos-pagar',
  templateUrl: './facturas-gastos-pagar.component.html'
})
export class FacturasGastosPagarComponent implements OnInit {

  public loading = false;

  appConfig = appConfig;

  selFactGasto: any[] = [];

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
      {sortable: 'false', canAutoResize: 'false', draggable: 'false', resizeable: 'false',
        headerCheckboxable: 'true', checkboxable: 'true', width: '70'},
      {prop: 'facturaProveedorNumero', name: 'No. factura', headerClass: 'table-header'},
      {prop: 'facturaProveedorFechaEmision', name: 'Fecha emisión', cellTemplate: this.tmplDate, headerClass: 'table-header'},
      {prop: 'facturaProveedorRucCliente', name: 'RUC proveedor', headerClass: 'table-header'},
      {prop: 'facturaProveedorRucClienteName', name: 'Nombre proveedor', headerClass: 'table-header'},
      {prop: 'facturaProveedorTotal', name: 'Valor total', cellTemplate: this.tmplAmount, headerClass: 'table-header'},
      {prop: 'facturaProveedorFechaPago', name: 'Fecha pago', cellTemplate: this.tmplDate, headerClass: 'table-header'},
      {prop: 'facturaProveedorFormaPagoDesc', name: 'Forma pago', headerClass: 'table-header'},
      {prop: 'facturaProveedorCompraDesc', name: 'Tipo compra', headerClass: 'table-header'}
    ];
    this.loadData();
  }

  loadData() {
    this.loading = true;
    this._facturaProveedorService.getFacturaProvAllByIdEstado(appConfig.idFactGastoPendientePago).subscribe(data => {
      this.facturasGastos = data;
      this.loading = false;
    }, error => {
      this.loading = false;
      if(error.status == 403) this._authService.logout();
    });
  }

  getFacts() {
    var factGastos: any[] = [];
    this.selFactGasto.forEach(factGasto => {
      var factGas_: any = {
        facturaProveedorNumero: factGasto.facturaProveedorNumero,
        facturaProveedorRucCliente: factGasto.facturaProveedorRucCliente,
        facturaProveedorRucClienteName: null,
        facturaProveedorFechaEmision: null,
        facturaProveedorTotal: null,
        facturaProveedorFechaPago: null,
        facturaProveedorFormaPago: null,
        facturaProveedorFormaPagoDesc: null,
        facturaProveedorCompra: null,
        facturaProveedorCompraDesc: null,
        facturaProveedorState: this.appConfig.idFactGastoListaPago,
        facturaProveedorTransfer: null
      };
      factGastos.push(factGas_);
    });
    return factGastos;
  }

  generaCashManage() {
    this.loading = true;
    this._facturaProveedorService.updateFacturaProv(this.getFacts()).subscribe(data => {
      this._notification.success("").html = "<b>Operación exitosa:</b> <br/> Datos guardados correctamente";
      this.loading = false;
      this.loadData();
    }, error => {
      this._notification.warn('Operación erronea: \n' + error.message + '...');
      this.loading = false;
    });
  }

  onSelect({selected}) {
    this.selFactGasto.splice(0, this.selFactGasto.length);
    this.selFactGasto.push(...selected);
  }

}
