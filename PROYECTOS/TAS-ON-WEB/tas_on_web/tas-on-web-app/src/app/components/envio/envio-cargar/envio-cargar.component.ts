import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {appConfig} from "../../../app.config";
import {EnvioService} from "../../../services/envio.service";
import {NotificationsService} from "angular2-notifications";
import {AuthService} from "../../../services/auth.service";

@Component({
  selector: 'app-envio-cargar',
  templateUrl: './envio-cargar.component.html',
  styleUrls: ['./envio-cargar.component.css']
})
export class EnvioCargarComponent implements OnInit {

  loading = false;
  appConfig = appConfig;
  archivoSolicitudes: any = {};

  options = {
    position: ["top", "right"],
    timeOut: 5000,
    showProgressBar: true,
    pauseOnHover: true,
    clickToClose: true,
    maxLength: 10
  };

  //Para dataTable
  @ViewChild('tmplIndex') tmplIndex: TemplateRef<any>;
  @ViewChild('tmplDateTime') tmplDateTime: TemplateRef<any>;
  @ViewChild('tmplTipoEnvio') tmplTipoEnvio: TemplateRef<any>;
  @ViewChild('tmplConductor') tmplConductor: TemplateRef<any>;
  @ViewChild('tmplVehiculo') tmplVehiculo: TemplateRef<any>;

  envios = [];
  columns = [];

  constructor(private _envios : EnvioService,  private _notification: NotificationsService, private _authService : AuthService) { }

  ngOnInit() {
    this.columns = [
      {name: '#', cellTemplate: this.tmplIndex, headerClass: 'table-header', width: 85},
      {prop: 'envioTipo', name: 'Tipo', cellTemplate: this.tmplTipoEnvio, headerClass: 'table-header'},
      {prop: 'envioNumeroDocumento', name: 'Número documento', headerClass: 'table-header'},
      {prop: 'clienteByEnvio.clienteRazonSocial', name: 'Cliente', headerClass: 'table-header'},
      {prop: 'localidadByEnvioOrigen.localidadDescripcion', name: 'Origen', headerClass: 'table-header'},
      {prop: 'envioDireccionOrigen', name: 'Dirección origen', headerClass: 'table-header'},
      {prop: 'envioFechaRecoleccion', name: 'Fecha recolección', cellTemplate: this.tmplDateTime, headerClass: 'table-header'},
      {prop: 'localidadByEnvioDestino.localidadDescripcion', name: 'Destino', headerClass: 'table-header'},
      {prop: 'envioDireccionDestino', name: 'Dirección destino', headerClass: 'table-header'},
      {prop: 'envioFechaEntrega', name: 'Fecha entrega', cellTemplate: this.tmplDateTime, headerClass: 'table-header'},
      {prop: 'envioNumeroPiezas', name: 'Piezas', headerClass: 'table-header'},
      {prop: 'envioNumeroEstibadores', name: 'Estibadores', headerClass: 'table-header'},
      {prop: 'conductorByEnvio', name: 'Conductor', cellTemplate: this.tmplConductor, headerClass: 'table-header'},
      {prop: 'vehiculoByEnvio', name: 'Vehículo', cellTemplate: this.tmplVehiculo, headerClass: 'table-header'},
    ];
  }

  onFileChange(event) {
    this.envios = [];
    let reader = new FileReader();
    if (event.target.files && event.target.files.length > 0) {
      let file = event.target.files[0];
      reader.readAsDataURL(file);
      reader.onload = () => {
      this.archivoSolicitudes = (
        {
          file: reader.result.split(',')[1],
          fileType: file.type,
          fileName: file.name,
          fileSize: file.size
        });
      };
    }
  }

  validarEnvios() {
    this.loading = true;
    this._envios.validarEnviosFile(this.archivoSolicitudes).subscribe(data => {
      this.loading = false;
      this.envios = data;
    }, error => {
      this.envios = [];
      this.loading = false;
      if(error.status == 403) this._authService.logout();
    });
  }

  cargarEnvios() {
    this.loading = true;
    this._envios.cargarEnviosFile(this.archivoSolicitudes).subscribe(data => {
      this.loading = false;
      if(data.response == "OK"){
        this._notification.success(data.responseMessage);
        this.envios = [];
      } else{
        this._notification.warn(data.responseMessage);
      }
    }, error => {
      this.envios = [];
      this.loading = false;
      if(error.status == 403) this._authService.logout();
    });
  }

  destroyed(event) { }

}
