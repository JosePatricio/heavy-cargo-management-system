import {Component, OnInit} from '@angular/core';
import {FacturaService} from "../../../../services/factura.service";
import {EmpresaService} from "../../../../services/empresa.service";
import {OfertaService} from "../../../../services/oferta.service";
import {appConfig} from "../../../../app.config";
import {NotificationsService} from "angular2-notifications";
import {AuthService} from "../../../../services/auth.service";

declare var $: any;

@Component({
  selector: 'app-oferta-entregar-factura-listar',
  templateUrl: './oferta-entregar-factura-listar.component.html'
})
export class OfertaEntregarFacturaListarComponent implements OnInit {

  public loading = false;
  appConfig = appConfig;

  facturas: any;
  selOfert: any[] = [];
  datosCompania: any = {};
  datosTasOn: any = {};
  numberDoc: string;
  dateInvoice: any;

  totalFactura: any = 0;
  subTotalFactura: any = 0;
  descuentos: any = 0;
  typePay: any;

  //Mensaje de Operacion exitosa o erronea
  opSucces: boolean = false;
  mensjOpGenFact: string = 'Error al recuperar detalle, por favor intente nuevamente...';

  constructor(public _oferta: OfertaService, public _facturaService: FacturaService, public _empresaService: EmpresaService,
              private _notification: NotificationsService, private _authService: AuthService) {
  }

  ngOnInit() {
    this.loadInvoice();
  }

  loadInvoice() {
    this.loading = true;
    this._facturaService.getInvoiceListByState(appConfig.facturaEstadoPre).subscribe(data => {
      this.facturas = data;
      this.loading = false;
    }, error => {
      if(error.status == 403) this._authService.logout();
    });
  }


  calcAmount(descuentoFact) {
    this.totalFactura = 0;
    this.subTotalFactura = 0;
    this.descuentos = 0;
    if (descuentoFact)
      descuentoFact = descuentoFact / this.selOfert.length;
    this.selOfert.forEach(oferta => {
      if (this.typePay === this.appConfig.idPagoNormal) {
        this.totalFactura = this.totalFactura + oferta.amount;
        this.subTotalFactura = this.totalFactura;
        this.descuentos = 0;
      } else if (this.typePay === this.appConfig.idPagoInmediato) {
        this.subTotalFactura = this.subTotalFactura + oferta.amount;
        this.descuentos = this.descuentos + descuentoFact;
        this.totalFactura = this.subTotalFactura - this.descuentos;
      }
    });
  }


  cerrarMensaje() {
    this.loadInvoice();
  }

  callDetailInvoice(invoiceNumb: string) {
    this.loading = true;
    this._facturaService.getInvoiceDetailByNumber(invoiceNumb).subscribe(data => {
      const detInvoice = data;
      this.datosCompania = detInvoice.client;
      this.typePay = data.invoiceTypePay;
      this.datosTasOn = detInvoice.tason;
      this.numberDoc = detInvoice.invoiceId;
      this.selOfert = detInvoice.offers;
      this.dateInvoice = detInvoice.invoiceDatePreFactura;
      this.calcAmount(detInvoice.invoiceDiscount);
      this.opSucces = true;
      this.selOfert = [{
        comments: 'Código de documento: ' + this.numberDoc + ', transporte de mercadería pesada',
        amount: this.subTotalFactura
      }];
      $("#modPreviewInvoice").modal('show');
      this.loadInvoice();
      this.loading = false;
    }, error => {
      this.opSucces = false;
      this._notification.warn(this.mensjOpGenFact);
      this.loadInvoice();
      this.loading = false;
    });
  }
}
