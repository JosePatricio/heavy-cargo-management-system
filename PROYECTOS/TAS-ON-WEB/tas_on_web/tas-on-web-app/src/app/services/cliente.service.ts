import {Injectable} from '@angular/core';
import {appConfig} from '../app.config';
import {Headers, Http, RequestOptions} from '@angular/http';
import 'rxjs/Rx';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import {Observable} from 'rxjs/Observable';
import {AuthService} from "./auth.service";

@Injectable()
export class ClienteService {

  clientesCombo: any[];

  constructor(private _http: Http, public _authService: AuthService) {
  }

  getClientes() {
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-usuarios/usuario/all/${appConfig.idClienteType}/${appConfig.idEstadoUsuarioActivo}`).map((data) => {
      return data.json();
    });
  }

  getClienteByState(stateUser: number) {
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-usuarios/usuario/all/${appConfig.idClienteType}/${stateUser}`).map((data) => {
      return data.json();
    });
  }

  getClientesById(idDocumento: string) {
    return this._http.get("assets/clientes.json").map((data) => {
      let clientes = data.json();
      for (let cliente of clientes) {
        if (cliente.idDocumento = idDocumento) {
          return cliente;
        }
      }

    });
  }

  addCliente(cliente: any) {
    var headers = new Headers();
    headers.append('Content-Type', 'application/json');
    headers.append('Accept', 'application/json');
    return this._http.post(appConfig.apiUrlServicesBase + '/rest-usuarios/usuario/create/', cliente, {headers: headers}).map((data) => {
      return "OK";
    });
  }

  getClientesByAll() {
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-client/client/all`).map((data) => {
      this.clientesCombo = data.json();
      return data.json();
    });
  }

  getCliente(idCliente: any) {
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-usuarios/usuario/read/${idCliente}`).map((data) => {
      return data.json();
    }).catch((error: any) => Observable.throw('{ error : no se encuentra usuario }'));
  }

  getClienteEmpresa(ruc: any) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-client/client/read/${ruc}`, {headers: headers}).map((data) => {
      return data.json();
    }).catch((error: any) => Observable.throw('{ error : no se cliente empresa }'));
  }

  getClientebyTipoCliente(idTipoCliente: any) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-client/client/clientes/${idTipoCliente}`, {headers: headers}).map((data) => {
      this.clientesCombo = data.json();
      return data.json();
    });
  }

  getClienteInfoBancaria() {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-client/client/banco/info/`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  updateClienteInfoBancaria(infoBancaria: any) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.put(appConfig.apiUrlServicesBase + '/rest-client/client/banco/update/', infoBancaria, {headers: headers}).map((data) => {
      return data.json();
    });
  }
}
