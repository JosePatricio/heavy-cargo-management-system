import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {Router} from "@angular/router";
import {LocalidadService} from "../../../services/localidad.service";
import {DatatableComponent} from "@swimlane/ngx-datatable/src/index";

declare var $: any;

@Component({
  selector: 'app-listar-localidad',
  templateUrl: './listar-localidad.component.html'
})
export class ListarLocalidadComponent implements OnInit {

  public loading: boolean = false;

  idPadre: number = 0;
  idAuxPadre: number[] = [0];
  idx: number = 0;
  localidad: any = {};
  nuevaLocalidad: any = {};

  //Mensaje de Operacion exitosa o erronea
  operacionCorrecta: boolean = false;
  mensajeOperacion: string = '';

//Para dataTable
  @ViewChild(DatatableComponent) table: DatatableComponent;
  @ViewChild('tmplIndex') tmplIndex: TemplateRef<any>;
  @ViewChild('tmplEstado') tmplEstado: TemplateRef<any>;
  @ViewChild('linkButton') linkButton: TemplateRef<any>;
  localidades = [];
  columns = [];

  constructor(private _router: Router, public _localidadService: LocalidadService) {
  }

  ngOnInit() {
    this.columns = [
      {name: '#', cellTemplate: this.tmplIndex, headerClass: 'table-header', width: 85},
      {prop: 'localidadId', name: 'Info.', cellTemplate: this.linkButton, headerClass: 'table-header', width: 115},
      {prop: 'localidadDescripcion', name: 'Descripción', headerClass: 'table-header'},
      {prop: 'localidadEstado', name: 'Estado', cellTemplate: this.tmplEstado, headerClass: 'table-header'}
    ];

    this.loadDataLocale(this.idPadre);
  }

  loadDataLocale(idPadre: number) {
    this.loading = true;
    this._localidadService.getLocalidadByPadre(idPadre).subscribe(data => {
      this.localidades = data;
      this.loading = false;
    });
  }

  verLocalidadByPadre(idPadre: number) {
    this.idPadre = idPadre;
    //Localidad
    if (this.idPadre)
      this.localidadSelec(idPadre);
    this.loadDataLocale(idPadre);
    //Logica de navegación
    this.idAuxPadre.push(idPadre);
    this.idx++;
    this.table.offset = 0;

  }

  localidadSelec(idLocal: number) {
    this.loading = true;
    this._localidadService.getLocalidadById(idLocal).subscribe(data => {
      this.localidad = data;
      this.loading = false;
    });
  }

  regresaLocalidad() {
    this.idAuxPadre.splice(this.idx, 1);
    this.idx--;
    let aux_ = this.idAuxPadre[this.idx];
    this.loadDataLocale(aux_);
    if (aux_)
      this.localidadSelec(aux_);
    this.table.offset = 0;
  }

  creaLocalidad() {
    $("#modNewLocale").modal('show');
  }

  saveLocale() {
    this.loading = true;
    if (this.idx == 0)
      this.nuevaLocalidad.localidadLocalidadPadre = 0;
    else
      this.nuevaLocalidad.localidadLocalidadPadre = this.idPadre;
    this.nuevaLocalidad.localidadEstado = 1;

    this.nuevaLocalidad.localidadDescripcion = this.nuevaLocalidad.localidadDescripcion.trim().toUpperCase();

    this._localidadService.addLocalidad(this.nuevaLocalidad).subscribe(data => {
      this.operacionCorrecta = true;
      this.mensajeOperacion = 'LOCALIDAD creada correctamente';
      this.loading = false;
      $("#modalResult").modal('show');
    }, error => {
      console.log('Error : ', error);
      this.operacionCorrecta = false;
      this.mensajeOperacion = 'LOCALIDAD no pudo ser creada';
      this.loading = false;
      $("#modalResult").modal('show');
    });
  }

  cerrarMensaje() {
    if (this.operacionCorrecta) {
      //GetAllCatalogos
      this.loadDataLocale(this.idPadre);
    }
  }

}
