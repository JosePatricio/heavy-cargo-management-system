import {Injectable} from '@angular/core';
import {appConfig} from '../app.config';
import {Headers, Http} from '@angular/http';
import 'rxjs/Rx';
import {AuthService} from "./auth.service";
import {SolicitudEnvioService} from '../services/solicitud-envio.service';
import {Observable} from "rxjs";


@Injectable()
export class OfertaService {
  solicitudesOfertas: any;
  listaSolicitudes: any;
  idSolicitud: String;

  constructor(private _http: Http, public _solicitudEnvioService: SolicitudEnvioService, public _authService: AuthService) {
    this.getSolicitudEnvio();
  }


  getSolicitudEnvio() {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-solicitud-envio/solicitud-envio/solicitudes/${appConfig.estadoSolicitudEnvioActiva}`, {headers: headers}).map((data) => {
      this.listaSolicitudes = data.json();
      return data.json();
    });
  }

  obtenerSolicitud(idSolicitud) {
    //this.idSolicitud = this.listaSolicitudes[index].idSolicitud;
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-solicitud-envio/solicitud-envio/solicitud/${idSolicitud}`, {headers: headers}).map((data) => {
      this.solicitudesOfertas = data.json();
      return data.json();
    });
  }


  obtenerOfertasByEstado(estado) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-oferta/oferta/ofertas/${estado}`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  obtenerAllOfertasByEstado(estado) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-oferta/oferta/ofertas-estado/${estado}`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  obtenerOfertasBy(query) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-oferta/oferta/ofertas-by/${query}`, {headers: headers}).map((data) => {
        return data.json();
    });
  }

  obtenerOfertaByOfertaId(idOferta) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-oferta/oferta/oferta/${idOferta}`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  obtenerSolicitudPorIndex(index) {
    return this.solicitudesOfertas[index];
  }

  ofertar(oferta: any) {
    var headers = new Headers();
    headers.append('Content-Type', 'application/json');
    headers.append('Accept', 'application/json');
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.post(appConfig.apiUrlServicesBase + '/rest-oferta/oferta/ofertar/', oferta, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  obtenerVehiculos(idSolicitud: string) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-vehiculo/vehiculo/vehiculos/${idSolicitud}`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  obtenerConductores(idSolicitud: string) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-conductor/conductor/conductores/${idSolicitud}`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  updateOffer(offer: any) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.post(appConfig.apiUrlServicesBase + '/rest-oferta/oferta/update-oferta/', offer, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  getOfertasBySolicitud(idSolicitud, estado) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-oferta/oferta/ofertas-solicitud/${idSolicitud}/${estado}`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  //TODO eliminarlo cuando lo reemplace el nuevo metodo getFoto
  getDocument(idOferta, estado) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-oferta/oferta/documentos/${idOferta}/${estado}`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  getFoto(fotoId) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-oferta/oferta/foto/${fotoId}`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  generateCashFile(offers: any[]) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return Observable.create(observer => {
      let xhr = new XMLHttpRequest();
      xhr.open('PUT', appConfig.apiUrlServicesBase + '/rest-oferta/oferta/oferta-generate-cash-file/', true);
      xhr.setRequestHeader('Authorization', 'Bearer ' + this._authService.getToken());
      xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
      xhr.responseType='blob';
      xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
          if (xhr.status === 200) {
            var contentType = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet';
            var blob = new Blob([xhr.response], {type: contentType });
            observer.next(blob);
            observer.complete();
          } else {
            observer.error(xhr.response);
          }
        }
      };
      xhr.send(JSON.stringify(offers));
    });
  }



}
