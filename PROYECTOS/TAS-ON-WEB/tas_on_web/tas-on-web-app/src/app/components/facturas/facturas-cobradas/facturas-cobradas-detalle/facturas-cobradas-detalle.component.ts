import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {EmpresaService} from "../../../../services/empresa.service";
import {FacturaService} from "../../../../services/factura.service";
import {appConfig} from "../../../../app.config";

@Component({
  selector: 'app-facturas-cobradas-detalle',
  templateUrl: './facturas-cobradas-detalle.component.html'
})
export class FacturasCobradasDetalleComponent implements OnInit {

  public loading: any = false;
  appConfig = appConfig;

  @Input('invoiceId') invoiceId;
  @Output() complete: EventEmitter<boolean> = new EventEmitter<boolean>();

  datosCompania: any = {};
  datosTasOn: any = {};
  facturaData: any = {};
  totalFactura: any = 0;

  constructor(public _empresaService: EmpresaService, public _facturaService: FacturaService,) {
  }

  ngOnInit() {
    this.loadData();
  }

  loadData() {
    //Datos Factura
    this.loading = true;
    this._facturaService.getInvoiceDetailByNumber(this.invoiceId).subscribe(
      data => {
        this.facturaData = data;
        this.datosCompania = data.client;
        this.datosTasOn = data.tason;
        this.calcAmount();
        this.loading = false;
      }, error => {
      });
  }

  calcAmount() {
    this.totalFactura = 0;
    this.facturaData.offers.forEach(oferta => {
      this.totalFactura = this.totalFactura + (oferta.amount + oferta.comision);
    });
  }


  closeDetail() {
    this.complete.emit(false);
  }
}
