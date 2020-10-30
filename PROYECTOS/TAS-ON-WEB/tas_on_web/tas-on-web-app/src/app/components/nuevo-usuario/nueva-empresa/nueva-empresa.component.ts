import {Component, OnInit} from '@angular/core';
import {UtilService} from "../../../services/util.service";
import {appConfig} from "../../../app.config";
import {PublicService} from "../../../services/public.service";
import {Router} from "@angular/router";
import Swal from "sweetalert2/dist/sweetalert2.all.min.js";

declare var $: any;

@Component({
  selector: 'app-nueva-empresa',
  templateUrl: './nueva-empresa.component.html'
})
export class NuevaEmpresaComponent implements OnInit {

  appConfig = appConfig;
  public loading: any = false;

  tipoPersona: any;
  provincias: any;
  tiposDocumento: any;
  ciudades: any;
  empresa: any;
  usuario: any;
  periocidad: any;
  idProvincia: any;
  tipoProducto: any;
  tipoDifusion: any;
  tiposSubasta: any;

  nextStep: any = false;

  constructor(private _router: Router, private _utilService: UtilService, private _publicService: PublicService) {
    this.loadValues();
  }

  ngOnInit() {
    this.empresa = {};
    this.usuario = {};
  }

  loadValues() {
    this.loading = true;
    this._publicService.getCatalogoItemByCatalogo(appConfig.idCatalogoPersona).subscribe(
      data => {
        this.tipoPersona = data;
        this.loading = false;
      },
      error => {
      }
    );

    this.loading = true;
    this._publicService.getLocalidadByPadre(appConfig.idLocalidadPadreEcuador, 1).subscribe(
      data => {
        this.provincias = data;
        this.loading = false;
      },
      error => {
      }
    );
    this.loading = true;
    this._publicService.getCatalogoItemByCatalogo(appConfig.idCatalogoPeriodo).subscribe(
      data => {
        this.periocidad = data;
        this.loading = false;
      },
      error => {
      }
    );
    this.loading = true;
    this._publicService.getCatalogoItemByCatalogo(appConfig.idCatalogoDocumento).subscribe(
      data => {
        this.tiposDocumento = data;
        this.loading = false;
      },
      error => {
        this.loading = false;
      }
    );
    this.loading = true;
    this._publicService.getCatalogoItemByCatalogo(appConfig.idCatalogoTipoProducto).subscribe(
      data => {
        this.tipoProducto = data;
        this.loading = false;
      },
      error => {
        this.loading = false;
      }
    );
    this.loading = true;
    this._publicService.getCatalogoItemByCatalogo(appConfig.idCatalogoMedioDifusion).subscribe(
      data => {
        this.tipoDifusion = data;
        this.loading = false;
      },
      error => {
        this.loading = false;
      }
    );
    this.loading = true;
    this._publicService.getCatalogoItemByCatalogo(appConfig.idCatalogoTiposSubasta).subscribe(
      data => {
        this.tiposSubasta = data;
        this.loading = false;
      },
      error => {
        this.loading = false;
      }
    );
  }

  private cleanUpCliente() {
    this.empresa = {};
    this.usuario = {
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

  siguiente() {
    this.loading = true;
    this.nextStep = true;
    this.loading = false;

    if(this.empresa.clienteConocimientoRegistro != null && this.empresa.clienteConocimientoRegistro != appConfig.idCatalogoItemVendedor){
      this.empresa.clienteCodigoVendedor = null;
    }
  }

  regresar() {
    this.loading = true;
    this.nextStep = false;
    this.loading = false;
  }

  saveNewUser() {
    $("#confirModal").modal('hide');
    this.loading = true;
    this.empresa.clienteTipoCliente = this.appConfig.idTipoClienteEmpresaCliente;
    this.usuario.usuarioTipoUsuario = this.appConfig.idUsuarioClienteAdmin;
    this.usuario.usuarioEstado = this.appConfig.idEstadoUsuarioCreado;
    this.usuario.usuarioPrincipal = true;
    var data = {
      empresa: this.empresa,
      cliente: this.usuario
    };
    this._publicService.createEmpresaCliente(data).subscribe(data => {
      this.loading = false;
      Swal.fire('', data.responseMessage, (data.response == 'OK')? 'success' : 'error').then((result) => {
        this.cerrarMensaje(data.response == 'OK');
      });
    }, error => {
      this.loading = false;
      Swal.fire('', error.message, 'error');
    });
  }

  confirmRegister() {
    $("#confirModal").modal('show');
  }

  cerrarMensaje(operacionCorrecta: boolean) {
    if (operacionCorrecta) {
      this._router.navigate(['/login']);
    }
  }
}
