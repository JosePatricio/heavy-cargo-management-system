import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {LoadingModule} from "ngx-loading";
import {NgxDatatableModule} from "@swimlane/ngx-datatable";
import {DateTimePickerModule} from "ng-pick-datetime";
import {MyDatePickerModule} from 'mydatepicker';
import {FormsModule} from "@angular/forms";
import {ChartsModule} from "ng2-charts";
import {NotificationsService, SimpleNotificationsModule} from "angular2-notifications/dist";
import {EnvioCargarComponent} from "./envio-cargar/envio-cargar.component";
import {EnvioConsultarComponent} from "./envio-consultar/envio-consultar.component";
import {EnvioPendientesComponent} from "./envio-pendientes/envio-pendientes.component";
import {EnvioDetalleComponent} from "./envio-detalle/envio-detalle.component";
import {EnvioRoutingModule} from "./envio-routing.module";


@NgModule({
  imports: [
    CommonModule,
    EnvioRoutingModule,
    LoadingModule,
    NgxDatatableModule,
    MyDatePickerModule,
    DateTimePickerModule,
    SimpleNotificationsModule.forRoot(),
    FormsModule,
    ChartsModule,
  ],
  declarations: [
    EnvioCargarComponent,
    EnvioConsultarComponent,
    EnvioPendientesComponent,
    EnvioDetalleComponent,

  ],
  entryComponents: [
    EnvioDetalleComponent,

  ],
  providers: [
    NotificationsService,
  ]
})
export class EnvioModule { }
