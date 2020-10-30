import {Attribute, Directive, forwardRef} from '@angular/core';
import {FormControl, NG_VALIDATORS, ValidationErrors} from "@angular/forms";

@Directive({
  selector: '[onlyDecimal][ngModel]',
  providers: [
    {
      provide: NG_VALIDATORS,
      useExisting: forwardRef(() => OnlyDecimalNumberDirective),
      multi: true
    }
  ]
})
export class OnlyDecimalNumberDirective {

  constructor(@Attribute('onlyDecimal') public onlyDecimal: any) {
  }

  validate(c: FormControl): ValidationErrors {
    var pattern = '^[\\d]+([.][\\d]{1,2})?$';

    if (null != c.value && c.value.match(pattern)) {
      return null;
    } else if (null != c.value && c.value.length > 0) {
      return {
        onlyDecimal: {
          valid: false
        }
      }
    }
  }

}
