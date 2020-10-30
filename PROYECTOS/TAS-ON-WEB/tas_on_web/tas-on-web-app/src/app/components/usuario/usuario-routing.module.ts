import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ListaRestableceComponent} from "./restablecer/lista-restablece/lista-restablece.component";
import {ListaDesbloqueaComponent} from "./desbloquear/lista-desbloquea/lista-desbloquea.component";
import {AprobarComponent} from "./aprobar/aprobar.component";

const routes: Routes = [
  {path: 'restablece-user', component: ListaRestableceComponent},
  {path: 'desbloquea-user', component: ListaDesbloqueaComponent},
  {path: 'aprueba-user', component: AprobarComponent},
  {path: '**', pathMatch: 'full', redirectTo: 'login'}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UsuarioRoutingModule { }
