import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {CatalogoItemService} from "../../../../services/catalogo-item.service";
import {UsuariosService} from "../../../../services/usuarios.service";

declare var $: any;

@Component({
  selector: 'app-lista-desbloquea',
  templateUrl: './lista-desbloquea.component.html'
})
export class ListaDesbloqueaComponent implements OnInit {

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
      this.catUsers = data.filter(catItem => catItem.catalogoItemDescripcion !== 'ADMINISTRADOR');
      this.loading = false;
    });
    this.loading = true;
    this._catalogoItemService.getCatalogoItemByCatalogo(1).subscribe(data => {
      this.catState = data;
      this.loading = false;
    });
  }

  getUser(tipoUsuario: number) {
    this.loading = true;
    this._usuariosService.getAllUserDetailByTipoAndStatus(tipoUsuario, 4).subscribe(data => {
      this.usuarios = data;
      this.loading = false;
    });
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
