import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {appConfig} from "../../../app.config";
import {UtilService} from "../../../services/util.service";
import {EmpresaService} from "../../../services/empresa.service";
import {ActivatedRoute, Router} from "@angular/router";
import {LocalidadService} from "../../../services/localidad.service";
import {AuthService} from "../../../services/auth.service";

declare var $: any;

@Component({
  selector: 'app-empresas-nuevo',
  templateUrl: './empresas-nuevo.component.html'
})
export class EmpresasNuevoComponent implements OnInit {

  public loading: boolean = false;

  appConfig = appConfig;

  tipoEmpresa: any;
  tipoPersona: any;
  cataRenta: any;
  cataIVA: any;
  periocidad: any;
  provincias: any;
  bancos: any;
  tipoCuentas: any;
  provinciaSeleccionada: any;
  ciudades: any;
  cliente: any;
  showCliente: boolean = false;
  showTransportista: boolean = false;
  showProveedor: boolean = false;
  showButtonNew: boolean = false;
  showButtonEdit: boolean = false;
  showButtonDelete: boolean = false;
  enableInput: boolean = true;
  operacionCorrecta: boolean = false;
  mensajeOperacion: string = '';

  @Input('tipo') tipo;
  @Input('ruc') ruc;
  @Input('tipoEmp') tipoEmp;
  @Output() complete: EventEmitter<boolean> = new EventEmitter<boolean>();

  constructor(private _utilService: UtilService, private _empresaService: EmpresaService, private _router: Router,
              private _activatedRoute: ActivatedRoute, private _localidad: LocalidadService, private _authService: AuthService) {
  }

  ngOnInit() {
    this.cliente = {};
    this.provinciaSeleccionada = {};
    this.loading = true;
    this._utilService.obtenerCatalogoItem(appConfig.idCatalogoTipoEmpresa).subscribe(
      data => {
        this.tipoEmpresa = data;
        if (this.tipo == 'new' && this.tipoEmp) {
          this.cliente.clienteTipoCliente = this.tipoEmp;
          this.showFields();
        }
      },
      error => {
        this.loading = false;
      }
    );
    this._utilService.obtenerCatalogoItem(appConfig.idCatalogoPersona).subscribe(
      data => {
        this.tipoPersona = data;
      },
      error => {
        this.loading = false;
      }
    );
    this._utilService.obtenerCatalogoItem(appConfig.idCatalogoRenta).subscribe(
      data => {
        this.cataRenta = data;
      },
      error => {
        this.loading = false;
      }
    );
    this._utilService.obtenerCatalogoItem(appConfig.idCatalogoIVA).subscribe(
      data => {
        this.cataIVA = data;
      },
      error => {
        this.loading = false;
      }
    );
    this._utilService.obtenerLocalidad(appConfig.idLocalidadPadreEcuador).subscribe(
      data => {
        this.provincias = data;
        this.loading = false;
      },
      error => {
        this.loading = false;
      }
    );
    this._utilService.obtenerCatalogoItem(appConfig.idCatalogoPeriodo).subscribe(
      data => {
        this.periocidad = data;
      },
      error => {
        this.loading = false;
      }
    );
    this._utilService.obtenerCatalogoItem(appConfig.idCatalogoBanco).subscribe(
      data => {
        this.bancos = data;
      },
      error => {
        this.loading = false;
      }
    );
    this._utilService.obtenerCatalogoItem(appConfig.idCatalogoTipoCuenta).subscribe(
      data => {
        this.tipoCuentas = data;
      },
      error => {
        this.loading = false;
      }
    );


    if (this.tipo == 'new') {
      this.showButtonNew = true;
      this.enableInput = false;
    }
    if (this.tipo == 'edit') {
      this.showButtonEdit = true;
      this.enableInput = true;
    }
    if (this.tipo == 'delete') {
      this.showButtonDelete = true;
      this.enableInput = true;
    }
    if (this.tipo == 'edit' || this.tipo == 'delete') {
      this.loading = true;
      this.ciudades = [];
      this._empresaService.readEmpresa(this.ruc).subscribe(
        data => {
          this.cliente = data;
          this.showFields();
          this.fillLocalidad(this.cliente.clienteLocalidadId);
        },
        error2 => {
          this.loading = false;
          if(error2.status == 403) this._authService.logout();
        });
    }
  }

  fillLocalidad(localidad) {
    this.loading = true;
    this._localidad.getLocalidadById(localidad).subscribe(data => {
      this.loading = false;
      this.provinciaSeleccionada.id = data.localidadLocalidadPadre;
      let ciudad = {localidadId: data.localidadId, localidadDescripcion: data.localidadDescripcion};
      this.ciudades.push(ciudad);
    }, error2 => {
      this.loading = false;
    });
  }

  mostrarCiudades() {
    this.loading = true;
    this._utilService.obtenerLocalidad(this.provinciaSeleccionada.id).subscribe(
      data => {
        this.ciudades = data;
        this.loading = false;
      },
      error => {
        this.loading = false;
      }
    );
  }

  showFields() {
    if (this.cliente.clienteTipoCliente == this.appConfig.idEmpresaCliente) {
      this.showCliente = true;
      this.showTransportista = false;
    } else {
      this.showTransportista = true;
      this.showCliente = false;
    }

    this.showProveedor = (this.cliente.clienteTipoCliente === this.appConfig.idEmpresaProveedor);
  }

  crearEmpresa() {
    this.loading = true;
    this._empresaService.createEmpresa(this.cliente).subscribe(data => {
      this.loading = false;
      this.operacionCorrecta = true;
      this.mensajeOperacion = data.responseMessage;
      $("#myModal").modal('show');
    }, error => {
      this.loading = false;
      this.operacionCorrecta = false;
      this.mensajeOperacion = "Error en la creación de la empresa";
      $("#myModal").modal('show');
    })
  }

  updateEmpres() {
    this.loading = true;
    this._empresaService.updateEmpresa(this.cliente).subscribe(data => {
      this.loading = false;
      this.operacionCorrecta = true;
      this.mensajeOperacion = data.responseMessage;
      $("#myModal").modal('show');
    }, error2 => {
      this.loading = false;
      this.operacionCorrecta = false;
      this.mensajeOperacion = "Error en la actualización de la empresa";
      $("#myModal").modal('show');
    });
  }

  deleteEmpresa() {
    this.loading = true;
    this._empresaService.deleteEmpresa(this.cliente).subscribe(data => {
      this.loading = false;
      this.operacionCorrecta = true;
      this.mensajeOperacion = data.responseMessage;
      $("#myModal").modal('show');
    }, error2 => {
      this.loading = false;
      this.operacionCorrecta = false;
      this.mensajeOperacion = "Error borrando la empresa";
      $("#myModal").modal('show');
    });
  }

  cerrarMensaje() {
    if (this.operacionCorrecta) {
      this.cancelaEmpresa();
    }
  }

  cancelaEmpresa() {
    this.complete.emit(true);
  }

}
