import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {LoadingModule} from "ngx-loading";
import {NgxDatatableModule} from "@swimlane/ngx-datatable";
import {DateTimePickerModule} from "ng-pick-datetime";
import {MyDatePickerModule} from 'mydatepicker';
import {FormsModule} from "@angular/forms";
import {ChartsModule} from "ng2-charts";
import {FacturasRoutingModule} from "./facturas-routing.module";
import {NotificationsService, SimpleNotificationsModule} from "angular2-notifications/dist";
import {FacturasPorRecibirComponent} from "./facturas-por-recibir/facturas-por-recibir.component";
import {FacturasPendientePagoComponent} from "./facturas-pendiente-pago/facturas-pendiente-pago.component";
import {FacturasPorCobrarComponent} from "./facturas-por-cobrar/facturas-por-cobrar.component";
import {FacturasCobradasComponent} from "./facturas-cobradas/facturas-cobradas.component";
import {FacturasManualesComponent} from "./facturas-manuales/facturas-manuales.component";
import {FacturasManualesListarComponent} from "./facturas-manuales-listar/facturas-manuales-listar.component";
import {FacturasComponent} from "./facturas.component";
import {FacturasPorCobrarDetalleComponent} from "./facturas-por-cobrar/facturas-por-cobrar-detalle/facturas-por-cobrar-detalle.component";
import {FacturasCobradasDetalleComponent} from "./facturas-cobradas/facturas-cobradas-detalle/facturas-cobradas-detalle.component";

@NgModule({
  imports: [
    CommonModule,
    FacturasRoutingModule,
    LoadingModule,
    NgxDatatableModule,
    MyDatePickerModule,
    DateTimePickerModule,
    SimpleNotificationsModule.forRoot(),
    FormsModule,
    ChartsModule,
  ],
  declarations: [
    FacturasPorRecibirComponent,
    FacturasPendientePagoComponent,
    FacturasPorCobrarComponent,
    FacturasCobradasComponent,
    FacturasManualesComponent,
    FacturasManualesListarComponent,
    FacturasComponent,
    FacturasPorCobrarDetalleComponent,
    FacturasCobradasDetalleComponent,
  ],
  entryComponents: [
    FacturasPorCobrarDetalleComponent,
    FacturasCobradasDetalleComponent,
  ],
  providers: [
    NotificationsService,
  ]
})
export class FacturasModule { }
