import {Component, ComponentFactoryResolver, OnInit, TemplateRef, ViewChild, ViewContainerRef} from '@angular/core';
import {Router} from "@angular/router";
import {OfertaService} from "../../../../services/oferta.service";
import {appConfig} from "../../../../app.config";
import {MisOfertasDetailComponent} from "./mis-ofertas-detail/mis-ofertas-detail.component";
import {AuthService} from "../../../../services/auth.service";

@Component({
  selector: 'app-mis-ofertas-listar',
  templateUrl: './mis-ofertas-listar.component.html'
})
export class MisOfertasListarComponent implements OnInit {

  public loading = false;
  viewOfferDetail: any = false;
  appConfig = appConfig;

  //Para dataTable
  @ViewChild('tmplIndex') tmplIndex: TemplateRef<any>;
  @ViewChild('tmplDate') tmplDate: TemplateRef<any>;
  @ViewChild('tmplAmount') tmplAmount: TemplateRef<any>;
  @ViewChild('tmpPeso') tmpPeso: TemplateRef<any>;
  @ViewChild('linkButton') linkButton: TemplateRef<any>;
  ofertas: any;
  columns = [];

  @ViewChild('dynamicInsert', {read: ViewContainerRef}) dynamicInsert: ViewContainerRef;

  constructor(public _router: Router, public _oferta: OfertaService, private viewContainerRef: ViewContainerRef,
              private componentFactoryResolver: ComponentFactoryResolver, private _authService: AuthService) {
  }

  ngOnInit() {
    this.columns = [
      {name: '#', cellTemplate: this.tmplIndex, headerClass: 'table-header', width: 85},
      {name: 'Info.', cellTemplate: this.linkButton, headerClass: 'table-header', width: 115},
      {prop: 'idSolicitud', name: 'Solicitud', headerClass: 'table-header'},
      {prop: 'origen', name: 'Origen', headerClass: 'table-header'},
      {prop: 'destino', name: 'Destino', headerClass: 'table-header'},
      {prop: 'peso', name: 'Peso', cellTemplate: this.tmpPeso, headerClass: 'table-header'},
      {prop: 'amount', name: 'Monto', cellTemplate: this.tmplAmount, headerClass: 'table-header', width: 120},
      {prop: 'date', name: 'Fecha', headerClass: 'table-header'}
    ];

    this.loadData();
  }

  loadData() {
    this.loading = true;
    this._oferta.obtenerOfertasByEstado(appConfig.ofertasEstadoCreada).subscribe(
      data => {
        this.ofertas = data;
        this.loading = false;
      },
      error => {
        if(error.status == 403) this._authService.logout();
      }
    );
  }

  resolveContent(idSolicitud: string, indexAction) {
    this.loading = true;
    const componentFactory = this.componentFactoryResolver.resolveComponentFactory(MisOfertasDetailComponent);
    if (this.dynamicInsert) {
      this.dynamicInsert.clear();
      const dyynamicComponent = <MisOfertasDetailComponent>this.dynamicInsert.createComponent(componentFactory).instance;
      dyynamicComponent.idSolicitud = idSolicitud;
      dyynamicComponent.indexAction = indexAction;
      dyynamicComponent.complete.subscribe(complete => {
        this.closeDetail(complete);
      });
    }
    this.loading = false;
  }

  callDetailSolicitud(solicitud: string) {
    this.viewOfferDetail = true;
    this.resolveContent(solicitud, 1);
  }

  callDetailSolicitudCancel(solicitud: string) {
    this.viewOfferDetail = true;
    this.resolveContent(solicitud, 0);
  }

  closeDetail(event: boolean) {
    this.viewOfferDetail = !event;
    this.loadData();
  }
}
