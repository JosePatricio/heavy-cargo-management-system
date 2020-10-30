import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {SolicitudEnvioService} from "../../../services/solicitud-envio.service";
import {AuthService} from "../../../services/auth.service";
import {appConfig} from "../../../app.config";
import Swal from "sweetalert2/dist/sweetalert2.all.min.js";

declare var $: any;

@Component({
  selector: 'app-calificar-transportista',
  templateUrl: './calificar-transportista.component.html'
})
export class CalificarTransportistaComponent implements OnInit {

  appConfig = appConfig;
  stars:any = [[0,0,0,0,0],[1,0,0,0,0],[1,1,0,0,0],[1,1,1,0,0],[1,1,1,1,0],[1,1,1,1,1]];
  loading = false;
  columns = [];
  temp = [];
  listaSolicitudes: any;
  calificacion: number;
  solicitudACalificar: any = {};
  operacionCorrecta: boolean = false;

  @ViewChild('tmplIndex') tmplIndex: TemplateRef<any>;
  @ViewChild('linkButton') linkButton: TemplateRef<any>;
  @ViewChild('tmpPeso') tmpPeso: TemplateRef<any>;
  @ViewChild('tmpDetalle') tmpDetalle: TemplateRef<any>;
  @ViewChild('tmpEstrellas') tmpEstrellas: TemplateRef<any>;
  @ViewChild('tmplDate') tmplDate: TemplateRef<any>;

  ngOnInit(): void {
    this.columns = [
      {name: '#', cellTemplate: this.tmplIndex, headerClass: 'table-header', width: 85},
      {prop: 'solicitudEnvioId', name: 'No. solicitud', headerClass: 'table-header'},
      {prop: 'numeroDocumento', name: 'No. documento', headerClass: 'table-header'},
      {prop: 'empresaRazonSocial', name: 'Empresa transporte', headerClass: 'table-header'},
      {name: 'Detalle',cellTemplate: this.tmpDetalle, headerClass: 'table-header'},
      {prop: 'ofertaFechaEntrega', name: 'F. entrega',cellTemplate: this.tmplDate, headerClass: 'table-header'},
      {prop: 'calificacion', name: 'Calificación',cellTemplate: this.tmpEstrellas, headerClass: 'table-header'},
      {name: 'Editar calificación', cellTemplate: this.linkButton, headerClass: 'table-header'},
    ];
    this.loadSolicitud();
  }

  constructor(public _solicitudEnvioService: SolicitudEnvioService, private _authService: AuthService) {

  }

  loadSolicitud() {
    this.loading = true;
    this._solicitudEnvioService.getCalificacionSolicitudes().subscribe(data => {
      this.temp = [...data]; // cache our list
      this.listaSolicitudes = data; // push our inital complete list
      this.loading = false;
    }, error => {
      this.loading = false;
      if(error.status == 403) this._authService.logout();
    });
  }


  calificar(solicitud: any) {
    this.solicitudACalificar = solicitud;
    this.calificacion = solicitud.calificacion;
    $("#editarCalificacion").modal('show');
  }

  enviarNuevaCalificacion(){
    this.loading = true;
    this.solicitudACalificar.calificacion =  this.calificacion;
    this._solicitudEnvioService.actualizarCalificacion(this.solicitudACalificar).subscribe(data => {
      this.loading = false;
      Swal.fire('', data.responseMessage, (data.response == 'OK')? 'success' : 'error').then((result) => {
        this.loadSolicitud();
      });
    }, error => {
      this.loading = false;
      if(error.status == 403) this._authService.logout();
    });
  }

  mouseEnter(nuevaCalificacion : any){
    this.calificacion = nuevaCalificacion;
  }


}
