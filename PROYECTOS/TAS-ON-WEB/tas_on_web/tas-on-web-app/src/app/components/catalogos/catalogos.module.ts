import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {LoadingModule} from "ngx-loading";
import {NgxDatatableModule} from "@swimlane/ngx-datatable";
import {DateTimePickerModule} from "ng-pick-datetime";
import {MyDatePickerModule} from 'mydatepicker';
import {FormsModule} from "@angular/forms";
import {ChartsModule} from "ng2-charts";
import {NotificationsService, SimpleNotificationsModule} from "angular2-notifications/dist";
import {CatalogosRoutingModule} from "./catalogos-routing.module";
import {ListarCatalogosComponent} from "./listar-catalogos/listar-catalogos.component";
import {ListarLocalidadComponent} from "./listar-localidad/listar-localidad.component";
import {NuevoCatalogoComponent} from "./nuevo-catalogo/nuevo-catalogo.component";
import {CatalogosComponent} from "./catalogos.component";

@NgModule({
  imports: [
    CommonModule,
    CatalogosRoutingModule,
    LoadingModule,
    NgxDatatableModule,
    MyDatePickerModule,
    DateTimePickerModule,
    SimpleNotificationsModule.forRoot(),
    FormsModule,
    ChartsModule,
  ],
  declarations: [
    CatalogosComponent,
    ListarCatalogosComponent,
    ListarLocalidadComponent,
    NuevoCatalogoComponent,

  ],
  entryComponents: [

  ],
  providers: [
    NotificationsService,
  ]
})
export class CatalogosModule { }
