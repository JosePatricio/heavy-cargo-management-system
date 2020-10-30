import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {OfertaService} from "../../../services/oferta.service";
import {appConfig} from "../../../app.config";
import {NotificationsService} from "angular2-notifications";
import {AuthService} from "../../../services/auth.service";

@Component({
  selector: 'app-oferta-listas-pago',
  templateUrl: './oferta-listas-pago.component.html'
})
export class OfertaListasPagoComponent implements OnInit {
  public loading = false;

  appConfig = appConfig;

  selOfert: any[] = [];

  //Para dataTable
  @ViewChild('tmplIndex') tmplIndex: TemplateRef<any>;
  @ViewChild('tmplTypePay') tmplTypePay: TemplateRef<any>;
  @ViewChild('tmplAmount') tmplAmount: TemplateRef<any>;
  @ViewChild('tmplDiscount') tmplDiscount: TemplateRef<any>;
  @ViewChild('tmplDate') tmplDate: TemplateRef<any>;
  columns = [];
  ofertas: any;

  constructor(public _oferta: OfertaService, private _notification: NotificationsService, private _authService: AuthService) {
  }

  ngOnInit() {
    this.columns = [
      {name: '#', cellTemplate: this.tmplIndex, headerClass: 'table-header', width: 85},
      {
        sortable: 'false', canAutoResize: 'false', draggable: 'false', resizeable: 'false',
        headerCheckboxable: 'true', checkboxable: 'true', width: '70'
      },
      {prop: 'numeroPrefactura', name: 'No. prefactura', headerClass: 'table-header'},
      {prop: 'invoiceId', name: 'No. factura', headerClass: 'table-header'},
      {prop: 'idSolicitud', name: 'No. solicitud', headerClass: 'table-header'},
      {prop: 'idOferta', name: 'No. oferta', headerClass: 'table-header'},
      {prop: 'supplier', name: 'Nombre proveedor', headerClass: 'table-header'},
      {prop: 'typePay', name: 'Tipo pago', cellTemplate: this.tmplTypePay, headerClass: 'table-header'},
      {prop: 'amount', name: 'Valor total', cellTemplate: this.tmplAmount, headerClass: 'table-header'},
      {prop: 'datePay', name: 'Fecha de pago', cellTemplate: this.tmplDate, headerClass: 'table-header'},
      {prop: 'expiredDay', name: 'Días vencidos', headerClass: 'table-header'},
    ];
    this.loadOffer();
  }

  loadOffer() {
    this.loading = true;
    this._oferta.obtenerAllOfertasByEstado(appConfig.ofertaEstadoListasPago).subscribe(data => {
      this.ofertas = data;
      this.loading = false;
    }, error => {
      if(error.status == 403) this._authService.logout();
    });
  }

  getOffers() {
    var offerts: any[] = [];
    this.selOfert.forEach(oferta => {
      var offer: any = {
        idSolicitud: null,
        amount: null,
        comments: null,
        date: null,
        idOferta: oferta.idOferta,
        state: null,
        idConductor: null,
        idVehiculo: null,
        conductor: null,
      };
      offerts.push(offer);
    });
    return offerts;
  }

  generaCashManagement() {
    var popup = window.open();
    popup.document.write('Generando archivo ...');

    this.loading = true;
    this._oferta.generateCashFile(this.getOffers()).subscribe(data => {
      this._notification.success('Operación exitosa');
      const url = window.URL.createObjectURL(data);
      var anchor = document.createElement("a");
      anchor.download = "cashManagement.xlsx";
      anchor.href = url;
      anchor.click();
      popup.location.href = url;
      this.loading = false;
      this.loadOffer();
      popup.close();
    }, error => {
      this._notification.warn('Operación erronea: \n' + error.message + '...');
      this.loading = false;
    });
    this.selOfert = [];
  }

  onSelect({selected}) {
    this.selOfert.splice(0, this.selOfert.length);
    this.selOfert.push(...selected);
  }

}
