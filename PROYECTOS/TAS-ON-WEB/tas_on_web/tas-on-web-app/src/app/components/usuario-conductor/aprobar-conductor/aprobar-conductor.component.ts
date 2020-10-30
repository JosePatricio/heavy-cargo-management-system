import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {appConfig} from "../../../app.config";
import {UsuariosService} from "../../../services/usuarios.service";
import {AuthService} from "../../../services/auth.service";
import Swal from "sweetalert2/dist/sweetalert2.all.min.js";

declare var $: any;

@Component({
  selector: 'app-aprobar-conductor',
  templateUrl: './aprobar-conductor.component.html'
})
export class AprobarConductorComponent implements OnInit {
  public loading = false;
  catUsers: any[];
  catState: any[];
  idCatUser: number;
  idCatState: number;
  userSelec: any = {};
  acepta: any = false;
  viewDriv: any = false;

  //Para dataTable
  @ViewChild('tmplIndex') tmplIndex: TemplateRef<any>;
  @ViewChild('linkButton') linkButton: TemplateRef<any>;
  usuarios: any[];
  columns = [];

  constructor(public _usuariosService: UsuariosService, private _authService: AuthService) {
  }

  ngOnInit() {
    this.columns = [
      {name: '#', cellTemplate: this.tmplIndex, headerClass: 'table-header', width: 85},
      {prop: 'idEstado', name: 'Info.', cellTemplate: this.linkButton, headerClass: 'table-header', width: 115},
      {prop: 'idDocumento', name: 'ID Documento', headerClass: 'table-header'},
      {prop: 'nombres', name: 'Nombres', headerClass: 'table-header'},
      {prop: 'apellidos', name: 'Apellidos', headerClass: 'table-header'},
      {prop: 'usuario', name: 'Usuario', headerClass: 'table-header'},
      {prop: 'estado', name: 'Estado', headerClass: 'table-header'}
    ];
    this.loadReqUser();
  }

  loadReqUser() {
    let tipoUsuario = this._authService.getTypeUser() == appConfig.idUsuarioEnviosAdmin ? appConfig.idUsuarioEnvios : appConfig.idUsuarioConductor;
    this.getUser(this._authService.getRucEmpresa(), tipoUsuario, appConfig.idEstadoUsuarioPendiente);
  }

  getUser(rucEmpresa: any, tipoUsuario: number, estado: number) {
    if (estado) {
      this.loading = true;
      this._usuariosService.getAllUserDetailByEmpresaTipoAndStatus
      (rucEmpresa, tipoUsuario, estado).subscribe(data => {
        this.usuarios = data;
        this.loading = false;
      });
    }
  }

  checkOk(userRestablece: any, acepta: boolean) {
    this.userSelec = userRestablece;
    this.acepta = acepta;
    let opcion: string = this.acepta? 'ACEPTAR' : 'RECHAZAR';
    Swal.fire({
      text: 'Está seguro en '+opcion+' el registro de ' +this.userSelec.nombres + ' ' + this.userSelec.apellidos,
      type: 'question',
      showCancelButton: true,
      confirmButtonText: 'Si',
      cancelButtonText: 'No'
    }).then((result) => {
      if (result.value) {
        this.aceptaUser();
      }
    })
  }

  aceptaUser() {
    this.loading = true;
    var useract: any = {
      usuarioIdDocumento: this.userSelec.idDocumento,
      usuarioEstado: this.acepta ? appConfig.idEstadoUsuarioCreado : appConfig.idEstadoUsuarioEliminado,
      usuarioTipoDocumento: null,
      usuarioNombres: null,
      usuarioApellidos: null,
      usuarioCelular: null,
      usuarioConvecional: null,
      usuarioRuc: null,
      usuarioDireccion: null,
      usuarioLocalidad: null,
      usuarioNombreUsuario: null,
      usuarioContrasenia: null,
      usuarioMail: null,
      usuarioTipoUsuario: null,
      usuarioPrincipal: null
    };

    this._usuariosService.activateUser(useract).subscribe(
      data => {
        this.loading=false;
        Swal.fire('', data.responseMessage, (data.response == 'OK')? 'success' : 'error').then((result) => {
          this.loadReqUser();
        });
      }, error => {
        this.loading = false;
        if(error.status == 403) this._authService.logout();
        Swal.fire('', error.message, 'error');
      }
    );
  }

  closeDetail(event: boolean) {
    this.viewDriv = !event;
    this.loadReqUser();
  }
}
