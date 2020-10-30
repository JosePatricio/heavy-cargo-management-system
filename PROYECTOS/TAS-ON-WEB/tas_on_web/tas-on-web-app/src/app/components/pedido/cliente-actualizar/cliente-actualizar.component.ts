import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {IMarker} from "../../../interfaces/marker";
import {UtilService} from "../../../services/util.service";
import {PedidoService} from "../../../services/pedido.service";
import {AuthService} from "../../../services/auth.service";
import {appConfig} from "../../../app.config";

declare var $: any;

@Component({
  selector: 'app-cliente-actualizar',
  templateUrl: './cliente-actualizar.component.html',
  styleUrls: ['./cliente-actualizar.component.css']
})
export class ClienteActualizarComponent implements OnInit {

  @Input('cliente') cliente: any = {};
  @Output() complete: EventEmitter<boolean> = new EventEmitter<boolean>();

  operacionCorrecta: boolean = false;
  mensajeOperacion: string = '';
  nuevoCliente: boolean = false;
  clienteNuevosDatos: any = {};
  loading: boolean = false;
  total: number = 0;
  producto: any = {};
  diasSemana: any = [];
  vendedores: any = [];
  map: any;
  lat = -0.18286265774035826;
  lng = -78.48490128915614;
  zoom: number = 15;
  ubicacionActualizada: boolean = false;
  clienteMarker:IMarker = {
    draggable: false,
    label: "Cliente",
    lat: null,
    lng: null,
  };
  verMiUbicacion: boolean = false;
  miUbicacionMarker:IMarker = {
    draggable: false,
    label: "Mi ubicación",
    lat: null,
    lng: null,
  };

  constructor(private _utilSerevice : UtilService, private _authService:AuthService,
              private _pedidoService: PedidoService) { }

  ngOnInit() {
    if(!this.cliente.clienteId){
      this.nuevoCliente = true;
      this.cliente.clienteVendedorAsignado = {};
    }
    this.consultarVendedores();
    Object.assign(this.clienteNuevosDatos, this.cliente);
    this.diasSemana.push({dia: 'Lunes', diaId: 1}, {dia: 'Martes', diaId: 2}, {dia: 'Miercoles', diaId: 3},
      {dia: 'Jueves', diaId: 4}, {dia: 'Viernes', diaId: 5}, {dia: 'Sabado', diaId: 6}, {dia: 'Domingo', diaId: 7});
  }

  mapReady(map) {
    this.map = map;
    this.obtenerPosicionCliente();
  }

  clickedMarker(label: string) {
    //console.log(`clicked the marker: ${label}`)
  }

  mapClicked($event: any) {
    /*
    this.clienteMarker.lat = $event.coords.lat;
    this.clienteMarker.lng = $event.coords.lng;
    */
  }

  markerDragEnd($event: any) {
    /*
    this.clienteMarker.lat = $event.coords.lat;
    this.clienteMarker.lng = $event.coords.lng;
    console.log(this.clienteMarker);
    */
  }

  actualizarUbicacionCliente(){
    this.clienteNuevosDatos.clienteLat = +this.miUbicacionMarker.lat;
    this.clienteNuevosDatos.clienteLng = +this.miUbicacionMarker.lng;
    this.obtenerPosicionCliente();
    this.ubicacionActualizada = true;
  }

  obtenerMiUbicacion(){
    try{
      this._utilSerevice.getPosition().then(pos=>
      {
        this.miUbicacionMarker.lat = pos.lat;
        this.miUbicacionMarker.lng = pos.lng;
        if(this.map){
          this.map.setCenter(this.miUbicacionMarker);
          this.map.setZoom(18);
          this.verMiUbicacion = true;
        }
      });  
    }catch (e) {
      
    }
    
  }

  obtenerPosicionCliente(){
    if(this.cliente && !this.nuevoCliente && this.cliente.clienteLat){
      this.clienteMarker.lat = +this.clienteNuevosDatos.clienteLat;
      this.clienteMarker.lng = +this.clienteNuevosDatos.clienteLng;
      this.clienteMarker.label =  this.clienteNuevosDatos.clienteRazonSocial;
      if(this.map){
        this.map.setCenter(this.clienteMarker);
        this.map.setZoom(16);
      }
    }
  }

  regresar() {
    this.complete.emit(false);
  }

  actualizar(){
    this.loading = true;
    this._pedidoService.actualizarCliente(this.clienteNuevosDatos).subscribe(
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

  guardarNuevoCliente(){
    this.loading = true;
    this._pedidoService.guardarCliente(this.clienteNuevosDatos).subscribe(
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

  cerrarMensaje() {
    if (this.operacionCorrecta) { this.complete.emit(true); }
  }


}
