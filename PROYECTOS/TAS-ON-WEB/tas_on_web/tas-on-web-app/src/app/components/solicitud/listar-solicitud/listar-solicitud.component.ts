import {Component, ComponentFactoryResolver, OnInit, TemplateRef, ViewChild, ViewContainerRef} from '@angular/core';
import {SolicitudEnvioService} from '../../../services/solicitud-envio.service';
import {appConfig} from "../../../app.config";
import {IMyDpOptions} from 'mydatepicker';
import {DatatableComponent} from "@swimlane/ngx-datatable/src/index";
import {NuevaSolicitudComponent} from "./nueva-solicitud/nueva-solicitud.component";
import {AuthService} from "../../../services/auth.service";

declare var $: any;

@Component({
  selector: 'app-listar-solicitud',
  templateUrl: './listar-solicitud.component.html'
})
export class ListarSolicitudComponent implements OnInit {

  public loading = false;
  solNew: any = true;
  secunecia = 1;
  noSecuencia = 2;
  appConfig = appConfig;

  //Para dataTable
  @ViewChild(DatatableComponent) table: DatatableComponent;
  @ViewChild('tmplIndex') tmplIndex: TemplateRef<any>;
  @ViewChild('tmplDate') tmplDate: TemplateRef<any>;
  @ViewChild('linkButton') linkButton: TemplateRef<any>;
  @ViewChild('tmpPeso') tmpPeso: TemplateRef<any>;
  @ViewChild('tmplFiltro') tmplFiltro: TemplateRef<any>;
  @ViewChild('tmplFiltroDate') tmplFiltroDate: TemplateRef<any>;
  listaSolicitudes: any;
  columns = [];
  temp = [];

  public myDatePickerOptions: IMyDpOptions = {
    // other options...
    dateFormat: appConfig.formatoFechaDatePicker,
    editableDateField: true
  };

  @ViewChild('dynamicInsert', {read: ViewContainerRef}) dynamicInsert: ViewContainerRef;

  constructor(public _solicitudEnvioService: SolicitudEnvioService, private viewContainerRef: ViewContainerRef,
              private componentFactoryResolver: ComponentFactoryResolver, private _authService: AuthService) {
  }

  ngOnInit() {
    this.columns = [
      {name: '#', cellTemplate: this.tmplIndex, headerClass: 'table-header', width: 85},
      {name: 'Info.', cellTemplate: this.linkButton, headerClass: 'table-header'},
      {prop: 'idSolicitud', name: 'No. solicitud', headerClass: 'table-header'},
      {prop: 'numeroDocCliente', name: 'No. documento', headerClass: 'table-header'},
      {prop: 'origen', name: 'Origen', headerClass: 'table-header'},
      {prop: 'destino', name: 'Destino', headerClass: 'table-header'},
      {prop: 'peso', name: 'Peso', cellTemplate: this.tmpPeso, headerClass: 'table-header'},
      {prop: 'numeroPiezas', name: 'No. piezas', headerClass: 'table-header'},
      {prop: 'fechaRecepcion', name: 'F. recolección', cellTemplate: this.tmplDate, headerClass: 'table-header'},
      {prop: 'personaRecibe', name: 'Persona recibe', headerClass: 'table-header'},
    ];
    this.loadSolicitud();
  }

  loadSolicitud() {
    this.loading = true;
    this._solicitudEnvioService.getSolicitudEnvioAllByEstado(appConfig.estadoSolicitudEnvioActiva).subscribe(data => {

      // cache our list
      this.temp = [...data];
      // push our inital complete list
      this.listaSolicitudes = data;

      this.loading = false;
    }, error => {
      if(error.status == 403) this._authService.logout();
    });
  }

  refrescarDoc() {
    this.loadSolicitud();
  }

  updateFilter(event, campo) {
    const val = event.target.value.toLowerCase();

    // filter our data
    const temp = this.temp.filter(function (d) {
      let data = d[campo] != null ? d[campo] : '';
      return data.toLowerCase().indexOf(val) !== -1 || !val;
    });

    // update the rows
    this.listaSolicitudes = temp;
    // Whenever the filter changes, always go back to the first page
    this.table.offset = 0;
  }

  updateFilterData(event, campo) {
    if (!event.jsdate)
      this.loadSolicitud();

    // filter our data
    const temp = this.temp.filter(function (d) {
      return d[campo] >= event.jsdate;
    });

    // update the rows
    this.listaSolicitudes = temp;
    // Whenever the filter changes, always go back to the first page
    this.table.offset = 0;
  }

  resolveContent(obtieneSecuencia: number, edit: number, idSolucitud: string) {
    this.loading = true;
    const componentFactory = this.componentFactoryResolver.resolveComponentFactory(NuevaSolicitudComponent);
    if (this.dynamicInsert) {
      this.dynamicInsert.clear();
      const dyynamicComponent = <NuevaSolicitudComponent>this.dynamicInsert.createComponent(componentFactory).instance;
      dyynamicComponent.obtieneSecuencia = obtieneSecuencia;
      dyynamicComponent.edit = edit;
      dyynamicComponent.idSolicitud = idSolucitud;
      dyynamicComponent.complete.subscribe(complete => {
        this.closeDetail(complete);
      });
    }
    this.loading = false;
  }

  nuevaSolicitud() {
    //this._router.navigate(['/panel/solicitud/nueva/1']);
    this.solNew = false;
    this.resolveContent(this.secunecia, 0, null);
  }

  editar(idSolucitud: string) {
    //this._router.navigate(['/panel/solicitud/editar', idSolucitud, index, bandera]);
    this.loading = true;
    this.solNew = false;

    this.resolveContent(this.noSecuencia, 1, idSolucitud);

    this.loading = false;
  }

  cancelar(idSolucitud: string) {
    this.loading = true;
    this.solNew = false;

    this.resolveContent(this.noSecuencia, 2, idSolucitud);

    this.loading = false;
  }

  eliminar(idSolucitud: string) {
    this.loading = true;
    this.solNew = false;

    this.resolveContent(this.noSecuencia, 2, idSolucitud);

    this.loading = false;
  }

  closeDetail(event: boolean) {
    this.solNew = event;
    this.loadSolicitud();
  }

}
