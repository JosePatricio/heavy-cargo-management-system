import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {LoadingModule} from "ngx-loading";
import {NgxDatatableModule} from "@swimlane/ngx-datatable";
import {DateTimePickerModule} from "ng-pick-datetime";
import {MyDatePickerModule} from 'mydatepicker';
import {FormsModule} from "@angular/forms";
import {ChartsModule} from "ng2-charts";
import {NotificationsService, SimpleNotificationsModule} from "angular2-notifications/dist";
import {UsuarioConductorRoutingModule} from "./usuario-conductor-routing.module";
import {AprobarConductorComponent} from "./aprobar-conductor/aprobar-conductor.component";
import {AdministrarConductoresComponent} from "./administrar-conductores/administrar-conductores.component";
import {AdministrarVehiculosComponent} from "./administrar-vehiculos/administrar-vehiculos.component";
import {DesvincularConductorComponent} from "./desvincular-conductor/desvincular-conductor.component";
import {ReestablecerConductorComponent} from "./reestablecer-conductor/reestablecer-conductor.component";
import {AngularMultiSelectModule} from 'angular4-multiselect-dropdown/angular4-multiselect-dropdown';
import {UsuarioConductorComponent} from "./usuario-conductor.component";
import { InformacionBancariaComponent } from './informacion-bancaria/informacion-bancaria.component';
import { InformacionBancariaActualizarComponent } from './informacion-bancaria/informacion-bancaria-actualizar/informacion-bancaria-actualizar.component';


@NgModule({
  imports: [
    CommonModule,
    UsuarioConductorRoutingModule,
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
    UsuarioConductorComponent,
    AprobarConductorComponent,
    AdministrarConductoresComponent,
    AdministrarVehiculosComponent,
    DesvincularConductorComponent,
    ReestablecerConductorComponent,
    InformacionBancariaComponent,
    InformacionBancariaActualizarComponent,

  ],
  entryComponents: [
    InformacionBancariaActualizarComponent
  ],
  providers: [
    NotificationsService,
  ]
})
export class UsuarioConductorModule { }
