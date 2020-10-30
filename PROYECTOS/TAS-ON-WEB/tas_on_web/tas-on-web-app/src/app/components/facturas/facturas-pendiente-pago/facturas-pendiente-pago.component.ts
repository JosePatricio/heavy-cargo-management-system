import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {appConfig} from "../../../app.config";
import {OfertaService} from "../../../services/oferta.service";
import {EmpresaService} from "../../../services/empresa.service";
import {FacturaService} from "../../../services/factura.service";
import {AuthService} from "../../../services/auth.service";

@Component({
  selector: 'app-facturas-pendiente-pago',
  templateUrl: './facturas-pendiente-pago.component.html'
})
export class FacturasPendientePagoComponent implements OnInit {

  public loading = false;

  appConfig = appConfig;

  @ViewChild('tmplIndex') tmplIndex: TemplateRef<any>;
  @ViewChild('tmplAmount') tmplAmount: TemplateRef<any>;
  @ViewChild('tmplDiscount') tmplDiscount: TemplateRef<any>;
  @ViewChild('tmplDate') tmplDate: TemplateRef<any>;
  @ViewChild('tmplTypePay') tmplTypePay: TemplateRef<any>;
  columns = [];
  ofertas: any;


  constructor(public _oferta: OfertaService, public _facturaService: FacturaService, private _authService: AuthService,
              public _empresaService: EmpresaService) {
  }

  ngOnInit() {
    this.columns = [
      {name: '#', cellTemplate: this.tmplIndex, headerClass: 'table-header', width: 85},
      {prop: 'numeroPrefactura', name: 'No. prefactura', headerClass: 'table-header'},
      {prop: 'invoiceId', name: 'No. factura TAS-ON', headerClass: 'table-header'},
      {prop: 'invoiceClientId', name: 'No. factura Cliente', headerClass: 'table-header'},
      {prop: 'idSolicitud', name: 'No. solicitud', headerClass: 'table-header'},
      {prop: 'idOferta', name: 'No. oferta', headerClass: 'table-header'},
      {prop: 'supplier', name: 'Nombre proveedor', headerClass: 'table-header'},
      {prop: 'typePay', name: 'Tipo pago', cellTemplate: this.tmplTypePay, headerClass: 'table-header'},
      {prop: 'amount', name: 'Valor total', cellTemplate: this.tmplAmount, headerClass: 'table-header'},
      {prop: 'descuento', name: 'Valor descuento', cellTemplate: this.tmplDiscount, headerClass: 'table-header'},
      {prop: 'datePay', name: 'Fecha de pago', cellTemplate: this.tmplDate, headerClass: 'table-header'},
      {prop: 'expiredDay', name: 'Días vencidos', headerClass: 'table-header'},
    ];
    this.loadOffer();
  }

  loadOffer() {
    this.loading = true;
    this._oferta.obtenerAllOfertasByEstado(appConfig.ofertaEstadoCobrar).subscribe(data => {
      this.ofertas = data;
      this.loading = false;
    }, error => {
      if(error.status == 403) this._authService.logout();
    });
  }

  refreshData() {
    this.loadOffer();
  }
}
