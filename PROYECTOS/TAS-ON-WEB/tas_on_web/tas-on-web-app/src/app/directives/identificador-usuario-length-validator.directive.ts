import { Directive } from '@angular/core';
import { NG_VALIDATORS, Validator, FormControl, ValidatorFn} from '@angular/forms';

@Directive({
  selector: '[identificadorUsuarioLengthValidator] [ngModel]',
  providers: [  
    {  
     provide: NG_VALIDATORS,  
     useExisting: IdentificadorUsuarioLengthValidatorDirective,  
     multi: true  
    }
  ]
})
export class IdentificadorUsuarioLengthValidatorDirective implements Validator {

  validator: ValidatorFn; 

  constructor() { 
    this.validator = this.identificadorUsuarioLengthValidator();  
  }

  validate(c: FormControl) {  
    return this.validator(c);  
   }  

   identificadorUsuarioLengthValidator(): ValidatorFn {  
    return (c: FormControl) => {

      if(null!=c.value && ''!=c.value){
        if (c.value.length === 10 || c.value.length === 13) {  
          return null;  
         } else {  
          return {  
           identificadorUsuarioTamanio: {  
            valid: false  
           }  
          };  
         }  
      }

    }  
   }  

}
