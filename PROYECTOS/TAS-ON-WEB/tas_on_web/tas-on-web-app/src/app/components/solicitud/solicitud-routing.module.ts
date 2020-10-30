import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {ListarSolicitudComponent} from "./listar-solicitud/listar-solicitud.component";
import {ListarSolicitudOfertasComponent} from "./listar-solicitud-ofertas/listar-solicitud-ofertas.component";
import {SeleccionarOfertaComponent} from "./listar-solicitud-ofertas/seleccionar-oferta/seleccionar-oferta.component";
import {SolicitudesFacturarseComponent} from "./solicitudes-facturarse/solicitudes-facturarse.component";
import {SolicitudEntregadaComponent} from "./solicitud-entregada/solicitud-entregada.component";
import {SolicitudProcesoEntregaComponent} from "./solicitud-proceso-entrega/solicitud-proceso-entrega.component";
import {SolicitudOfertasAprobadasComponent} from "./solicitud-ofertas-aprobadas/solicitud-ofertas-aprobadas.component";
import {SolicitudCanceladasComponent} from "./solicitud-canceladas/solicitud-canceladas.component";
import {SolicitudPagarComponent} from "./solicitud-pagar/solicitud-pagar.component";
import {SolicitudPagadasComponent} from "./solicitud-pagadas/solicitud-pagadas.component";
import {SolicitudAdminComponent} from "../solicitud-admin/solicitud-admin.component";
import {CalificarTransportistaComponent} from "./calificar-transportista/calificar-transportista.component";
import {SolicitudDetailComponent} from "./solicitud-detail/solicitud-detail.component";
import {SolicitudDespacharComponent} from "./solicitud-despachar/solicitud-despachar.component";

const routes: Routes = [
  {path: 'listar', component: ListarSolicitudComponent},
  {path: 'listar-solicitud-oferta', component: ListarSolicitudOfertasComponent},
  {path: 'solicitud-facturarse', component: SolicitudesFacturarseComponent},
  {path: 'despachar', component: SolicitudDespacharComponent},
  {path: 'solicitud-entregada', component: SolicitudEntregadaComponent},
  {path: 'solicitud-proceso-entrega', component: SolicitudProcesoEntregaComponent},
  {path: 'solicitud-ofertas-aprobadas', component: SolicitudOfertasAprobadasComponent},

  {path: 'canceladas', component: SolicitudCanceladasComponent},
  {path: 'solicitud-pagar', component: SolicitudPagarComponent},
  {path: 'solicitud-pagadas', component: SolicitudPagadasComponent},
  {path: 'solicitud-admin', component: SolicitudAdminComponent},
  {path: 'calificar-transportista', component: CalificarTransportistaComponent},

  {path: 'seleccionar-oferta/:solicitud', component: SeleccionarOfertaComponent},

  {path: 'solicitud-detail/:idSolicitud', component: SolicitudDetailComponent},
  {path: '**', pathMatch: 'full', redirectTo: 'login'}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SolicitudRoutingModule { }
