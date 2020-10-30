import {Injectable} from '@angular/core';
import {AuthService} from "./auth.service";
import {Headers, Http} from "@angular/http";
import {appConfig} from "../app.config";

@Injectable()
export class FacturaProveedorService {

  constructor(private _http: Http, public _authService: AuthService) {
  }

  createFacturaProv(factProv: any) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.post(appConfig.apiUrlServicesBase + '/rest-factura-proveedor/factura-proveedor/create/', factProv, {headers: headers}).map((data) => {
      return data.json();
    })
  }

  updateFacturaProv(invoices: any) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.put(appConfig.apiUrlServicesBase + '/rest-factura-proveedor/factura-proveedor/update/', invoices, {headers: headers}).map((data) => {
      return data.json();
    })
  }

  getFacturaProvByIdFact() {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-factura-proveedor/factura-proveedor/factura/code-factura/`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  getFacturaProvAllByIdEstado(estado: number) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-factura-proveedor/factura-proveedor/by-estado/${estado}`, {headers: headers}).map((data) => {
      return data.json();
    });
  }
}
