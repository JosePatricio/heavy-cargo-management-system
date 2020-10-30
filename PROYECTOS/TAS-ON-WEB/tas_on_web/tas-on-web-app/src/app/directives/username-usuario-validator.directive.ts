import {Directive} from '@angular/core';
import {FormControl, NG_ASYNC_VALIDATORS, ValidationErrors, Validator} from '@angular/forms';
import {Http} from "@angular/http";
import 'rxjs/Rx';
import 'rxjs/add/operator/map';
import {appConfig} from "../app.config";
import {UtilsCommon} from "../utils/utils.common";

@Directive({
  selector: '[userNameValidator] [ngModel]',
  providers: [
    {
      provide: NG_ASYNC_VALIDATORS,
      useExisting: UsernameUsuarioValidatorDirective,
      multi: true
    }
  ]
})
export class UsernameUsuarioValidatorDirective extends UtilsCommon implements Validator {

  constructor(private _http: Http) {
    super();
  }

  validate(c: FormControl): ValidationErrors {
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-public/public/user/${c.value}`, this.getPublicHeader()).map((data) => {
      var response = data.json();
      if (null != response.response && response.response === 'ERROR') {
        return null;
      } else {
        return {
          username: {
            valid: false
          }
        }
      }

    });
  }
}
