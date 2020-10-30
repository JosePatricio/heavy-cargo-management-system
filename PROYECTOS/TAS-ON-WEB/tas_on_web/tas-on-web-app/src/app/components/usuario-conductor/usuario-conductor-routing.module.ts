import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AprobarConductorComponent} from "./aprobar-conductor/aprobar-conductor.component";
import {AdministrarConductoresComponent} from "./administrar-conductores/administrar-conductores.component";
import {AdministrarVehiculosComponent} from "./administrar-vehiculos/administrar-vehiculos.component";
import {DesvincularConductorComponent} from "./desvincular-conductor/desvincular-conductor.component";
import {ReestablecerConductorComponent} from "./reestablecer-conductor/reestablecer-conductor.component";
import {InformacionBancariaComponent} from "./informacion-bancaria/informacion-bancaria.component";

const routes: Routes = [
  {path: 'aprobar-conductor', component: AprobarConductorComponent},
  {path: 'administrar-conductores', component: AdministrarConductoresComponent},
  {path: 'administrar-vehiculos', component: AdministrarVehiculosComponent},
  {path: 'desvincular-conductor', component: DesvincularConductorComponent},
  {path: 'reestablecer-conductor', component: ReestablecerConductorComponent},
  {path: 'informacion-bancaria', component: InformacionBancariaComponent},
  {path: '**', pathMatch: 'full', redirectTo: 'login'}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UsuarioConductorRoutingModule { }
