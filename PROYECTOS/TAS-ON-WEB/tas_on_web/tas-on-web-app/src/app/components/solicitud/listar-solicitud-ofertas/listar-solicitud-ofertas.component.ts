import {Component, ComponentFactoryResolver, OnInit, TemplateRef, ViewChild, ViewContainerRef} from '@angular/core';
import {SolicitudEnvioService} from '../../../services/solicitud-envio.service';
import 'rxjs/Rx';
import {DatatableComponent} from "@swimlane/ngx-datatable/src/index";
import {SeleccionarOfertaComponent} from "./seleccionar-oferta/seleccionar-oferta.component";
import {appConfig} from "../../../app.config";
import {AuthService} from "../../../services/auth.service";

declare var $: any;

@Component({
  selector: 'app-listar-solicitud-ofertas',
  templateUrl: './listar-solicitud-ofertas.component.html'
})
export class ListarSolicitudOfertasComponent implements OnInit {

  horasCaducidad: number = this._authService.getHorasCaducidadSolicitud();
  public loading = false;
  viewDetail: any = false;
  appConfig = appConfig;

  //Para dataTable
  @ViewChild('detailSolicitud') table: DatatableComponent;
  @ViewChild('tmplIndex') tmplIndex: TemplateRef<any>;
  @ViewChild('tmplDate') tmplDate: TemplateRef<any>;
  @ViewChild('tmplDateTime') tmplDateTime: TemplateRef<any>;
  @ViewChild('tmplAmount') tmplAmount: TemplateRef<any>;
  @ViewChild('tmpPeso') tmpPeso: TemplateRef<any>;
  @ViewChild('linkButton') linkButton: TemplateRef<any>;
  solOffer: any;
  columns = [];

  //Para componente dinamico
  @ViewChild('dynamicInsert', {read: ViewContainerRef}) dynamicInsert: ViewContainerRef;

  constructor(public _solicitudEnvioService: SolicitudEnvioService, private viewContainerRef: ViewContainerRef,
              private componentFactoryResolver: ComponentFactoryResolver, private _authService: AuthService) {
  }

  ngOnInit() {
    /**/
    this.columns = [
      {name: '#', cellTemplate: this.tmplIndex, headerClass: 'table-header', width: 60},
      {name: 'Info.', cellTemplate: this.linkButton, headerClass: 'table-header', width: 95, sortable: 'false'},
      {prop: 'idSolicitud', name: 'Número de Solicitud', headerClass: 'table-header'},
      {prop: 'numeroDocCliente', name: 'No. documento', headerClass: 'table-header'},
      {prop: 'origen', name: 'Origen', headerClass: 'table-header'},
      {prop: 'destino', name: 'Destino', headerClass: 'table-header'},
      {prop: 'fechaRecepcion', name: 'Fecha recolección', cellTemplate: this.tmplDateTime, headerClass: 'table-header'},
      {prop: 'peso', name: 'Peso', cellTemplate: this.tmpPeso, headerClass: 'table-header', width: 80},
      {prop: 'numeroPiezas', name: 'No. Piezas', headerClass: 'table-header'},
    ];

    this.loadData();
  }

  loadData() {
    this.loading = true;
    this._solicitudEnvioService.getSolicitudEnvioOfertas().subscribe(data => {
      this.solOffer = data;
      this.loading = false;
    }, error => {
      this.loading = false;
      if(error.status == 403) this._authService.logout();
    });
  }

  seleccionarOferta(idSolucitud: string, row: any) {
    this.table.rowDetail.collapseAllRows();
    this.table.rowDetail.toggleExpandRow(row);
    this.loading = true;
    setTimeout(() => {
      this.viewDetail = true;
      this.resolveContent(idSolucitud);
    }, 1000);
  }

  resolveContent(idSolucitud: string) {
    this.loading = true;
    const componentFactory = this.componentFactoryResolver.resolveComponentFactory(SeleccionarOfertaComponent);
    if (this.dynamicInsert) {
      this.dynamicInsert.clear();
      const dyynamicComponent = <SeleccionarOfertaComponent>this.dynamicInsert.createComponent(componentFactory).instance;
      dyynamicComponent.idSolicitud = idSolucitud;
      dyynamicComponent.complete.subscribe(complete => {
        this.closeDetail(complete);
      });
    }
    this.loading = false;
  }

  closeDetail(event: any) {
    this.viewDetail = !event.complete;
    this.table.rowDetail.collapseAllRows();
    if (event.load)
      this.loadData();
  }

}
