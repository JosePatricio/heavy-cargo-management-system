import {Component, ComponentFactoryResolver, OnInit, ViewChild, ViewContainerRef} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {NuevoPasswordComponent} from "../nuevo-password/nuevo-password.component";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  public loading: any = false;
  isLogin: boolean = true;

  usuario: string = "";
  password: string = "";
  messageShow: boolean = false;
  message: String = "";
  mostrarContrasena: boolean = false;
  tipoContrasena: string = "password";

  @ViewChild('dynamicInsert', {read: ViewContainerRef}) dynamicInsert: ViewContainerRef;

  constructor(public _authService: AuthService, private viewContainerRef: ViewContainerRef,
              private componentFactoryResolver: ComponentFactoryResolver) {
  }

  ngOnInit() {
    this._authService.validar();
  }

  resolveContent(username: string) {
    this.loading = true;
    const componentFactory = this.componentFactoryResolver.resolveComponentFactory(NuevoPasswordComponent);
    if (this.dynamicInsert) {
      this.dynamicInsert.clear();
      const dyynamicComponent = <NuevoPasswordComponent>this.dynamicInsert.createComponent(componentFactory).instance;
      dyynamicComponent.password.username = username;
      dyynamicComponent.complete.subscribe(complete => {
        this.isLogin = complete;
      });
    }
    this.loading = false;
  }


  ingresar() {
    this.loading = true;
    if (!this.usuario || !this.password) {
      this.message = "Ingrese el usuario y contraseña";
      this.messageShow = true;
      this.loading = false;
    } else {
      this._authService.login(this.usuario, this.password).subscribe(
        data => {
          this.loading = false;
          if (Number(data) === 5) {
            this.isLogin = false;
            this.resolveContent(this.usuario);
          }
        },
        error => {
          this.messageShow = true;
          this.message = error;
          this.loading = false;
        }
      );

    }

  }

  showPassword(){
    this.mostrarContrasena = true;
    this.tipoContrasena = "text";
  }

  hidePassword(){
    this.mostrarContrasena = false;
    this.tipoContrasena = "password";
  }

}
