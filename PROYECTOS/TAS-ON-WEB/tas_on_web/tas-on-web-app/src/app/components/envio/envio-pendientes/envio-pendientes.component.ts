import {Component, ComponentFactoryResolver, OnInit, TemplateRef, ViewChild, ViewContainerRef} from '@angular/core';
import {AuthService} from "../../../services/auth.service";
import {EnvioService} from "../../../services/envio.service";
import {appConfig} from "../../../app.config";
import {EnvioDetalleComponent} from "../envio-detalle/envio-detalle.component";

@Component({
  selector: 'app-envio-pendientes',
  templateUrl: './envio-pendientes.component.html',
  styleUrls: ['./envio-pendientes.component.css']
})
export class EnvioPendientesComponent implements OnInit {

  loading = false;
  envioDetail: any = true;
  envios: any;
  appConfig = appConfig;
  columns = [];

  @ViewChild('tmplIndex') tmplIndex: TemplateRef<any>;
  @ViewChild('tmplDateTime') tmplDateTime: TemplateRef<any>;
  @ViewChild('tmplTipoEnvio') tmplTipoEnvio: TemplateRef<any>;
  @ViewChild('linkButton') linkButton: TemplateRef<any>;

  @ViewChild('dynamicInsert', {read: ViewContainerRef}) dynamicInsert: ViewContainerRef;

  constructor(private _authService: AuthService, private _envioService: EnvioService,
              private componentFactoryResolver: ComponentFactoryResolver) { }

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
      {prop: 'envioVehiculo', name: 'Vehículo', headerClass: 'table-header'},
      {prop: 'envioObservaciones', name: 'Observaciones', headerClass: 'table-header'},
    ];
    this.consultarEnviosPendientes();
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
  }

}
