import {Component, EventEmitter, Input, OnInit, Output, TemplateRef, ViewChild} from '@angular/core';
import {PublicService} from "../../../../services/public.service";
import {NotificationsService} from "angular2-notifications";
import {appConfig} from "../../../../app.config";
import {PagoService} from "../../../../services/pago.service";
import {IMyDpOptions} from "mydatepicker";
import {FacturaService} from "../../../../services/factura.service";
import {UtilsCommon} from "../../../../utils/utils.common";
import {InfoPago} from '../../../../interfaces/infoPago';

@Component({
  selector: 'app-facturas-por-cobrar-detalle',
  templateUrl: './facturas-por-cobrar-detalle.component.html',
  styleUrls: ['./facturas-por-cobrar-detalle.component.css']
})
export class FacturasPorCobrarDetalleComponent implements OnInit {

  public loading: any = false;
  appConfig = appConfig;

  public myDatePickerOptions: IMyDpOptions = {
    // other options...
    dateFormat: appConfig.formatoFechaDatePicker,
    editableDateField: false
  };

  tipoPago: any = [];
  bancos: any = [];
  pago: any = {};
  infoPago: InfoPago = {};

  newPay: any = false;
  detalleSolic: any = true;

  @Input('invoice') invoice;
  @Output() complete: EventEmitter<boolean> = new EventEmitter<boolean>();

  //Para dataTable
  @ViewChild('tmplDate') tmplDate: TemplateRef<any>;
  @ViewChild('tmplAmount') tmplAmount: TemplateRef<any>;
  @ViewChild('tmplValorComic') tmplValorComic: TemplateRef<any>;
  rows = [];
  columns = [];

  //Retenciones
  rowsret = [];
  columnsret = [];

  //Solicitudes
  columnSol = [];
  rowSol = [];


  constructor(private _publicService: PublicService, private _facturaService: FacturaService, private _pagoService: PagoService, private _notification: NotificationsService) {
  }

  ngOnInit() {
    this.columns = [
      {prop: 'pagoId', name: 'No. pago', headerClass: 'table-header'},
      {prop: 'pagoTipo', name: 'Tipo', headerClass: 'table-header'},
      {prop: 'pagoNumeroDocumento', name: 'No. documento', headerClass: 'table-header'},
      {prop: 'pagoFechaDocumento', name: 'Fecha pago', cellTemplate: this.tmplDate, headerClass: 'table-header'},
      {prop: 'pagoValor', name: 'Valor', cellTemplate: this.tmplAmount, headerClass: 'table-header'},
      {name: 'Estado', headerClass: 'table-header'},
    ];

    this.columnsret = [
      {prop: 'codigoRetencion', name: 'Cod. retención', headerClass: 'table-header'},
      {prop: 'porcentajeRetener', name: 'Porcentaje retención', headerClass: 'table-header'},
      {prop: 'numDocSustento', name: 'No. documento sustento', headerClass: 'table-header'},
      {prop: 'valorRetenido', name: 'Valor Retenido', headerClass: 'table-header'},
    ];

    this.columnSol = [
      {prop: 'idSolicitud', name: 'No. solicitud', headerClass: 'table-header'},
      {prop: 'idOferta', name: 'Id. Oferta', headerClass: 'table-header'},
      //{prop: 'comments', name: 'Descripción', headerClass: 'table-header'},
      {prop: 'origen', name: 'Origen', headerClass: 'table-header'},
      {prop: 'destino', name: 'Destino', headerClass: 'table-header'},
      {
        prop: 'fechaEntrega',
        name: 'Fecha entrega mercadería',
        cellTemplate: this.tmplDate,
        headerClass: 'table-header'
      },
      {name: 'Valor', cellTemplate: this.tmplValorComic, headerClass: 'table-header'}
    ];

    this.loadData();
  }

  loadData() {
    this.loading = true;
    this._facturaService.getInvoiceDetailByNumber(this.invoice.invoiceId).subscribe(
      data => {
        this.rowSol = data.offers;
        this.loading = false;
      }, error => {
      });

    this.pago.pagoFacturaId = this.invoice.invoiceId;
  }

  loadPagoDetail(boolean) {

    if (!boolean) {
      this.loading = true;
      this._pagoService.getPagos(this.invoice.invoiceId).subscribe(
        data => {
          this.rows = data;
          this.loading = false;
          this.loadCatalog();
        }, error => {
        });

      this.pago.pagoFacturaId = this.invoice.invoiceId;
    }
    this.detalleSolic = boolean;
  }

