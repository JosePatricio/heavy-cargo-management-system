import {Component, OnInit} from '@angular/core';
import {appConfig} from '../../../app.config';
import {Router} from '@angular/router';
import {UtilService} from '../../../services/util.service';
import {PublicService} from "../../../services/public.service";
import Swal from "sweetalert2/dist/sweetalert2.all.min.js";

@Component({
  selector: 'app-nuevo-cliente-login',
  templateUrl: './nuevo-cliente-login.component.html'
})
export class NuevoClienteLoginComponent implements OnInit {

  appConfig = appConfig;

  public indexEdit: any;
  public loading = false;

  tiposDocumento: any;
  empresa: any = {};
  existeEmpresa: boolean = false;
  provincias: any;
  usuario: any = {};

  constructor(private _router: Router, private _utilService: UtilService, private _publicService: PublicService) {
  }

  ngOnInit() {
    this.cleanUpCliente();
    this.loadTypeDoc();
  }

  loadTypeDoc() {
    this.loading = true;
    this._utilService.obtenerCatalogoItem(appConfig.idCatalogoDocumento).subscribe(
      data => {
        this.tiposDocumento = data;
        this.loading = false;
      },
      error => {
        console.log(error);
      }
    );
  }

  agregarCliente() {
    this.usuario.usuarioTipoUsuario = appConfig.idClienteType;
    this.usuario.usuarioEstado = appConfig.idEstadoUsuarioActivo;
    this.loading = true;
    this._publicService.createUsuarioEmpresa(this.usuario).subscribe(data => {
      this.loading = false;
      Swal.fire('', data.responseMessage, (data.response == 'OK')? 'success' : 'error').then((result) => {
        this.cerrarMensaje(data.response == 'OK');
      });
    }, error => {
      this.loading = false;
      Swal.fire('', error.message, 'error');
    });

  }

  private cleanUpCliente() {
    this.usuario = {
      "usuarioIdDocumento": "",
      "usuarioTipoDocumento": "",
      "usuarioNombres": "",
      "usuarioApellidos": "",
      "usuarioMail": "",
      "usuarioCelular": "",
      "usuarioConvecional": "",
      "usuarioRuc": "",
      "usuarioDireccion": "",
      "usuarioTipoUsuario": "",
      "usuarioNombreUsuario": "",
      "usuarioContrasenia": "",
      "usuarioEstado": ""
    };

    this.empresa = {
      "clienteRazonSocial" : ""
    }
  }

  cerrarMensaje(operacionCorrecta) {
    if (operacionCorrecta) {
      this._router.navigate(['/login']);
    }
  }

  buscarRuc(){
    this.existeEmpresa = false;
    if(this.usuario.usuarioRuc != null && this.usuario.usuarioRuc.length == 13){
      this.loading = true;
      this._publicService.getEmpresaByRuc(this.usuario.usuarioRuc).subscribe(data => {
        if(data.responseMessage){
          this.empresa.clienteRazonSocial = "Empresa no registrada";
        }else{
          this.empresa = data;
          this.existeEmpresa = true;
        }
        this.loading = false;
      }, error => {
        this.existeEmpresa = false;
        this.loading = false;
      });
    } else {
      this.empresa.clienteRazonSocial = "";
    }
  }

}
