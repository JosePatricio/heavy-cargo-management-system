import {Component, ComponentFactoryResolver, OnInit, ViewChild, ViewContainerRef} from '@angular/core';
import {PedidoService} from "../../../services/pedido.service";
import {AuthService} from "../../../services/auth.service";
import {appConfig} from "../../../app.config";
import {DatePipe} from "@angular/common";
import {PedidoVerComponent} from "../pedido-ver/pedido-ver.component";

declare var $: any;

@Component({
  selector: 'app-visita-nueva',
  templateUrl: './visita-nueva.component.html',
  styleUrls: ['./visita-nueva.component.css']
})
export class VisitaNuevaComponent implements OnInit {

  loading:boolean = false;
  visita: any = {};
  formatoFecha = "DD/MM/YYYY";
  vendedores: any = [];
  clientes: any = [];
  pipe = new DatePipe('es-EC');
  visitas: any = [];
  visitasGroupedByVendedor: any = [];
  appConfig = appConfig;
  visitasDetail: boolean = true;

  operacionCorrecta: boolean = false;
  mensajeOperacion: string = '';

  verVisitasPedidoTomado:boolean = true;
  verVisitasPendientes:boolean = true;

  @ViewChild('dynamicInsert', {read: ViewContainerRef}) dynamicInsert: ViewContainerRef;

  constructor(private componentFactoryResolver: ComponentFactoryResolver,
              private _authService: AuthService, private _pedidoService : PedidoService) {
    this.visita.visitaFecha = new Date();
  }

  ngOnInit() {
    this.consultarVendedores();
    this.consultarClientes();
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

  consultarClientes(){
    this.loading = true;
    this._pedidoService.getClientesBy('').subscribe(
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

  onValueFechaChange(value: Date): void {
    this.consultarVisitas(this.pipe.transform(value, appConfig.formatoFechaURL));
  }

  consultarVisitas(fechaVisita:string){
    if(!fechaVisita) return;
    this.loading = true;
    let query = "?fecha=".concat(fechaVisita);
    this._pedidoService.getVisitasAllBy(query).subscribe(
      data => {
        this.visitas = data;
        this.loading = false;
        this.agruparVisitasPorVendedor();
      },
      error => {
        if(error.status == 403) this._authService.logout();
        this.loading = false;
      }
    );
  }

  agruparVisitasPorVendedor(){
    this.visitasGroupedByVendedor = [];
    this.visitas.forEach(visita => {
      let vendedor = this.visitasGroupedByVendedor.find(vendedorAsignado => vendedorAsignado.vendedorId == visita.vendedor.vendedorId);
      if(vendedor){
        let vendedorEncontrado = vendedor;
        if(!vendedorEncontrado.visita){
          vendedorEncontrado.visita = [];
        }
        vendedorEncontrado.visita.push(visita);
      } else{
        let vendedor = <any>{};
        vendedor.vendedorId = visita.vendedor.vendedorId;
        vendedor.vendedorNombres = visita.vendedor.vendedorNombres;
        vendedor.visita = [];
        vendedor.visita.push(visita);
        this.visitasGroupedByVendedor.push(vendedor);
      }
    });
  }

  agendarVisita(){
    this.loading = true;
    this._pedidoService.agendarVisita(this.visita).subscribe(
      data => {
        if(data.response == "OK"){
          this.operacionCorrecta = true;
          this.mensajeOperacion = data.responseMessage;
        }else{
          this.operacionCorrecta = false;
          this.mensajeOperacion = data.responseMessage;
        }
        $("#myModal").modal('show');
        this.consultarVisitas(this.pipe.transform(this.visita.visitaFecha, appConfig.formatoFechaURL));
        this.loading = false;
      },
      error => {
        if(error.status == 403) this._authService.logout();
        this.loading = false;
      }
    );
  }

  consultarPedido(visitaId: any){
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
    this.visitasDetail = false;
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
    this.visitasDetail = true;
  }

  cerrarMensaje() {
    if (this.operacionCorrecta) { }
  }

}
