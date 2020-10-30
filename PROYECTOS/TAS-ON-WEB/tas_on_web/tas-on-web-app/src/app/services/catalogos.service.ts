import {Injectable} from '@angular/core';
import {Headers, Http} from "@angular/http";
import {appConfig} from "../app.config";
import {AuthService} from "./auth.service";

@Injectable()
export class CatalogosService {

  constructor(private _http: Http, public _authService: AuthService) {
  }

  getAllCatalogos() {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-catalogo/catalogo/all/`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  getCatalogo(idCatalogo: any) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-catalogo/catalogo/read/${idCatalogo}`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  addCatalogo(catalogo: any) {
    var headers = new Headers();
    headers.append('Content-Type', 'application/json');
    headers.append('Accept', 'application/json');
    return this._http.post(appConfig.apiUrlServicesBase + `/rest-catalogo/catalogo/create/`, catalogo, {headers: headers}).map((data) => {
      console.log(data.json());
      return "OK";
    });
  }
}
