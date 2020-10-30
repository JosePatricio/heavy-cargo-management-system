import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {appConfig} from "../../../app.config";
import {Router} from "@angular/router";
import {UtilService} from "../../../services/util.service";
import {PublicService} from "../../../services/public.service";
import {DatatableComponent} from "@swimlane/ngx-datatable/src/components/datatable.component";
import {Vehiculo} from "../../../interfaces/vehiculo";
import {Empresa} from "../../../interfaces/empresa";
import {Cliente} from "../../../interfaces/cliente";
import {NgForm} from "@angular/forms";
import Swal from "sweetalert2/dist/sweetalert2.all.min.js";

declare var $: any;

@Component({
  selector: 'app-nuevo-conductor-dependencia-propia',
  templateUrl: './nuevo-conductor-dependencia-propia.component.html',
  styleUrls: ['./nuevo-conductor-dependencia-propia.component.css']
})
export class NuevoConductorDependenciaPropiaComponent implements OnInit {

  appConfig = appConfig;

  public indexEdit: any;
  public loading = false;
  nextStep: number = 0;

  provincias: any;
  idProvincia: any;
  ciudades: any;
  tiposLicencia: any;
  tipoLicenciaConductor: any;

  unidadesCapacidad: any;
  tiposCarga: any;
  tiposCamion: any;

  cliente: Cliente = {};
  empresa: Empresa = {};
  vehiculo: Vehiculo = {};
  vehiculos = [];
  columns = [];
  //Mensaje de Operacion exitosa o erronea
  operacionCorrecta: boolean = false;
  mensajeOperacion: string = '';

  camposValidos: boolean = true;
  deshabilitarRegistro: boolean = false;

  selected = [];
  selectedFirst = [];

  dropdownList = [];
  selectedItems = [];
  dropdownSettings = {};

  idArcsaAlimentos: number = 1;
  idArcsaCosmeticos: number = 2;
  idArcsaMedicamentos: number = 3;
  idBasc: number = 4;
  idPaseInternacional: number = 5;

  @ViewChild('formaVehiculo') formaVehiculo: NgForm;

  //Para dataTable
  @ViewChild(DatatableComponent) table: DatatableComponent;
  @ViewChild('tmplIndex') tmplIndex: TemplateRef<any>;
  @ViewChild('tmplCapacidad') tmplCapacidad: TemplateRef<any>;
  @ViewChild('tmplTipoCamion') tmplTipoCamion: TemplateRef<any>;
  @ViewChild('tmplTipoCarga') tmplTipoCarga: TemplateRef<any>;
  @ViewChild('tmplCertificados') tmplCertificados: TemplateRef<any>;


  constructor(private _router: Router, private _utilService: UtilService, private _publicService: PublicService) {
  }

