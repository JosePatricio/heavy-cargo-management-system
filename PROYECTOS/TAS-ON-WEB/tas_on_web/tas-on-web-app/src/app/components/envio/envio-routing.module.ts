import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {EnvioCargarComponent} from "./envio-cargar/envio-cargar.component";
import {EnvioConsultarComponent} from "./envio-consultar/envio-consultar.component";
import {EnvioPendientesComponent} from "./envio-pendientes/envio-pendientes.component";

const routes: Routes = [
  {path: 'cargar', component: EnvioCargarComponent},
  {path: 'consultar', component: EnvioConsultarComponent},
  {path: 'pendientes', component: EnvioPendientesComponent},
  {path: '**', pathMatch: 'full', redirectTo: 'login'}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class EnvioRoutingModule { }
