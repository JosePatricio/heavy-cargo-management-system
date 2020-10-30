import {Component, ComponentFactoryResolver, OnInit, TemplateRef, ViewChild, ViewContainerRef} from '@angular/core';
import {appConfig} from "../../../app.config";
import {AuthService} from "../../../services/auth.service";
import {EnvioService} from "../../../services/envio.service";
import {EnvioDetalleComponent} from "../envio-detalle/envio-detalle.component";
import {DatePipe} from "@angular/common";
import {UtilService} from "../../../services/util.service";

@Component({
  selector: 'app-envio-consultar',
  templateUrl: './envio-consultar.component.html',
  styleUrls: ['./envio-consultar.component.css']
})
export class EnvioConsultarComponent implements OnInit {

  loading = false;
  envioDetail: any = true;
  envios: any;
  appConfig = appConfig;
  columns = [];

  razonSocial: string;
  nroDocumento: string;
  fechaRecoleccionEnvioDesde: any;
  fechaRecoleccionEnvioHasta: any;
  estados: any;
  estado: any = null;
  conductor: number;

  @ViewChild('tmplIndex') tmplIndex: TemplateRef<any>;
  @ViewChild('tmplDateTime') tmplDateTime: TemplateRef<any>;
  @ViewChild('tmplTipoEnvio') tmplTipoEnvio: TemplateRef<any>;
  @ViewChild('linkButton') linkButton: TemplateRef<any>;

  @ViewChild('dynamicInsert', {read: ViewContainerRef}) dynamicInsert: ViewContainerRef;

  constructor(public _authService: AuthService, private _envioService: EnvioService,
              private componentFactoryResolver: ComponentFactoryResolver, private _utilService: UtilService) { }

  ngOnInit() {
    this.columns = [
      {name: '#', cellTemplate: this.tmplIndex, headerClass: 'table-header', width: 85},
      {name: 'Info.', cellTemplate: this.linkButton, headerClass: 'table-header', width: 115},
      {prop: 'envioNumeroDocumento', name: 'Número documento', headerClass: 'table-header'},
      {prop: 'envioClienteRazonSocial', name: 'Cliente', headerClass: 'table-header'},
      {prop: 'envioTipo', name: 'Tipo', cellTemplate: this.tmplTipoEnvio, headerClass: 'table-header'},
      {prop: 'envioEstado', name: 'Estado', headerClass: 'table-header'},
      {prop: 'envioOrigen', name: 'Origen', headerClass: 'table-header'},
      {prop: 'envioDireccionOrigen', name: 'Dirección origen', headerClass: 'table-header'},
      {prop: 'envioFechaRecoleccion', name: 'Fecha recolección', cellTemplate: this.tmplDateTime, headerClass: 'table-header'},
      {prop: 'envioDestino', name: 'Destino', headerClass: 'table-header'},
      {prop: 'envioDireccionDestino', name: 'Dirección destino', headerClass: 'table-header'},
      {prop: 'envioFechaEntrega', name: 'Fecha entrega', cellTemplate: this.tmplDateTime, headerClass: 'table-header'},
      {prop: 'envioNumeroPiezas', name: 'Piezas', headerClass: 'table-header'},
      {prop: 'envioNumeroEstibadores', name: 'Estibadores', headerClass: 'table-header'},
      {prop: 'envioObservaciones', name: 'Observaciones', headerClass: 'table-header'},
      {prop: 'envioConductor', name: 'Conductor', headerClass: 'table-header'},
      {prop: 'envioVehiculo', name: 'Vehículo', headerClass: 'table-header'},
    ];
    this.consultarEstadosEnvios();
    this.consultarEnviosPendientes();
  }

  consultarEstadosEnvios(){
    this._utilService.obtenerCatalogoItem(appConfig.idEstadosEnvios).subscribe(
      data => {
        this.estados = data;
      },
      error => {
      }
    );
  }

  consultarEnviosPendientes(){
    this.loading = true;
    this._envioService.obtenerEnviosPendientes().subscribe(
      data => {
        this.envios = data;
        this.loading = false;
      },
      error => {
        this.loading = false;
        if(error.status == 403) this._authService.logout();
      }
    );
  }

  verDetalle(envio){
    this.loading = true;
    this.envioDetail = false;
    this.resolveContent(envio);
    this.loading = false;
  }

  resolveContent(envio) {
    this.loading = true;
    const componentFactory = this.componentFactoryResolver.resolveComponentFactory(EnvioDetalleComponent);
    if (this.dynamicInsert) {
      this.dynamicInsert.clear();
      const dyynamicComponent = <EnvioDetalleComponent>this.dynamicInsert.createComponent(componentFactory).instance;
      dyynamicComponent.envio = envio;
      dyynamicComponent.complete.subscribe(complete => {
        this.closeDetail(complete);
      });
    }
    this.loading = false;
  }

  closeDetail(event: boolean) {
    this.envioDetail = true;
    if(event) this.consultarEnviosPendientes();
    this.limpiarCampos();
  }


  buscarEnvios(){
    let query:string = "?";
    const pipe = new DatePipe('es-EC');
    if(this.estado) {
      if(query != "?") query = query.concat("&");
      query=query.concat("estado="+this.estado);
    }
    if(this.razonSocial){
      if(query != "?") query = query.concat("&");
      query=query.concat("razonSocial="+this.razonSocial);
    }
    if(this.nroDocumento){
      if(query != "?") query = query.concat("&");
      query=query.concat("documento="+this.nroDocumento);
    }
    if(this.fechaRecoleccionEnvioDesde){
      if(query != "?") query = query.concat("&");
      query=query.concat("fechaRecoleccionDesde="+pipe.transform(this.fechaRecoleccionEnvioDesde, appConfig.formatoFechaURL));
    }
    if(this.fechaRecoleccionEnvioHasta){
      if(query != "?") query = query.concat("&");
      query=query.concat("fechaRecoleccionHasta="+pipe.transform(this.fechaRecoleccionEnvioHasta, appConfig.formatoFechaURL));
    }
    if(this.conductor){
      if(query != "?") query = query.concat("&");
      query=query.concat("conductor="+this.conductor);
    }
    this.loadEnviosByQuery(query);
  }

  loadEnviosByQuery(query: string) {
    this.loading = true;
    this._envioService.obtenerEnviosBy(query).subscribe(data => {
      this.envios = data;
      this.loading = false;
    }, error => {
      if(error.status == 403) this._authService.logout();
    });
  }

  limpiarCampos(){
    this.razonSocial =  null;
    this.nroDocumento = null;
    this.fechaRecoleccionEnvioDesde = null;
    this.fechaRecoleccionEnvioHasta = null;
    this.estado = null;
    this.conductor = null;
  }

}
