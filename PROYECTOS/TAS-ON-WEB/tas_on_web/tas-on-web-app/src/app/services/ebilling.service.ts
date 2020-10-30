import {Injectable} from '@angular/core';
import {Headers, Http} from "@angular/http";
import {appConfig} from "../app.config";
import {AuthService} from "./auth.service";
import {Observable} from "rxjs";

@Injectable()
export class EbillingService {

  constructor(private _http: Http, public _authService: AuthService) {
  }

  getTokenHeader() {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return headers;
  }

  getAdquiriente(ruc:string){
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-ebilling/ebilling/adquiriente/${ruc}`, { headers: this.getTokenHeader() }).map((data) => {
      return data.json();
    });
  }

  getEBillingUserByUserID(userID: any) {
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-ebilling/ebilling/usuario/${userID}`, { headers: this.getTokenHeader() }).map((data) => {
      if(data.status == 204) return null;
      return data.json();
    });
  }

  createUser(usuarioEbilling:any){
    return this._http.post(appConfig.apiUrlServicesBase + '/rest-ebilling/ebilling/update/usuario', usuarioEbilling, { headers: this.getTokenHeader() }).map((data) => {
      return data.json();
    })
  }

  getEbillings(){
    return this._http.get(appConfig.apiUrlServicesBase + '/rest-ebilling/ebilling/all', { headers: this.getTokenHeader() }).map((data) => {
      return data.json();
    })
  }

  sendEbilling(ebillingRequest: any){
    return this._http.post(appConfig.apiUrlServicesBase + '/rest-ebilling/ebilling/send', ebillingRequest, { headers: this.getTokenHeader() }).map((data) => {
      return data.json();
    })
  }

  getRIDE(claveAcceso: string){
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return Observable.create(observer => {
      let xhr = new XMLHttpRequest();
      xhr.open('GET', appConfig.apiUrlServicesBase + `/rest-ebilling/ebilling/download/RIDE/${claveAcceso}`, true);
      xhr.setRequestHeader('Authorization', 'Bearer ' + this._authService.getToken());
      xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
      xhr.responseType='blob';
      xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
          if (xhr.status === 200) {
            var contentType = 'application/pdf';
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

  getXML(claveAcceso: string){
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return Observable.create(observer => {
      let xhr = new XMLHttpRequest();
      xhr.open('GET', appConfig.apiUrlServicesBase + `/rest-ebilling/ebilling/download/XML/${claveAcceso}`, true);
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