  ngOnInit() {
    this.columns = [
      {name: '#', cellTemplate: this.tmplIndex, headerClass: 'table-header', width: 85},
      {prop: 'vehiculoPlaca', name: 'Placa', headerClass: 'table-header'},
      {prop: 'vehiculoAnio', name: 'Año', headerClass: 'table-header'},
      {prop: 'vehiculoModelo', name: 'Marca', headerClass: 'table-header'},
      {prop: 'vehiculoCapacidad', name: 'Capacidad', cellTemplate: this.tmplCapacidad, headerClass: 'table-header'},
      {name: 'Certificados', cellTemplate: this.tmplCertificados, headerClass: 'table-header'},
      {prop: 'vehiculoTipoCamion', name: 'Tipo vehículo', cellTemplate: this.tmplTipoCamion, headerClass: 'table-header'},
      {prop: 'vehiculoTipoCarga', name: 'Tipo carga', cellTemplate: this.tmplTipoCarga, headerClass: 'table-header'},
    ];

    this.loadProvincias();
    this.loadTypeLic();
    this.loadUnidadesCapacidad();
    this.loadTiposCarga();
    this.loadTiposCamion();

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

  onSelect({ selected }) {
    if(this.selectedFirst == selected[0]) this.selected = [];
    this.selectedFirst = this.selected[0];
  }

  loadProvincias() {
    this.loading = true;
    this._publicService.getLocalidadByPadre(appConfig.idLocalidadPadreEcuador, 1).subscribe(
      data => {
        this.provincias = data;
        this.loading = false;
      },
      error => {
      }
    );
  }

  loadTypeLic() {
    this.loading = true;
    this._utilService.obtenerCatalogoItem(appConfig.idCatalogoLicencia).subscribe(
      data => {
        this.tiposLicencia = data;
        this.loading = false;
      },
      error => {
        console.log(error);
      }
    );
  }

  loadUnidadesCapacidad() {
    this.loading = true;
    this._utilService.obtenerCatalogoItem(appConfig.idCatalogoPesos).subscribe(
      data => {
        this.unidadesCapacidad = data;
        this.loading = false;
      },
      error => {
        console.log(error);
      }
    );
  }

  loadTiposCarga() {
    this.loading = true;
    this._utilService.obtenerCatalogoItem(appConfig.idCatalogoCarga).subscribe(
      data => {
        this.tiposCarga = data;
        this.loading = false;
      },
      error => {
        console.log(error);
      }
    );
  }

  loadTiposCamion(){
    this.loading = true;
    this._utilService.obtenerCatalogoItem(appConfig.idCatalogoCamion).subscribe(
      data => {
        this.tiposCamion = data;
        this.loading = false;
      },
      error => {
        console.log(error);
      }
    );
  }

  siguiente() {
    this.loading = true;
    this.nextStep++;
    this.loading = false;
  }

  regresar() {
    this.loading = true;
    this.nextStep--;
    this.loading = false;
  }

  confirmRegister(forma: any) {
    this.deshabilitarRegistro = true;
    if (!forma.valid || this.vehiculos.length == 0) {
      this.camposValidos = false;
      this.deshabilitarRegistro = false;
      return;
    }

    var conductorByUsuarioIdDocumento = {
      "conductorApellido": this.cliente.usuarioApellidos,
      "conductorEmail": this.cliente.usuarioMail,
      "conductorNombre": this.cliente.usuarioNombres,
      "conductorTelefono" : this.cliente.usuarioCelular,
      "conductorUsuario" : this.cliente.usuarioIdDocumento,
      "conductorTipoLicencia" : this.tipoLicenciaConductor
    };

    var conductorByUsuarioIdDocumentoList = [];
    conductorByUsuarioIdDocumentoList.push(conductorByUsuarioIdDocumento);

    this.cliente.conductorsByUsuarioIdDocumento = conductorByUsuarioIdDocumentoList;
    this.cliente.usuarioEstado = appConfig.idEstadoUsuarioCreado;
    this.cliente.usuarioPrincipal = true;
    this.cliente.usuarioTipoDocumento = appConfig.idRuc;
    this.cliente.usuarioRuc = this.cliente.usuarioIdDocumento;
    this.cliente.usuarioTipoUsuario = appConfig.idUsuarioConductorAdmin;
    this.cliente.vehiculosByUsuarioIdDocumento = this.vehiculos;
    this.empresa.clienteRazonSocial = this.cliente.usuarioNombres + " " + this.cliente.usuarioApellidos;
    this.empresa.clienteRuc = this.cliente.usuarioIdDocumento;
    this.empresa.clienteTipoCliente = appConfig.idTipoClienteEmpresaTransportistaIndependiente;
    this.empresa.clienteTipoId = appConfig.idPersonaNatural;
    this.empresa.clienteLocalidadId = this.cliente.usuarioLocalidad;
    this.empresa.clienteComision = 0;
    this.empresa.clienteDiasCredito = 0;
    this.empresa.clienteDiasPeriodicidad = 0;

    this.camposValidos = true;

    var objetoAEnviar = {
      "cliente" : this.cliente,
      "empresa" : this.empresa
    };

    this.loading = true;
    this._publicService.createEmpresaCliente(objetoAEnviar).subscribe(data => {
      this.loading = false;
      Swal.fire('', data.responseMessage, (data.response == 'OK')? 'success' : 'error').then((result) => {
        this.cerrarMensaje(data.response == 'OK');
        this.deshabilitarRegistro = data.response == 'OK';
      });
    }, error => {
      this.loading = false;
      this.deshabilitarRegistro = false;
      Swal.fire('', error.message, 'error');
    });
  }

  cerrarMensaje(operacionCorrecta: boolean) {
    if (operacionCorrecta) {
      this._router.navigate(['/login']);
    }
  }

  mostrarCiudades() {
    this.loading = true;
    this._publicService.getLocalidadByPadre(this.idProvincia, 1).subscribe(
      data => {
        this.ciudades = data;
        this.loading = false;
      },
      error => {
      }
    );
  }

  agregarVehiculoPopUp(){
    this.vehiculo = {};
    this.selectedItems = [];
    this.formaVehiculo.reset();
    $("#addCarModal").modal('show');
  }

  agregarVehiculo(){
    this.revisarCertificados();
    this.vehiculos.push(this.vehiculo);
    this.vehiculos = [...this.vehiculos];
    $("#addCarModal").modal('hide');
  }

  eliminarVehiculo(){
    this.vehiculos.forEach( (item, index) => {
      if(item == this.selected[0]) this.vehiculos.splice(index,1);
    });
    this.selected = [];
  }

  consultarDescCapacidad(elementoId: number){
    var capacidad =  this.unidadesCapacidad.filter(x => x.catalogoItemId == elementoId)[0];
    return capacidad.catalogoItemDescripcion;
  }

  consultarDescTipoCamion(elementoId: number){
    var capacidad =  this.tiposCamion.filter(x => x.catalogoItemId == elementoId)[0];
    return capacidad.catalogoItemDescripcion;
  }

  consultarDescTipoCarga(elementoId: number){
    var capacidad =  this.tiposCarga.filter(x => x.catalogoItemId == elementoId)[0];
    return capacidad.catalogoItemDescripcion;
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

}
