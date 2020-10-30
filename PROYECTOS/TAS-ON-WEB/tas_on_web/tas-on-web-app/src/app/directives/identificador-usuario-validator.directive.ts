import { Directive } from '@angular/core';
import {NG_VALIDATORS, NG_ASYNC_VALIDATORS, Validator, FormControl, ValidatorFn, ValidationErrors } from '@angular/forms';
import {Http, Headers, RequestOptions} from '@angular/http';
import 'rxjs/Rx';
import 'rxjs/add/operator/map';
import { Observable } from 'rxjs/Observable';
import { appConfig } from '../app.config';

@Directive({
  selector: '[identificadorUsuarioValidator] [ngModel]',
  providers: [  
    {  
     provide: NG_ASYNC_VALIDATORS,  
     useExisting: IdentificadorUsuarioValidatorDirective,  
     multi: true  
    }
  ]
})
export class IdentificadorUsuarioValidatorDirective implements Validator{

  constructor(private _http:Http) {}  

  validate(c: FormControl): ValidationErrors {

        //if(null != c.value && (c.value.length==10 || c.value.length==13) ){
          
          return this._http.get(appConfig.apiUrlServicesBase+`/rest-usuarios/usuario/read/${c.value}`).map((data)=>{
           
           var response=data.json();
            console.log(response);
            
            //response: "ERROR"
            if(null!=response.response && response.response==='ERROR'){
              return null;
            }else{
              return {  
                identificadorUsuario: {  
                 valid: false  
                }  
              }
            } 

          });

        //}

  }

}
