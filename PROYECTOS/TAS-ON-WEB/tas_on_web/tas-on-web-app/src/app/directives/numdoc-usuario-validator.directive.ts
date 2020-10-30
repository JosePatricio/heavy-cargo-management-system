import {Directive} from '@angular/core';
import {FormControl, NG_ASYNC_VALIDATORS, ValidationErrors, Validator} from '@angular/forms';
import {Http} from "@angular/http";
import 'rxjs/Rx';
import 'rxjs/add/operator/map';
import {appConfig} from "../app.config";
import {UtilsCommon} from "../utils/utils.common";

@Directive({
  selector: '[numDocUserVal] [ngModel]',
  providers: [
    {
      provide: NG_ASYNC_VALIDATORS,
      useExisting: NumdocUsuarioValidatorDirective,
      multi: true
    }
  ]
})
export class NumdocUsuarioValidatorDirective extends UtilsCommon implements Validator {

  constructor(private _http: Http) {
    super();
  }

  validate(c: FormControl): ValidationErrors {
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-public/public/usuario-read/${c.value}`, this.getPublicHeader()).map((data) => {
      var response = data.json();
      if (null != response.response && response.response === 'ERROR') {
        return null;
      } else {
        return {
          numdoc: {
            valid: false
          }
        }
      }

    });
  }
}
