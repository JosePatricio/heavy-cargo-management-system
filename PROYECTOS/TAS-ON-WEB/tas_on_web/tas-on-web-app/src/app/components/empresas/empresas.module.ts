import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {LoadingModule} from "ngx-loading";
import {NgxDatatableModule} from "@swimlane/ngx-datatable";
import {DateTimePickerModule} from "ng-pick-datetime";
import {MyDatePickerModule} from 'mydatepicker';
import {FormsModule} from "@angular/forms";
import {ChartsModule} from "ng2-charts";
import {NotificationsService, SimpleNotificationsModule} from "angular2-notifications/dist";
import {EmpresasRoutingModule} from "./empresas-routing.module";
import {EmpresasComponent} from "./empresas.component";
import {EmpresasListarComponent} from "./empresas-listar/empresas-listar.component";
import {EmpresasNuevoComponent} from "./empresas-nuevo/empresas-nuevo.component";

@NgModule({
  imports: [
    CommonModule,
    EmpresasRoutingModule,
    LoadingModule,
    NgxDatatableModule,
    MyDatePickerModule,
    DateTimePickerModule,
    SimpleNotificationsModule.forRoot(),
    FormsModule,
    ChartsModule,
  ],
  declarations: [
    EmpresasComponent,
    EmpresasListarComponent,
    EmpresasNuevoComponent,
  ],
  entryComponents: [
    EmpresasNuevoComponent,

  ],
  providers: [
    NotificationsService,
  ]
})
export class EmpresasModule { }
