import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {appConfig} from '../../app.config';
import {Router} from '@angular/router';
import {AuthService} from '../../services/auth.service';
import Swal from "sweetalert2/dist/sweetalert2.all.min.js";

@Component({
  selector: 'app-nuevo-password',
  templateUrl: './nuevo-password.component.html',
  styleUrls: ['./nuevo-password.component.css']
})
export class NuevoPasswordComponent implements OnInit {

  public loading = false;
  appConfig = appConfig;

  mostrarContrasenaActual: boolean = false;
  tipoContrasenaActual: string = "password";

  mostrarContrasenaNueva: boolean = false;
  tipoContrasenaNueva: string = "password";

  mostrarContrasenaConfirmar: boolean = false;
  tipoContrasenaConfirmar: string = "password";

  @Input('password') password: any;
  @Output() complete: EventEmitter<boolean> = new EventEmitter<boolean>();

  constructor(private _router: Router, public _authService: AuthService) {
    this.cleanConfirma();
  }

  ngOnInit() {
  }

  updatePassword() {
    this.loading = true;
    this._authService.updatePassword(this.password).subscribe(
      data => {
        this.loading = false;
        Swal.fire('', data.responseMessage, (data.response == 'OK')? 'success' : 'error').then((result) => {
          if(data.response == 'OK'){
            this._router.navigate(['/login']);
            this.complete.emit(true);
          }
        });
      },
      error => {
        this.loading = false;
        Swal.fire('', error.message, 'error');
      }
    );
  }

  return() {
    this.cleanConfirma();
    this.complete.emit(true);
  }

  private cleanConfirma() {
    this.password = {};
  }

  showPasswordActual(){
    this.mostrarContrasenaActual = true;
    this.tipoContrasenaActual = "text";
  }

  hidePasswordActual(){
    this.mostrarContrasenaActual = false;
    this.tipoContrasenaActual = "password";
  }

  showPasswordNueva(){
    this.mostrarContrasenaNueva = true;
    this.tipoContrasenaNueva = "text";
  }

  hidePasswordNueva(){
    this.mostrarContrasenaNueva = false;
    this.tipoContrasenaNueva = "password";
  }

  showPasswordConfirmar(){
    this.mostrarContrasenaConfirmar = true;
    this.tipoContrasenaConfirmar = "text";
  }

  hidePasswordConfirmar(){
    this.mostrarContrasenaConfirmar = false;
    this.tipoContrasenaConfirmar = "password";
  }
}
