import {Injectable} from '@angular/core';
import {Headers, Http} from "@angular/http";
import {appConfig} from "../app.config";

@Injectable()
export class LocalidadService {

  constructor(private _http: Http) {
  }

  addLocalidad(localidad: any) {
    var headers = new Headers();
    headers.append('Content-Type', 'application/json');
    headers.append('Accept', 'application/json');
    return this._http.post(appConfig.apiUrlServicesBase + `/rest-localidad/localidad/create/`, localidad, {headers: headers}).map((data) => {
      return "OK";
    });
  }

  getLocalidadByPadre(idLocalPadre: number) {
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-localidad/localidad/all/${idLocalPadre}/1`).map((data) => {
      return data.json();
    });
  }

  getLocalidadById(idLocal: number) {
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-localidad/localidad/read/${idLocal}/`).map((data) => {
      return data.json();
    });
  }
}
