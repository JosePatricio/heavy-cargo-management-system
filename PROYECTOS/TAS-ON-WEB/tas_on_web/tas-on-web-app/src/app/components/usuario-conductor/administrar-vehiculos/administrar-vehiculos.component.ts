import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {ConductorService} from "../../../services/conductor.service";
import {UtilService} from "../../../services/util.service";
import {AuthService} from "../../../services/auth.service";
import {appConfig} from "../../../app.config";
import {NgForm} from "@angular/forms";
import Swal from "sweetalert2/dist/sweetalert2.all.min.js";

declare var $: any;

@Component({
  selector: 'app-administrar-vehiculos',
  templateUrl: './administrar-vehiculos.component.html',
  styleUrls: ['./administrar-vehiculos.component.css']
})
export class AdministrarVehiculosComponent implements OnInit {

  public loading = false;
  vehiculos: any;
  vehiculo: any = {};
  unidadesCapacidad: any;
  tiposCarga: any;
  tiposCamion: any;
  columns = [];
  appConfig = appConfig;
  dropdownList = [];
  selectedItems = [];
  dropdownSettings = {};
  idArcsaAlimentos: number = 1;
  idArcsaCosmeticos: number = 2;
  idArcsaMedicamentos: number = 3;
  idBasc: number = 4;
  idPaseInternacional: number = 5;

  constructor(public _conductorService:ConductorService, public _utilService:UtilService,
              private _authService: AuthService) { }

  @ViewChild('formaVehiculo') formaVehiculo: NgForm;


  @ViewChild('tmplIndex') tmplIndex: TemplateRef<any>;
  @ViewChild('linkButton') linkButton: TemplateRef<any>;
  @ViewChild('tmplCapacidad') tmplCapacidad: TemplateRef<any>;
  @ViewChild('tmplTrueFalse') tmplTrueFalse: TemplateRef<any>;
  @ViewChild('tmplTipoCamion') tmplTipoCamion: TemplateRef<any>;
  @ViewChild('tmplTipoCarga') tmplTipoCarga: TemplateRef<any>;
  @ViewChild('tmplCertificados') tmplCertificados: TemplateRef<any>;

  ngOnInit() {
    this.columns = [
      {name: '#', cellTemplate: this.tmplIndex, headerClass: 'table-header', width: 85},
      {name: 'Editar', cellTemplate: this.linkButton, headerClass: 'table-header', width: 115},
      {prop: 'vehiculoPlaca', name: 'Placa', headerClass: 'table-header'},
      {prop: 'vehiculoModelo', name: 'Modelo', headerClass: 'table-header'},
      {prop: 'vehiculoAnio', name:'Año', headerClass: 'table-header'},
      {name: 'Capacidad', cellTemplate: this.tmplCapacidad, headerClass: 'table-header'},
      {name: 'Certificados', cellTemplate: this.tmplCertificados, headerClass: 'table-header'},
      {prop: 'vehiculoTipoCamion', cellTemplate: this.tmplTipoCamion, name: 'Tipo camión', headerClass: 'table-header'},
      {prop: 'vehiculoTipoCarga', name: 'Tipo carga', cellTemplate: this.tmplTipoCarga, headerClass: 'table-header'},
    ];
    if(this._authService.getTypeUser()==appConfig.idUsuarioEnviosAdmin) {
      this.columns.splice(2,0,
        {prop: 'vehiculoId', name: 'ID', headerClass: 'table-header', width: 85},
      );
    }
    this.loadUnidadesCapacidad();
    this.loadTiposCamion();
    this.loadTiposCarga();
    this.loadVehiculos();

    this.dropdownList = [
      {"id":this.idArcsaAlimentos,"itemName":"ARCSA alimentos"},
      {"id":this.idArcsaCosmeticos,"itemName":"ARCSA cosméticos"},
      {"id":this.idArcsaMedicamentos,"itemName":"ARCSA medicamentos"},
      {"id":this.idBasc,"itemName":"BASC"},
      {"id":this.idPaseInternacional,"itemName":"Pase internacional"}
    ];
    this.selectedItems = [];
    this.dropdownSettings = {
      singleSelection: false,
      text:"Ninguno",
      selectAllText:'Seleccionar todos',
      unSelectAllText:'Quitar todos',
      enableSearchFilter: false,
      classes:""
    };
  }

  loadVehiculos(){
    this.loading = true;
    this._conductorService.getMisVehiculos().subscribe(
      data => {
        this.vehiculos = data;
        this.loading = false;
      },
      error => {
        this.loading = false;
        if(error.status == 403) this._authService.logout();
      });
  }

  loadUnidadesCapacidad() {
    this._utilService.obtenerCatalogoItem(appConfig.idCatalogoPesos).subscribe(
      data => {
        this.unidadesCapacidad = data;
      },
      error => { }
    );
  }

  loadTiposCarga() {
    this._utilService.obtenerCatalogoItem(appConfig.idCatalogoCarga).subscribe(
      data => {
        this.tiposCarga = data;
      },
      error => { }
    );
  }

