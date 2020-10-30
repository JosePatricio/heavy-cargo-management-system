import {Injectable} from '@angular/core';
import {Http} from "@angular/http";
import {appConfig} from "../app.config";
import {UtilsCommon} from "../utils/utils.common";

@Injectable()
export class PublicService extends UtilsCommon {

  constructor(private _http: Http) {
    super();
  }

  getLocalidadByPadre(idLocalidadPadre: any, estado: number) {
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-public/public/localidad-all/${idLocalidadPadre}/${estado}`, this.getPublicHeader()).map((data) => {
      return data.json();
    });
  }

  getCatalogoItemByCatalogo(idCatalogo: any) {
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-public/public/catalogo-item-all/${idCatalogo}`, this.getPublicHeader()).map((data) => {
      return data.json();
    });
  }

  getUserByEmail(email: string) {
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-public/public/email/${email}`, this.getPublicHeader()).map((data) => {
      return data.json();
    });
  }

  getUserByUserName(username: string) {
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-public/public/user/${username}`, this.getPublicHeader()).map((data) => {
      return data.json();
    });
  }

  getAllEmpresaByType(idTipo: number) {
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-public/public/empresa-clientes/${idTipo}`, this.getPublicHeader()).map((data) => {
      return data.json();
    });
  }

  createEmpresaCliente(data: any) {
    return this._http.post(appConfig.apiUrlServicesBase + `/rest-public/public/empresa-create/`, data, this.getPublicHeader()).map((data) => {
      return data.json();
    });
  }

  createUsuarioEmpresa(usuario: any) {
    return this._http.post(appConfig.apiUrlServicesBase + `/rest-public/public/usuario-create/`, usuario, this.getPublicHeader()).map((data) => {
      return data.json();
    });
  }

  vehiculoPlaca(placa: any) {
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-public/public/vehiculo-placa/${placa}`, this.getPublicHeader()).map((data) => {
      return data.json();
    });
  }

  restablecerPassword(email: string, identificador: string) {
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-public/public/usuario-restablecer/${email}/${identificador}`, this.getPublicHeader()).map((data) => {
      return data.json();
    });
  }

  solicitudesAllById(idSolicitud: number) {
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-public/public/solicitudes-all/${idSolicitud}`, this.getPublicHeader()).map((data) => {
      return data.json();
    });
  }

  getEmpresaByRuc(ruc: string){
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-public/public/empresa-read/${ruc}`, this.getPublicHeader()).map((data) => {
      return data.json();
    });
  }

  getEmpresaTransporteByRuc(ruc: string){
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-public/public/empresa-transporte-read/${ruc}`, this.getPublicHeader()).map((data) => {
      return data.json();
    });
  }

  getHome(){
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-public/public/home`, this.getPublicHeader()).map((data) => {
      return data.json();
    });
  }

}
