import {Component, OnInit, ViewChild} from '@angular/core';
import {appConfig} from "../../app.config";
import {Router} from "@angular/router";
import {PublicService} from "../../services/public.service";
import {NgForm} from "@angular/forms";
import Swal from "sweetalert2/dist/sweetalert2.all.min.js";

@Component({
  selector: 'app-recuperar-contrasenia',
  templateUrl: './recuperar-contrasenia.component.html',
  styleUrls: ['./recuperar-contrasenia.component.css']
})
export class RecuperarContraseniaComponent implements OnInit {
  public loading = false;
  usuario: any = {};
  appConfig = appConfig;

  @ViewChild('forma') forma: NgForm;

  constructor(private _router: Router, private _publicService: PublicService) {
  }

  ngOnInit() {
  }

  public resolved(captchaResponse: string) {
    console.log(`Resolved captcha with response ${captchaResponse}:`);
  }

  sendRequest() {
    this.loading = true;
    this._publicService.restablecerPassword(this.usuario.correoRecupera, this.usuario.identificador).subscribe(
      data => {
        this.loading=false;
        Swal.fire('', data.responseMessage, (data.response == 'OK')? 'success' : 'error').then((result) => {
          this.close(data.response == 'OK');
        });
        this.loading = false;
      },
      error => {
        this.loading = false;
        Swal.fire('', error.message, 'error');
      }
    );
  }

  close(operacionCorrecta: boolean) {
    if (operacionCorrecta) {
      this._router.navigate(['/login']);
    }
    this.forma.resetForm();
  }

}
