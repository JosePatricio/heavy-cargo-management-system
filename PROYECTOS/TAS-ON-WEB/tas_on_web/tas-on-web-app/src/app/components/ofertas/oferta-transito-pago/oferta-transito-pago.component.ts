import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {appConfig} from "../../../app.config";
import {OfertaService} from "../../../services/oferta.service";
import {NotificationsService} from "angular2-notifications";
import {UtilsCommon} from "../../../utils/utils.common";
import {AuthService} from "../../../services/auth.service";

@Component({
  selector: 'app-oferta-transito-pago',
  templateUrl: './oferta-transito-pago.component.html',
  styleUrls: ['./oferta-transito-pago.component.css']
})
export class OfertaTransitoPagoComponent implements OnInit {
  public loading = false;

  appConfig = appConfig;

  //Para dataTable
  @ViewChild('tmplIndex') tmplIndex: TemplateRef<any>;
  @ViewChild('tmplTypePay') tmplTypePay: TemplateRef<any>;
  @ViewChild('tmplAmount') tmplAmount: TemplateRef<any>;
  @ViewChild('tmplDiscount') tmplDiscount: TemplateRef<any>;
  @ViewChild('frmCash') frmCash: TemplateRef<any>;
  @ViewChild('tmplDate') tmplDate: TemplateRef<any>;
  columns = [];
  ofertas: any;

  constructor(public _oferta: OfertaService, private _notification: NotificationsService, private _authService: AuthService) {
  }

  ngOnInit() {
    this.columns = [
      {name: '#', cellTemplate: this.tmplIndex, headerClass: 'table-header', width: 85},
      {prop: 'numeroPrefactura', name: 'No. prefactura', headerClass: 'table-header'},
      {prop: 'invoiceId', name: 'No. factura', headerClass: 'table-header'},
      {prop: 'fechaPago', name: 'Fecha pago factura', cellTemplate: this.tmplDate, headerClass: 'table-header'},
      {prop: 'idSolicitud', name: 'No. solicitud', headerClass: 'table-header'},
      {prop: 'idOferta', name: 'No. oferta', headerClass: 'table-header'},
      {prop: 'supplier', name: 'Nombre proveedor', headerClass: 'table-header'},
      {prop: 'typePay', name: 'Tipo pago', cellTemplate: this.tmplTypePay, headerClass: 'table-header'},
      {prop: 'amount', name: 'Valor', cellTemplate: this.tmplAmount, headerClass: 'table-header'},
      {prop: 'descuento', name: 'Valor descuento', cellTemplate: this.tmplDiscount, headerClass: 'table-header'},
      {prop: 'expiredDay', name: 'Días vencidos', headerClass: 'table-header'},
      {name: 'Número de transferencia cash', cellTemplate: this.frmCash, headerClass: 'table-header'},
    ];
    this.loadOffer();
  }

  formatNumeric(row: any) {
    if (UtilsCommon.restrictedUser(this._authService.getTypeUser()))
      return "xxxx";
    else
      return UtilsCommon.formatNumeric(row.amount, 0);
  }

  loadOffer() {
    this.loading = true;
    this._oferta.obtenerAllOfertasByEstado(appConfig.ofertaEstadoTransitoPago).subscribe(data => {
      this.ofertas = data;
      this.loading = false;
    }, error => {
      if(error.status == 403) this._authService.logout();
    });
  }

  saveCash(oferta: any) {
    this.loading = true;
    var offer: any = {
      idSolicitud: oferta.idSolicitud,
      amount: null,
      comments: null,
      date: null,
      idOferta: oferta.idOferta,
      state: appConfig.ofertaEstadoTransitoPago,
      idConductor: null,
      idVehiculo: null,
      conductor: null,
      nroTransferencia: oferta.noTransfe
    };
    this._oferta.updateOffer(offer).subscribe(data => {
      this.loadOffer();
      this._notification.success("Número de transferencia cash actualizado correctamente...")
    }, error => {
    });
  }

}
