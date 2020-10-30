import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {LoadingModule} from "ngx-loading";
import {NgxDatatableModule} from "@swimlane/ngx-datatable";
import {DateTimePickerModule} from "ng-pick-datetime";
import {MyDatePickerModule} from 'mydatepicker';
import {FormsModule} from "@angular/forms";
import {ChartsModule} from "ng2-charts";
import {NotificationsService, SimpleNotificationsModule} from "angular2-notifications/dist";
import {PedidoRoutingModule} from "./pedido-routing.module";
import {PedidoNuevoComponent} from "./pedido-nuevo/pedido-nuevo.component";
import {VisitaConsultarComponent} from "./visita-consultar/visita-consultar.component";
import {VisitaNuevaComponent} from "./visita-nueva/visita-nueva.component";
import {PedidosConsultarComponent} from "./pedidos-consultar/pedidos-consultar.component";
import {ClienteConsultarComponent} from "./cliente-consultar/cliente-consultar.component";
import {ProductosConsultarComponent} from "./productos-consultar/productos-consultar.component";
import {
  BsDatepickerModule,
  DatepickerModule,
  TimepickerModule,
  TypeaheadModule,
  TimepickerConfig
} from "ngx-bootstrap";
import {PedidoVerComponent} from "./pedido-ver/pedido-ver.component";
import {ProductosActualizarComponent} from "./productos-actualizar/productos-actualizar.component";
import {VisitasNuevasComponent} from "./visitas-nuevas/visitas-nuevas.component";
import {ClienteActualizarComponent} from "./cliente-actualizar/cliente-actualizar.component";
import {PedidoComponent} from "./pedido.component";
import {AgmCoreModule} from "@agm/core";
import {TimepickerActions} from "ngx-bootstrap/timepicker";

@NgModule({
  imports: [
    CommonModule,
    PedidoRoutingModule,
    LoadingModule,
    NgxDatatableModule,
    MyDatePickerModule,
    DateTimePickerModule,
    SimpleNotificationsModule.forRoot(),
    FormsModule,
    ChartsModule,
    TypeaheadModule.forRoot(),
    BsDatepickerModule.forRoot(),
    DateTimePickerModule,
    TimepickerModule,
    DatepickerModule,
    AgmCoreModule.forRoot({
      apiKey: 'AIzaSyBOz7CMQ4Vsfhcla0LqDKfBZgztVzW65rY'
    })
  ],
  declarations: [
    PedidoComponent,
    PedidoNuevoComponent,
    VisitaConsultarComponent,
    VisitaNuevaComponent,
    PedidosConsultarComponent,
    ClienteConsultarComponent,
    ProductosConsultarComponent,
    PedidoVerComponent,
    ProductosActualizarComponent,
    VisitasNuevasComponent,
    ClienteActualizarComponent,

  ],
  entryComponents: [
    PedidoNuevoComponent,
    PedidoVerComponent,
    VisitasNuevasComponent,
    ProductosActualizarComponent,
    ClienteActualizarComponent,
  ],
  providers: [
    NotificationsService,
    TimepickerConfig,
    TimepickerActions
  ]
})
export class PedidoModule { }
