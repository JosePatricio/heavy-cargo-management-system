import {Component, ComponentFactoryResolver, OnInit, TemplateRef, ViewChild, ViewContainerRef} from '@angular/core';
import {Router} from '@angular/router';
import {ClienteService} from '../../../services/cliente.service';
import {CatalogoItemService} from "../../../services/catalogo-item.service";
import {SolicitudPagarComponent} from "../../solicitud/solicitud-pagar/solicitud-pagar.component";
import {DatatableComponent} from "@swimlane/ngx-datatable/src/index";
import {NuevoClienteComponent} from "./nuevo-cliente/nuevo-cliente.component";
import {UsuariosService} from "../../../services/usuarios.service";
import {appConfig} from "../../../app.config";
import {AuthService} from "../../../services/auth.service";

declare var $: any;

@Component({
  selector: 'app-listar-cliente',
  templateUrl: './listar-cliente.component.html'
})
export class ListarClienteComponent implements OnInit {
  appConfig = appConfig;

  public loading = false;
  clieteSeleccionado: any;

  newEditClient: any = false;

  catState: any[];
  idCatState: number;

  empresaClienteSeleccionada: any = '';
  clienteCombo: any;

  //Para dataTable
  @ViewChild(SolicitudPagarComponent) table: DatatableComponent;
  @ViewChild('tmplIndex') tmplIndex: TemplateRef<any>;
  @ViewChild('tmplDate') tmplDate: TemplateRef<any>;
  @ViewChild('tmplAmount') tmplAmount: TemplateRef<any>;
  @ViewChild('tmpPeso') tmpPeso: TemplateRef<any>;
  @ViewChild('linkButton') linkButton: TemplateRef<any>;
  clients = [];
  columns = [];

  @ViewChild('dynamicInsert', {read: ViewContainerRef}) dynamicInsert: ViewContainerRef;

  constructor(private _router: Router, public _clienteService: ClienteService, private _catalogoItemService: CatalogoItemService,
              private viewContainerRef: ViewContainerRef, private componentFactoryResolver: ComponentFactoryResolver,
              public _usuariosService: UsuariosService, private _authService: AuthService) {
  }

  ngOnInit() {
    this.columns = [
      {prop: 'peso', name: '#', cellTemplate: this.tmplIndex, headerClass: 'table-header', width: 85},
      {prop: 'peso', name: 'Info.', cellTemplate: this.linkButton, headerClass: 'table-header', width: 115},
      {prop: 'usuarioIdDocumento', name: 'Número de documento', headerClass: 'table-header'},
      {prop: 'usuarioNombres', name: 'Nombres', headerClass: 'table-header'},
      {prop: 'usuarioApellidos', name: 'Apellidos', headerClass: 'table-header'},
      {prop: 'usuarioNombreUsuario', name: 'Usuario', headerClass: 'table-header'},
      {prop: 'usuarioMail', name: 'Correo Electrónico', headerClass: 'table-header'},
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

    this._clienteService.getClientebyTipoCliente(appConfig.idTipoClienteEmpresaCliente).subscribe(data => {
      this.clienteCombo = data;
    }, error => {
      if(error.status == 403) this._authService.logout();
    });
    this.loadData();
  }

  loadData() {
    this.loading = true;
    this.empresaClienteSeleccionada = this.empresaClienteSeleccionada ? this.empresaClienteSeleccionada : '0';
    this.idCatState = this.idCatState ? this.idCatState : 0;
    this._usuariosService.usuarioByEntAndState(this.empresaClienteSeleccionada, this.idCatState).subscribe(data => {
      this.clients = data;
      this.loading = false;
    });
  }

  resolveContent(idCliente: string, editParam: number) {
    this.loading = true;
    const componentFactory = this.componentFactoryResolver.resolveComponentFactory(NuevoClienteComponent);
    if (this.dynamicInsert) {
      this.dynamicInsert.clear();
      const dyynamicComponent = <NuevoClienteComponent>this.dynamicInsert.createComponent(componentFactory).instance;
      dyynamicComponent.idCliente = idCliente;
      dyynamicComponent.editParam = editParam;
      dyynamicComponent.complete.subscribe(complete => {
        this.closeDetail(complete);
      });
    }
    this.loading = false;
  }


  nuevoCliente() {
    //this._router.navigate(['/panel/cliente/nuevo']);
    this.newEditClient = true;
    this.resolveContent(null, 1);
  }

  editar(idDocumento: string) {
    //this._router.navigate(['/panel/cliente/editar', idDocumento, index]);
    this.newEditClient = true;
    this.resolveContent(idDocumento, 2);

  }

  abrirModal(cliente) {
    $("#myModal").modal('show');
    this.clieteSeleccionado = cliente;
  }

  closeDetail(event: boolean) {
    this.newEditClient = !event;
    this.loadData();
  }
}

