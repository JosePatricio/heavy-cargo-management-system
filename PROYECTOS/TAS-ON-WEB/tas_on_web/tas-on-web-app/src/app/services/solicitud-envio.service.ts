import {Injectable} from '@angular/core';
import {appConfig} from '../app.config';
import {Headers, Http, RequestOptions} from '@angular/http';

import 'rxjs/Rx';
import {AuthService} from "./auth.service";

@Injectable()
export class SolicitudEnvioService {

  solicitudEnvio: any[];
  listaSolicitudes: any[];


  constructor(private _http: Http, public _authService: AuthService) {
  }

  getSolicitudEnvioByEstado(estado: number) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-solicitud-envio/solicitud-envio/solicitudes/${estado}`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  getSolicitudEnvio(idSolicitudEnvio) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-solicitud-envio/solicitud-envio/read/${idSolicitudEnvio}`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  addSolicitudEnvio(solicitudEnvio: any) {
    var headers = new Headers();
    headers.append('Content-Type', 'application/json');
    headers.append('Accept', 'application/json');
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.post(appConfig.apiUrlServicesBase + '/rest-solicitud-envio/solicitud-envio/create/', solicitudEnvio, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  editSolicitudEnvio(solicitudEnvio) {
    var headers = new Headers();
    headers.append('Content-Type', 'application/json');
    headers.append('Accept', 'application/json');
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.put(appConfig.apiUrlServicesBase + '/rest-solicitud-envio/solicitud-envio/update/', solicitudEnvio, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  cancelarSolicitudEnvio(solicitudEnvio: any) {
    var headers = new Headers();
    headers.append('Content-Type', 'application/json');
    headers.append('Accept', 'application/json');
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.post(appConfig.apiUrlServicesBase + '/rest-solicitud-envio/solicitud-envio/solicitud-cancel/', solicitudEnvio, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  removeSolicitudEnvio(solicitudEnvio) {
    var headers = new Headers();
    headers.append('Content-Type', 'application/json');
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    var options = new RequestOptions({headers: headers, body: solicitudEnvio});
    return this._http.delete(appConfig.apiUrlServicesBase + '/rest-solicitud-envio/delete/', options).map((data) => {
      console.log(data.json());
      return "OK";
    });
  }

  getSolicitudEnvioByAll(index) {
    return this.solicitudEnvio[index];
  }

  //consumo de servicio web para cargar secuencial de solicitudenvio


  obtenerSecuencial(rucEmpresaUsuario: any) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-secuencia/secuencia/secuencia/${rucEmpresaUsuario}`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  getDiasHabiles(caducidad: any) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-secuencia/secuencia/dias-habiles/${caducidad}`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  getSolicitudesByIdex(index) {
    return this.listaSolicitudes[index];
  }

  getSolicitudEnvioAllByEstado(estado: number) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-solicitud-envio/solicitud-envio/solicitudes-all/${estado}`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  getCalificacionSolicitudes(){
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-calificacion-transportista/calificacion-transportista/all`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  actualizarCalificacion(solicitudACalificar: any) {
    var headers = new Headers();
    headers.append('Content-Type', 'application/json');
    headers.append('Accept', 'application/json');
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.put(appConfig.apiUrlServicesBase + '/rest-calificacion-transportista/calificacion-transportista/update', solicitudACalificar, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  getSolicitudEnvioOfertas() {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-solicitud-envio/solicitud-envio/solicitudes-ofertas`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  getSolicitudEnvioByIdSolicitud(idSolicitud) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-solicitud-envio/solicitud-envio/solicitud/${idSolicitud}`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  acceptOfertaBySolicitud(offer: any) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.post(appConfig.apiUrlServicesBase + '/rest-solicitud-envio/solicitud-envio/solicitud-oferta/', offer, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  getEstadoSolicitudes(queryRUCCliente: string) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-solicitud-envio/solicitud-envio/estado-solicitudes${queryRUCCliente}`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  getSolicitudesBy(query:string) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-solicitud-envio/solicitud-envio/solicitudes-by${query}`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  getDefaultValues(){
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-solicitud-envio/solicitud-envio/nueva`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  getUltimosDatosOrigen(origen:number){
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-solicitud-envio/solicitud-envio/origen/${origen}`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  getUltimosDatosDestino(destino:number){
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-solicitud-envio/solicitud-envio/destino/${destino}`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

}
