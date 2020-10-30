import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {EmpresasListarComponent} from "./empresas-listar/empresas-listar.component";

const routes: Routes = [
  {path: 'empresa-listar', component: EmpresasListarComponent},
  {path: '**', pathMatch: 'full', redirectTo: 'login'}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class EmpresasRoutingModule { }
