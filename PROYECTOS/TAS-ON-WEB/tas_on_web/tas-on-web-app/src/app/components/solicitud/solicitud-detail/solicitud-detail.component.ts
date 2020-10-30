import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Location} from "@angular/common";
import {OfertaService} from "../../../services/oferta.service";
import {AuthService} from "../../../services/auth.service";
import {appConfig} from "../../../app.config";
import {DomSanitizer} from "@angular/platform-browser";
import Swal from "sweetalert2/dist/sweetalert2.all.min.js";

@Component({
  selector: 'app-solicitud-detail',
  templateUrl: './solicitud-detail.component.html',
  styleUrls: ['./solicitud-detail.component.css']
})
export class SolicitudDetailComponent implements OnInit {
  public loading = false;

  idSolicitud: string;
  solicitud: any = {};
  oferta: any = {};
  appConfig = appConfig;

  imagenesRecoleccion = new Array<any>();
  imagenesRecoleccionOriginales = new Array<any>();
  imagenesEntrega = new Array<any>();
  imagenesEntregaOriginales = new Array<any>();
  clickedVerFotosRecoleccion: boolean = false;
  clickedVerFotosEntrega: boolean = false;


  constructor(private _activatedRoute: ActivatedRoute, public _ofertaService: OfertaService, private _location: Location,
              private _authService: AuthService, private sanitizer: DomSanitizer) {
  }

  ngOnInit() {
    this.loading = true;
    this._activatedRoute.params.subscribe(parametros => {
      this.idSolicitud = parametros.idSolicitud;
    });

    this._ofertaService.obtenerSolicitud(this.idSolicitud).subscribe(
      data => {
        this.solicitud = data;
        this.oferta = data.offer;
        this.loading = false;
      },
      error => {
        this.loading = false;
        if(error.status == 403) this._authService.logout();
      });

  }

  verFotosRecoleccion(){
    this.loading = true;
    if(this.solicitud.fotosRecoleccion)
      this.solicitud.fotosRecoleccion.forEach(fotoId => this.getFoto(fotoId, true));
    this.loading = false;
  }

  verFotosEntrega(){
    this.loading = true;
    if(this.solicitud.fotosEntrega)
      this.solicitud.fotosEntrega.forEach(fotoId => this.getFoto(fotoId, false));
    this.loading = false;
  }


  getFoto(fotoId, imagenesRecoleccion:boolean){
    let that = this;
    this._ofertaService.getFoto(fotoId).subscribe(data => {
        if(imagenesRecoleccion){
          that.imagenesRecoleccion.push(that.sanitizer.bypassSecurityTrustResourceUrl('data:image/jpg;base64,'+data.fileContent));
          that.imagenesRecoleccionOriginales.push(data.fileContent);
        }
        else{
          that.imagenesEntrega.push(that.sanitizer.bypassSecurityTrustResourceUrl('data:image/jpg;base64,'+data.fileContent));
          that.imagenesEntregaOriginales.push(data.fileContent);
        }
      },
      error => {
        if(error.status == 403) this._authService.logout();
      });
  }

  verImagenRecoleccion(index){
    Swal.fire({
      imageUrl: 'data:image/jpg;base64,'+this.imagenesRecoleccionOriginales[index],
      imageAlt: 'Imagen de recolección',
      showConfirmButton: false
    });
  }

  verImagenEntrega(index){
    Swal.fire({
      imageUrl: 'data:image/jpg;base64,'+this.imagenesEntregaOriginales[index],
      imageAlt: 'Imagen de entrega',
      showConfirmButton: false
    });
  }

  regresar(){
    this._location.back();
  }
}
