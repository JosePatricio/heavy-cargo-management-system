import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {SolicitudRoutingModule} from "./solicitud-routing.module";
import {ListarSolicitudComponent} from "./listar-solicitud/listar-solicitud.component";
import {LoadingModule} from "ngx-loading";
import {NgxDatatableModule} from "@swimlane/ngx-datatable";
import {DateTimePickerModule} from "ng-pick-datetime";
import {MyDatePickerModule} from 'mydatepicker';
import {SeleccionarOfertaComponent} from "./listar-solicitud-ofertas/seleccionar-oferta/seleccionar-oferta.component";
import {ListarSolicitudOfertasComponent} from "./listar-solicitud-ofertas/listar-solicitud-ofertas.component";
import {SolicitudesFacturarseComponent} from "./solicitudes-facturarse/solicitudes-facturarse.component";
import {SolicitudEntregadaComponent} from "./solicitud-entregada/solicitud-entregada.component";
import {SolicitudProcesoEntregaComponent} from "./solicitud-proceso-entrega/solicitud-proceso-entrega.component";
import {SolicitudOfertasAprobadasComponent} from "./solicitud-ofertas-aprobadas/solicitud-ofertas-aprobadas.component";
import {SolicitudCanceladasComponent} from "./solicitud-canceladas/solicitud-canceladas.component";
import {SolicitudPagarComponent} from "./solicitud-pagar/solicitud-pagar.component";
import {SolicitudPagadasComponent} from "./solicitud-pagadas/solicitud-pagadas.component";
import {SolicitudAdminComponent} from "../solicitud-admin/solicitud-admin.component";
import {CalificarTransportistaComponent} from "./calificar-transportista/calificar-transportista.component";
import {FormsModule} from "@angular/forms";
import {ChartsModule} from "ng2-charts";
import {NuevaSolicitudComponent} from "./listar-solicitud/nueva-solicitud/nueva-solicitud.component";
import {SolicitudesFacturarseDetalleComponent} from "./solicitudes-facturarse/solicitudes-facturarse-detalle/solicitudes-facturarse-detalle.component";
import {SolicitudPagadasDetalleComponent} from "./solicitud-pagadas/solicitud-pagadas-detalle/solicitud-pagadas-detalle.component";
import {SolicitudPagarDetalleComponent} from "./solicitud-pagar/solicitud-pagar-detalle/solicitud-pagar-detalle.component";
import {SolicitudOfertasComponent} from "./solicitud-ofertas/solicitud-ofertas.component";
import {SolicitudComponent} from "./solicitud.component";
import {TypeaheadModule} from "ngx-bootstrap";
import {TerminosModule} from "../terminos/terminos.module";
import {SolicitudDetailComponent} from "./solicitud-detail/solicitud-detail.component";
import {SolicitudDespacharComponent} from "./solicitud-despachar/solicitud-despachar.component";

@NgModule({
  imports: [
    CommonModule,
    SolicitudRoutingModule,
    LoadingModule,
    NgxDatatableModule,
    MyDatePickerModule,
    DateTimePickerModule,
    FormsModule,
    ChartsModule,
    TypeaheadModule.forRoot(),
    TerminosModule

  ],
  declarations: [
    ListarSolicitudComponent,
    ListarSolicitudOfertasComponent,
    SeleccionarOfertaComponent,
    SolicitudesFacturarseComponent,
    SolicitudDespacharComponent,
    SolicitudEntregadaComponent,
    SolicitudProcesoEntregaComponent,
    SolicitudOfertasAprobadasComponent,
    SolicitudCanceladasComponent,
    SolicitudPagarComponent,
    SolicitudPagadasComponent,
    SolicitudAdminComponent,
    CalificarTransportistaComponent,
    NuevaSolicitudComponent,
    SolicitudesFacturarseDetalleComponent,
    SolicitudPagadasDetalleComponent,
    SolicitudPagarDetalleComponent,
    SolicitudOfertasComponent,
    SolicitudComponent,
    SolicitudDetailComponent
  ],
  entryComponents: [
    SeleccionarOfertaComponent,
    NuevaSolicitudComponent,
    SolicitudesFacturarseDetalleComponent,
    SolicitudPagadasDetalleComponent,
    SolicitudPagarDetalleComponent,

  ],
  providers: [
  ]
})
export class SolicitudModule { }
