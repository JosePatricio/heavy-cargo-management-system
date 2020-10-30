import {Component, ComponentFactoryResolver, OnInit, ViewChild, ViewContainerRef} from '@angular/core';
import {AuthService} from "../../../services/auth.service";
import {PedidoService} from "../../../services/pedido.service";
import {appConfig} from "../../../app.config";
import {DatePipe} from "@angular/common";
import {PedidoVerComponent} from "../pedido-ver/pedido-ver.component";

@Component({
  selector: 'app-pedidos-consultar',
  templateUrl: './pedidos-consultar.component.html',
  styleUrls: ['./pedidos-consultar.component.css']
})
export class PedidosConsultarComponent implements OnInit {

  loading: boolean = false;
  pedidosGroupedByProducto: any = [];
  pedidoDetail: any = true;
  pedidos: any = [];
  appConfig = appConfig;
  formatoFecha = "DD/MM/YYYY";
  pipe = new DatePipe('es-EC');
  fechaCreacionPedido: Date = null;
  fechaEntregaPedido: Date = null;

  @ViewChild('dynamicInsert', {read: ViewContainerRef}) dynamicInsert: ViewContainerRef;

  constructor(private componentFactoryResolver: ComponentFactoryResolver,
              private _authService: AuthService, private _pedidoService: PedidoService) { }

  ngOnInit() {

  }

  consultarPedidos(){
    this.loading = true;
    let query : string = "?";
    if (this.fechaCreacionPedido) query = query.concat("fechaPedido="+this.pipe.transform(this.fechaCreacionPedido, appConfig.formatoFechaURL)+"&");
    if (this.fechaEntregaPedido) query = query.concat("fechaEntrega="+this.pipe.transform(this.fechaEntregaPedido, appConfig.formatoFechaURL));
    this._pedidoService.getAllPedidosBy(query).subscribe(
      data => {
        this.pedidos = data;
        this.loading = false;
        this.agruparPedidosPorProducto();
      },
      error => {
        if(error.status == 403) this._authService.logout();
        this.loading = false;
      }
    );
  }

  agruparPedidosPorProducto(){
    this.pedidosGroupedByProducto = [];
    this.pedidos.forEach(pedido => {
      let detallePedido = pedido.detalle;
      detallePedido.forEach(detalle => {
        let producto = this.pedidosGroupedByProducto.find(productoBuscar => productoBuscar.productoId == detalle.productoId);
        if(producto){
          let productoEncontrado = producto;
          if(!productoEncontrado.detalle){
            productoEncontrado.detalle = [];
          }
          productoEncontrado.detalle.push(this.agregarDetalle(pedido, detalle));
          productoEncontrado.cantidad = +productoEncontrado.cantidad +detalle.productoCantidad;
        }else{
          let producto = <any>{};
          producto.productoId = detalle.productoId;
          producto.productoNombre = detalle.productoNombre;
          producto.cantidad = 0;
          producto.productoCodigo = detalle.productoCodigo;
          producto.detalle = [];
          producto.detalle.push(this.agregarDetalle(pedido, detalle));
          producto.cantidad = +producto.cantidad +detalle.productoCantidad;
          this.pedidosGroupedByProducto.push(producto);
        }
      }
      );
    });
  }

  agregarDetalle(pedido, producto){
    let nuevoDetalle = <any>{};
    nuevoDetalle.clienteRazonSocial = pedido.clienteRazonSocial;
    nuevoDetalle.vendedor = pedido.usuarioCrea;
    nuevoDetalle.fechaEntregaDesde =  pedido.fechaEntregaDesde;
    nuevoDetalle.fechaEntregaHasta = pedido.fechaEntregaHasta;
    nuevoDetalle.cantidad = producto.productoCantidad;
    nuevoDetalle.precioUnitario = producto.productoPrecioConImp;
    nuevoDetalle.total = producto.precioTotalConImp;
    nuevoDetalle.visitaId = pedido.visitaId;
    nuevoDetalle.fechaPedido = pedido.fechaCreacion;
    return nuevoDetalle;
  }

  limpiarCampos(){
    this.fechaCreacionPedido = null;
    this.fechaEntregaPedido = null;
  }

  consultarPedido(visitaId: any) {
    this.loading = true;
    this._pedidoService.getPedido(visitaId).subscribe(
      data => {
        this.verPedido(data);
        this.loading = false;
      },
      error => {
        if(error.status == 403) this._authService.logout();
        this.loading = false;
      }
    );
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

  closeDetail(event: boolean) {
    this.pedidoDetail = true;
  }

}
