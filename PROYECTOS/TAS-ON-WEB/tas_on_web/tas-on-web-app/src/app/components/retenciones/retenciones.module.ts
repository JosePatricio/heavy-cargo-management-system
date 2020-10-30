import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {LoadingModule} from "ngx-loading";
import {NgxDatatableModule} from "@swimlane/ngx-datatable";
import {DateTimePickerModule} from "ng-pick-datetime";
import {MyDatePickerModule} from 'mydatepicker';
import {FormsModule} from "@angular/forms";
import {NotificationsService, SimpleNotificationsModule} from "angular2-notifications/dist";
import {RetencionesComponent} from "./retenciones.component";
import {RetencionesListarComponent} from "./listar/retenciones-listar.component";
import {RetencionesRoutingModule} from "./retenciones-routing.module";

@NgModule({
  imports: [
    CommonModule,
    RetencionesRoutingModule,
    LoadingModule,
    NgxDatatableModule,
    MyDatePickerModule,
    DateTimePickerModule,
    SimpleNotificationsModule.forRoot(),
    FormsModule,
  ],
  declarations: [
    RetencionesComponent,
    RetencionesListarComponent,

  ],
  entryComponents: [

  ],
  providers: [
    NotificationsService,
  ]
})
export class RetencionesModule { }
