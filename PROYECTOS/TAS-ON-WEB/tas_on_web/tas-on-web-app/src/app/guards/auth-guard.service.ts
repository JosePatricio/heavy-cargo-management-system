import { Injectable } from '@angular/core';
import {Router, ActivatedRouteSnapshot, RouterStateSnapshot, CanActivate} from '@angular/router';
import { AuthService } from '../services/auth.service';
import { Route } from '@angular/router/src/config';

@Injectable()
export class AuthGuardService implements CanActivate{

  constructor(private _authService:AuthService, private _router:Router) { }

  canActivate(next:ActivatedRouteSnapshot, state:RouterStateSnapshot):boolean{

    if(this._authService.isAuthenticated()){
      return true;
    }else{
      this._router.navigate(['/login']);
      return false;
    }

  };

}
