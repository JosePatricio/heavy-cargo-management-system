import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {SolicitudEnvioService} from "../../../../services/solicitud-envio.service";
import {OfertaService} from "../../../../services/oferta.service";
import {appConfig} from "../../../../app.config";
import {Archivo} from "../../../../interfaces/archivo";
import {AuthService} from "../../../../services/auth.service";
import Swal from "sweetalert2/dist/sweetalert2.all.min.js";

@Component({
  selector: 'app-oferta-curso-detail',
  templateUrl: './oferta-recibir-detail.component.html',
  styleUrls: ['./oferta-recibir-detail.component.css']
})
export class OfertaCursoDetailComponent implements OnInit {

  public loading = false;
  idSolicitud: string;
  solicitud: any;
  fileStart: any;
  appConfig = appConfig;
  imagenes = new Array<Archivo>();
  imagenSeleccionada = -1;

  offer: any;
  operacionCorrecta: boolean = false;
  mensajeOperacion: string = '';
  bloquearBotonActualizar: boolean = false;

  constructor(private _router: Router, private _activatedRoute: ActivatedRoute, private _authService: AuthService,
              public _solicitudEnvioService: SolicitudEnvioService, public _oferta: OfertaService) {
  }

  ngOnInit() {
    this.solicitud = {};
    this.solicitud.offer = {};
    this.offer = {};
    this.fileStart = {};
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
        this.loading = false;
        if(error.status == 403) this._authService.logout();
      }
    );

  }

  seleccionarImagen(imagenIndex){
    if(imagenIndex == this.imagenSeleccionada){
      this.imagenSeleccionada = -1;
    } else {
      this.imagenSeleccionada = imagenIndex;
    }
  }

  eliminarImagen(){
    this.imagenes.splice(this.imagenSeleccionada,1);
    this.imagenSeleccionada = -1;
  }

  onFileChange(event) {
    let reader = new FileReader();
    if (event.target.files && event.target.files.length > 0) {
      let file = event.target.files[0];
      reader.readAsDataURL(file);
      reader.onload = () => {
          if(file.type == "image/jpg" || file.type == "image/jpeg" || file.type == "image/png")
          {
            this.imagenes.push(
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

  updateOfertas() {
    this.bloquearBotonActualizar = true;
    this.offer.idOferta = this.solicitud.offer.idOferta;
    this.offer.fotos = this.imagenes;
    this.loading = true;
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

  cerrarMensaje() {
    if (this.operacionCorrecta) {
      this._router.navigate(['/panel/ofertas/oferta-en-curso']);
    }
  }

}
