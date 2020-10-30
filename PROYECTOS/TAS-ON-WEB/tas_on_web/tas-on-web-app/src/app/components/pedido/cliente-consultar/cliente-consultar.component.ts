import {Component, ComponentFactoryResolver, OnInit, TemplateRef, ViewChild, ViewContainerRef} from '@angular/core';
import {AuthService} from "../../../services/auth.service";
import {PedidoService} from "../../../services/pedido.service";
import {ClienteActualizarComponent} from "../cliente-actualizar/cliente-actualizar.component";
import {VisitasNuevasComponent} from "../visitas-nuevas/visitas-nuevas.component";

@Component({
  selector: 'app-cliente-consultar',
  templateUrl: './cliente-consultar.component.html',
  styleUrls: ['./cliente-consultar.component.css']
})
export class ClienteConsultarComponent implements OnInit {

  loading:boolean = false;
  columns = [];
  clientes: any = [];
  vendedores: any = [];
  clienteDetail: boolean = false;

  razonSocial: any;
  ruc: any;
  vendedorAsignado: any;
  diaVisita: any;
  diasSemana: any = [];

  @ViewChild('dynamicInsert', {read: ViewContainerRef}) dynamicInsert: ViewContainerRef;
  @ViewChild('linkButton') linkButton: TemplateRef<any>;
  @ViewChild('diaVisitaTmpl') diaVisitaTmpl: TemplateRef<any>;

  constructor(private componentFactoryResolver: ComponentFactoryResolver,
              private _authService: AuthService, private _pedidoService : PedidoService) { }

  ngOnInit() {
    this.diasSemana.push({dia: 'Lunes', diaId: 1}, {dia: 'Martes', diaId: 2}, {dia: 'Miercoles', diaId: 3},
      {dia: 'Jueves', diaId: 4}, {dia: 'Viernes', diaId: 5}, {dia: 'Sabado', diaId: 6}, {dia: 'Domingo', diaId: 7},
      {dia: 'Sin asignar', diaId: -1});

    this.columns = [
      {name: '', cellTemplate: this.linkButton, headerClass: 'table-header', width: 60},
      {prop: 'clienteRazonSocial', name: 'Razon social', headerClass: 'table-header'},
      {prop: 'clienteDireccion', name: 'Direccion', headerClass: 'table-header'},
      {prop: 'clienteVendedorAsignado.vendedorNombres', name: 'Vendedor', headerClass: 'table-header'},
      {prop: 'diaSemanaVisita', name: 'Dia visita', cellTemplate: this.diaVisitaTmpl, headerClass: 'table-header'},
    ];

    this.consultarClientes();
    this.consultarVendedores();
  }

  consultarClientes(){
    let query = "?";
    if(this.razonSocial) query = query.concat("razonSocial="+this.razonSocial+"&");
    if(this.ruc) query = query.concat("ruc="+this.ruc+"&");
    if(this.vendedorAsignado) query = query.concat("vendedor="+this.vendedorAsignado+"&");
    if(this.diaVisita) query = query.concat("diaVisita="+this.diaVisita+"&");

    this.loading = true;
    this._pedidoService.getClientesBy(query).subscribe(
      data => {
        this.clientes = data;
        this.loading = false;
      },
      error => {
        if(error.status == 403) this._authService.logout();
        this.loading = false;
      }
    );
  }

  limpiarCampos(){
    this.razonSocial = null;
    this.ruc = null;
    this.vendedorAsignado = null;
    this.diaVisita = null;
  }

  consultarVendedores(){
    this.loading = true;
    this._pedidoService.getVendedores().subscribe(
      data => {
        this.vendedores = data;
        this.loading = false;
      },
      error => {
        if(error.status == 403) this._authService.logout();
        this.loading = false;
      }
    );
  }

  editarCliente(cliente: any){
    this.clienteDetail = true;
    const componentFactory = this.componentFactoryResolver.resolveComponentFactory(ClienteActualizarComponent);
    if (this.dynamicInsert) {
      this.dynamicInsert.clear();
      const dyynamicComponent = <ClienteActualizarComponent>this.dynamicInsert.createComponent(componentFactory).instance;
      dyynamicComponent.cliente = cliente;
      dyynamicComponent.complete.subscribe(complete => {
        this.closeDetail(complete);
      });
    }
  }

  nuevoCliente(){
    this.clienteDetail = true;
    const componentFactory = this.componentFactoryResolver.resolveComponentFactory(ClienteActualizarComponent);
    if (this.dynamicInsert) {
      this.dynamicInsert.clear();
      const dyynamicComponent = <ClienteActualizarComponent>this.dynamicInsert.createComponent(componentFactory).instance;
      dyynamicComponent.cliente = {};
      dyynamicComponent.complete.subscribe(complete => {
        this.closeDetail(complete);
      });
    }
  }

  closeDetail(event: boolean) {
    this.clienteDetail = false;
    if(event) this.consultarClientes();
  }

  agendarmeVisita(){
    this.clienteDetail = true;
    const componentFactory = this.componentFactoryResolver.resolveComponentFactory(VisitasNuevasComponent);
    if (this.dynamicInsert) {
      this.dynamicInsert.clear();
      const dyynamicComponent = <VisitasNuevasComponent>this.dynamicInsert.createComponent(componentFactory).instance;
      dyynamicComponent.clientes = this.clientes;
      dyynamicComponent.complete.subscribe(complete => {
        this.closeDetail(complete);
      });
    }
  }

}
