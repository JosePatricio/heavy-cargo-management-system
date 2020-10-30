import {Component, OnInit} from '@angular/core';
import {appConfig} from '../../../app.config';
import {Router} from '@angular/router';
import {UtilService} from '../../../services/util.service';
import {PublicService} from "../../../services/public.service";
import {Cliente} from "../../../interfaces/cliente";
import Swal from "sweetalert2/dist/sweetalert2.all.min.js";

@Component({
  selector: 'app-nuevo-conductor-login',
  templateUrl: './nuevo-conductor-login.component.html'
})
export class NuevoConductorLoginComponent implements OnInit {

  appConfig = appConfig;

  public indexEdit: any;
  public loading = false;

  tiposDocumento: any;
  empresa: any = {};
  existeEmpresa: boolean = false;
  provincias: any;
  usuario: Cliente = {};
  tipoLicenciaConductor: any;
  tiposLicencia: any;
  idProvincia: any;
  ciudades: any;

  constructor(private _router: Router, private _utilService: UtilService, private _publicService: PublicService) {
  }

  ngOnInit() {
    this.loadTypeDoc();
    this.loadTypeLic();
    this.loadProvincias();
  }

  loadProvincias() {
    this.loading = true;
    this._publicService.getLocalidadByPadre(appConfig.idLocalidadPadreEcuador, 1).subscribe(
      data => {
        this.loading = false;
        this.provincias = data;
      },
      error => {
        this.loading = false;
      }
    );
  }

  loadTypeDoc() {
    this.loading = true;
    this._utilService.obtenerCatalogoItem(appConfig.idCatalogoDocumento).subscribe(
      data => {
        this.tiposDocumento = data;
        this.loading = false;
      },
      error => {
        this.loading = false;
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
        this.loading = false;
      }
    );
  }

  agregarConductor() {

    this.usuario.usuarioEstado = appConfig.idEstadoUsuarioPendiente;
    this.usuario.usuarioTipoUsuario = appConfig.idConductorType;
    this.usuario.vehiculosByUsuarioIdDocumento = [];

    var conductorByUsuarioIdDocumento = {
      "conductorApellido": this.usuario.usuarioApellidos,
      "conductorEmail": this.usuario.usuarioMail,
      "conductorNombre": this.usuario.usuarioNombres,
      "conductorTelefono" : this.usuario.usuarioCelular,
      "conductorTipoLicencia" : this.tipoLicenciaConductor,
      "conductorUsuario" : this.usuario.usuarioIdDocumento
    };

    var conductorByUsuarioIdDocumentoList = [];
    conductorByUsuarioIdDocumentoList.push(conductorByUsuarioIdDocumento);
    this.usuario.conductorsByUsuarioIdDocumento = conductorByUsuarioIdDocumentoList;

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

  cerrarMensaje(operacionCorrecta: boolean) {
    if (operacionCorrecta) {
      this._router.navigate(['/login']);
    }
  }

  buscarRuc(){
    this.existeEmpresa = false;
    if(this.usuario.usuarioRuc != null && this.usuario.usuarioRuc.length == 13){
      this.loading = true;
      this._publicService.getEmpresaTransporteByRuc(this.usuario.usuarioRuc).subscribe(data => {
        if(data.responseMessage){
          this.empresa.clienteRazonSocial = "Empresa de transporte no registrada";
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
