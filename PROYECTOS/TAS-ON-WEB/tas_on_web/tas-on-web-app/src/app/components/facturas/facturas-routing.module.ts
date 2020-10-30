import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {FacturasPorRecibirComponent} from "./facturas-por-recibir/facturas-por-recibir.component";
import {FacturasPendientePagoComponent} from "./facturas-pendiente-pago/facturas-pendiente-pago.component";
import {FacturasPorCobrarComponent} from "./facturas-por-cobrar/facturas-por-cobrar.component";
import {FacturasCobradasComponent} from "./facturas-cobradas/facturas-cobradas.component";
import {FacturasManualesComponent} from "./facturas-manuales/facturas-manuales.component";
import {FacturasManualesListarComponent} from "./facturas-manuales-listar/facturas-manuales-listar.component";

const routes: Routes = [
  {path: 'facturas-por-recibir', component: FacturasPorRecibirComponent},
  {path: 'facturas-pendiente-pago', component: FacturasPendientePagoComponent},
  {path: 'facturas-por-cobrar', component: FacturasPorCobrarComponent},
  {path: 'facturas-cobradas', component: FacturasCobradasComponent},
  {path: 'facturas-manuales', component: FacturasManualesComponent},
  {path: 'facturas-manuales-listar', component: FacturasManualesListarComponent},
  {path: '**', pathMatch: 'full', redirectTo: 'login'}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class FacturasRoutingModule { }
