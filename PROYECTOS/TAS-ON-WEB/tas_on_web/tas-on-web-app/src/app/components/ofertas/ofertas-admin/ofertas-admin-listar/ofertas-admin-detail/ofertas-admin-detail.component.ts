import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {OfertaService} from "../../../../../services/oferta.service";
import * as cloneDeep from 'lodash/cloneDeep';
import {appConfig} from "../../../../../app.config";

declare var $: any;

@Component({
  selector: 'app-ofertas-admin-detail',
  templateUrl: './ofertas-admin-detail.component.html'
})
export class OfertasAdminDetailComponent implements OnInit {

  oferta: any;
  ofertaChange: any;
  showData: any;
  appConfig = appConfig;
  //Mensaje de Operacion exitosa o erronea
  operacionCorrecta: boolean = false;
  mensajeOperacion: string = '';
  public loading = false;

  @Input('idOferta') idOferta;
  @Output() complete: EventEmitter<boolean> = new EventEmitter<boolean>();

  constructor(private _activatedRoute: ActivatedRoute, public _ofertaService: OfertaService) {
  }

  ngOnInit() {
    this.loading = true;
    this.oferta = {};
    this.ofertaChange = {};
    this.oferta.offer = {};
    this.ofertaChange.offer = {};
    this.oferta.offer.amount = '';
    this.showData = {};
    this.showData.receive = true;
    //this._activatedRoute.params.subscribe(parameters => {
    this._ofertaService.obtenerOfertaByOfertaId(this.idOferta).subscribe(
      data => {
        this.oferta = data;
        this.ofertaChange = cloneDeep(this.oferta);
        this.checkData(this.oferta.offer.state)
        this.loading = false;
      }, error2 => {
      })
    //});
  }

  updateOferta() {
    if (JSON.stringify(this.oferta).toLowerCase() === JSON.stringify(this.ofertaChange).toLowerCase()) {
      this.operacionCorrecta = true;
      this.mensajeOperacion = 'No se han modificado los datos';
      $("#myModal").modal('show');
    } else {

    }
  }

  checkData(estado) {
    switch (estado) {
      case 31:
        this.showData.receive = false;
        break;
      case 32:
        this.showData.receive = false;
        break;
      case 24:
        this.showData.course = true;
        this.showData.receive = false;
        break;
    }
  }

  cerrarMensaje() {
    this.complete.emit(true);
  }

}
