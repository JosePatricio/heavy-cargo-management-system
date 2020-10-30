import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {FacturasGastosComponent} from "./facturas-gastos/facturas-gastos.component";
import {FacturasGastosPagarComponent} from "./facturas-gastos-pagar/facturas-gastos-pagar.component";
import {FacturasGastosTransitoPagoComponent} from "./facturas-gastos-transito-pago/facturas-gastos-transito-pago.component";
import {FacturasGastosPagadasComponent} from "./facturas-gastos-pagadas/facturas-gastos-pagadas.component";


const routes: Routes = [
  {path: 'gastos', component: FacturasGastosComponent},
  {path: 'gastos-pagar', component: FacturasGastosPagarComponent},
  {path: 'gastos-transito-pago', component: FacturasGastosTransitoPagoComponent},
  {path: 'pagadas', component: FacturasGastosPagadasComponent},
  {path: '**', pathMatch: 'full', redirectTo: 'login'}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class FacturasProveedorRoutingModule { }
