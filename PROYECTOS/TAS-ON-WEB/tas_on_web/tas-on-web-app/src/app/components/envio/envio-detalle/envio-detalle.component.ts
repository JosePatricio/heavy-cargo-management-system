import {Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {UtilService} from "../../../services/util.service";
import {NotificationsService} from "angular2-notifications";
import {AuthService} from "../../../services/auth.service";
import {appConfig} from "../../../app.config";
import {Archivo} from "../../../interfaces/archivo";
import {EnvioService} from "../../../services/envio.service";
import {DomSanitizer} from "@angular/platform-browser";
import {NgForm} from "@angular/forms";

declare var $: any;

@Component({
  selector: 'app-envio-detalle',
  templateUrl: './envio-detalle.component.html',
  styleUrls: ['./envio-detalle.component.css']
})
export class EnvioDetalleComponent implements OnInit {

  @Input('envio') envio;
  @Output() complete: EventEmitter<boolean> = new EventEmitter<boolean>();

  @ViewChild('forma') forma: NgForm;
  loading = false;
  appConfig = appConfig;
  imagenes = new Array<Archivo>();
  operacionCorrecta: boolean = false;
  mensajeOperacion: string = '';
  imagenSeleccionada = -1;
  fileStart: any = {};
  deshabilitarGuardar: boolean = false;

  options = {
    position: ["top", "right"],
    timeOut: 5000,
    showProgressBar: true,
    pauseOnHover: true,
    clickToClose: true,
    maxLength: 10
  };

  estadoCompletado: boolean = false;
  envioPago: number;
  public show: any;
  verImagenSeleccionada;
  clickedVerFotosEntrega: boolean = false;
  clickedVerFotosRecoleccion: boolean = false;

  imagenesRecoleccionIds = new Array<number>();
  imagenesRecoleccion = new Array<any>();
  imagenesRecoleccionIdsCargadas = new Array<number>();
  imagenesEntregaIds = new Array<number>();
  imagenesEntrega = new Array<any>();
  imagenesEntregaIdsCargadas = new Array<number>();

  constructor(private sanitizer: DomSanitizer, private _router: Router, private _activatedRoute: ActivatedRoute, private _utilService: UtilService,
              private _notification: NotificationsService, public _authService: AuthService, private _envioService : EnvioService) { }

  ngOnInit() {
    this.consultarFotos();
  }

  consultarFotos(){
    this.loading = true;
    this._envioService.obtenerFotosId(this.envio.envioId).subscribe(
      data => {
        this.imagenesRecoleccionIds = data.recoleccion;
        this.imagenesEntregaIds = data.entrega;
        this.loading = false;
        this.imagenes = new Array<Archivo>();
        this.deshabilitarGuardar = false;
        this.comprobarFotosCargadas();
      },
      error => {
        this.loading = false;
        if(error.status == 403) this._authService.logout();
      }
    );
  }

  regresar() {
    if(this.estadoCompletado) this.actualizar();
    else this.complete.emit(false);
  }

  actualizar() {
    this.complete.emit(true);
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
          this.operacionCorrecta = false;
          this.show = true;
          this.mensajeOperacion = "Solo puede subir imágenes en formato jpg, jpeg o png";
          $("#myModal").modal('show');
        }
      };
    }
  }

  comprobarFotosCargadas(){
    if(this.imagenesRecoleccionIds && this.clickedVerFotosRecoleccion){
      this.imagenesRecoleccionIds.forEach(fotoId=>{
        if(!this.imagenesRecoleccionIdsCargadas.find(f => f == fotoId))
          this.getFoto(fotoId, true);
      })
    }
    if(this.imagenesEntregaIds && this.clickedVerFotosEntrega){
      this.imagenesEntregaIds.forEach(fotoId=>{
        if(!this.imagenesEntregaIdsCargadas.find(f => f == fotoId))
          this.getFoto(fotoId, false);
      })
    }
  }

  guardarFotos(){
    this.loading = true;
    this.deshabilitarGuardar = true;
    let objetoEnviar: any = {};
    objetoEnviar.envioId = this.envio.envioId;
    objetoEnviar.fotos = this.imagenes;
    objetoEnviar.estadoCompletado = this.estadoCompletado;
    objetoEnviar.pago = null;
    if(this.estadoCompletado && this.envio.envioTipo == 'C') objetoEnviar.pago = this.envioPago;

    this._envioService.guardarFotos(objetoEnviar).subscribe(data => {
        if(data.response == "OK") this._notification.success(data.responseMessage);
        else this._notification.warn(data.responseMessage);
      },
      error => {
        this.loading = false;
        if(error.status == 403) this._authService.logout();
        this._notification.warn(error);
        this.deshabilitarGuardar = false;
      });
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

  verFotosRecoleccion(){
    this.loading = true;
    if(this.imagenesRecoleccionIds)
      this.imagenesRecoleccionIds.forEach(fotoId => this.getFoto(fotoId, true));
    this.loading = false;
  }

  verFotosEntrega(){
    this.loading = true;
    if(this.imagenesEntregaIds)
      this.imagenesEntregaIds.forEach(fotoId => this.getFoto(fotoId, false));
    this.loading = false;
  }

  getFoto(fotoId, imagenesRecoleccion:boolean){
    let that = this;
    this._envioService.getFoto(fotoId).subscribe(data => {
        if(imagenesRecoleccion){
          that.imagenesRecoleccion.push(that.sanitizer.bypassSecurityTrustResourceUrl('data:image/jpg;base64,'+data.fileContent));
          that.imagenesRecoleccionIdsCargadas.push(fotoId);
        }
        else{
          that.imagenesEntrega.push(that.sanitizer.bypassSecurityTrustResourceUrl('data:image/jpg;base64,'+data.fileContent));
          that.imagenesEntregaIdsCargadas.push(fotoId);
        }
    },
    error => {
      if(error.status == 403) this._authService.logout();
    });
  }

  verImagen(index, imagenesRecoleccion:boolean){
    this.show = false;
    this.verImagenSeleccionada = imagenesRecoleccion ? this.imagenesRecoleccion[index] : this.imagenesEntrega[index];
    $("#myModal").modal('show');
  }

  destroyed(event) {
    this.loading = false;
    if(this.estadoCompletado) this.actualizar();
    else this.consultarFotos();
  }


}

