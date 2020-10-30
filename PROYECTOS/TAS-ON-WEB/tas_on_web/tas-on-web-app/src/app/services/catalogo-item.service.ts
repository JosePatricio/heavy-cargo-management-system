import {Injectable} from '@angular/core';
import {Headers, Http} from "@angular/http";
import {appConfig} from "../app.config";

@Injectable()
export class CatalogoItemService {

  constructor(private _http: Http) {
  }

  addItemCatalogo(catalogoItem: any) {
    var headers = new Headers();
    headers.append('Content-Type', 'application/json');
    headers.append('Accept', 'application/json');
    return this._http.post(appConfig.apiUrlServicesBase + `/rest-catalogo-item/catalogo-item/create/`, catalogoItem, {headers: headers}).map((data) => {
      console.log(data.json());
      return "OK";
    });
  }

  getCatalogoItemByCatalogo(idCatalogo: number) {
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-catalogo-item/catalogo-item/all/${idCatalogo}`).map((data) => {
      return data.json();
    });
  }

  getCatalogoItemByCatalogoItem(idCatalogo: number) {
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-catalogo-item/catalogo-item/read/${idCatalogo}`).map((data) => {
      return data.json();
    });
  }
}
