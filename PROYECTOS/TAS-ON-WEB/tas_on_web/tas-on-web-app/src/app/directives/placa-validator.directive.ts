import {Directive} from '@angular/core';
import {FormControl, NG_ASYNC_VALIDATORS, ValidationErrors, Validator} from "@angular/forms";
import {UtilsCommon} from "../utils/utils.common";
import {Http} from "@angular/http";
import {appConfig} from "../app.config";

@Directive({
  selector: '[placaVal] [ngModel]',
  providers: [
    {
      provide: NG_ASYNC_VALIDATORS,
      useExisting: PlacaValidatorDirective,
      multi: true
    }
  ]
})
export class PlacaValidatorDirective extends UtilsCommon implements Validator {

  constructor(private _http: Http) {
    super();
  }

  validate(c: FormControl): ValidationErrors {
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-public/public/vehiculo-placa/${c.value}`, this.getPublicHeader()).map((data) => {
      var response: any;
      try {
        response = data.json();
      } catch (e) {
        response = {response: 'ERROR'};
      }

      if (null != response.response && response.response === 'ERROR') {
        return null;
      } else {
        return {
          placaval: {
            valid: false
          }
        }
      }

    });
  }
}
