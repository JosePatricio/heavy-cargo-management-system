import {Component, OnInit} from '@angular/core';
import {FacturaService} from "../../../services/factura.service";
import {AuthService} from "../../../services/auth.service";
import {appConfig} from "../../../app.config";
import {EbillingService} from "../../../services/ebilling.service";
import {NotificationsService} from "angular2-notifications";

declare var $: any;

@Component({
  selector: 'app-facturas-manuales',
  templateUrl: './facturas-manuales.component.html'
})
export class FacturasManualesComponent implements OnInit {
  appConfig = appConfig;
  bloquearBotonGenerarFactura: boolean = false;
  loading = false;
  detalleList:Array<any> = new Array<any>();
  detalle: any = {};

  facturaManual: any = {
    infoFactura: {},
    detalles: {}
  };

  constructor(private _facturaService: FacturaService, private _authService: AuthService,
              private _ebillingService: EbillingService, private _notification: NotificationsService) {
  }

  ngOnInit() {
  }

  generarFactura(){
    this.bloquearBotonGenerarFactura = true;
    this.detalleList.push(this.detalle);
    this.facturaManual.detalles.detalle = this.detalleList;
    this.facturaManual.infoFactura.direccionComprador = this.facturaManual.infoFactura.dirEstablecimiento;
    console.log(this.facturaManual);
    this.loading = true;
    this._facturaService.generateManualInvoice(this.facturaManual).subscribe(data => {
      console.log(data);
      this.loading = false;
      this._notification.success(data.responseMessage);
      this.bloquearBotonGenerarFactura = false;
      this.limpiarCampos();
    }, error => {
      if(error.status == 403) this._authService.logout();
      this.loading = false;
      this._notification.warn(error.message);
      this.bloquearBotonGenerarFactura = false;
    });
  }

  buscarComprador(){
    if(this.facturaManual.infoFactura.identificacionComprador && this.facturaManual.infoFactura.tipoIdentificacionComprador=='04'
    && this.facturaManual.infoFactura.identificacionComprador.length == 13){
      this.consultarAdquiriente(this.facturaManual.infoFactura.identificacionComprador);
    }
    if(this.facturaManual.infoFactura.identificacionComprador && this.facturaManual.infoFactura.tipoIdentificacionComprador=='05'
      && this.facturaManual.infoFactura.identificacionComprador.length == 10){
      this.consultarAdquiriente(this.facturaManual.infoFactura.identificacionComprador);
    }
  }

  consultarAdquiriente(numeroDocumento){
    this.loading = true;
    this._ebillingService.getAdquiriente(numeroDocumento).subscribe(
      data => {
        this.loading = false;
        if(data) {
          this.facturaManual.infoFactura.dirEstablecimiento = data.adquirienteDireccion;
          this.facturaManual.infoFactura.razonSocialComprador = data.adquirienteRazonSocial;
        }
      },
      error => {
        if(error.status == 403) this._authService.logout();
        this.loading = false;
      }
    );
  }

  limpiarCampos(){
    this.facturaManual = {
      infoFactura: {},
      detalles : {}
    };
    this.detalle = {};
    this.detalleList = new Array<any>();
  }

}
