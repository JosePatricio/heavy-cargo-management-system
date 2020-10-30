import {RouterModule, Routes} from '@angular/router';

import {LoginComponent} from '../components/login/login.component';
import {SessionCaducadaComponent} from '../components/login/session-caducada.component';
import {PanelControlComponent} from '../components/panel-control/panel-control.component';
import {RecuperarContraseniaComponent} from '../components/recuperar-contrasenia/recuperar-contrasenia.component';
import {TerminosComponent} from '../components/terminos/terminos.component';
import {PortadaComponent} from '../components/portada/portada.component';
import {AuthGuardService} from '../guards/auth-guard.service';
import {IndexComponent} from "../components/index/index.component";
import {ApisComponent} from "../components/apis/apis.component";
import {GeneraTokenComponent} from "../components/apis/genera-token/genera-token.component";

const routes: Routes = [
  {path: '', component: IndexComponent},
  {path: 'login', component: LoginComponent},
  {path: 'sesionCaducada', component: SessionCaducadaComponent},
  {
    path: 'panel',
    component: PanelControlComponent,
    canActivate: [AuthGuardService],
    children: [
      {path: 'portada', component: PortadaComponent},
      {path: 'terminos', component: TerminosComponent},
      {
        path: 'apis',
        component: ApisComponent,
        children: [
          {path: 'token', component: GeneraTokenComponent},
          {path: '**', pathMatch: 'full', redirectTo: 'login'}
        ]
      },
      {
        path: 'empresa',
        loadChildren: './components/empresas/empresas.module#EmpresasModule',
      },
      {
        path: 'retenciones',
        loadChildren: './components/retenciones/retenciones.module#RetencionesModule',
      },
      {
        path: 'conductor',
        loadChildren: './components/conductor/conductor.module#ConductorModule',
      },
      {
        path: 'catalogos',
        loadChildren: './components/catalogos/catalogos.module#CatalogosModule',
      },
      {
        path: 'cliente',
        loadChildren: './components/cliente/cliente.module#ClienteModule',
      },
      {
        path: 'envio',
        loadChildren: './components/envio/envio.module#EnvioModule',
      },
      {
        path: 'usuario',
        loadChildren: './components/usuario/usuario.module#UsuarioModule',
      },
      {
        path: 'ebilling',
        loadChildren: './components/ebilling/ebilling.module#EbillingModule',
      },
      {
        path: 'facturas-prov',
        loadChildren: './components/facturas-proveedor/facturas-proveedor.module#FacturasProveedorModule',
      },
      {
        path: 'solicitudes',
        loadChildren: './components/solicitud/solicitud.module#SolicitudModule',
      },
      {
        path: 'ofertas',
        loadChildren: './components/ofertas/oferta.module#OfertaModule',
      },
      {
        path: 'pedido',
        loadChildren: './components/pedido/pedido.module#PedidoModule',
      },
      {
        path: 'facturas',
        loadChildren: './components/facturas/facturas.module#FacturasModule',
      },
      {
        path: 'usuario-conductor',
        loadChildren: './components/usuario-conductor/usuario-conductor.module#UsuarioConductorModule',
      },
    ]
  },
  {
    path: 'nuevo-usuario',
    loadChildren: './components/nuevo-usuario/nuevo-usuario.module#NuevoUsuarioModule',
  },
  {path: 'recuperar-contrasenia', component: RecuperarContraseniaComponent},
  {path: '**', pathMatch: 'full', redirectTo: 'login'}
];

export const appRouting = RouterModule.forRoot(routes);
