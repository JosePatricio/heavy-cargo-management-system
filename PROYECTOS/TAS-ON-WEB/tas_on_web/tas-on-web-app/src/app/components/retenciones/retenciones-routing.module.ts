import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {RetencionesListarComponent} from "./listar/retenciones-listar.component";

const routes: Routes = [
  {path: 'retenciones-listar', component: RetencionesListarComponent},
  {path: '**', pathMatch: 'full', redirectTo: 'login'}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class RetencionesRoutingModule { }
