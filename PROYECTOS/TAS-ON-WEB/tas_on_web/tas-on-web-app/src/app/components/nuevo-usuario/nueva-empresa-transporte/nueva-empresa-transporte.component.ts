import {Component, OnInit} from '@angular/core';
import {appConfig} from "../../../app.config";
import {Router} from "@angular/router";
import {UtilService} from "../../../services/util.service";
import {PublicService} from "../../../services/public.service";
import {Empresa} from "../../../interfaces/empresa";
import {Cliente} from "../../../interfaces/cliente";
import Swal from "sweetalert2/dist/sweetalert2.all.min.js";

declare var $: any;

@Component({
  selector: 'app-nueva-empresa-transporte',
  templateUrl: './nueva-empresa-transporte.component.html',
  styleUrls: ['./nueva-empresa-transporte.component.css']
})
export class NuevaEmpresaTransporteComponent implements OnInit {

  appConfig = appConfig;

  public loading = false;
  nextStep: number = 0;

  provincias: any;
  idProvinciaEmpresa: any;
  idProvinciaUsuario: any;
  ciudadesEmpresa: any;
  ciudadesUsuario: any;
  tiposDocumento: any;
  unidadesCapacidad: any;
  tiposCarga: any;
  tiposCamion: any;

  cliente: Cliente = {};
  empresa: Empresa = {};
  columns = [];

  camposValidos: boolean = true;
  deshabilitarRegistro: boolean = false;

  constructor(private _router: Router, private _utilService: UtilService, private _publicService: PublicService) {
  }

  ngOnInit() {
    this.loadProvincias();
    this.loadTiposDocumento();
  }

  loadTiposDocumento(){
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
  }

  loadProvincias() {
    this.loading = true;
    this._publicService.getLocalidadByPadre(appConfig.idLocalidadPadreEcuador, 1).subscribe(
      data => {
        this.provincias = data;
        this.loading = false;
      },
      error => {
        this.loading = false;
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
    if (!forma.valid) {
      this.camposValidos = false;
      this.deshabilitarRegistro = false;
      return;
    }

    this.camposValidos = true;

    this.cliente.usuarioEstado = appConfig.idEstadoUsuarioCreado;
    this.cliente.usuarioPrincipal = true;
    this.cliente.usuarioRuc = this.empresa.clienteRuc;
    this.cliente.usuarioTipoUsuario = appConfig.idUsuarioConductorAdmin;
    this.cliente.conductorsByUsuarioIdDocumento = [];
    this.empresa.clienteTipoCliente = appConfig.idTipoClienteEmpresaTransportista;
    this.empresa.clienteTipoId = appConfig.idPersonaJuridica;
    this.empresa.clienteComision = 0;
    this.empresa.clienteDiasCredito = 0;
    this.empresa.clienteDiasPeriodicidad = 0;

    var objetoAEnviar = {
      "cliente" : this.cliente,
      "empresa" : this.empresa
    };

    this.loading = true;
    this._publicService.createEmpresaCliente(objetoAEnviar).subscribe(data => {
      Swal.fire('', data.responseMessage, (data.response == 'OK')? 'success' : 'error').then((result) => {
        this.cerrarMensaje(data.response == 'OK');
      });
      this.loading = false;
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

  mostrarCiudadesEmpresa() {
    this.loading = true;
    this._publicService.getLocalidadByPadre(this.idProvinciaEmpresa, 1).subscribe(
      data => {
        this.ciudadesEmpresa = data;
        this.loading = false;
      },
      error => {
        this.loading = false;
      }
    );
  }

  mostrarCiudades() {
    this.loading = true;
    this._publicService.getLocalidadByPadre(this.idProvinciaUsuario, 1).subscribe(
      data => {
        this.ciudadesUsuario = data;
        this.loading = false;
      },
      error => {
        this.loading = false;
      }
    );
  }

}
