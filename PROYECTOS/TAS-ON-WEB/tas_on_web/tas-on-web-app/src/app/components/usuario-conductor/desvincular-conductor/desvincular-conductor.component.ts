import { Component, OnInit } from '@angular/core';
import {UsuariosService} from "../../../services/usuarios.service";
import {CatalogoItemService} from "../../../services/catalogo-item.service";
import {Router} from "@angular/router";

declare var $: any;

@Component({
  selector: 'app-desvincular-conductor',
  templateUrl: './desvincular-conductor.component.html'
})
export class DesvincularConductorComponent implements OnInit {

  constructor(private _router: Router, public _usuariosService: UsuariosService, private _catalogoItemService: CatalogoItemService) {
  }

  public loading = false;
  catUsers: any[];
  catState: any[];
  idCatUser: number;
  idCatState: number;
  userSelec: any = {};
  usuarios: any[];

  ngOnInit() {
    this.loading = true;
    this._catalogoItemService.getCatalogoItemByCatalogo(2).subscribe(data => {
      this.catUsers = data;
      this.loading = false;
    }, error => {
      this.loading = false;
    });

    this.loading = true;
    this._catalogoItemService.getCatalogoItemByCatalogo(1).subscribe(data => {
      this.catState = data;
      this.loading = false;
    }, error => {
      this.loading = false;
    });
  }

  getUser(tipoUsuario: number, estado: number) {
    if (estado) {
      this.loading = true;
      this._usuariosService.getAllUserDetailByTipoAndStatus
      (tipoUsuario, estado).subscribe(data => {
        this.usuarios = data;
        this.loading = false;
      });
    }
  }

  restablece(userRestablece: any) {
    this.userSelec = userRestablece;
    console.log(userRestablece);
    $("#restableceModal").modal('show');
  }

  restUserPass() {
    console.log(this.userSelec);
  }


}
