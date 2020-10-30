import {Component, OnInit} from '@angular/core';
import {appConfig} from "../../../../app.config";
import {Router} from "@angular/router";
import {OfertaService} from "../../../../services/oferta.service";
import {AuthService} from "../../../../services/auth.service";

@Component({
  selector: 'app-oferta-curso-listar',
  templateUrl: './oferta-recibir-listar.component.html',
  styleUrls: ['./oferta-recibir-listar.component.css']
})
export class OfertaCursoListarComponent implements OnInit {

  public loading = false;
  ofertas: any;

  constructor(public _router: Router, public _oferta: OfertaService, private _authService: AuthService) {
  }

  ngOnInit() {
    this.loading = true;
    this._oferta.obtenerOfertasByEstado(appConfig.ofertaEstadoCurso).subscribe(
      data => {
        this.ofertas = data;
        this.loading = false;
      },
      error => {
        if(error.status == 403) this._authService.logout();
      }
    );
  }

  callDetailSolicitud(solicitud: string) {
    this._router.navigate(['/panel/ofertas/oferta-en-curso-detail', solicitud]);
  }

}
