import {Injectable} from '@angular/core';
import {appConfig} from '../app.config';
import {Headers, Http, RequestOptions} from '@angular/http';
import {AuthService} from "./auth.service";

import 'rxjs/Rx';

@Injectable()
export class ConductorService {

  clientesCombo: any[];

  constructor(private _http: Http, public _authService: AuthService) {
  }

  getConductores() {
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-usuarios/usuario/all/${appConfig.idConductorType}/${appConfig.idEstadoUsuarioActivo}`).map((data) => {
      return data.json();
    });
  }

  getConductoresByState(stateDriver: number) {
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-usuarios/usuario/all/${appConfig.idConductorType}/${stateDriver}`).map((data) => {
      return data.json();
    });
  }

  getMisConductores(){
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-conductor/conductor/conductores`,{ headers: headers }).map( data => {
      return data.json();
    });
  }


  getConductorById(idDriver: number) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-conductor/conductor/read/${idDriver}`,{ headers: headers }).map((data) => {
      return data.json();
    });
  }

  getClienteConductor(idClieCond: number) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-usuarios/usuario/read/${idClieCond}`,{ headers: headers }).map((data) => {
      return data.json();
    });
  }

  addConductor(conductor: any) {
    var headers = new Headers();
    headers.append('Content-Type', 'application/json');
    headers.append('Accept', 'application/json');
    return this._http.post(appConfig.apiUrlServicesBase + '/rest-usuarios/usuario/create/', conductor, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  removeChofer(conductor) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    var options = new RequestOptions({headers: headers, body: conductor});
    return this._http.delete(appConfig.apiUrlServicesBase + '/rest-conductor/conductor/delete/', options).map((data) => {
      return data.json();
    });
  }

  getClientesByAll() {
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-client/client/all`).map((data) => {
      this.clientesCombo = data.json();
      return data.json();
    });
  }

  getClientebyTipoCliente(idTipoCliente: any) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-client/client/clientes/${idTipoCliente}`, {headers: headers}).map((data) => {
      this.clientesCombo = data.json();
      return data.json();
    });
  }

  removerVehiculo(vehiculo) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    var options = new RequestOptions({headers: headers, body: vehiculo});
    return this._http.delete(appConfig.apiUrlServicesBase + '/rest-vehiculo/vehiculo/delete/', options).map((data) => {
      return data.json();
    });
  }

  addConductorCamion(conductor) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    var options = new RequestOptions({headers: headers, body: conductor});
    return this._http.post(appConfig.apiUrlServicesBase + '/rest-conductor/conductor/create/', conductor, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  addVehiculo(vehiculo) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    var options = new RequestOptions({headers: headers, body: vehiculo});
    return this._http.post(appConfig.apiUrlServicesBase + '/rest-vehiculo/vehiculo/create/', vehiculo, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  updateVehiculo(vehiculo) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    var options = new RequestOptions({headers: headers, body: vehiculo});
    return this._http.put(appConfig.apiUrlServicesBase + '/rest-vehiculo/vehiculo/update/', vehiculo, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  updateConductorCamion(chofer) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    var options = new RequestOptions({headers: headers, body: chofer});
    return this._http.put(appConfig.apiUrlServicesBase + '/rest-conductor/conductor/update', chofer, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  getMisVehiculos() {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-vehiculo/vehiculo/vehiculos`, {headers: headers}).map((data) => {
      return data.json();
    });
  }
}
