import {Component, EventEmitter, Input, OnInit, Output, TemplateRef, ViewChild} from '@angular/core';
import {DatePipe} from "@angular/common";
import {PedidoService} from "../../../services/pedido.service";
import {AuthService} from "../../../services/auth.service";

declare var $: any;

@Component({
  selector: 'app-visitas-nuevas',
  templateUrl: './visitas-nuevas.component.html',
  styleUrls: ['./visitas-nuevas.component.css']
})
export class VisitasNuevasComponent implements OnInit {

  @Input('clientes') clientes: any = [];
  @Output() complete: EventEmitter<boolean> = new EventEmitter<boolean>();
  columns = [];
  formatoFecha = "DD/MM/YYYY";
  pipe = new DatePipe('es-EC');
  visitaFecha: Date = new Date();
  clientesVisita: any = [];
  loading:boolean = false;
  operacionCorrecta: boolean = false;
  mensajeOperacion: string = '';

  @ViewChild('linkButton') linkButton: TemplateRef<any>;

  constructor(private _pedidoService : PedidoService, private _authService: AuthService) { }

  ngOnInit() {
    Object.assign(this.clientesVisita, this.clientes);
    if(this.clientesVisita.length==0) this.regresar();
    this.columns = [
      {name: '', cellTemplate: this.linkButton, headerClass: 'table-header', width: 60},
      {prop: 'clienteRazonSocial', name: 'Razon social', headerClass: 'table-header'},
      {prop: 'clienteRuc', name: 'RUC', headerClass: 'table-header'},
      {prop: 'clienteDireccion', name: 'Direccion', headerClass: 'table-header'},
    ];
  }

  eliminar(rowIndex) {
    this.clientesVisita.splice(rowIndex, 1);
    if(this.clientesVisita.length==0) this.regresar();
    this.actualizarTabla();
  }

  agendarmeVisitas(){
    this.loading = true;

    let objetoEnviar = <any>{};
    objetoEnviar.fechaVisita = this.visitaFecha;
    objetoEnviar.clientesId = [];
    this.clientesVisita.forEach(cliente => {
      objetoEnviar.clientesId.push(cliente.clienteId);
    });

    this._pedidoService.agendarmeVisitas(objetoEnviar).subscribe(
      data => {
        if(data.response == "OK"){
          this.operacionCorrecta = true;
          this.mensajeOperacion = data.responseMessage;
        }else{
          this.operacionCorrecta = false;
          this.mensajeOperacion = data.responseMessage;
                  }
        this.loading = false;
        $("#myModal").modal('show');
      },
      error => {
        if(error.status == 403) this._authService.logout();
        this.loading = false;
      }
    );
  }

  actualizarTabla() {
    this.clientesVisita = [...this.clientesVisita];
  }

  regresar() {
    this.complete.emit(false);
  }

  cerrarMensaje() {
    if (this.operacionCorrecta) { this.regresar(); }
  }

}
