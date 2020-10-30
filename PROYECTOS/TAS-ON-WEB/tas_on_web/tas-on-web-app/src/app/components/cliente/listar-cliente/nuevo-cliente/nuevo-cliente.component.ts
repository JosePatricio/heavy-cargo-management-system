import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {appConfig} from '../../../../app.config';
import {ActivatedRoute, Router} from '@angular/router';
import {UtilService} from '../../../../services/util.service';
import {ClienteService} from '../../../../services/cliente.service';
import {FormGroup} from "@angular/forms";
import {CatalogoItemService} from "../../../../services/catalogo-item.service";

declare var $: any;

@Component({
  selector: 'app-nuevo-cliente',
  templateUrl: './nuevo-cliente.component.html'
})
export class NuevoClienteComponent implements OnInit {

  appConfig = appConfig;

  public loading = false;
  public indexEdit: any;

  tiposDocumento: any;
  tiposPersona: any;
  provincias: any;
  provinciaSeleccionada: string = '';
  cantones: any;
  cliente: any;
  clienteCombo: any;
  empresa: any;

  //Mensaje de Operacion exitosa o erronea
  operacionCorrecta: boolean = false;
  mensajeOperacion: string = '';
  empresaClienteSeleccionada: string = '';
  temporal: any;
  form: FormGroup;

  @Input('idCliente') idCliente;
  @Input('editParam') editParam;
  @Output() complete: EventEmitter<boolean> = new EventEmitter<boolean>();

  constructor(private _router: Router, private _activatedRoute: ActivatedRoute, public _clienteService: ClienteService, private _utilService: UtilService, public _catalogoItemService: CatalogoItemService) {
  }

  ngOnInit() {
    this.cleanUpCliente();
    this.cleanUpTemporal();

    this.loadCatalog();

    if (this.editParam != 1) {
      this.loadData(this.idCliente);
    } else {
      this.loadComboCliente();
    }
  }

  loadComboCliente() {
    this.loading = true;
    this._clienteService.getClientebyTipoCliente(appConfig.idTipoClienteEmpresaCliente).subscribe(data => {
      this.clienteCombo = data;
      if (this.editParam != 1) {
        this.empresaClienteSeleccionada = this.cliente.usuarioRuc;
        if (this.cliente.usuarioLocalidad) {
          this._utilService.obtenerLocalidadById(this.cliente.usuarioLocalidad).subscribe(
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
            },
            error => {
              console.log(error);
            }
          );
        }
        this.loading = false;
        this.cargarDatosCliente();
      }
    });
  }

  loadData(idCliente: string) {
    this.loading = true;
    this._clienteService.getCliente(idCliente).subscribe(data => {
      this.cliente = data;
      this.loadComboCliente();
      this.loading = false;
    }, error => {
      this.loading = false;
    });
  }

  loadCatalog() {
    this.loading = true;
    this._utilService.obtenerCatalogoItem(appConfig.idCatalogoDocumento).subscribe(
      data => {
        this.tiposDocumento = data;
        this.loading = false;
      },
      error => {
        console.log(error);
        this.loading = false;
      }
    );
    this.loading = true;
    this._utilService.obtenerCatalogoItem(appConfig.idCatalogoPersona).subscribe(
      data => {
        this.tiposPersona = data;
        this.loading = false;
      },
      error => {
        console.log(error);
        this.loading = false;
      }
    );

    this.loading = true;
    this._utilService.obtenerLocalidad(appConfig.idLocalidadPadreEcuador).subscribe(
      data => {
        this.provincias = data;
        this.loading = false;
      },
      error => {
        console.log(error);
        this.loading = false;
      }
    );
  }

  cargarCantones() {
    this.loading = true;
    this.cliente.usuarioLocalidad = '';
    if (this.provinciaSeleccionada) {
      this._utilService.obtenerLocalidad(this.provinciaSeleccionada).subscribe(
        data => {
          this.cantones = data;
          this.loading = false;
        },
        error => {
          console.log(error);
          this.loading = false;
        }
      );
    }

  }

  cargarDatosCliente() {
    this.empresa = this.clienteCombo.filter(clienteCombo => clienteCombo.ruc.indexOf(this.empresaClienteSeleccionada) !== -1);
    this.temporal.usuarioRazonSocial = this.empresa[0].razonSocial;
    this.cliente.usuarioRuc = this.empresa[0].ruc;
    this.temporal.usuarioDireccion = this.empresa[0].direccion;
    this.temporal.usuarioTipoPersona = this.empresa[0].tipo;
    this.temporal.usuarioLocalidad = this.empresa[0].localidad;
    this.loading = true;
    this._catalogoItemService.getCatalogoItemByCatalogoItem(this.empresa[0].periodoFacturacion).subscribe(
      data => {
        this.temporal.usuarioPeriodoFacturacion = data.catalogoItemDescripcion;
        this.loading = false;
      }, error => {
        this.loading = false;
      });
    this.temporal.usuarioDiasCredito = this.empresa[0].diasCredito;
  }

  agregarCliente() {
    this.loading = true;
    this.cliente.usuarioTipoUsuario = appConfig.idClienteType;
    this.cliente.usuarioEstado = appConfig.idEstadoUsuarioActivo;
    this._clienteService.addCliente(this.cliente).subscribe(data => {
      this.operacionCorrecta = true;
      this.mensajeOperacion = 'Cliente creado correctamente';
      this.loading = false;
      $("#myModal").modal('show');
    }, error => {
      console.log('Error : ', error);
      this.operacionCorrecta = false;
      this.mensajeOperacion = 'El cliente no pudo ser creado';
      this.loading = false;
      $("#myModal").modal('show');
    });
  }

  editarCliente() {
    this.operacionCorrecta = false;
    this.mensajeOperacion = 'El cliente no pudo ser actualizado';
  }

  private cleanUpTemporal() {
    this.temporal = {};
  }

  private cleanUpCliente() {
    this.cliente = {
      "usuarioIdDocumento": "",
      "usuarioTipoDocumento": "",
      "usuarioNombres": "",
      "usuarioApellidos": "",
      "usuarioMail": "",
      "usuarioCelular": "",
      "usuarioConvecional": "",
      "usuarioRuc": "",
      "usuarioNombreUsuario": "",
      "usuarioContrasenia": "",
      "usuarioEstado": ""
    };
  }

  onFileChange(event) {
    if (event.target.files.length > 0) {
      let file = event.target.files[0];
    }
  }

  cerrarMensaje() {
    if (this.operacionCorrecta) {
      //this._router.navigate(['/panel/cliente/listar']);
      this.closePage();
    }
  }

  closePage() {
    this.complete.emit(true);
  }
}
