import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {MisOfertasListarComponent} from "./mis-ofertas/mis-ofertas-listar/mis-ofertas-listar.component";
import {MisOfertasDetailComponent} from "./mis-ofertas/mis-ofertas-listar/mis-ofertas-detail/mis-ofertas-detail.component";
import {OfertaAprobadaListarComponent} from "./oferta-aprobada/oferta-aprobada-listar/oferta-aprobada-listar.component";
import {OfertaAprobadaDetailComponent} from "./oferta-aprobada/oferta-aprobada-detail/oferta-aprobada-detail.component";
import {OfertaCursoListarComponent} from "./oferta-recibir/oferta-recibir-listar/oferta-recibir-listar.component";
import {OfertaCursoDetailComponent} from "./oferta-recibir/oferta-recibir-detail/oferta-recibir-detail.component";
import {OfertaEntregarListarComponent} from "./oferta-entregar/oferta-entregar-listar/oferta-entregar-listar.component";
import {OfertaEntregarDetailComponent} from "./oferta-entregar/oferta-entregar-detail/oferta-entregar-detail.component";
import {OfertaGenerarFacturaListComponent} from "./oferta-generar-factura/oferta-generar-factura-list/oferta-generar-factura-list.component";
import {OfertaEntregarFacturaListarComponent} from "./oferta-entregar-factura/oferta-entregar-factura-listar/oferta-entregar-factura-listar.component";
import {OfertaFinalizadaListarComponent} from "./oferta-finalizada/oferta-finalizada-listar/oferta-finalizada-listar.component";
import {OfertaFinalizadaDetailComponent} from "./oferta-finalizada/oferta-finalizada-detail/oferta-finalizada-detail.component";
import {OfertaFacturaListarComponent} from "./oferta-factura/oferta-factura-listar/oferta-factura-listar.component";
import {OfertaFacturaDetailComponent} from "./oferta-factura/oferta-factura-detail/oferta-factura-detail.component";
import {OfertasAdminListarComponent} from "./ofertas-admin/ofertas-admin-listar/ofertas-admin-listar.component";
import {NuevaOfertaComponent} from "./nueva-oferta/nueva-oferta.component";
import {RealizarOfertaComponent} from "./realizar-oferta/realizar-oferta.component";
import {OfertaListasPagoComponent} from "./oferta-listas-pago/oferta-listas-pago.component";
import {OfertaTransitoPagoComponent} from "./oferta-transito-pago/oferta-transito-pago.component";
import {OfertaPagadasComponent} from "./oferta-pagadas/oferta-pagadas.component";

const routes: Routes = [
  {path: 'nueva/:idSolicitud/:index', component: NuevaOfertaComponent},
  {path: 'puja', component: RealizarOfertaComponent},
  {path: 'mis-ofertas', component: MisOfertasListarComponent},
  {path: 'mis-ofertas-detail/:solicitud/:flag', component: MisOfertasDetailComponent},
  {path: 'oferta-aprobada', component: OfertaAprobadaListarComponent},
  {path: 'oferta-aprobada-detail/:solicitud', component: OfertaAprobadaDetailComponent},
  {path: 'oferta-en-curso', component: OfertaCursoListarComponent},
  {path: 'oferta-en-curso-detail/:solicitud', component: OfertaCursoDetailComponent},
  {path: 'oferta-entregar', component: OfertaEntregarListarComponent},
  {path: 'oferta-entregar-detail/:solicitud', component: OfertaEntregarDetailComponent},
  {path: 'oferta-generar-factura', component: OfertaGenerarFacturaListComponent},
  {path: 'oferta-entregar-factura', component: OfertaEntregarFacturaListarComponent},
  {path: 'oferta-finalizada', component: OfertaFinalizadaListarComponent},
  {path: 'oferta-finalizada-detail/:solicitud', component: OfertaFinalizadaDetailComponent},
  {path: 'oferta-factura', component: OfertaFacturaListarComponent},
  {path: 'oferta-factura-detail/:solicitud', component: OfertaFacturaDetailComponent},
  {path: 'ofertas', component: OfertasAdminListarComponent},
  {path: 'ofertas-listas-pago', component: OfertaListasPagoComponent},
  {path: 'ofertas-transito-pago', component: OfertaTransitoPagoComponent},
  {path: 'ofertas-pagadas', component: OfertaPagadasComponent},
  {path: '**', pathMatch: 'full', redirectTo: 'login'}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class OfertaRoutingModule { }
