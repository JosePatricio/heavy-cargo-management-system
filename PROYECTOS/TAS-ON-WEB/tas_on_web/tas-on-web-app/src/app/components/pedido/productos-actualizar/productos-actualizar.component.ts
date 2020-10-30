import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {PedidoService} from "../../../services/pedido.service";
import {AuthService} from "../../../services/auth.service";
import {appConfig} from "../../../app.config";
import {UtilService} from "../../../services/util.service";

declare var $: any;

@Component({
  selector: 'app-productos-actualizar',
  templateUrl: './productos-actualizar.component.html',
  styleUrls: ['./productos-actualizar.component.css']
})
export class ProductosActualizarComponent implements OnInit {

  @Input('producto') producto: any = {};
  @Output() complete: EventEmitter<boolean> = new EventEmitter<boolean>();
  operacionCorrecta: boolean = false;
  mensajeOperacion: string = '';
  nuevoProducto: boolean = false;
  productoNuevosDatos: any = {};
  loading: boolean = false;
  categorias: any = [];
  tiposVolumen: any;


  constructor( private _utilService: UtilService, private _pedidoService: PedidoService,
               private _authService:AuthService) { }

  ngOnInit() {
    if(!this.producto.productoId){
      this.nuevoProducto = true;
    }
    this.consultarCategorias();
    this.loadCatalogoVolumen();
    Object.assign(this.productoNuevosDatos, this.producto);
  }

  loadCatalogoVolumen(){
    this.loading = true;
    this._utilService.obtenerCatalogoItem(appConfig.idCatalogoVolumen).subscribe(
      data => {
        this.tiposVolumen = data;
        this.loading = false;
      },
      error => {
        this.loading = false;
      }
    );
  }
  consultarCategorias(){
    this.loading = true;
    this._pedidoService.getCategorias().subscribe(
      data => {
        this.categorias = data;
        this.loading = false;
      },
      error => {
        if(error.status == 403) this._authService.logout();
        this.loading = false;
      }
    );
  }

  actualizarProducto(){
    this.loading = true;
    this._pedidoService.actualizarProducto(this.productoNuevosDatos).subscribe(
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
        if(error.status == 403) this._authService.logout();
        this.loading = false;
      }
    );
  }
  guardarProducto(){
    this.loading = true;
    this._pedidoService.guardarProducto(this.productoNuevosDatos).subscribe(
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
        if(error.status == 403) this._authService.logout();
        this.loading = false;
      }
    );
  }

  regresar() {
    this.complete.emit(false);
  }

  cerrarMensaje() {
    if (this.operacionCorrecta) { this.complete.emit(true); }
  }

}
