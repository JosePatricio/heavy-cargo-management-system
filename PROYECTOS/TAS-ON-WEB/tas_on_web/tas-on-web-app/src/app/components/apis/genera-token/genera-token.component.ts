import {Component, OnInit} from '@angular/core';
import {ApiUsersService} from "../../../services/api-users.service";
import {AuthService} from "../../../services/auth.service";

@Component({
  selector: 'app-genera-token',
  templateUrl: './genera-token.component.html'
})
export class GeneraTokenComponent implements OnInit {

  public loading = false;
  tokenUser: any;
  idUser: any;
  generado: boolean = false;

  constructor(private _apiUsersService: ApiUsersService, private _authService: AuthService) {
  }

  ngOnInit() {
    this.idUser = localStorage.getItem('id_user');
  }

  generarToken() {
    this.loading = true;
    this._apiUsersService.getToken(this.idUser).subscribe(data => {
      this.tokenUser = data.token;
      this.loading = false;
      this.generado = true;
    }, error => {
      this.loading = false;
      if(error.status == 403) this._authService.logout();
    })

  }

  toClickBoard() {
    let selBox = document.createElement('textarea');
    selBox.style.position = 'fixed';
    selBox.style.left = '0';
    selBox.style.top = '0';
    selBox.style.opacity = '0';
    selBox.value = this.tokenUser;
    document.body.appendChild(selBox);
    selBox.focus();
    selBox.select();
    document.execCommand('copy');
    document.body.removeChild(selBox);

  }

}
