import {Attribute, Directive, forwardRef} from '@angular/core';
import {AbstractControl, NG_VALIDATORS, ValidationErrors} from "@angular/forms";

@
  Directive({
    selector: '[ivaValidator][formControlName],[ivaValidator][formControl],[ivaValidator][ngModel]',
    providers: [
      {
        provide: NG_VALIDATORS,
        useExisting: forwardRef(() => IvaValidatorDirective),
        multi: true
      }
    ]
  })
export class IvaValidatorDirective {

  constructor(@Attribute('ivaValidator') public ivaValidator: any) {
  }

  validate(c: AbstractControl): ValidationErrors {
    let e = c.root.get(this.ivaValidator);
    let subIVA = parseFloat(e.value ? e.value : 0);
    let IVA = parseFloat(c.value ? c.value : 0);
    let chkIva = this.divider(12, IVA) * subIVA;
    const validator = chkIva ? Number(chkIva.toFixed(2)) >= (99.98) : false;

    if ((subIVA === 0 && IVA === 0) || (null != c.value && validator)) {
      return null;
    } else if (!validator) {
      return {
        ivaValidator: {
          valid: false
        }
      }
    }
  }

  divider(a, b) {
    if (b === 0)
      return null;

    return a / b;
  }

}
