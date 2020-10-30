import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {LoadingModule} from "ngx-loading";
import {NgxDatatableModule} from "@swimlane/ngx-datatable";
import {DateTimePickerModule} from "ng-pick-datetime";
import {MyDatePickerModule} from 'mydatepicker';
import {FormsModule} from "@angular/forms";
import {ChartsModule} from "ng2-charts";
import {NotificationsService, SimpleNotificationsModule} from "angular2-notifications/dist";
import {ConductorRoutingModule} from "./conductor-routing.module";
import {NuevoConductorComponent} from "./listar-conductor/nuevo-conductor/nuevo-conductor.component";
import {ListarConductorComponent} from "./listar-conductor/listar-conductor.component";
import {AngularMultiSelectModule} from 'angular4-multiselect-dropdown/angular4-multiselect-dropdown';
import {ConductorComponent} from "./conductor.component";

@NgModule({
  imports: [
    CommonModule,
    ConductorRoutingModule,
    LoadingModule,
    NgxDatatableModule,
    MyDatePickerModule,
    DateTimePickerModule,
    SimpleNotificationsModule.forRoot(),
    FormsModule,
    ChartsModule,
    AngularMultiSelectModule,
  ],
  declarations: [
    ConductorComponent,
    NuevoConductorComponent,
    ListarConductorComponent,

  ],
  entryComponents: [
    NuevoConductorComponent,

  ],
  providers: [
    NotificationsService,
  ]
})
export class ConductorModule { }
