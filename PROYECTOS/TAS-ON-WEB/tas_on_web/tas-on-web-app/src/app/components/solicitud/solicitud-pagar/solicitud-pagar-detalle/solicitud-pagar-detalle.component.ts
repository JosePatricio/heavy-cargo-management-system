import {Component, EventEmitter, Input, OnInit, Output, TemplateRef, ViewChild} from '@angular/core';
import {SolicitudPagadasDetalleComponent} from "../../solicitud-pagadas/solicitud-pagadas-detalle/solicitud-pagadas-detalle.component";
import {DatatableComponent} from "@swimlane/ngx-datatable/src/index";
import {FacturaService} from "../../../../services/factura.service";
import {appConfig} from "../../../../app.config";

@Component({
  selector: 'app-solicitud-pagar-detalle',
  templateUrl: './solicitud-pagar-detalle.component.html'
})
export class SolicitudPagarDetalleComponent implements OnInit {

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

  constructor(public _facturaService: FacturaService) {
  }

  ngOnInit() {

    this.columns1 = [
      {prop: 'idOferta', name: 'Cantidad', headerClass: 'table-header'},
      {prop: 'comments', name: 'Comentarios', headerClass: 'table-header'},
      {prop: 'amount', name: 'Precio unitario', cellTemplate: this.amount_, headerClass: 'table-header'}
    ];
    this.showDetail();
  }

  showDetail() {
    this.loading = true;
    this._facturaService.getInvoiceDetailByNumber(this.idFactura).subscribe(data => {

      this.factura = data;
      this.datosCompania = data.client;
      this.datosTasOn = data.tason;
      this.ofertas = data.offers;

      this.loading = false;
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
