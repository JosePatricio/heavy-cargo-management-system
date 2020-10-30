import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {ConductorService} from "../../../services/conductor.service";
import {appConfig} from "../../../app.config";
import {UtilService} from "../../../services/util.service";
import {AuthService} from "../../../services/auth.service";
import { NgForm } from '@angular/forms';
import Swal from "sweetalert2/dist/sweetalert2.all.min.js";

declare var $: any;

@Component({
  selector: 'app-administrar-conductores',
  templateUrl: './administrar-conductores.component.html',
  styleUrls: ['./administrar-conductores.component.css']
})
export class AdministrarConductoresComponent implements OnInit {

  @ViewChild('formaConductor') formaConductor: NgForm;
  public loading = false;

  //Notifications
  options = {
    position: ["top", "right"],
    timeOut: 5000,
    showProgressBar: true,
    pauseOnHover: true,
    clickToClose: true,
    maxLength: 10
  };

  constructor(public _conductorService:ConductorService, public _utilService:UtilService,
              public _authService: AuthService) { }

  conductores: any;
  tiposLicencia: any;
  conductor: any = {};
  columns = [];
  appConfig = appConfig;
  yaTieneCedula: boolean = false;

  @ViewChild('tmplIndex') tmplIndex: TemplateRef<any>;
  @ViewChild('tmplTipoLicencia') tmplTipoLicencia: TemplateRef<any>;
  @ViewChild('tmplNombres') tmplNombres: TemplateRef<any>;
  @ViewChild('linkButton') linkButton: TemplateRef<any>;

  ngOnInit() {
    this.columns = [
      {name: '#', cellTemplate: this.tmplIndex, headerClass: 'table-header', width: 85},
      {name: 'Editar', cellTemplate: this.linkButton, headerClass: 'table-header', width: 115},
      {name: 'Nombre', cellTemplate: this.tmplNombres, headerClass: 'table-header'},
      {prop: 'conductorCedula', name: 'Cédula', headerClass: 'table-header'},
      {prop: 'conductorTipoLicencia', cellTemplate: this.tmplTipoLicencia, name: 'Licencia', headerClass: 'table-header'},
      {prop: 'conductorTelefono', name: 'Teléfono', headerClass: 'table-header'},
      {prop: 'conductorEmail', name: 'Correo', headerClass: 'table-header'},
    ];
    if(this._authService.getTypeUser()==appConfig.idUsuarioEnviosAdmin) {
      this.columns.splice(2,0,
        {prop: 'conductorId', name: 'ID', headerClass: 'table-header', width: 85},
      );
    }
    this.consultarTiposLicencia();
  }

  loadData(){
    this.loading = true;
    this._conductorService.getMisConductores().subscribe(
      data => {
        this.conductores = data;
        this.loading = false;
      },
      error => {
        this.loading = false;
        if(error.status == 403) this._authService.logout();
      });
  };

  consultarTiposLicencia(){
    this.loading = true;
    this._utilService.obtenerCatalogoItem(appConfig.idCatalogoLicencia).subscribe(
      data => {
        this.tiposLicencia = data;
        this.loading = false;
        this.loadData();
      },
      error => {
        this.loading = false;
        if(error.status == 403) this._authService.logout();
      }
    );
  }

  consultarDescTipoLicencia(elementoId: number){
    var tipoLicencia =  this.tiposLicencia.filter(x => x.catalogoItemId == elementoId)[0];
    return tipoLicencia.catalogoItemDescripcion;
  }

  concatenarTexto(texto1:string, texto2:string){
    try{
      return texto1.concat(" ").concat(texto2).trim();
    } catch (e) {
      return texto1;
    }
  }

  editarConductor(conductor: any){
    this.conductor = Object.assign({}, conductor);
    this.yaTieneCedula = conductor.conductorCedula;
    this.mostrarModal();
  }

  nuevoConductor(){
    this.formaConductor.reset();
    this.conductor = {};
    this.yaTieneCedula = false;
    this.mostrarModal();
  }

  guardarConductor(){
    this.loading = true;
    this._conductorService.addConductorCamion(this.conductor).subscribe(data => {
      this.loading = false;
      Swal.fire('', data.responseMessage, (data.response == 'OK')? 'success' : 'error').then((result) => {
        this.formaConductor.reset();
        this.loadData();
      });
    }, error => {
      this.loading = false;
      if(error.status == 403) this._authService.logout();
      Swal.fire('', error.message, 'error');
    });
  }

  actualizarConductor(){
    this.loading = true;
    this._conductorService.updateConductorCamion(this.conductor).subscribe(data => {
      this.loading = false;
      Swal.fire('', data.responseMessage, (data.response == 'OK')? 'success' : 'error').then((result) => {
        this.formaConductor.reset();
        this.loadData();
      });
    }, error => {
      this.loading = false;
      if(error.status == 403) this._authService.logout();
      Swal.fire('', error.message, 'error');
      this.loadData();
    });

  }

  cerrarMensaje(){
    this.formaConductor.reset();
    this.ocultarModal();
  }

  mostrarModal(){
    $("#modNewConductor").modal('show');
  }

  ocultarModal(){
    $("#modNewConductor").modal('hide');
  }

}
