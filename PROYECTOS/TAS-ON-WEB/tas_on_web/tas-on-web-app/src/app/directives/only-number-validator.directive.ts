import {Attribute, Directive, forwardRef} from '@angular/core';
import {FormControl, NG_VALIDATORS, ValidationErrors} from "@angular/forms";

@Directive({
  selector: '[onlyNumber][ngModel]',
  providers: [
    {
      provide: NG_VALIDATORS,
      useExisting: forwardRef(() => OnlyNumberValidatorDirective),
      multi: true
    }
  ]
})
export class OnlyNumberValidatorDirective {

  constructor(@Attribute('onlyNumber') public datesLessValidator: any) {
  }

  validate(c: FormControl): ValidationErrors {
    var pattern = '[^a-z ]\\ *([.0-9])*\\d';

    if (null != c.value && c.value.match(pattern)) {
      return null;
    } else if (null != c.value && c.value.length > 0) {
      return {
        onlyNumber: {
          valid: false
        }
      }
    }
  }

}
