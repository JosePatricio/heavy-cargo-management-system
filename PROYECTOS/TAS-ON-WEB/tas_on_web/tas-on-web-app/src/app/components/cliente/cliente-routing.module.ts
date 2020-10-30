import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {NuevoClienteComponent} from "./listar-cliente/nuevo-cliente/nuevo-cliente.component";
import {ListarClienteComponent} from "./listar-cliente/listar-cliente.component";

const routes: Routes = [
  {path: 'nuevo', component: NuevoClienteComponent},
  {path: 'editar/:idCliente/:index', component: NuevoClienteComponent},
  {path: 'listar', component: ListarClienteComponent},
  {path: '**', pathMatch: 'full', redirectTo: 'login'}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ClienteRoutingModule { }
