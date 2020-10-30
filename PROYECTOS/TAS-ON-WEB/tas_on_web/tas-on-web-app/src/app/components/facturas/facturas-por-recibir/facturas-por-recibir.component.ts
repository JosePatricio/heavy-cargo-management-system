import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {OfertaService} from "../../../services/oferta.service";
import {FacturaService} from "../../../services/factura.service";
import {NotificationsService} from "angular2-notifications";
import {IMyDpOptions} from "mydatepicker";
import {AuthService} from "../../../services/auth.service";
import {appConfig} from "../../../app.config";
import {NgForm} from "@angular/forms";
import {UtilsCommon} from "../../../utils/utils.common";

declare var $: any;

@Component({
  selector: 'app-facturas-por-recibir',
  templateUrl: './facturas-por-recibir.component.html'
})
export class FacturasPorRecibirComponent implements OnInit {

  public loading = false;

  //temporal
  selOfert: any[] = [];
  datosCompania: any = {};
  datosTasOn: any = {};
  invoiceData: any = {};
  appConfig = appConfig;

  maxDate = new Date();

  //Data Table
  @ViewChild('tmplIndex') tmplIndex: TemplateRef<any>;
  @ViewChild('linkButton') linkButton: TemplateRef<any>;
  @ViewChild('tmplAmount') tmplAmount: TemplateRef<any>;
  @ViewChild('tmplDiscount') tmplDiscount: TemplateRef<any>;
  @ViewChild('tmplTypePay') tmplTypePay: TemplateRef<any>;
  facturas: any;
  columns = [];

  public myDatePickerOptions: IMyDpOptions = {
    // other options...
    dateFormat: appConfig.formatoFechaDatePicker,
    editableDateField: false
  };

  @ViewChild('forma') forma: NgForm;

  constructor(public _oferta: OfertaService, public _facturaService: FacturaService, public _authService: AuthService, private _notification: NotificationsService) {
  }

  ngOnInit() {
    this.columns = [
      {name: '#', headerClass: 'table-header', cellTemplate: this.tmplIndex, width: 85},
      {name: 'Info.', headerClass: 'table-header', cellTemplate: this.linkButton, width: 85},
      {prop: 'invoiceId', name: 'No. Documento', headerClass: 'table-header'},
      {prop: 'company', name: 'Nombre de proveedor', headerClass: 'table-header'},
      {prop: 'invoicesTypePayDesc', name: 'Tipo de pago', cellTemplate: this.tmplTypePay, headerClass: 'table-header'},
      {prop: 'invoiceAmount', name: 'Valor del documento', cellTemplate: this.tmplAmount, headerClass: 'table-header'},
      {prop: 'invoicesDiscount', name: 'Valor descuento', cellTemplate: this.tmplDiscount, headerClass: 'table-header'}
    ];
    this.loadInvoice();
  }

  loadInvoice() {
    this.loading = true;
    this._facturaService.getInvoiceAllListByState(appConfig.facturaEstadoPre).subscribe(data => {
      this.facturas = data;
      this.loading = false;
    }, error => {
      if(error.status == 403) this._authService.logout();
    });
    this.invoiceData = {};
  }

  setOfferts(tipoPago, discount) {
    var totalFactura = 0;
    this.selOfert.forEach(oferta => {
      totalFactura = totalFactura + oferta.amount;
    });
    this.invoiceData.subTotal = totalFactura;
    if (tipoPago === appConfig.idPagoInmediato)
      totalFactura = totalFactura - discount;
    return UtilsCommon.numericFormat(totalFactura);
  }

  callDetailInvoice(invoice: any) {
    this.loading = true;
    this._facturaService.getInvoiceDetailByNumber(invoice.invoiceId).subscribe(data => {
      this.datosCompania = data.client;
      this.datosTasOn = data.tason;
      this.selOfert = data.offers;
      this.invoiceData.fechRecivFact = new Date();
      this.invoiceData.noFact = data.invoiceNumer;
      this.invoiceData.noAut = data.authorizationNumber;
      this.invoiceData.fechPreFact = data.invoiceDatePreFactura;
      this.invoiceData.fechAut = data.invoiceDateFactura;
      this.invoiceData.numDoc = data.invoiceId;
      this.invoiceData.amountInv = this.setOfferts(data.invoiceTypePay, data.invoiceDiscount);
      this.invoiceData.userReciv = this._authService.getIdUsuario();
      this.invoiceData.invoiceTypePay = data.invoiceTypePay;
      this.invoiceData.invoicesTypePayDesc = invoice.invoicesTypePayDesc;
      this.invoiceData.invoiceDiscount = data.invoiceDiscount;
      $("#modPreviewInvoice").modal('show');
      this.loading = false;
    }, error => {
      this._notification.warn("Error al obtener documento !!");
    });
  }

  onCloseDialog() {
    this.loadInvoice();
    this.forma.resetForm();
  }

  saveInvoice() {
    this.loading = true;
    var invoice: any = {
      numberInvoice: this.invoiceData.numDoc,
      idInvoice: this.invoiceData.noFact,
      personInvoice: this.invoiceData.userEntre,
      authNroInvoice: this.invoiceData.noAut,
      dateAuthInvoice: this.invoiceData.fechAut.formatted
    };

    this._facturaService.updateInvoice(invoice).subscribe(data => {
      this._notification.success("Operación Exitosa: " + data.responseMessage);
      this.loading = false;
      this.forma.resetForm();
      this.loadInvoice();
    }, error => {
      this._notification.warn("Operación Erronea: " + error.message);
      this.loading = false;
    });
  }
}
