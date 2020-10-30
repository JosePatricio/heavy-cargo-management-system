import {Component, ComponentFactoryResolver, OnInit, TemplateRef, ViewChild, ViewContainerRef} from '@angular/core';
import {PedidoNuevoComponent} from "../pedido-nuevo/pedido-nuevo.component";
import {ClienteActualizarComponent} from "../cliente-actualizar/cliente-actualizar.component";
import {PedidoService} from "../../../services/pedido.service";
import {AuthService} from "../../../services/auth.service";
import {DatePipe} from "@angular/common";
import {appConfig} from "../../../app.config";
import {PedidoVerComponent} from "../pedido-ver/pedido-ver.component";

@Component({
  selector: 'app-visita-consultar',
  templateUrl: './visita-consultar.component.html',
  styleUrls: ['./visita-consultar.component.css']
})
export class VisitaConsultarComponent implements OnInit {

  loading: boolean = false;
  fechaVisita = new Date();
  visitas: any;
  pedidoDetail: any = true;
  pipe = new DatePipe('es-EC');
  formatoFecha = "DD/MM/YYYY";

  @ViewChild('dynamicInsert', {read: ViewContainerRef}) dynamicInsert: ViewContainerRef;


  @ViewChild('tmplIndex') tmplIndex: TemplateRef<any>;
  @ViewChild('linkButton') linkButton: TemplateRef<any>;

  constructor(private componentFactoryResolver: ComponentFactoryResolver, private _authService: AuthService,
              private _pedidoService: PedidoService) { }

  ngOnInit() {
  }

  editar(visita: any){
    this.pedidoDetail = false;
    const componentFactory = this.componentFactoryResolver.resolveComponentFactory(PedidoNuevoComponent);
    if (this.dynamicInsert) {
      this.dynamicInsert.clear();
      const dyynamicComponent = <PedidoNuevoComponent>this.dynamicInsert.createComponent(componentFactory).instance;
      dyynamicComponent.visita = visita;
      dyynamicComponent.complete.subscribe(complete => {
        this.closeDetail(complete);
      });
    }
  }

  editarCliente(cliente: any){
    this.pedidoDetail = false;
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

  consultarPedido(visita: any){
    this.loading = true;
    if(visita.pedido){
      this.verPedido(visita.pedido);
    } else{
      this._pedidoService.getPedido(visita.visitaId).subscribe(
        data => {
          visita.pedido = data;
          this.verPedido(data);
          this.loading = false;
        },
        error => {
          if(error.status == 403) this._authService.logout();
          this.loading = false;
        }
      );
    }
  }

  verPedido(pedido){
    this.pedidoDetail = false;
    const componentFactory = this.componentFactoryResolver.resolveComponentFactory(PedidoVerComponent);
    if (this.dynamicInsert) {
      this.dynamicInsert.clear();
      const dyynamicComponent = <PedidoVerComponent>this.dynamicInsert.createComponent(componentFactory).instance;
      dyynamicComponent.pedido = pedido;
      dyynamicComponent.complete.subscribe(complete => {
        this.closeDetail(complete);
      });
    }
  }

  consultarPedidos(fechaVisita:string){
    if(!fechaVisita) return;
    this.loading = true;
    let query = "?fecha=".concat(fechaVisita);
    this._pedidoService.getVisitasBy(query).subscribe(
      data => {
        this.visitas = data;
        this.loading = false;
      },
      error => {
        if(error.status == 403) this._authService.logout();
        this.loading = false;
      }
    );
  }

  onValueChange(value: Date): void {
    this.consultarPedidos(this.pipe.transform(value, appConfig.formatoFechaURL));
  }

  closeDetail(event: boolean) {
    this.pedidoDetail = true;
    if(event) this.consultarPedidos(this.pipe.transform(new Date(), appConfig.formatoFechaURL));
  }

}
