import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {appConfig} from '../app.config';
import {Headers, Http} from '@angular/http';

@Injectable()
export class AuthService {

  constructor(private _http: Http, private _router: Router) {
  }


  public login(idUsuario: string, password: string) {
    var headers = new Headers();
    headers.append('Content-Type', 'x-www-form-urlencoded');
    let body = `username=${idUsuario}&password=${password}`;
    return this._http.post(appConfig.apiUrlServicesBase + '/rest-login/login/web/', body, {headers: headers}).map((r) => {
      if (null != r) {
        let dataJson = JSON.stringify(r.json());
        let data = JSON.parse(dataJson);
        if(data.response == "OK"){
          if (data.estadoUsuario == "1") {
            localStorage.setItem('id_user', idUsuario);
            localStorage.setItem('data', dataJson);
            this._router.navigate(['/panel/portada']);
          } else
            return data.estadoUsuario;
        } else{
          throw new Error(data.responseMessage)
        }
      }
    });
  }

  public updatePassword(password) {
    var headers = new Headers();
    headers.append('Content-Type', 'application/json');
    headers.append('Accept', 'application/json');
    return this._http.post(appConfig.apiUrlServicesBase + '/rest-login/login/change-password/', password, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  public solicitarActualizarInformacionBancaria(password: string) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this.getToken());
    headers.append('Content-Type', 'application/json');
    return this._http.post(appConfig.apiUrlServicesBase + '/rest-security/security/banco/solicitud/update', password, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  public isAuthenticated(): boolean {
    let id_user: string = localStorage.getItem('id_user');
    if (null != id_user && id_user.length > 0) {
      return true;
    } else {
      return false;
    }
  }

  public logout() {
    localStorage.removeItem('id_user');
    localStorage.clear();
    this._router.navigate(['/login']);
  }

  public logoutSesion() {
    localStorage.removeItem('id_user');
    localStorage.clear();
    this._router.navigate(['/sesionCaducada']);
  }

  public validar() {
    if (this.isAuthenticated()) {
      this._router.navigate(['/panel']);
    } else {
      this._router.navigate(['/login']);

    }
  }

  public validaIndex() {
    if (this.isAuthenticated()) {
      this._router.navigate(['/panel']);
    } else {
      this._router.navigate(['/']);

    }
  }

  public getIdUsuario(): string {
    let nomUser = JSON.parse(localStorage.getItem("data"));
    if(!nomUser) return null;
    return nomUser.nombres;
  }

  public getToken(): string {
    let data = JSON.parse(localStorage.getItem("data"));
    return data.token;
  }

  public getTypeName() {
    let data = JSON.parse(localStorage.getItem("data"));
    return data.tipoUsuarioDesc;
  };

  public getTypeUser(): number {
    let dataStore = JSON.parse(localStorage.getItem("data"));
    return Number(dataStore.tipoUsuario);
  }

  public getRucEmpresa(): number {
    let dataStore = JSON.parse(localStorage.getItem("data"));
    return dataStore.rucEmpresa;
  }

  public getClienteNotaCredito(): any {
    let dataStore = JSON.parse(localStorage.getItem("data"));
    return dataStore.notaCredito;
  }

  public getHorasCaducidadSolicitud(): any {
    let dataStore = JSON.parse(localStorage.getItem("data"));
    return dataStore.horasCaducidadSolicitud;
  }

  private getCatalogo(idCatalogo: any) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-catalogo-item/catalogo-item/read/${idCatalogo}`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

}
