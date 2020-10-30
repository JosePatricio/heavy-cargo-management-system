import {Injectable} from '@angular/core';
import {appConfig} from '../app.config';
import {Http, Headers} from '@angular/http';
import 'rxjs/Rx';
import {AuthService} from "./auth.service";

@Injectable()
export class UtilService {

  constructor(private _http: Http, public _authService: AuthService) {
  }

  obtenerLocalidad(idLocalidadPadre: any) {
    var headers = new Headers();
    //   headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-localidad/localidad/all/${idLocalidadPadre}/1`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  obtenerLocalidadById(idLocalidad: any) {
    var headers = new Headers();
    // headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-localidad/localidad/read/${idLocalidad}`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  obtenerCatalogoItem(idCatalogo) {
    var headers = new Headers();
    //headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-catalogo-item/catalogo-item/all/${idCatalogo}`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  //Recupera la informacion de Usuariopo
  obtenerUsuariobyNombre(nombreUduario) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-usuarios/usuario/user/${nombreUduario}`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  //Obtiene la posicion segun las coordenadas del navegador
  getPosition(): Promise<any>
  {
    return new Promise((resolve, reject) => {
      navigator.geolocation.getCurrentPosition(resp => {
          resolve({lng: resp.coords.longitude, lat: resp.coords.latitude});
        },
        err => {
          alert(err.message);
          reject(err);
        },
        { enableHighAccuracy: true, timeout: 5000});
    });

  }

}
