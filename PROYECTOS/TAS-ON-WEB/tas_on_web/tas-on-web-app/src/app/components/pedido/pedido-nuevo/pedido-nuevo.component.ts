import {Component, EventEmitter, Input, OnInit, Output, TemplateRef, ViewChild} from '@angular/core';
import {TypeaheadMatch} from "ngx-bootstrap";
import {toNumber} from "ngx-bootstrap/timepicker/timepicker.utils";
import {Archivo} from "../../../interfaces/archivo";
import {AuthService} from "../../../services/auth.service";
import {PedidoService} from "../../../services/pedido.service";
import {UtilService} from "../../../services/util.service";

declare var $: any;

@Component({
  selector: 'app-pedido-nuevo',
  templateUrl: './pedido-nuevo.component.html',
  styleUrls: ['./pedido-nuevo.component.css']
})
export class PedidoNuevoComponent implements OnInit {

  @Input('visita') visita: any = {};
  @Output() complete: EventEmitter<boolean> = new EventEmitter<boolean>();

  loading: boolean = false;
  columns = [];
  productosSolicitados: any = [];
  pedido: any = {};

  selectedValue: string;
  selectedOption: any;
  productos: any;
  producto: any = {};
  fechaEntregaPedido: Date = new Date();
  horarioRecibeDesde: Date = new Date();
  horarioRecibeHasta: Date = new Date();
  imagenesCredito = new Array<Archivo>();
  imagenEntregaSeleccionada = -1;

  operacionCorrecta: boolean = false;
  mensajeOperacion: string = '';

  @ViewChild('linkButton') linkButton: TemplateRef<any>;

  constructor(private _authService: AuthService, private _utilSerevice : UtilService,
              private _pedidoService: PedidoService) {
    this.fechaEntregaPedido.setDate(this.fechaEntregaPedido.getDate() + 1);
    this.horarioRecibeDesde.setHours(7, 30);
    this.horarioRecibeHasta.setHours(15, 30);
    this.pedido.personaRecibe = "Cualquiera en el local";
    this.pedido.total = 0;
    this.pedido.solicitaCredito = false;
  }

  ngOnInit() {
    this.columns = [
      {name: '', cellTemplate: this.linkButton, headerClass: 'table-header', width: 60},
      {prop: 'productoNombre', name: 'Producto', headerClass: 'table-header'},
      {prop: 'productoCantidad', name: 'Cantidad', headerClass: 'table-header'},
      {prop: 'productoPrecioConImp', name: 'Precio + Imp', headerClass: 'table-header'},
      {prop: 'precioTotalConImp', name: 'Total', headerClass: 'table-header'},
    ];
    this.consultarProductos();
    this.consultarMiUbicacion();
  }

  hacerPedido() {
    this.pedido.visitaId = this.visita.visitaId;
    this.pedido.fechaEntregaDesde = new Date();
    this.pedido.fechaEntregaDesde.setHours(this.horarioRecibeDesde.getHours(), this.horarioRecibeDesde.getMinutes(), 0);
    this.pedido.fechaEntregaDesde.setDate(this.fechaEntregaPedido.getDate());
    this.pedido.fechaEntregaHasta = new Date();
    this.pedido.fechaEntregaHasta.setHours(this.horarioRecibeHasta.getHours(), this.horarioRecibeHasta.getMinutes(), 0);
    this.pedido.fechaEntregaHasta.setDate(this.fechaEntregaPedido.getDate());
    this.pedido.detalle = this.productosSolicitados;
    if(this.pedido.solicitaCredito){
      this.pedido.documentosCredito = this.imagenesCredito;
    }
    this.guardarPedido();
  }

  guardarPedido(){
    this.loading = true;
    this._pedidoService.guardarPedido(this.pedido).subscribe(
      data => {
        if(data.response == "OK"){
          this.operacionCorrecta = true;
          this.mensajeOperacion = data.responseMessage;
        }else{
          this.operacionCorrecta = false;
          this.mensajeOperacion = data.responseMessage;
        }
        $("#myModal").modal('show');
        this.loading = false;
      },
      error => {
        if (error.status == 403) this._authService.logout();
        this.loading = false;
      }
    );
  }

