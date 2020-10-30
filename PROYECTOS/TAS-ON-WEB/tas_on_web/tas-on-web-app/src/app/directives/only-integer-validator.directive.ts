import {Attribute, Directive, forwardRef} from '@angular/core';
import {FormControl, NG_VALIDATORS, ValidationErrors} from "@angular/forms";

@Directive({
  selector: '[onlyInteger][ngModel]',
  providers: [
    {
      provide: NG_VALIDATORS,
      useExisting: forwardRef(() => OnlyIntegerValidatorDirective),
      multi: true
    }
  ]
})
export class OnlyIntegerValidatorDirective {

  constructor(@Attribute('onlyInteger') public datesLessValidator: any) {
  }

  validate(c: FormControl): ValidationErrors {
    const pattern = '^[0-9]*$';
    let value = c.value;

    if (typeof c.value === 'number')
      value = c.value.toString();

    if (null != value && value.match(pattern)) {
      return null;
    } else if (null != value && value.length > 0) {
      return {
        onlyInteger: {
          valid: false
        }
      }
    }
  }

}
