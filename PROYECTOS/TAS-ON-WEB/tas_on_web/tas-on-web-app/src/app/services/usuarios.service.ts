import {Injectable} from '@angular/core';
import {Headers, Http} from "@angular/http";
import {appConfig} from "../app.config";
import {AuthService} from "./auth.service";

@Injectable()
export class UsuariosService {

  constructor(private _http: Http, public _authService: AuthService) {
  }

  getTokenHeader() {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return {headers: headers}
  }

  getAllUserByTipoAndStatus(tipoUsuario: any, estado: number) {
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-usuarios/usuario/all/${tipoUsuario}/${estado}`).map((data) => {
      return data.json();
    });
  }

  getAllUserDetailByTipoAndStatus(tipoUsuario: number, estado: number) {
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-usuarios/usuario/all-usuario/${tipoUsuario}/${estado}`).map((data) => {
      return data.json();
    });
  }

  getAllUserDetailByEmpresaTipoAndStatus(rucEmpresa: any, tipoUsuario: number, estado: number) {
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-usuarios/usuario/all-usuario-empresa/${rucEmpresa}/${tipoUsuario}/${estado}`).map((data) => {
      return data.json();
    });
  }

  activateUser(user: any) {
    return this._http.post(appConfig.apiUrlServicesBase + `/rest-usuarios/usuario/activate-user/`, user, this.getTokenHeader()).map((data) => {
      return data.json();
    });
  }

  usuarioByEntAndState(idEmpresa: number, estado: number) {
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-usuarios/usuario/empresa-usuario/${idEmpresa}/${estado}`, this.getTokenHeader()).map((data) => {
      return data.json();
    });
  }

}
