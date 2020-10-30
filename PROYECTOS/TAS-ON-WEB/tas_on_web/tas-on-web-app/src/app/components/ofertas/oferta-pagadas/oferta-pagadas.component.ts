import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {NotificationsService} from "angular2-notifications";
import {appConfig} from "../../../app.config";
import {OfertaService} from "../../../services/oferta.service";
import {DatatableComponent} from '@swimlane/ngx-datatable/src';
import {AuthService} from "../../../services/auth.service";
import {DatePipe} from "@angular/common";

@Component({
  selector: 'app-oferta-pagadas',
  templateUrl: './oferta-pagadas.component.html',
  styleUrls: ['./oferta-pagadas.component.css']
})
export class OfertaPagadasComponent implements OnInit {
  public loading = false;
  ofertas: any;
  appConfig = appConfig;

  razonSocial: any;
  solicitud: any;
  fechaDesde: any;
  fechaHasta: any;
  factura: any;

  //Para dataTable
  @ViewChild('tmplIndex') tmplIndex: TemplateRef<any>;
  @ViewChild(DatatableComponent) table: DatatableComponent;
  @ViewChild('tmplDate') tmplDate: TemplateRef<any>;
  @ViewChild('tmplTypePay') tmplTypePay: TemplateRef<any>;
  @ViewChild('tmplAmount') tmplAmount: TemplateRef<any>;
  @ViewChild('tmplFiltro') tmplFiltro: TemplateRef<any>;
  rows = [];
  columns = [];
  temp = [];

  constructor(public _oferta: OfertaService, private _notification: NotificationsService, private _authService: AuthService) {
  }

  ngOnInit() {
    this.columns = [
      {name: '#', cellTemplate: this.tmplIndex, headerClass: 'table-header', width: 85},
      {prop: 'supplier', name: 'Compañía de transporte', headerClass: 'table-header', headerTemplate: this.tmplFiltro},
      {prop: 'numeroPrefactura', name: 'No. prefactura', headerClass: 'table-header'},
      {prop: 'invoiceId', name: 'No. factura', headerClass: 'table-header'},
      {prop: 'idSolicitud', name: 'No. solicitud', headerClass: 'table-header'},
      {prop: 'idOferta', name: 'No. oferta', headerClass: 'table-header'},
      {prop: 'origen', name: 'Origen', headerClass: 'table-header'},
      {prop: 'destino', name: 'Destino', headerClass: 'table-header'},
      {prop: 'typePay', name: 'Tipo pago', cellTemplate: this.tmplTypePay, headerClass: 'table-header'},
      {prop: 'amount', name: 'Valor', cellTemplate: this.tmplAmount, headerClass: 'table-header'},
      {prop: 'datePay', name: 'Fecha de pago', cellTemplate: this.tmplDate, headerClass: 'table-header'},
      {prop: 'expiredDay', name: 'Días teoricos de pago', headerClass: 'table-header'},
      {prop: 'date', name: 'Días reales de pago', headerClass: 'table-header'}
    ];
  }

  loadOfferByQuery(query: string) {
    this.loading = true;
    this._oferta.obtenerOfertasBy(query).subscribe(data => {
      this.ofertas = data;
      this.temp = [...data]; // cache our list
      this.rows = data; // push our inital complete list
      this.loading = false;
    }, error => {
      if(error.status == 403) this._authService.logout();
    });
  }

  updateFilter(event) {
    //if (event.target.value.toLowerCase().length == 0)
     // this.loadOffer();

    const val = event.target.value.toLowerCase();

    // filter our data
    const temp = this.temp.filter(function (d) {
      let data = d.supplier != null ? d.supplier : '';
      return data.toLowerCase().indexOf(val) !== -1 || !val;
    });

    // update the rows
    this.rows = temp;
    // Whenever the filter changes, always go back to the first page
    this.table.offset = 0;
  }

  buscarOfertas(){
    let query = "?estado="+appConfig.ofertaEstadoFinalizada;
    query=query.concat("&tipo="+appConfig.idConductorType);
    const pipe = new DatePipe('es-EC');
    if(this.razonSocial) query=query.concat("&razonSocial="+this.razonSocial);
    if(this.solicitud) query=query.concat("&solicitud="+this.solicitud);
    if(this.fechaDesde) query=query.concat("&fechaDesde="+pipe.transform(this.fechaDesde, appConfig.formatoFechaURL));
    if(this.fechaHasta) query=query.concat("&fechaHasta="+pipe.transform(this.fechaHasta, appConfig.formatoFechaURL));
    if(this.factura) query=query.concat("&facturaNro="+this.factura);
    this.loadOfferByQuery(query);
  }

  limpiarDatos(){
    this.razonSocial="";
    this.solicitud="";
    this.factura="";
    this.fechaDesde=null;
    this.fechaHasta=null;
  }
}
