import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {NuevoConductorComponent} from "./listar-conductor/nuevo-conductor/nuevo-conductor.component";
import {ListarConductorComponent} from "./listar-conductor/listar-conductor.component";

const routes: Routes = [
  {path: 'nuevo', component: NuevoConductorComponent},
  {path: 'editar/:idConductor/:index', component: NuevoConductorComponent},
  {path: 'listar', component: ListarConductorComponent},
  {path: '**', pathMatch: 'full', redirectTo: 'login'}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ConductorRoutingModule { }
