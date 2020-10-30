import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {LoadingModule} from "ngx-loading";
import {NgxDatatableModule} from "@swimlane/ngx-datatable";
import {DateTimePickerModule} from "ng-pick-datetime";
import {MyDatePickerModule} from 'mydatepicker';
import {FormsModule} from "@angular/forms";
import {ChartsModule} from "ng2-charts";
import {UsuarioRoutingModule} from "./usuario-routing.module";
import {ListaRestableceComponent} from "./restablecer/lista-restablece/lista-restablece.component";
import {ListaDesbloqueaComponent} from "./desbloquear/lista-desbloquea/lista-desbloquea.component";
import {AprobarComponent} from "./aprobar/aprobar.component";
import {UsuarioComponent} from "./usuario.component";

@NgModule({
  imports: [
    CommonModule,
    UsuarioRoutingModule,
    LoadingModule,
    NgxDatatableModule,
    MyDatePickerModule,
    DateTimePickerModule,
    FormsModule,
    ChartsModule,
  ],
  declarations: [
    UsuarioComponent,
    ListaRestableceComponent,
    ListaDesbloqueaComponent,
    AprobarComponent
  ],
  entryComponents: [

  ],
  providers: [
  ]
})
export class UsuarioModule { }
