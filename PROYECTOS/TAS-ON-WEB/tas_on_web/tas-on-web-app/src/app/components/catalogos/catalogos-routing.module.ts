import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ListarCatalogosComponent} from "./listar-catalogos/listar-catalogos.component";
import {ListarLocalidadComponent} from "./listar-localidad/listar-localidad.component";
import {NuevoCatalogoComponent} from "./nuevo-catalogo/nuevo-catalogo.component";

const routes: Routes = [
  {path: 'listacatal', component: ListarCatalogosComponent},
  {path: 'listalocal', component: ListarLocalidadComponent},
  {path: 'catitem/:idCatalogo', component: NuevoCatalogoComponent},
  {path: '**', pathMatch: 'full', redirectTo: 'login'}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CatalogosRoutingModule { }
