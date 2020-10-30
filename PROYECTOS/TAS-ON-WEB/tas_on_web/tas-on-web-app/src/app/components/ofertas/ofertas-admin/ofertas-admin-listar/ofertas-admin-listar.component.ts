import {Component, ComponentFactoryResolver, OnInit, TemplateRef, ViewChild, ViewContainerRef} from '@angular/core';
import {UtilService} from "../../../../services/util.service";
import {appConfig} from "../../../../app.config";
import {OfertaService} from "../../../../services/oferta.service";
import {Router} from "@angular/router";
import {SolicitudPagarComponent} from "../../../solicitud/solicitud-pagar/solicitud-pagar.component";
import {DatatableComponent} from "@swimlane/ngx-datatable/src/index";
import {OfertasAdminDetailComponent} from "./ofertas-admin-detail/ofertas-admin-detail.component";
import {AuthService} from "../../../../services/auth.service";

@Component({
  selector: 'app-ofertas-admin-listar',
  templateUrl: './ofertas-admin-listar.component.html'
})
export class OfertasAdminListarComponent implements OnInit {

  public loading = false;
  etapasOfertas: any;
  stateSelected: any;
  appConfig = appConfig;

  ofertasIs: any = true;

  //Para dataTable
  @ViewChild(SolicitudPagarComponent) table: DatatableComponent;
  @ViewChild('tmplIndex') tmplIndex: TemplateRef<any>;
  @ViewChild('tmplDate') tmplDate: TemplateRef<any>;
  @ViewChild('tmplAmount') tmplAmount: TemplateRef<any>;
  @ViewChild('tmpPeso') tmpPeso: TemplateRef<any>;
  @ViewChild('linkButton') linkButton: TemplateRef<any>;
  ofertas: any;
  columns = [];

  @ViewChild('dynamicInsert', {read: ViewContainerRef}) dynamicInsert: ViewContainerRef;

  constructor(private _utilService: UtilService, public _oferta: OfertaService, public _router: Router,
              private viewContainerRef: ViewContainerRef, private componentFactoryResolver: ComponentFactoryResolver,
              private _authService: AuthService) {
  }

  ngOnInit() {
    this.columns = [
      {prop: 'peso', name: '#', cellTemplate: this.tmplIndex, headerClass: 'table-header', width: 85},
      {prop: 'idOferta', name: 'Info.', cellTemplate: this.linkButton, headerClass: 'table-header', width: 95},
      {prop: 'idOferta', name: 'Id Oferta', headerClass: 'table-header'},
      {prop: 'idSolicitud', name: 'Solicitud', headerClass: 'table-header'},
      {prop: 'origen', name: 'Origen', headerClass: 'table-header'},
      {prop: 'destino', name: 'Destino', headerClass: 'table-header'},
      {prop: 'peso', name: 'Peso', cellTemplate: this.tmpPeso, headerClass: 'table-header'},
      {prop: 'amount', name: 'Monto', cellTemplate: this.tmplAmount, headerClass: 'table-header', width: 120},
      {prop: 'date', name: 'Fecha', headerClass: 'table-header'}
    ];

    this.loading = true;
    this.stateSelected = {};
    this._utilService.obtenerCatalogoItem(appConfig.idCatalogoOfertas).subscribe(
      data => {
        this.etapasOfertas = data;
        this.loading = false;
      },
      error => {
      }
    );
  }

  getOfertas() {
    this.loading = true;
    this._oferta.obtenerAllOfertasByEstado(this.stateSelected.state).subscribe(
      data => {
        this.ofertas = data;
        this.loading = false;
      },
      error => {
        if(error.status == 403) this._authService.logout();
      }
    );
  }

  resolveContent(idOferta: number) {
    this.loading = true;
    const componentFactory = this.componentFactoryResolver.resolveComponentFactory(OfertasAdminDetailComponent);
    if (this.dynamicInsert) {
      this.dynamicInsert.clear();
      const dyynamicComponent = <OfertasAdminDetailComponent>this.dynamicInsert.createComponent(componentFactory).instance;
      dyynamicComponent.idOferta = idOferta;
      dyynamicComponent.complete.subscribe(complete => {
        this.closeDetail(complete);
      });
    }
    this.loading = false;
  }

  callDetailOferta(idOferta) {
    //this._router.navigate(['/panel/ofertas/ofertas-detail', idOferta]);
    this.ofertasIs = false;
    this.resolveContent(idOferta);
  }

  closeDetail(event: boolean) {
    this.ofertasIs = event;
    this.getOfertas();
  }
}
