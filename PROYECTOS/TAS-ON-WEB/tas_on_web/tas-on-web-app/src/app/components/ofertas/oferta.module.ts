import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {LoadingModule} from "ngx-loading";
import {NgxDatatableModule} from "@swimlane/ngx-datatable";
import {DateTimePickerModule} from "ng-pick-datetime";
import {MyDatePickerModule} from 'mydatepicker';
import {FormsModule} from "@angular/forms";
import {ChartsModule} from "ng2-charts";
import {OfertaRoutingModule} from "./oferta-routing.module";
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
import {OfertaTransitoPagoComponent} from "./oferta-transito-pago/oferta-transito-pago.component";
import {OfertaPagadasComponent} from "./oferta-pagadas/oferta-pagadas.component";
import {OfertaListasPagoComponent} from "./oferta-listas-pago/oferta-listas-pago.component";
import {OfertaEntregarFacturaComponent} from "./oferta-entregar-factura/oferta-entregar-factura.component";
import {OfertasAdminComponent} from "./ofertas-admin/ofertas-admin.component";
import {OfertasAdminDetailComponent} from "./ofertas-admin/ofertas-admin-listar/ofertas-admin-detail/ofertas-admin-detail.component";

@NgModule({
  imports: [
    CommonModule,
    OfertaRoutingModule,
    LoadingModule,
    NgxDatatableModule,
    MyDatePickerModule,
    DateTimePickerModule,
    FormsModule,
    ChartsModule,
  ],
  declarations: [
    MisOfertasListarComponent,
    MisOfertasDetailComponent,
    OfertaAprobadaListarComponent,
    OfertaAprobadaDetailComponent,
    OfertaCursoListarComponent,
    OfertaCursoDetailComponent,
    OfertaEntregarListarComponent,
    OfertaEntregarDetailComponent,
    OfertaGenerarFacturaListComponent,
    OfertaEntregarFacturaListarComponent,
    OfertaFinalizadaListarComponent,
    OfertaFinalizadaDetailComponent,
    OfertaFacturaListarComponent,
    OfertaFacturaDetailComponent,
    OfertasAdminListarComponent,
    NuevaOfertaComponent,
    RealizarOfertaComponent,
    OfertaTransitoPagoComponent,
    OfertaPagadasComponent,
    OfertaListasPagoComponent,
    OfertaEntregarFacturaComponent,
    OfertasAdminComponent,
    OfertasAdminDetailComponent,

  ],
  entryComponents: [
    MisOfertasDetailComponent,
    OfertasAdminDetailComponent,
  ],
  providers: [
  ]
})
export class OfertaModule { }