  loadCatalog() {
    this.loading = true;
    this._publicService.getCatalogoItemByCatalogo(appConfig.idCatalogoTipoPago).subscribe(
      data => {
        this.tipoPago = data.filter(catItem => catItem.catalogoItemDescripcion !== 'EFECTIVO');
        this.tipoPago = this.tipoPago.filter(catItem => catItem.catalogoItemDescripcion !== 'TARJETA DE CREDITO');
        this.loading = false;
      },
      error => {
      }
    );

    this.loading = true;
    this._publicService.getCatalogoItemByCatalogo(appConfig.idCatalogoBanco).subscribe(
      data => {
        this.bancos = data;
        this.loading = false;
      },
      error => {
      }
    );
  }

  guardaPago() {
    if (this.pago.pagoValor <= this.invoice.invoiceBalanceDue) {
      this.loading = true;
      var objetoAEnviar: any;
      var esRetencion: boolean;
      if(this.pago.pagoTipoId == 81){//es retencion
        objetoAEnviar = this.infoPago;
        esRetencion = true;
      } else {
        this.clearAccesKey();
        objetoAEnviar = this.pago;
        esRetencion = false;
      }

      if(this.pago.pagoFechaDocumento) this.pago.pagoFechaDocumento = this.pago.pagoFechaDocumento.formatted;
      if(parseInt(this.pago.pagoTipoId) == this.appConfig.tipoPagoNotaCredito){
        this.pago.pagoFechaDocumento = null;
      }
      this._pagoService.createPago(objetoAEnviar, esRetencion).subscribe(
        data => {
          this._notification.success("Operación exitosa: " + data.responseMessage);
          this.newPay = false;
          this.loading = false;
          this.loadPagoDetail(false);
          this.updateSaldo();
          this.limpiarDatos();
        }, error => {
          this._notification.warn("Pago no registrado, favor verificar los datos ingresados...");
          this.newPay = true;
          this.loading = false;
          this.loadPagoDetail(false);
        });
    } else {
      this._notification.warn("Pago no registrado, el valor ingresado es mayor al monto de la factura...");
    }
  }

  limpiarDatos() {
    this.pago.claveAcceso = "";
    this.pago.rucCliente = "";
    this.pago.razonCliente = "";
    this.pago.pagoValor = null;
    this.rowsret = null;
    this.pago.pagoFechaDocumento = null;
    this.infoPago = {};
  }

  pagoTipoChanged(){
    this.pago.pagoValor = 0.00;
  }

  cancelarPago() {
    this.limpiarDatos();
    this.loadPagoDetail(false);
    this.loadData();
    this.newPay = false;
  }

  clearAccesKey() {
    this.pago.claveAcceso = "";
    this.pago.rucCliente = "";
    this.pago.razonCliente = "";

    delete this.pago.claveAcceso;
    delete this.pago.rucCliente;
    delete this.pago.razonCliente;
  }

  checkPagoTipo() {
    if(parseInt(this.pago.pagoTipoId) === this.appConfig.tipoPagoRetencion)
      return "pagoRetencion";
    else if(parseInt(this.pago.pagoTipoId) === this.appConfig.tipoPagoNotaCredito)
      return "pagoNotaCredito";
    else
      return "otros";
  }

  valorRetenido(impuestos: any) {
    let valor = 0;
    impuestos.impuesto.forEach(impuesto_ => {
      valor += impuesto_.valorRetenido
    });
    return valor;
  }

  checkRetOnline() {
    this.loading = true;
    this._pagoService.getRetencionDetail(this.pago.claveAcceso, this.invoice.invoiceId).subscribe(
      data => {
        this.pago.rucCliente = data.comprobante.infoCompRetencion.identificacionSujetoRetenido;
        this.pago.razonCliente = data.comprobante.infoCompRetencion.razonSocialSujetoRetenido;
        this.pago.pagoValor = this.valorRetenido(data.comprobante.impuestos);
        this.rowsret = data.comprobante.impuestos.impuesto;
        this.pago.pagoFechaDocumento=UtilsCommon.dataForDatePiker(data.comprobante.infoCompRetencion.fechaEmision);
        this.infoPago.facturaId = data.facturaId;
        this.infoPago.comprobanteXML = data.comprobanteXML;
        this.infoPago.comprobanteRetencion = data.comprobante;
        this.loading = false;
      }, error => {
        error = error.json();
        this._notification.warn(error.developerMessage);
        this.loading = false;
      });
  }

  buscarNotaCredito(){
    this.loading = true;
    this._pagoService.getNotaCreditoDetail(this.invoice.invoiceId).subscribe(
      data => {
        this.pago.pagoValor = data.valor;
        this.loading = false;
      }, error => {
        error = error.json();
        this._notification.warn(error.developerMessage);
        this.loading = false;
      });
  }

  closeDetail() {
    this.complete.emit(true);
  }

  updateSaldo() {
    let totalPagos = 0;
    this.rows.forEach(pago => {
      totalPagos += pago.pagoValor;
    });
    totalPagos = totalPagos + parseFloat(this.pago.pagoValor);
    this.invoice.invoiceBalanceDue = this.invoice.invoiceAmount - totalPagos;
  }
}
