import {Injectable} from '@angular/core';
import {Headers, Http} from "@angular/http";
import {AuthService} from "./auth.service";
import {appConfig} from '../app.config';

@Injectable()
export class PagoService {

  constructor(private _http: Http, public _authService: AuthService) {
  }

  public createPago(pago: any, esRetencion: boolean) {
    var headers = new Headers();
    var url = `/rest-pago/pago/create/`;
    headers.append('Content-Type', 'application/json');
    headers.append('Accept', 'application/json');
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    if(esRetencion) url = `/rest-pago/pago/create-retencion/`;
    return this._http.post(appConfig.apiUrlServicesBase + url, pago, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  public getPagos(facturaId: string) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-pago/pago/pagos/${facturaId}`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  public getPagoDetail(pagoId: number) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-pago/pago/pago-detail/${pagoId}`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  public getRetencionDetail(claveAcceso: string, facturaId: string) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-pago/pago/pago-retencion/${claveAcceso}/${facturaId}`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  public getNotaCreditoDetail(facturaId: string) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-pago/pago/pago-nota-credito/consultar/${facturaId}`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

}
