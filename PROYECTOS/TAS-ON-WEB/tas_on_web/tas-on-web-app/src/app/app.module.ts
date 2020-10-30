import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {HttpModule} from '@angular/http';
import {HashLocationStrategy, LocationStrategy} from '@angular/common';

import {NgIdleKeepaliveModule} from '@ng-idle/keepalive'; // this includes the core NgIdleModule but includes keepalive providers for easy wireup
import {appRouting} from './routes/tason.route';
//Alerts
import {NotificationsService, SimpleNotificationsModule} from 'angular2-notifications';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
//Spinners
import {LoadingModule} from 'ngx-loading';
//RECAPTCHA
import {RECAPTCHA_LANGUAGE, RECAPTCHA_SETTINGS, RecaptchaModule} from 'ng-recaptcha';
import {RecaptchaFormsModule} from 'ng-recaptcha/forms';
//MaskFields
import {InputMaskModule} from 'ng2-inputmask';
//Services
import {AuthService} from './services/auth.service';
import {ClienteService} from './services/cliente.service';
import {ConductorService} from './services/conductor.service';
import {UtilService} from './services/util.service';
import {OfertaService} from './services/oferta.service';
import {CatalogosService} from './services/catalogos.service';
import {CatalogoItemService} from './services/catalogo-item.service';
import {LocalidadService} from './services/localidad.service';
import {UsuariosService} from './services/usuarios.service';
import {EmpresaService} from "./services/empresa.service";
import {FacturaService} from "./services/factura.service";
import {FacturaProveedorService} from "./services/factura-proveedor.service";
import {PublicService} from "./services/public.service";
import {PagoService} from "./services/pago.service";
import {SolicitudEnvioService} from './services/solicitud-envio.service';
import {EbillingService} from './services/ebilling.service';
//Guards
import {AuthGuardService} from './guards/auth-guard.service';
//Directive
import {EmailUsuarioValidatorDirective} from './directives/email-usuario-validator.directive';
import {UsernameUsuarioValidatorDirective} from './directives/username-usuario-validator.directive';
import {IdentificadorUsuarioLengthValidatorDirective} from './directives/identificador-usuario-length-validator.directive';
import {EqualValidatorDirective} from './components/nuevo-password/equal-validator.directive';
import {IdentificadorUsuarioValidatorDirective} from './directives/identificador-usuario-validator.directive';
import {DatesLessValidatorDirective} from './directives/dates-less-validator.directive';
import {EmailPatternValidatorDirective} from './directives/email-pattern-validator.directive';
import {RucClienteValidatorDirective} from './directives/ruc-cliente-validator.directive';
import {NumdocUsuarioValidatorDirective} from './directives/numdoc-usuario-validator.directive';
import {OnlyIntegerValidatorDirective} from './directives/only-integer-validator.directive';
//Components
import {AppComponent} from './app.component';
import {NavBarComponent} from './components/nav-bar/nav-bar.component';
import {PanelControlComponent} from './components/panel-control/panel-control.component';
import {LoginComponent} from './components/login/login.component';
import {PortadaComponent} from './components/portada/portada.component';
import {RecuperarContraseniaComponent} from './components/recuperar-contrasenia/recuperar-contrasenia.component';
import {NuevoPasswordComponent} from './components/nuevo-password/nuevo-password.component';
import {SessionCaducadaComponent} from './components/login/session-caducada.component';
import {PlacaValidatorDirective} from './directives/placa-validator.directive';
import {RecaptchaSettings} from "ng-recaptcha/recaptcha/recaptcha-settings";
import {OnlyNumberValidatorDirective} from './directives/only-number-validator.directive';
import {IndexComponent} from './components/index/index.component';
import {OnlyDecimalNumberDirective} from './directives/only-decimal-number.directive';
import {IvaValidatorDirective} from './directives/iva-validator.directive';
import {ApisComponent} from './components/apis/apis.component';
import {GeneraTokenComponent} from './components/apis/genera-token/genera-token.component';
import {ApiUsersService} from "./services/api-users.service";
import {EnvioService} from "./services/envio.service";
import {PedidoService} from "./services/pedido.service";
import {TerminosModule} from "./components/terminos/terminos.module";

@NgModule({
  declarations: [
    AppComponent,
    IndexComponent,
    ApisComponent,
    NavBarComponent,
    PanelControlComponent,
    PortadaComponent,
    SessionCaducadaComponent,
    LoginComponent,
    RecuperarContraseniaComponent,
    NuevoPasswordComponent,
    GeneraTokenComponent,

    OnlyDecimalNumberDirective,
    EqualValidatorDirective,
    IdentificadorUsuarioValidatorDirective,
    EmailUsuarioValidatorDirective,
    UsernameUsuarioValidatorDirective,
    IdentificadorUsuarioLengthValidatorDirective,
    DatesLessValidatorDirective,
    EmailPatternValidatorDirective,
    RucClienteValidatorDirective,
    NumdocUsuarioValidatorDirective,
    PlacaValidatorDirective,
    OnlyNumberValidatorDirective,
    OnlyIntegerValidatorDirective,
    IvaValidatorDirective
  ],
  entryComponents: [
    NuevoPasswordComponent,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    appRouting,
    HttpModule,
    NgIdleKeepaliveModule.forRoot(),
    BrowserAnimationsModule,
    SimpleNotificationsModule.forRoot(),
    LoadingModule,
    RecaptchaModule.forRoot(),
    RecaptchaFormsModule,
    TerminosModule
  ],
  providers: [
    { provide: LocationStrategy, useClass: HashLocationStrategy },
    ApiUsersService,
    AuthGuardService,
    AuthService,
    CatalogoItemService,
    CatalogosService,
    ClienteService,
    ConductorService,
    EbillingService,
    EmpresaService,
    FacturaService,
    FacturaProveedorService,
    LocalidadService,
    NotificationsService,
    OfertaService,
    PagoService,
    PublicService,
    SolicitudEnvioService,
    UsuariosService,
    UtilService,
    EnvioService,
    PedidoService,
    { provide: RECAPTCHA_LANGUAGE, useValue: 'es'},
    { provide: RECAPTCHA_SETTINGS, useValue: {siteKey: '6LfKHYoUAAAAALYQmbJP886LZIv-QdPQioaJzhgl'} as RecaptchaSettings}
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
