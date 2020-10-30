import {Component, OnInit} from '@angular/core';
import {appConfig} from "../../app.config";


@Component({
  selector: 'app-nuevo-usuario',
  templateUrl: './nuevo-usuario.component.html',
  styleUrls: ['./nuevo-usuario.component.css']
})
export class NuevoUsuarioComponent implements OnInit {
  appConfig = appConfig;
  public indexEdit: any;

  crearCliente: boolean = false;
  crearConductor: boolean = false;

  tipoUsuario: any = '';

  typesUser: any = [
    {
      idTipo: appConfig.idEmpresaType,
      nombreTipo: "Empresa cliente"
    },
    {
      idTipo: appConfig.idClienteType,
      nombreTipo: "Usuario cliente"
    },
    /*{
      idTipo: appConfig.idUsuarioConductorAdmin,
      nombreTipo: "Conductor dependencia propia"
    },*/
    {
      idTipo: appConfig.idUsuarioConductor,
      nombreTipo: "Conductor de compañía de transporte"
    },
    {
      idTipo: appConfig.idTipoClienteEmpresaTransportista,
      nombreTipo: "Registrar compañía de transporte"
    }

  ];


  constructor() {
  }

  ngOnInit() {
  }

  onChangeTipoPersona() {
    if (null != this.tipoUsuario && '' != this.tipoUsuario) {
      if (this.tipoUsuario === appConfig.idClienteType) {
        this.crearCliente = true;
        this.crearConductor = false;
      }
      if (this.tipoUsuario === appConfig.idUsuarioConductorAdmin) {
        this.crearCliente = false;
        this.crearConductor = true;
      }
    } else {
      this.crearCliente = false;
      this.crearConductor = false;
    }
  }

}
