import {Injectable} from '@angular/core';
import {AuthService} from "./auth.service";
import {Headers, Http} from "@angular/http";
import {appConfig} from "../app.config";
import {Observable} from "rxjs";

@Injectable()
export class FacturaService {

  constructor(private _http: Http, public _authService: AuthService) {
  }

  createFactura(cliente: any) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.post(appConfig.apiUrlServicesBase + '/rest-client/client/create', cliente, {headers: headers}).map((data) => {
      return data.json();
    })
  }

  canCreatePreInvoice() {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + '/rest-factura/factura/can-create-preinvoice/', {headers: headers}).map((data) => {
      return data.json();
    })
  }

  createPreInvoice(invoice: any) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.post(appConfig.apiUrlServicesBase + '/rest-factura/factura/create-preinvoice/', invoice, {headers: headers}).map((data) => {
      return data.json();
    })
  }

  generateManualInvoice(invoice: any) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.post(appConfig.apiUrlServicesBase + '/rest-factura/factura/generate-manual/', invoice, {headers: headers}).map((data) => {
      return data.json();
    })
  }
  updateInvoice(invoice: any) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.put(appConfig.apiUrlServicesBase + '/rest-factura/factura/invoice-update/', invoice, {headers: headers}).map((data) => {
      return data.json();
    })
  }

  getCodeFactura() {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-factura/factura/code-factura/`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  getInvoiceListByState(state: number) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-factura/factura/invoices/${state}`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  getInvoiceAllListByState(state: number) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-factura/factura/invoices-all/${state}`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  getInvoiceAllBy(query: string) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-factura/factura/invoices-all-by/${query}`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  getFacturasManualesBy(query: string) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-factura/factura/manual-by/${query}`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  getRetencionesFacturasBy(query: string) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-factura/factura/retencion-by/${query}`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  getInvoiceDetailByNumber(numbPreInv: string) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-factura/factura/invoice-detail/${numbPreInv}`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  getFacturaManualXML(claveAcceso: string){
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return Observable.create(observer => {
      let xhr = new XMLHttpRequest();
      xhr.open('GET', appConfig.apiUrlServicesBase + `/rest-factura/factura/manual/download/XML/${claveAcceso}`, true);
      xhr.setRequestHeader('Authorization', 'Bearer ' + this._authService.getToken());
      xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
      xhr.responseType='blob';
      xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
          if (xhr.status === 200) {
            var contentType = 'application/xml';
            var blob = new Blob([xhr.response], {type: contentType });
            observer.next(blob);
            observer.complete();
          } else {
            observer.error(xhr.response);
          }
        }
      };
      xhr.send();
    });
  }

  getRetencionXML(claveAcceso: string){
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return Observable.create(observer => {
      let xhr = new XMLHttpRequest();
      xhr.open('GET', appConfig.apiUrlServicesBase + `/rest-factura/factura/retencion/download/XML/${claveAcceso}`, true);
      xhr.setRequestHeader('Authorization', 'Bearer ' + this._authService.getToken());
      xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
      xhr.responseType='blob';
      xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
          if (xhr.status === 200) {
            var contentType = 'application/xml';
            var blob = new Blob([xhr.response], {type: contentType });
            observer.next(blob);
            observer.complete();
          } else {
            observer.error(xhr.response);
          }
        }
      };
      xhr.send();
    });
  }
}
