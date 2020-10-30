import {Component, ComponentFactoryResolver, OnInit, TemplateRef, ViewChild, ViewContainerRef} from '@angular/core';
import {AuthService} from "../../../services/auth.service";
import {PedidoService} from "../../../services/pedido.service";
import {ProductosActualizarComponent} from "../productos-actualizar/productos-actualizar.component";

@Component({
  selector: 'app-productos-consultar',
  templateUrl: './productos-consultar.component.html',
  styleUrls: ['./productos-consultar.component.css']
})
export class ProductosConsultarComponent implements OnInit {

  loading: boolean = false;
  productoDetail: boolean = false;
  columns = [];
  productos: any;

  @ViewChild('linkButton') linkButton: TemplateRef<any>;
  @ViewChild('dynamicInsert', {read: ViewContainerRef}) dynamicInsert: ViewContainerRef;

  constructor(private componentFactoryResolver: ComponentFactoryResolver,
              private _authService: AuthService, private _pedidoService: PedidoService) { }

  ngOnInit() {
    this.columns = [
      {name: '', cellTemplate: this.linkButton, headerClass: 'table-header', width: 60},
      {prop: 'productoCodigo', name: 'Codigo', headerClass: 'table-header'},
      {prop: 'productoNombre', name: 'Producto', headerClass: 'table-header'},
      {prop: 'productoCategoria', name: 'Categoría', headerClass: 'table-header'},
      {prop: 'productoPrecioConImp', name: 'Precio final', headerClass: 'table-header'},
    ];
    this.consultarProductos();
  }

  consultarProductos() {
    this.loading = true;
    this._pedidoService.getProductos().subscribe(
      data => {
        this.loading = false;
        this.productos = data;
      },
      error => {
        if (error.status == 403) this._authService.logout();
        this.loading = false;
      }
    );
  }

  editarProducto(producto: any){
    this.productoDetail = true;
    const componentFactory = this.componentFactoryResolver.resolveComponentFactory(ProductosActualizarComponent);
    if (this.dynamicInsert) {
      this.dynamicInsert.clear();
      const dyynamicComponent = <ProductosActualizarComponent>this.dynamicInsert.createComponent(componentFactory).instance;
      dyynamicComponent.producto = producto;
      dyynamicComponent.complete.subscribe(complete => {
        this.closeDetail(complete);
      });
    }
  }

  nuevoProducto(){
    this.productoDetail = true;
    const componentFactory = this.componentFactoryResolver.resolveComponentFactory(ProductosActualizarComponent);
    if (this.dynamicInsert) {
      this.dynamicInsert.clear();
      const dyynamicComponent = <ProductosActualizarComponent>this.dynamicInsert.createComponent(componentFactory).instance;
      dyynamicComponent.producto = {};
      dyynamicComponent.complete.subscribe(complete => {
        this.closeDetail(complete);
      });
    }
  }

  closeDetail(event: boolean) {
    this.productoDetail = false;
    if(event) this.consultarProductos();
  }

}
