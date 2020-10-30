import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {appConfig} from "../../../../app.config";
import {Router} from "@angular/router";
import {OfertaService} from "../../../../services/oferta.service";
import {DatatableComponent} from "@swimlane/ngx-datatable/src/index";
import {AuthService} from "../../../../services/auth.service";

@Component({
  selector: 'app-oferta-finalizada-listar',
  templateUrl: './oferta-finalizada-listar.component.html',
  styleUrls: ['./oferta-finalizada-listar.component.css']
})
export class OfertaFinalizadaListarComponent implements OnInit {

  public loading = false;
  appConfig = appConfig;

  //Principal data table
  @ViewChild(DatatableComponent) table: DatatableComponent;
  @ViewChild('ordinal') ordinal: TemplateRef<any>;
  @ViewChild('tmplDate') tmplDate: TemplateRef<any>;
  @ViewChild('tmplAmount') tmplAmount: TemplateRef<any>;
  @ViewChild('linkButton') linkButton: TemplateRef<any>;
  ofertas = [];
  columns = [];

  constructor(public _router: Router, public _oferta: OfertaService, private _authService: AuthService) {
  }

  ngOnInit() {
    this.loading = true;
    this._oferta.obtenerOfertasByEstado(appConfig.ofertaEstadoFinalizada).subscribe(
      data => {
        this.ofertas = data;
        this.loading = false;
      },
      error => {
        this.loading = false;
        if(error.status == 403) this._authService.logout();
      }
    );

    this.columns = [
      {prop: 'idSolicitud', name: '#', width: 85, cellTemplate: this.ordinal, headerClass: 'table-header'},
      {prop: 'idSolicitud', name: 'Info.', cellTemplate: this.linkButton, headerClass: 'table-header'},
      {prop: 'idSolicitud', name: 'No. solicitud', headerClass: 'table-header'},
      {prop: 'fechaEntrega', name: 'Fecha entrega mercadería', cellTemplate: this.tmplDate, headerClass: 'table-header'},
      {prop: 'origen', name: 'Origen', headerClass: 'table-header'},
      {prop: 'destino', name: 'Destino', headerClass: 'table-header'},
      {prop: 'amount', name: 'Valor', cellTemplate: this.tmplAmount, headerClass: 'table-header'},
      {prop: 'datePay', name: 'Fecha pago', cellTemplate: this.tmplDate, headerClass: 'table-header'}
    ];
  }

  callDetailSolicitud(solicitud: string) {
    this._router.navigate(['/panel/ofertas/oferta-finalizada-detail', solicitud]);
  }

}
