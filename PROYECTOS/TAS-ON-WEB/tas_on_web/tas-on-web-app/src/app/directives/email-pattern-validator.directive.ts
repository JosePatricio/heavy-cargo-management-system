import {Attribute, Directive, forwardRef} from '@angular/core';
import {FormControl, NG_VALIDATORS, ValidationErrors} from "@angular/forms";

@Directive({
  selector: '[emailPattern][ngModel]',
  providers: [
    {
      provide: NG_VALIDATORS,
      useExisting: forwardRef(() => EmailPatternValidatorDirective),
      multi: true
    }
  ]
})

export class EmailPatternValidatorDirective {

  constructor(@Attribute('emailPattern') public datesLessValidator: any) {
  }

  validate(c: FormControl): ValidationErrors {
    var pattern = '[a-zA-Z0-9!#$%&\'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&\'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?';

    if (null != c.value && c.value.match(pattern)) {
      return null;
    } else if (null != c.value && c.value.length > 0) {
      return {
        emailPattern: {
          valid: false
        }
      }
    }
  }

}
