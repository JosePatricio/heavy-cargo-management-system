import {Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {appConfig} from '../../../../app.config';
import {ActivatedRoute, Router} from '@angular/router';
import {UtilService} from '../../../../services/util.service';
import {ConductorService} from '../../../../services/conductor.service';
import {Chofer} from '../../../../interfaces/chofer';
import {Vehiculo} from '../../../../interfaces/vehiculo';
import {CatalogoItemService} from "../../../../services/catalogo-item.service";

import {IMyDpOptions} from 'mydatepicker';
import 'rxjs/Rx';
import {AuthService} from "../../../../services/auth.service";
import {NgForm} from "@angular/forms";

declare var $: any;

@Component({
  selector: 'app-nuevo-conductor',
  templateUrl: './nuevo-conductor.component.html',
  styleUrls: ['./nuevo-conductor.component.css']
})
export class NuevoConductorComponent implements OnInit {
  appConfig = appConfig;
  public myDatePickerOptions: IMyDpOptions = {
    // other options...
    dateFormat: appConfig.formatoFechaDatePicker
  };

  public loading = false;

  tiposDocumento: any;
  tiposCamion: any;
  tiposLicencia: any;
  tiposCarga: any;
  tipoUnidades: any;
  provincias: any;
  provinciaSeleccionada: string = '';
  cantones: any;
  conductor: any;
  choferes: Chofer;
  conductorsByUsuarioIdDocumento: any[] = [];
  vehiculo: Vehiculo;
  vehiculosByUsuarioIdDocumento: any[] = [];
  //Mensaje de Operacion exitosa o erronea
  operacionCorrecta: boolean = false;
  mensajeOperacion: string = '';
  temporal: any;
  clienteCombo: any;
  empresa: any;
  empresaClienteSeleccionada: string = '';
  band: string;
  bandact: string;
  index: any;

  dropdownList = [];
  selectedItems = [];
  dropdownSettings = {};

  idArcsaAlimentos: number = 1;
  idArcsaCosmeticos: number = 2;
  idArcsaMedicamentos: number = 3;
  idBasc: number = 4;
  idPaseInternacional: number = 5;

  @Input('idConductor') idConductor;
  @Input('indexEdit') indexEdit;
  @Output() complete: EventEmitter<boolean> = new EventEmitter<boolean>();

  @ViewChild('formaVehiculo') formaVehiculo: NgForm;

  constructor(private _router: Router, private _activatedRoute: ActivatedRoute,
              public _conductorService: ConductorService, private _utilService: UtilService,
              public _catalogoItemService: CatalogoItemService, private _authService: AuthService) {
    this.cleanUpConductor();
    this.cleanUpChofer();
    this.cleanUpVehiculo();
    this.cleanUpTemporal();

    this.loadCatalog();
  }

  ngOnInit() {
    if (this.indexEdit === 1) {
      this.band = "nuevo";
      this._conductorService.getClientebyTipoCliente(appConfig.idTipoClienteEmpresaTransportista).subscribe(data => {
        this.clienteCombo = data;
      });
    }
    else {
      this.loadData(this.idConductor);
    }
    this.conductor.conductorsByUsuarioIdDocumento = [];

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

  loadCatalog() {
    this.loading = true;
    this._utilService.obtenerCatalogoItem(appConfig.idCatalogoDocumento).subscribe(
      data => {
        this.tiposDocumento = data;
        this.loading = false;
      },
      error => {
        if(error.status == 403) this._authService.logout();
        console.log(error);
      }
    );
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
    this.loading = true;
    this._utilService.obtenerCatalogoItem(appConfig.idCatalogoCamion).subscribe(
      data => {
        this.tiposCamion = data;
        this.loading = false;
      },
      error => {
        if(error.status == 403) this._authService.logout();
        console.log(error);
      }
    );
    this.loading = true;
    this._utilService.obtenerCatalogoItem(appConfig.idCatalogoPesos).subscribe(
      data => {
        this.tipoUnidades = data;
        this.loading = false;
      },
      error => {
        console.log(error);
      }
    );
    this.loading = true;
    this._utilService.obtenerLocalidad(appConfig.idLocalidadPadreEcuador).subscribe(
      data => {
        this.provincias = data;
        this.loading = false;
      },
      error => {
        if(error.status == 403) this._authService.logout();
        console.log(error);
      }
    );
  }

  loadData(idConductor: any) {
    this.loading = true;
    this._conductorService.getClienteConductor(idConductor).subscribe(data => {
      this.conductor = data;
      this.conductorsByUsuarioIdDocumento = this.conductor.conductorsByUsuarioIdDocumento;
      this.vehiculosByUsuarioIdDocumento = this.conductor.vehiculosByUsuarioIdDocumento;
      this.band = "actualiza";
      this.loading = false;

      this.loading = true;
      this._conductorService.getClientebyTipoCliente(appConfig.idTipoClienteEmpresaTransportista).subscribe(data => {
        this.clienteCombo = data;
        this.empresaClienteSeleccionada = this.conductor.usuarioRuc;
        this.cargarDatosCliente();
        this.loading = false;
        this.loading = true;
        this._utilService.obtenerLocalidadById(this.conductor.usuarioLocalidad).subscribe(
          data => {
            this.provinciaSeleccionada = data.localidadLocalidadPadre;
            if (this.provinciaSeleccionada) {
              this._utilService.obtenerLocalidad(this.provinciaSeleccionada).subscribe(
                data => {
                  this.cantones = data;
                },
                error => {
                  console.log(error);
                }
              );
            }
            this.loading = false;
          },
          error => {
            console.log(error);
          }
        );
      });
    }, error => {
      console.log(error);
    });
  }

  cargarConductoresVehiculos(){
    this.loading = true;
    this._conductorService.getClienteConductor(this.idConductor).subscribe(data => {
      this.conductor = data;
      this.conductorsByUsuarioIdDocumento = this.conductor.conductorsByUsuarioIdDocumento;
      this.vehiculosByUsuarioIdDocumento = this.conductor.vehiculosByUsuarioIdDocumento;
      this.loading = false;
    }, error => {
      this.loading = false;
      if(error.status == 403) this._authService.logout();
    });
    this.conductor.conductorsByUsuarioIdDocumento = [];
  }

  cargarCantones() {
    if (this.provinciaSeleccionada) {
      this._utilService.obtenerLocalidad(this.provinciaSeleccionada).subscribe(
        data => {
          this.cantones = data;
        },
        error => {
          console.log(error);
        }
      );
    }
  }

  agregarChofer() {
    this.choferes.conductorUsuario = this.conductor.usuarioIdDocumento;
    this.choferes.conductorId = null;
    this.conductor.conductorsByUsuarioIdDocumento = this.conductorsByUsuarioIdDocumento;
    if (this.band == "actualiza") {
      this.loading = true;
      this._conductorService.addConductorCamion(this.choferes).subscribe(data => {
        $("#modNewItem").modal('hide');
        this.operacionCorrecta = true;
        this.mensajeOperacion = 'Conductor ingresado correctamente';
        $("#myModal").modal('show');
        this.loading = false;
        this.cargarConductoresVehiculos();
      }, error => {
        console.log('Error : ', error);
        this.operacionCorrecta = false;
        this.mensajeOperacion = 'El conductor no pudo ser ingresado';
        $("#myModal").modal('show');
        this.loading = false;
      });
    }
    this.conductorsByUsuarioIdDocumento.push(this.choferes);
    this.cleanUpChofer();
  }

  agregarVehiculo() {
    this.vehiculo.vehiculoId = null;
    this.vehiculo.vehiculoUsuario = this.conductor.usuarioIdDocumento;
    this.conductor.vehiculosByUsuarioIdDocumento = this.vehiculosByUsuarioIdDocumento;
    if (!this.vehiculo.vehiculoArcsaAlimentos) {
      this.vehiculo.vehiculoArcsaAlimentos = "false";
    }
    if (this.band == "actualiza") {
      this.loading=true;
      this._conductorService.addVehiculo(this.vehiculo).subscribe(data => {
        this.operacionCorrecta = true;
        this.mensajeOperacion = 'Vehiculo ingresado correctamente';
        this.loading=false;
        this.cargarConductoresVehiculos();
      }, error => {
        console.log('Error : ', error);
        if(error.status == 403) this._authService.logout();
        this.operacionCorrecta = false;
        this.mensajeOperacion = 'El vehiculo no pudo ser ingresado';
        $("#myModal").modal('show');
        this.loading=false;
        this.cargarConductoresVehiculos();
      });
    }
    this.vehiculosByUsuarioIdDocumento.push(this.vehiculo);
    this.cleanUpVehiculo();
  }

  agregarConductor() {
    $("#modNewConductor").modal('hide');
    this.loading = true;
    this.conductor.usuarioTipoUsuario = appConfig.idConductorType;
    this.conductor.usuarioEstado = appConfig.idEstadoUsuarioCreado;
    this._conductorService.addConductor(this.conductor).subscribe(data => {
      this.loading = false;
      this.operacionCorrecta = true;
      this.mensajeOperacion = 'Conductor creado correctamente';
      $("#myModal").modal('show');
      this.cargarConductoresVehiculos();
    }, error => {
      this.loading = false;
      if(error.status == 403) this._authService.logout();
      this.operacionCorrecta = false;
      this.mensajeOperacion = 'El conductor no pudo ser creado';
      $("#myModal").modal('show');
      this.cargarConductoresVehiculos();
    });
  }

  editarConductor() {
    this.operacionCorrecta = false;
    this.mensajeOperacion = 'El conductor no pudo ser actualizado';
    $("#myModal").modal('show');
  }

  private cleanUpChofer() {
    this.choferes = {
      "conductorId": "",
      "conductorNombre": "",
      "conductorEmail": "",
      "conductorUsuario": "",
      "conductorApellido": "",
      "conductorTipoLicencia": "",
      "conductorTelefono": "",
      "conductorEstado": "",
      "conductorCedula": null
    };
  }

  private cleanUpVehiculo() {
    this.vehiculo = {
      "vehiculoId": "",
      "vehiculoUsuario": "",
      "vehiculoModelo": "",
      "vehiculoAnio": "",
      "vehiculoPlaca": "",
      "vehiculoTipoCarga": "",
      "vehiculoTipoCamion": "",
      "vehiculoArcsaAlimentos": "",
      "vehiculoCapacidad": "",
      "vehiculoTipoCapacidad": ""
    };

  }

  private cleanUpConductor() {
    this.conductor = {
      "usuarioIdDocumento": "",
      "usuarioTipoDocumento": "",
      "usuarioNombres": "",
      "usuarioApellidos": "",
      "usuarioMail": "",
      "usuarioCelular": "",
      "usuarioConvecional": "",
      "usuarioRuc": "",
      "usuarioDireccion": "",
      "usuarioLocalidad": "",
      "usuarioNombreUsuario": "",
      "usuarioContrasenia": "",
      "usuarioEstado": ""
    };
  }

  private cleanUpTemporal() {
    this.temporal = {};
  }

  newItem() {
    this.cleanUpChofer();
    this.bandact = "off";
    $("#modNewConductor").modal('show');
  }

  abrirNuevo() {
    this.cleanUpVehiculo();
    this.selectedItems = [];
    this.bandact = "off";
    $("#modNewVehiculo").modal('show');

  }

  cerrarMensaje() {
    /*
    if (this.operacionCorrecta) {
      this.closeWindow()
    }
    */
    $("#modNewItem").modal('hide');

  }

  modificarChofer(choferId) {
    this.choferes.conductorUsuario = this.conductor.usuarioIdDocumento;
    this.conductor.conductorsByUsuarioIdDocumento = this.conductorsByUsuarioIdDocumento;
    if (this.band == "actualiza") {
      if (this.choferes.conductorId == null) {
        this.agregarChofer();
      } else {
        this.loading = true;
        $("#modNewConductor").modal('hide');
        this._conductorService.updateConductorCamion(this.choferes).subscribe(data => {
          this.operacionCorrecta = true;
          this.mensajeOperacion = 'Conductor actualizado correctamente';
          $("#myModal").modal('show');
          this.loading = false;
          this.cargarConductoresVehiculos();
        }, error => {
          this.loading = false;
          console.log('Error : ', error);
          if(error.status == 403) this._authService.logout();
          this.operacionCorrecta = false;
          this.mensajeOperacion = 'El conductor no pudo ser Actualizado';
          $("#myModal").modal('show');
          this.cargarConductoresVehiculos();
        });
      }
    } else {
      this.conductorsByUsuarioIdDocumento.splice(this.index, 1);
      this.conductorsByUsuarioIdDocumento.push(this.choferes);
    }
    this.cleanUpChofer();
  }

  modificarVehiculo(vehiculoId) {
    this.vehiculo.vehiculoUsuario = this.conductor.usuarioIdDocumento;
    this.conductor.vehiculosByUsuarioIdDocumento = this.vehiculosByUsuarioIdDocumento;
    if (!this.vehiculo.vehiculoArcsaAlimentos) {
      this.vehiculo.vehiculoArcsaAlimentos = "false";
    }
    if (this.band == "actualiza") {
      if (this.vehiculo.vehiculoId == null) {
        this.agregarVehiculo();
      } else {
        this.loading=true;
        this._conductorService.updateVehiculo(this.vehiculo).subscribe(data => {
          this.operacionCorrecta = true;
          this.mensajeOperacion = 'Vehiculo actualizado correctamente';
          $("#myModal").modal('show');
          this.loading=false;
          this.cargarConductoresVehiculos();
        }, error => {
          console.log('Error : ', error);
          this.operacionCorrecta = false;
          this.mensajeOperacion = 'El vehiculo no pudo ser actualizado';
          $("#myModal").modal('show');
          this.loading=false;
          if(error.status == 403) this._authService.logout();
          this.cargarConductoresVehiculos();
        });
      }
    } else {
      this.vehiculosByUsuarioIdDocumento.splice(this.index, 1);
      this.vehiculosByUsuarioIdDocumento.push(this.vehiculo);
    }
    this.cleanUpVehiculo();
  }

  cargarDatosCliente() {
    this.empresa = this.clienteCombo.filter(clienteCombo => clienteCombo.ruc.indexOf(this.empresaClienteSeleccionada) !== -1);
    if(this.empresa[0]){
      this.temporal.usuarioRazonSocial = this.empresa[0].razonSocial;
      this.conductor.usuarioRuc = this.empresa[0].ruc;
      this.temporal.usuarioDireccion = this.empresa[0].direccion;
      this.temporal.usuarioTipoPersona = this.empresa[0].tipo;
      this.temporal.usuarioLocalidad = this.empresa[0].localidad;
      this._catalogoItemService.getCatalogoItemByCatalogoItem(this.empresa[0].periodoFacturacion).subscribe(
        data => {
          this.temporal.usuarioPeriodoFacturacion = data.catalogoItemDescripcion;
        }, error => {
          if(error.status == 403) this._authService.logout();
        });
      this.temporal.usuarioDiasCredito = this.empresa[0].diasCredito;
    }
  }

  elevarProveedor() {
    this.conductor.usuarioRuc = this.conductor.usuarioIdDocumento + "001";
    this.empresaClienteSeleccionada = this.conductor.usuarioRuc;
    this.cargarDatosCliente();

  }

  editarVehiculo(vehiculo, i) {
    this.selectedItems = [];
    this.vehiculo = vehiculo;
    if(this.vehiculo.vehiculoArcsaAlimentos) this.selectedItems.push(this.dropdownList.find(x => x.id == this.idArcsaAlimentos));
    if(this.vehiculo.vehiculoArcsaCosmeticos) this.selectedItems.push(this.dropdownList.find(x => x.id == this.idArcsaCosmeticos));
    if(this.vehiculo.vehiculoArcsaMedicamentos) this.selectedItems.push(this.dropdownList.find(x => x.id == this.idArcsaMedicamentos));
    if(this.vehiculo.vehiculoBasc) this.selectedItems.push(this.dropdownList.find(x => x.id == this.idBasc));
    if(this.vehiculo.vehiculoPaseInternacional) this.selectedItems.push(this.dropdownList.find(x => x.id == this.idPaseInternacional));
    this.bandact = "on";
    $("#modNewVehiculo").modal('show');
  }

  editarChofer(chofer, i) {
    this.choferes = chofer;
    this.bandact = "on";
    $("#modNewConductor").modal('show');
  }

  guardarVehiculo(vehiculo) {
    this.revisarCertificados(vehiculo);
    $("#modNewVehiculo").modal('hide');
    vehiculo.vehiculoPlaca = vehiculo.vehiculoPlaca.toUpperCase();
    if (this.bandact == "on") {
      this.modificarVehiculo(vehiculo);
    } else {
      this.agregarVehiculo();

    }
  }

  guardaChofer(chofer) {
    $("#modNewConductor").modal('hide');
    if (this.bandact == "on") {
      this.modificarChofer(chofer);
    } else {
      this.agregarChofer();
    }
  }

  closeWindow() {
    this.complete.emit(true);
  }

  revisarCertificados(vehiculo){
    vehiculo.vehiculoArcsaAlimentos = this.selectedItems && !!this.selectedItems.find(x => x.id == this.idArcsaAlimentos);
    vehiculo.vehiculoArcsaCosmeticos = this.selectedItems && !!this.selectedItems.find(x => x.id == this.idArcsaCosmeticos);
    vehiculo.vehiculoArcsaMedicamentos = this.selectedItems && !!this.selectedItems.find(x => x.id == this.idArcsaMedicamentos);
    vehiculo.vehiculoBasc = this.selectedItems && !!this.selectedItems.find(x => x.id == this.idBasc);
    vehiculo.vehiculoPaseInternacional = this.selectedItems && !!this.selectedItems.find(x => x.id == this.idPaseInternacional);
  }

}
