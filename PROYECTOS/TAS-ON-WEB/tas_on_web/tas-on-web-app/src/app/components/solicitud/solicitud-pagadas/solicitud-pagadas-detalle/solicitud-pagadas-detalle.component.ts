import {Component, EventEmitter, Input, OnInit, Output, TemplateRef, ViewChild} from '@angular/core';
import {OfertaService} from "../../../../services/oferta.service";
import {DatatableComponent} from "@swimlane/ngx-datatable/src/index";
import {NotificationsService} from "angular2-notifications";
import {FacturaService} from "../../../../services/factura.service";
import {appConfig} from "../../../../app.config";
import {EmpresaService} from "../../../../services/empresa.service";
import {UtilsCommon} from "../../../../utils/utils.common";

@Component({
  selector: 'app-solicitud-pagadas-detalle',
  templateUrl: './solicitud-pagadas-detalle.component.html'
})
export class SolicitudPagadasDetalleComponent implements OnInit {

  public loading = false;
  imageReceive: any;
  appConfig = appConfig;

  totalFactura: any = 0;
  datosCompania: any = {};
  datosTasOn: any = {};

  //Detalle data table
  @ViewChild(SolicitudPagadasDetalleComponent) table1: DatatableComponent;
  @ViewChild('date_') date_: TemplateRef<any>;
  @ViewChild('amount_') amount_: TemplateRef<any>;
  factura: any = {};
  ofertas = [];
  columns1 = [];

  @Input('idFactura') idFactura;
  @Output() complete: EventEmitter<boolean> = new EventEmitter<boolean>();

  constructor(public _facturaService: FacturaService, private _notification: NotificationsService, private _oferta: OfertaService, public _empresaService: EmpresaService) {
  }

  ngOnInit() {

    this.columns1 = [
      {prop: 'idOferta', name: 'Cantidad', headerClass: 'table-header'},
      {prop: 'comments', name: 'Comentarios', headerClass: 'table-header'},
      {prop: 'amount', name: 'Precio unitario', cellTemplate: this.amount_, headerClass: 'table-header'}
    ];
    this.showDetail();
  }

  initDatosEnFact() {
    //Datos Compania
    this.loading = true;
    this._empresaService.getClienteByToken().subscribe(data => {
      this.datosCompania = data;
      this.loading = false;
    }, error => {
    });

    //Datos TasOn
    this.loading = true;
    this._empresaService.readEmpresa(appConfig.rucTas).subscribe(data => {
      this.datosTasOn = data;
      this.loading = false;
    }, error => {
    });
  }

  showDetail() {
    this.loading = true;
    this._facturaService.getInvoiceDetailByNumber(this.idFactura).subscribe(data => {

      this.factura = data;
      this.ofertas = data.offers;

      this.loading = false;
      this.initDatosEnFact();
      this.calcAmount();
    }, error => {
    });
  }

  calcAmount() {
    this.totalFactura = 0;
    this.ofertas.forEach(oferta => {
      this.totalFactura = this.totalFactura + (oferta.amount + oferta.comision);
    });
  }

  closeDetail() {
    this.complete.emit(true);
  }

}
