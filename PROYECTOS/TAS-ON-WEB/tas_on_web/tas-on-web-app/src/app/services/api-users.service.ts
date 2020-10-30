import {Injectable} from '@angular/core';
import {Headers, Http} from "@angular/http";
import {AuthService} from "./auth.service";
import {appConfig} from "../app.config";

@Injectable()
export class ApiUsersService {

  constructor(private _http: Http, public _authService: AuthService) {
  }

  getToken(user: string) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-api/service-api/token/`, {headers: headers}).map((data) => {
      return data.json();
    });
  }
}
