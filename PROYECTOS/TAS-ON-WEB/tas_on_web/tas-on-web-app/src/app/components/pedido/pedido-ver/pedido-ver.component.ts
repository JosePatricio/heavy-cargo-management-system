import {Component, EventEmitter, Input, OnInit, Output, TemplateRef, ViewChild} from '@angular/core';
import {appConfig} from "../../../app.config";
import {DomSanitizer} from "@angular/platform-browser";
import {PedidoService} from "../../../services/pedido.service";

declare var $: any;

@Component({
  selector: 'app-pedido-ver',
  templateUrl: './pedido-ver.component.html',
  styleUrls: ['./pedido-ver.component.css']
})
export class PedidoVerComponent implements OnInit {

  @Input('pedido') pedido: any = {};
  @Output() complete: EventEmitter<boolean> = new EventEmitter<boolean>();

  @ViewChild('tmplIndex') tmplIndex: TemplateRef<any>;

  loading: boolean = false;
  documentosCredito = new Array<any>();
  documentoCreditoSeleccionado;
  columns = [];
  appConfig = appConfig;

  constructor(private sanitizer: DomSanitizer, private _pedidoService: PedidoService) {
  }

  ngOnInit() {
    this.columns = [
      {name: '#', cellTemplate: this.tmplIndex, headerClass: 'table-header', width: 20},
      {prop: 'productoNombre', name: 'Producto', headerClass: 'table-header'},
      {prop: 'productoCantidad', name: 'Cantidad', headerClass: 'table-header'},
      {prop: 'productoPrecioConImp', name: 'Precio + Imp', headerClass: 'table-header'},
      {prop: 'precioTotalConImp', name: 'Total', headerClass: 'table-header'},
    ];
  }

  regresar() {
    this.complete.emit(false);
  }

  verDocumentosCredito() {
    this.loading = true;
    let that = this;
    that.documentosCredito = new Array<any>();
    this._pedidoService.getDocumentosCredito(this.pedido.pedidoId).subscribe(
      data => {
        this.loading = false;
        data.forEach(function(img){
          that.documentosCredito.push(that.sanitizer.bypassSecurityTrustResourceUrl('data:image/jpg;base64,'+img));
        });
      }, error2 => {
        this.loading = false;
      });
  }

  verImagen(index){
    this.documentoCreditoSeleccionado = this.documentosCredito[index];
    $("#myModal").modal('show');
  }

  cerrarMensaje() {
  }

}
