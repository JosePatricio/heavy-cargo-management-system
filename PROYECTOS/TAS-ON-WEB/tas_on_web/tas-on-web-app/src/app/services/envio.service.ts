import {Injectable} from '@angular/core';
import {appConfig} from '../app.config';
import {Headers, Http} from '@angular/http';

import 'rxjs/Rx';
import {AuthService} from "./auth.service";

@Injectable()
export class EnvioService {

  constructor(private _http: Http, public _authService: AuthService) {
  }

  validarEnviosFile(archivoEnvios: any){
    var headers = new Headers();
    headers.append('Content-Type', 'application/json');
    headers.append('Accept', 'application/json');
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.post(appConfig.apiUrlServicesBase + '/rest-envio/envio/archivo/validar/', archivoEnvios, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  cargarEnviosFile(archivoEnvios: any){
    var headers = new Headers();
    headers.append('Content-Type', 'application/json');
    headers.append('Accept', 'application/json');
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.post(appConfig.apiUrlServicesBase + '/rest-envio/envio/archivo/cargar/', archivoEnvios, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  obtenerEnviosPendientes(){
    var headers = new Headers();
    headers.append('Content-Type', 'application/json');
    headers.append('Accept', 'application/json');
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-envio/envio/pendientes`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  obtenerEnviosByEstado(estado){
    var headers = new Headers();
    headers.append('Content-Type', 'application/json');
    headers.append('Accept', 'application/json');
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-envio/envio/by?estado=${estado}`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  obtenerEnviosBy(query){
    var headers = new Headers();
    headers.append('Content-Type', 'application/json');
    headers.append('Accept', 'application/json');
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-envio/envio/by${query}`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  obtenerFotosId(envioId){
    var headers = new Headers();
    headers.append('Content-Type', 'application/json');
    headers.append('Accept', 'application/json');
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-envio/envio/fotos?envioId=${envioId}`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  guardarFotos(fotos: any){
    var headers = new Headers();
    headers.append('Content-Type', 'application/json');
    headers.append('Accept', 'application/json');
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.post(appConfig.apiUrlServicesBase + '/rest-envio/envio/fotos/subir/', fotos, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  getFoto(fotoId){
    var headers = new Headers();
    headers.append('Content-Type', 'application/json');
    headers.append('Accept', 'application/json');
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-envio/envio/fotos/ver?fotoId=${fotoId}`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

}
