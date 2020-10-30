import {Component, ComponentFactoryResolver, OnInit, ViewChild, ViewContainerRef} from '@angular/core';
import {ClienteService} from "../../../services/cliente.service";
import {AuthService} from "../../../services/auth.service";
import Swal from "sweetalert2/dist/sweetalert2.all.min.js";
import {appConfig} from "../../../app.config";
import {InformacionBancariaActualizarComponent} from "./informacion-bancaria-actualizar/informacion-bancaria-actualizar.component";

declare var $: any;

@Component({
  selector: 'app-informacion-bancaria',
  templateUrl: './informacion-bancaria.component.html',
  styleUrls: ['./informacion-bancaria.component.css']
})
export class InformacionBancariaComponent implements OnInit {

  loading: boolean = false;
  informacionBancaria: any = {};
  appConfig = appConfig;
  tipoContrasena: string = "password";
  mostrarContrasena: boolean = false;
  password: string = "";
  puedeActualizarDatos: boolean = false;

  @ViewChild('dynamicInsert', {read: ViewContainerRef}) dynamicInsert: ViewContainerRef;

  constructor(private _clienteService: ClienteService, private _authService: AuthService,
              private viewContainerRef: ViewContainerRef, private componentFactoryResolver: ComponentFactoryResolver) { }

  ngOnInit() {
    this.consultarInformacionBancaria();
  }

  consultarInformacionBancaria(){
    this.loading = true;
    this._clienteService.getClienteInfoBancaria().subscribe(data => {
      this.loading = false;
      this.informacionBancaria = data;
    }, error => {
      this.loading = false;
      if(error.status == 403) this._authService.logout();
    });
  }

  showPassword(){
    this.mostrarContrasena = true;
    this.tipoContrasena = "text";
  }

  hidePassword(){
    this.mostrarContrasena = false;
    this.tipoContrasena = "password";
  }

  actualizarInformacion(){
    $("#passwordModal").modal('show');
  }

  solicitarActualizarInformacionBancaria(){
    this.loading = true;
    this._authService.solicitarActualizarInformacionBancaria(this.password).subscribe(data => {
      this.loading = false;
      this.password = '';
      this.puedeActualizarDatos = data.response == 'OK';
      Swal.fire('', data.responseMessage, (data.response == 'OK')? 'success' : 'error').then((result) => {
        this.actualizarDatos();
      });
      }, error => {
      this.loading = false;
      if(error.status == 403) this._authService.logout();
      Swal.fire('', error.message, 'error');
    });
  }

  actualizarDatos(){
    const componentFactory = this.componentFactoryResolver.resolveComponentFactory(InformacionBancariaActualizarComponent);
    if (this.dynamicInsert) {
      this.dynamicInsert.clear();
      const dyynamicComponent = <InformacionBancariaActualizarComponent>this.dynamicInsert.createComponent(componentFactory).instance;
      dyynamicComponent.informacionBancaria = this.informacionBancaria;
      dyynamicComponent.complete.subscribe(complete => {
        this.puedeActualizarDatos = false;
        if(complete){
          this.consultarInformacionBancaria();
        }
      });
    }
  }

}
