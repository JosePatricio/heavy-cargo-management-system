import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {PedidoNuevoComponent} from "./pedido-nuevo/pedido-nuevo.component";
import {VisitaConsultarComponent} from "./visita-consultar/visita-consultar.component";
import {VisitaNuevaComponent} from "./visita-nueva/visita-nueva.component";
import {PedidosConsultarComponent} from "./pedidos-consultar/pedidos-consultar.component";
import {ClienteConsultarComponent} from "./cliente-consultar/cliente-consultar.component";
import {ProductosConsultarComponent} from "./productos-consultar/productos-consultar.component";


const routes: Routes = [
  {path: 'nuevo', component: PedidoNuevoComponent},
  {path: 'consultar', component: VisitaConsultarComponent},
  {path: 'visita-nueva', component: VisitaNuevaComponent},
  {path: 'all/consultar', component: PedidosConsultarComponent},
  {path: 'clientes', component: ClienteConsultarComponent},
  {path: 'productos', component: ProductosConsultarComponent},
  {path: '**', pathMatch: 'full', redirectTo: 'login'}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PedidoRoutingModule { }