  consultarMiUbicacion(){
    try{
      this._utilSerevice.getPosition().then(pos=>
      {
        this.pedido.lat = pos.lat;
        this.pedido.lng = pos.lng;
      });
    } catch (e) {
      this.pedido.lat = null;
      this.pedido.lng = null;
    }

  }

  regresar() {
    this.complete.emit(false);
  }

  onSelect(event: TypeaheadMatch): void {
    this.selectedOption = event.item;
    this.producto.productoId = this.selectedOption.productoId;
    this.producto.productoNombre = this.selectedOption.productoNombre;
    this.producto.productoCodigo = this.selectedOption.productoCodigo;
    this.producto.productoPrecioSinImp = this.selectedOption.productoPrecioSinImp;
    this.producto.productoPrecioConImp = this.selectedOption.productoPrecioConImp;
    this.producto.productoPrecioPvpConImp = this.selectedOption.productoPrecioPvpConImp;
    this.producto.productoPrecioPvpSinImp = this.selectedOption.productoPrecioPvpSinImp;

  }

  agregarProducto() {
    let nuevoPedido: any = {};
    if (this.selectedOption && this.producto.productoCodigo && this.producto.productoCantidad && this.producto.productoCantidad > 0) {
      this.productosSolicitados.forEach((item, index) => {
        if (item.productoCodigo == this.producto.productoCodigo) {
          this.producto.productoCantidad = toNumber(item.productoCantidad) + toNumber(this.producto.productoCantidad);
          this.productosSolicitados.splice(index, 1)
        }
      });
      nuevoPedido.productoId = this.producto.productoId;
      nuevoPedido.productoCodigo = this.producto.productoCodigo;
      nuevoPedido.productoNombre = this.producto.productoNombre;
      nuevoPedido.productoCantidad = this.producto.productoCantidad;
      nuevoPedido.productoPrecioConImp = this.selectedOption.productoPrecioConImp;
      nuevoPedido.productoPrecioSinImp = this.selectedOption.productoPrecioSinImp;
      nuevoPedido.precioTotalSinImp = (nuevoPedido.productoCantidad * nuevoPedido.productoPrecioSinImp).toFixed(2);
      nuevoPedido.precioTotalConImp = (nuevoPedido.productoCantidad * nuevoPedido.productoPrecioConImp).toFixed(2);
      this.productosSolicitados.push(nuevoPedido);
      this.actualizarTabla();
      this.limpiarCamposNuevoProducto();
    }
  }

  limpiarCamposNuevoProducto() {
    this.producto = {};
    this.selectedOption = {};
    this.selectedValue = "";
  }

  eliminar(rowIndex) {
    this.productosSolicitados.splice(rowIndex, 1);
    this.actualizarTabla();
  }

  eliminarPorCodigo(codigo) {
    this.productosSolicitados.forEach((item, index) => {
      if (item.codigo == codigo) {
        this.productosSolicitados.splice(index, 1)
      }
    });
    this.actualizarTabla();
  }

  actualizarTabla() {
    this.productosSolicitados = [...this.productosSolicitados];
    this.pedido.total = 0;
    this.productosSolicitados.forEach(p => this.pedido.total = +p.precioTotalConImp + this.pedido.total);
    this.pedido.total = +this.pedido.total.toFixed(2);
  }

  onFileChange(event, nombre: string) {
    let reader = new FileReader();
    if (event.target.files && event.target.files.length > 0) {
      let file = event.target.files[0];
      reader.readAsDataURL(file);
      reader.onload = () => {
        if (file.type == "image/jpg" || file.type == "image/jpeg" || file.type == "image/png") {
          this.imagenesCredito.push(
            {
              file: reader.result.split(',')[1],
              fileType: file.type,
              fileName: file.name,
              fileSize: file.size
            });
        } else {
          $("#myModal").modal('show');
        }
      };
    }
  }

  seleccionarImagen(imagenIndex) {
    if (imagenIndex == this.imagenEntregaSeleccionada) {
      this.imagenEntregaSeleccionada = -1;
    } else {
      this.imagenEntregaSeleccionada = imagenIndex;
    }
  }

  eliminarImagen() {
    this.imagenesCredito.splice(this.imagenEntregaSeleccionada, 1);
    this.imagenEntregaSeleccionada = -1;
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

  cerrarMensaje() {
    if (this.operacionCorrecta) { this.complete.emit(true); }
  }

}
