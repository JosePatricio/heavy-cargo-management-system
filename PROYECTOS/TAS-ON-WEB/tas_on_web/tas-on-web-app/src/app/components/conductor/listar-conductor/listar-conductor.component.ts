import {Component, ComponentFactoryResolver, OnInit, TemplateRef, ViewChild, ViewContainerRef} from '@angular/core';
import {Router} from '@angular/router';
import {ConductorService} from '../../../services/conductor.service';
import {CatalogoItemService} from "../../../services/catalogo-item.service";
import {ListarSolicitudComponent} from "../../solicitud/listar-solicitud/listar-solicitud.component";
import {DatatableComponent} from "@swimlane/ngx-datatable/src/index";
import {appConfig} from "../../../app.config";
import {ClienteService} from "../../../services/cliente.service";
import {UsuariosService} from "../../../services/usuarios.service";
import {NuevoConductorComponent} from "./nuevo-conductor/nuevo-conductor.component";
import {AuthService} from "../../../services/auth.service";

declare var $: any;

@Component({
  selector: 'app-listar-conductor',
  templateUrl: './listar-conductor.component.html'
})
export class ListarConductorComponent implements OnInit {
  appConfig = appConfig;
  public loading = false;
  drivNew: any = false;

  catState: any[];
  idCatState: number;
  conductorSeleccionado: any;
  empresaClienteSeleccionada: any = '';
  clienteCombo: any;

  //Para dataTable
  @ViewChild(ListarSolicitudComponent) table: DatatableComponent;
  @ViewChild('tmplIndex') tmplIndex: TemplateRef<any>;
  @ViewChild('tmplDate') tmplDate: TemplateRef<any>;
  @ViewChild('linkButton') linkButton: TemplateRef<any>;
  conductores: any[];
  columns = [];

  @ViewChild('dynamicInsert', {read: ViewContainerRef}) dynamicInsert: ViewContainerRef;

  constructor(private _router: Router, public _conductorService: ConductorService, private _catalogoItemService: CatalogoItemService,
              private viewContainerRef: ViewContainerRef, private componentFactoryResolver: ComponentFactoryResolver,
              public _clienteService: ClienteService, public _usuariosService: UsuariosService, private _authService: AuthService) {
  }

  ngOnInit() {
    this.columns = [
      {prop: 'idSolicitud', name: '#', cellTemplate: this.tmplIndex, headerClass: 'table-header', width: 85},
      {prop: 'idSolicitud', name: 'Info.', cellTemplate: this.linkButton, headerClass: 'table-header', width: 115},
      {prop: 'usuarioIdDocumento', name: 'Número de documento', headerClass: 'table-header'},
      {prop: 'usuarioNombres', name: 'Nombres', headerClass: 'table-header'},
      {prop: 'usuarioApellidos', name: 'Apellidos', headerClass: 'table-header'},
      {prop: 'usuarioNombreUsuario', name: 'Usuario', headerClass: 'table-header'},
      {prop: 'usuarioMail', name: 'Correo electrónico', headerClass: 'table-header'}
    ];

    this.loadCatalogs();
  }

  loadCatalogs() {
    this.loading = true;
    this._catalogoItemService.getCatalogoItemByCatalogo(1).subscribe(data => {
      this.catState = data;
      this.loading = false;
    }, error => {
      this.loading = false;
    });
    this.loading = true;
    this._clienteService.getClientebyTipoCliente(appConfig.idTipoClienteEmpresaTransportista).subscribe(data => {
      this.clienteCombo = data;
      this.loading = false;
    }, error => {
      this.loading = false;
      if(error.status == 403) this._authService.logout();
    });
  }

  loadData() {
    this.loading = true;
    this.empresaClienteSeleccionada = this.empresaClienteSeleccionada ? this.empresaClienteSeleccionada : '0';
    this.idCatState = this.idCatState ? this.idCatState : 0;
    this._usuariosService.usuarioByEntAndState(this.empresaClienteSeleccionada, this.idCatState).subscribe(data => {
      this.conductores = data;
      this.loading = false;
    });
  }

  resolveContent(idConductor: string, indexEdit: number) {
    this.loading = true;
    const componentFactory = this.componentFactoryResolver.resolveComponentFactory(NuevoConductorComponent);
    if (this.dynamicInsert) {
      this.dynamicInsert.clear();
      const dyynamicComponent = <NuevoConductorComponent>this.dynamicInsert.createComponent(componentFactory).instance;
      dyynamicComponent.idConductor = idConductor;
      dyynamicComponent.indexEdit = indexEdit
      dyynamicComponent.complete.subscribe(complete => {
        this.closeDetail(complete);
      });
    }
    this.loading = false;
  }

  nuevoConductor() {
    //this._router.navigate(['/panel/conductor/nuevo']);
    this.drivNew = true;
    this.resolveContent(null, 1)
  }

  editarConductor(idDocumento: string) {
    //this._router.navigate(['/panel/conductor/editar', idDocumento, index]);
    this.drivNew = true;
    this.resolveContent(idDocumento, 2)
  }

  abrirModal(conductor) {
    $("#myModal").modal('show');
    this.conductorSeleccionado = conductor;
  }

  closeDetail(event: boolean) {
    this.drivNew = !event;
    this.loadData();
  }
}
