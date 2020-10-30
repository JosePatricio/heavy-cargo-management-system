import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {Router} from '@angular/router';
import {OfertaService} from '../../../services/oferta.service';
import {SolicitudEnvioService} from '../../../services/solicitud-envio.service';
import {AuthService} from "../../../services/auth.service";

@Component({
  selector: 'app-realizar-oferta',
  templateUrl: './realizar-oferta.component.html',
  styleUrls: ['./realizar-oferta.component.css']
})
export class RealizarOfertaComponent implements OnInit {

  public loading = false;

  /**Table**/
  @ViewChild('tmplIndex') tmplIndex: TemplateRef<any>;
  @ViewChild('tmpPeso') tmpPeso: TemplateRef<any>;
  @ViewChild('linkButton') linkButton: TemplateRef<any>;
  @ViewChild('tmpDiaPago') tmpDiaPago: TemplateRef<any>;
  columns = [];
  ofertas: any = [];

  constructor(private _router: Router, public _ofertaService: OfertaService, private _authService: AuthService,
              public _solicitudEnvioService: SolicitudEnvioService) {
  }

  ngOnInit() {
    this.columns = [
      {name: '#', cellTemplate: this.tmplIndex, headerClass: 'table-header', width: 85},
      {name: 'Info.', cellTemplate: this.linkButton, headerClass: 'table-header', width: 115},
      {prop: 'idSolicitud', name: 'Número solicitud', headerClass: 'table-header'},
      {prop: 'origen', name: 'Origen', headerClass: 'table-header'},
      {prop: 'destino', name: 'Destino', headerClass: 'table-header'},
      {prop: 'peso', name: 'Peso', headerClass: 'table-header'},
      {prop: 'diasPagos', name: 'Días de pagos', cellTemplate: this.tmpDiaPago, headerClass: 'table-header'}
    ];
    this.loadData();
  }

  loadData() {
    this.loading = true;
    this._ofertaService.getSolicitudEnvio().subscribe(
      data => {
        this.ofertas = data;
        this.loading = false;
      },
      error => {
        this.loading = false;
        if(error.status == 403) this._authService.logout();
      });
  }

  ofertar(idSolucitud: string, index: string) {
    this._router.navigate(['/panel/ofertas/nueva', idSolucitud, index]);
  }

}
