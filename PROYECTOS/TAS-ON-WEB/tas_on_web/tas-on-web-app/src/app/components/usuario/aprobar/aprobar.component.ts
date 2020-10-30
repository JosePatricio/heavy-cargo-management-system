import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {UsuariosService} from "../../../services/usuarios.service";
import {appConfig} from "../../../app.config";
import {AuthService} from "../../../services/auth.service";
import Swal from "sweetalert2/dist/sweetalert2.all.min.js";

declare var $: any;

@Component({
  selector: 'app-aprobar',
  templateUrl: './aprobar.component.html'
})
export class AprobarComponent implements OnInit {
  public loading = false;
  catUsers: any[];
  catState: any[];
  idCatUser: number;
  idCatState: number;
  userSelec: any = {};
  acepta: any = false;
  viewUser: any = false;

  //Para dataTable
  @ViewChild('tmplIndex') tmplIndex: TemplateRef<any>;
  @ViewChild('linkButton') linkButton: TemplateRef<any>;
  usuarios: any[];
  columns = [];

  columnsRol = [];
  elementsRol = [];
  tiposRol = [];
  rolNuevoUsuario: any;

  constructor(public _usuariosService: UsuariosService, private _authService: AuthService) {
  }

  ngOnInit() {
    this.columns = [
      {prop: 'idEstado', name: '#', cellTemplate: this.tmplIndex, headerClass: 'table-header', width: 85},
      {prop: 'idEstado', name: 'Info.', cellTemplate: this.linkButton, headerClass: 'table-header', width: 115},
      {prop: 'idDocumento', name: 'ID Documento', headerClass: 'table-header'},
      {prop: 'nombres', name: 'Nombres', headerClass: 'table-header'},
      {prop: 'apellidos', name: 'Apellidos', headerClass: 'table-header'},
      {prop: 'usuario', name: 'Usuario', headerClass: 'table-header'},
      {prop: 'estado', name: 'Estado', headerClass: 'table-header'}
    ];
    this.loadReqUser();

    this.columnsRol = ['Acción', 'Cliente', 'Cliente reducido'];
    this.elementsRol = [
      {accion: 'Ingresar/Editar/Cancelar solicitudes', rol1: 'Sí', rol2: 'Sí'},
      {accion: 'Seleccionar ofertas', rol1: 'Sí', rol2: 'No'},
      {accion: 'Ver el estado de las ofertas', rol1: 'Sí', rol2: 'Sí'},
      {accion: 'Ver el precio de las ofertas', rol1: 'Sí', rol2: 'No'},
    ];

    this.tiposRol= [
      {id: appConfig.idUsuarioCliente, descripcion: 'Cliente'},
      {id: appConfig.idUsuarioClienteReducido, descripcion: 'Cliente reducido'},
    ];
  }

  loadReqUser() {
    this.getUser(this._authService.getRucEmpresa(), appConfig.idUsuarioCliente, appConfig.idEstadoUsuarioPendiente);
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
    this.rolNuevoUsuario = null;
    $("#restableceModal").modal('show');
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
      usuarioTipoUsuario: this.rolNuevoUsuario,
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
    this.viewUser = !event;
    this.loadReqUser();
  }
}
