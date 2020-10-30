import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {LoadingModule} from "ngx-loading";
import {NgxDatatableModule} from "@swimlane/ngx-datatable";
import {DateTimePickerModule} from "ng-pick-datetime";
import {MyDatePickerModule} from 'mydatepicker';
import {FormsModule} from "@angular/forms";
import {ChartsModule} from "ng2-charts";
import {NotificationsService, SimpleNotificationsModule} from "angular2-notifications/dist";
import {ClienteRoutingModule} from "./cliente-routing.module";
import {NuevoClienteComponent} from "./listar-cliente/nuevo-cliente/nuevo-cliente.component";
import {ListarClienteComponent} from "./listar-cliente/listar-cliente.component";
import {ClienteComponent} from "./cliente.component";

@NgModule({
  imports: [
    CommonModule,
    ClienteRoutingModule,
    LoadingModule,
    NgxDatatableModule,
    MyDatePickerModule,
    DateTimePickerModule,
    SimpleNotificationsModule.forRoot(),
    FormsModule,
    ChartsModule,
  ],
  declarations: [
    ClienteComponent,
    NuevoClienteComponent,
    ListarClienteComponent,

  ],
  entryComponents: [
    NuevoClienteComponent,

  ],
  providers: [
    NotificationsService,
  ]
})
export class ClienteModule { }
