import {Component, OnInit} from '@angular/core';
import {SolicitudEnvioService} from "../../../../services/solicitud-envio.service";
import {ActivatedRoute, Router} from "@angular/router";
import {OfertaService} from "../../../../services/oferta.service";
import {appConfig} from "../../../../app.config";
import {Archivo} from "../../../../interfaces/archivo";
import {DomSanitizer} from "@angular/platform-browser";
import {AuthService} from "../../../../services/auth.service";
import Swal from "sweetalert2/dist/sweetalert2.all.min.js";

@Component({
  selector: 'app-oferta-entregar-detail',
  templateUrl: './oferta-entregar-detail.component.html',
  styleUrls: ['./oferta-entregar-detail.component.css']
})
export class OfertaEntregarDetailComponent implements OnInit {

  public loading = false;
  idSolicitud: string;
  solicitud: any;
  offer: any;
  appConfig = appConfig;

  imagenesRecoleccion = new Array<any>();
  imagenesRecoleccionOriginales = new Array<any>();

  imagenesEntrega = new Array<Archivo>();
  imagenEntregaSeleccionada = -1;

  operacionCorrecta: boolean = false;
  mensajeOperacion: string = '';
  bloquearBotonActualizar: boolean = false;
  clickedVerFotosRecoleccion: boolean = false;

  constructor(private sanitizer: DomSanitizer, private _router: Router, private _activatedRoute: ActivatedRoute,
              public _solicitudEnvioService: SolicitudEnvioService, public _oferta: OfertaService,
              private _authService: AuthService) {
  }

  ngOnInit() {

    this.solicitud = {};
    this.solicitud.offer = {};
    this.offer = {};
    this.loading = true;
    this._activatedRoute.params.subscribe(parametros => {
      this.idSolicitud = parametros.solicitud;
      this.loading = false;
    });
    this.loading = true;
    this._solicitudEnvioService.getSolicitudEnvioByIdSolicitud(this.idSolicitud).subscribe(
      data => {
        this.solicitud = data;
        this.loading = false;
      },
      error => {
      }
    );

    let that = this;
  }

  onFileChange(event) {
    let reader = new FileReader();
    if (event.target.files && event.target.files.length > 0) {
      let file = event.target.files[0];
      reader.readAsDataURL(file);
      reader.onload = () => {
        if(file.type == "image/jpg" || file.type == "image/jpeg" || file.type == "image/png")
        {
          this.imagenesEntrega.push(
            {
              file: reader.result.split(',')[1],
              fileType: file.type,
              fileName: file.name,
              fileSize: file.size
            });
        } else{
          Swal.fire('','Solo puede subir imágenes en formato jpg, jpeg o png', 'warning');
        }
      };
    }
  }

  seleccionarImagen(imagenIndex){
    if(imagenIndex == this.imagenEntregaSeleccionada){
      this.imagenEntregaSeleccionada = -1;
    } else {
      this.imagenEntregaSeleccionada = imagenIndex;
    }
  }

  eliminarImagen(){
    this.imagenesEntrega.splice(this.imagenEntregaSeleccionada,1);
    this.imagenEntregaSeleccionada = -1;
  }

  updateOfertas() {
    this.loading = true;
    this.bloquearBotonActualizar = true;
    this.offer.idOferta = this.solicitud.offer.idOferta;
    this.offer.fotos = this.imagenesEntrega;
    this._oferta.updateOffer(this.offer).subscribe(data => {
      this.loading = false;
      this.operacionCorrecta = data.response == 'OK';
      this.bloquearBotonActualizar = this.operacionCorrecta;
      Swal.fire('', data.responseMessage, (data.response == 'OK')? 'success' : 'error').then((result) => {
        this.cerrarMensaje();
      });
    }, error => {
      this.loading = false;
      if(error.status == 403) this._authService.logout();
      this.operacionCorrecta = false;
      this.bloquearBotonActualizar = false;
      Swal.fire('', error.message, 'error');
    });
  }

  verFotosRecoleccion(){
    this.loading = true;
    if(this.solicitud.fotosRecoleccion)
      this.solicitud.fotosRecoleccion.forEach(fotoId => this.getFoto(fotoId));
    this.loading = false;
  }

  getFoto(fotoId){
    let that = this;
    this._oferta.getFoto(fotoId).subscribe(data => {
        that.imagenesRecoleccion.push(that.sanitizer.bypassSecurityTrustResourceUrl('data:image/jpg;base64,'+data.fileContent));
        that.imagenesRecoleccionOriginales.push(data.fileContent);
      },
      error => {
        if(error.status == 403) this._authService.logout();
      });
  }

  verImagen(index){
    Swal.fire({
      imageUrl: 'data:image/jpg;base64,'+this.imagenesRecoleccionOriginales[index],
      imageAlt: 'Imagen de recolección',
      showConfirmButton: false
    });
  }

  cerrarMensaje() {
    if (this.operacionCorrecta) {
      this._router.navigate(['/panel/ofertas/oferta-entregar']);
    }
  }
}
