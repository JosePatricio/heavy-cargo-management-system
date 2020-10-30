import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {CatalogosService} from "../../../services/catalogos.service";
import {AuthService} from "../../../services/auth.service";

declare var $: any;

@Component({
  selector: 'app-listar-catalogos',
  templateUrl: './listar-catalogos.component.html'
})
export class ListarCatalogosComponent implements OnInit {

  catalogoSelec: any;
  catalogos: any[];
  public loading: any = false;

  constructor(private _router: Router, public _catalogosService: CatalogosService, private _authService: AuthService) {
  }

  ngOnInit() {
    this.loading = true;
    this._catalogosService.getAllCatalogos().subscribe(data => {
      this.catalogos = data;
      this.loading = false;
    }, error => {
      this.loading = false;
      if(error.status == 403) this._authService.logout();
    });
  }

  nuevoCatalogo() {
    this._router.navigate(['/panel/catalogos/catitem', 0]);
  }


  vercatalogoitem(idCatalogo: string) {
    this._router.navigate(['/panel/catalogos/catitem', idCatalogo]);
  }
}