  loadTiposCamion(){
    this._utilService.obtenerCatalogoItem(appConfig.idCatalogoCamion).subscribe(
      data => {
        this.tiposCamion = data;
      },
      error => { }
    );
  }

  consultarDescCapacidad(elementoId: number){
    try{
      var capacidad =  this.unidadesCapacidad.filter(x => x.catalogoItemId == elementoId)[0];
      return capacidad.catalogoItemDescripcion;
    }catch (e) {
      return "";
    }
  }

  consultarDescTipoCamion(elementoId: number){
    try{
      var capacidad =  this.tiposCamion.filter(x => x.catalogoItemId == elementoId)[0];
      return capacidad.catalogoItemDescripcion;
    }catch (e) {
      return "";
    }

  }

  consultarDescTipoCarga(elementoId: number){
    try{
      var capacidad =  this.tiposCarga.filter(x => x.catalogoItemId == elementoId)[0];
      return capacidad.catalogoItemDescripcion;
    }catch (e) {
      return "";
    }

  }

  editarVehiculo(vehiculo: any){
    this.selectedItems = [];
    this.vehiculo = Object.assign({}, vehiculo);
    if(this.vehiculo.vehiculoArcsaAlimentos) this.selectedItems.push(this.dropdownList.find(x => x.id == this.idArcsaAlimentos));
    if(this.vehiculo.vehiculoArcsaCosmeticos) this.selectedItems.push(this.dropdownList.find(x => x.id == this.idArcsaCosmeticos));
    if(this.vehiculo.vehiculoArcsaMedicamentos) this.selectedItems.push(this.dropdownList.find(x => x.id == this.idArcsaMedicamentos));
    if(this.vehiculo.vehiculoBasc) this.selectedItems.push(this.dropdownList.find(x => x.id == this.idBasc));
    if(this.vehiculo.vehiculoPaseInternacional) this.selectedItems.push(this.dropdownList.find(x => x.id == this.idPaseInternacional));
    this.mostrarModal();
  }

  nuevoVehiculo(){
    this.formaVehiculo.reset();
    this.vehiculo = {};
    this.mostrarModal();
  }

  cerrarModal(){
    this.formaVehiculo.reset();
    this.ocultarModal();
  }


  mostrarModal(){
    $("#modNewVehiculo").modal('show');
  }

  ocultarModal(){
    $("#modNewVehiculo").modal('hide');
  }

  actualizarVehiculo() {
    this.loading=true;
    this.revisarCertificados();
    this._conductorService.updateVehiculo(this.vehiculo).subscribe(data => {
      this.loading=false;
      Swal.fire('', data.responseMessage, (data.response == 'OK')? 'success' : 'error').then((result) => {
        this.formaVehiculo.reset();
        this.loadVehiculos();
      });
    }, error => {
      this.loading = false;
      if(error.status == 403) this._authService.logout();
      Swal.fire('', error.message, 'error');
      this.loadVehiculos();
    });
  }

  revisarCertificados(){
    this.vehiculo.vehiculoArcsaAlimentos = this.selectedItems && !!this.selectedItems.find(x => x.id == this.idArcsaAlimentos);
    this.vehiculo.vehiculoArcsaCosmeticos = this.selectedItems && !!this.selectedItems.find(x => x.id == this.idArcsaCosmeticos);
    this.vehiculo.vehiculoArcsaMedicamentos = this.selectedItems && !!this.selectedItems.find(x => x.id == this.idArcsaMedicamentos);
    this.vehiculo.vehiculoBasc = this.selectedItems && !!this.selectedItems.find(x => x.id == this.idBasc);
    this.vehiculo.vehiculoPaseInternacional = this.selectedItems && !!this.selectedItems.find(x => x.id == this.idPaseInternacional);
  }

  consultarCertificados(vehiculo: any):string{
    let certificados: string = "";
    if(vehiculo.vehiculoArcsaAlimentos) certificados = certificados.concat("ARCSA alimentos, ");
    if(vehiculo.vehiculoArcsaCosmeticos) certificados = certificados.concat("ARCSA cosmeticos, ");
    if(vehiculo.vehiculoArcsaMedicamentos) certificados = certificados.concat("ARCSA medicamentos, ");
    if(vehiculo.vehiculoBasc) certificados = certificados.concat("BASC, ");
    if(vehiculo.vehiculoPaseInternacional) certificados = certificados.concat("Pase internacional, ");
    var re = /, $/;
    certificados = certificados.replace(re, "");
    return certificados.trim();
  }

  guardarVehiculo(){
    this.loading=true;
    this.revisarCertificados();
    this._conductorService.addVehiculo(this.vehiculo).subscribe(data => {
      this.loading=false;
      Swal.fire('', data.responseMessage, (data.response == 'OK')? 'success' : 'error').then((result) => {
        this.formaVehiculo.reset();
        this.loadVehiculos();
      });
    }, error => {
      this.loading = false;
      if(error.status == 403) this._authService.logout();
      Swal.fire('', error.message, 'error');
      this.loadVehiculos();
    });
  }

  onItemSelect(item:any){  }
  OnItemDeSelect(item:any){  }
  onSelectAll(items: any){  }
  onDeSelectAll(items: any){  }

}
