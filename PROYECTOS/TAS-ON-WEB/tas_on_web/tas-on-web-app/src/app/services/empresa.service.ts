import { Injectable } from '@angular/core';
import {Headers, Http, RequestOptions} from "@angular/http";
import {appConfig} from "../app.config";
import {AuthService} from "./auth.service";

@Injectable()
export class EmpresaService {

  constructor(private _http: Http, public _authService: AuthService) { }

  createEmpresa(cliente:any){
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.post(appConfig.apiUrlServicesBase + '/rest-client/client/create', cliente, { headers: headers }).map((data) => {
      return data.json();
    })
  }

  readEmpresa(ruc:string){
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-client/client/read/${ruc}`, { headers: headers }).map((data) => {
      return data.json();
    });
  }

  updateEmpresa(cliente:any){
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.put(appConfig.apiUrlServicesBase + '/rest-client/client/update', cliente, { headers: headers }).map((data) => {
      return data.json();
    });
  }

  deleteEmpresa(cliente:any){
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    var options = new RequestOptions({ headers: headers, body: cliente });
    return this._http.delete(appConfig.apiUrlServicesBase + '/rest-client/client/delete/', options).map((data) => {
      return data.json();
    });
  }

  getAllEmpresa(){
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-client/client/all`, { headers: headers }).map((data) => {
      return data.json();
    });
  }

  getAllEmpresaByType(typeEmp:any){
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-client/client/clientes/${typeEmp}`, { headers: headers }).map((data) => {
      return data.json();
    });
  }

  getClienteByToken() {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + '/rest-client/client/read-auth', {headers: headers}).map((data) => {
      return data.json();
    });
  }

}
