import {Attribute, Directive, forwardRef} from '@angular/core';
import {AbstractControl, NG_VALIDATORS, Validator} from "@angular/forms";
import {UtilsCommon} from "../utils/utils.common";

@Directive({
  selector: '[datesLessValidator][formControlName],[datesLessValidator][formControl],[datesLessValidator][ngModel]',
  providers: [
    {
      provide: NG_VALIDATORS,
      useExisting: forwardRef(() => DatesLessValidatorDirective),
      multi: true
    }
  ]
})
export class DatesLessValidatorDirective implements Validator {

  constructor(@Attribute('datesLessValidator') public datesLessValidator: any) {
  }

  validate(c: AbstractControl): { [key: string]: any } {
    let v = c.value;
    let e: any;
    // control vlaue
    e = c.root.get(this.datesLessValidator);
    if (e !== null && !UtilsCommon.empty(e.value) &&
      UtilsCommon.empty(e.value.jsdate)) {
      const dateFormat = UtilsCommon.dataForDatePiker(e.value);
      e = {};
      e.value = dateFormat;
    }
    if (c !== null && !UtilsCommon.empty(c.value) &&
      UtilsCommon.empty(c.value.jsdate)) {
      const dateFormat = UtilsCommon.dataForDatePiker(c.value);
      v = dateFormat;
    }
    if (e == null) {
      if (v != null && v != '' && v.jsdate > new Date()) {
        return {
          dateLess: {
            valid: false
          }
        }
      }
    } else if (this.checkData(v, e)) {
      return {
        dateLess: {
          valid: false
        }
      }
    } else {
      return null;
    }

  }

  checkData(dateJSV, dateJSE) {
    if (dateJSV != null && dateJSV != '' && dateJSV.jsdate < dateJSE.value.jsdate)
      return true;
    else if (dateJSE.value != null && typeof dateJSE.value.jsdate === 'object') {
      if (dateJSV != null && dateJSV != '' && dateJSV.jsdate.jsdate)
        return true;
    }

    return false;
  }

}
